package com.example.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.ChatDetail;
import com.example.demo.entity.ShoppingUser;
import com.example.demo.mapper.ChatDetailMapper;
import com.example.demo.mapper.ShoppingUserMapper;
import com.example.demo.service.ChatService;
import com.example.demo.service.TokenService;
import freemarker.log.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * desc: WebSocket服务端
 */
@ServerEndpoint("/send/{chatId}/{token}")
@Component
public class WebSocketServer {

    private TokenService tokenService;

    static Logger logger = Logger.getLogger("WebSocketServer");
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收频道topic
     */
    private String topic = "";
    /**
     * 判断是哪个聊天框
     */
    private String chatId = "";

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token,@PathParam("chatId")String chatId) {
        this.session = session;
        tokenService=applicationContext.getBean(TokenService.class);
        this.topic = tokenService.getUseridFromToken(token);
        this.chatId=chatId;
        if (webSocketMap.containsKey(topic)) {
            webSocketMap.remove(topic);
            subOnlineCount();
            //加入set中
        }
        webSocketMap.put(topic, this);
        addOnlineCount();

        logger.info("用户连接:" + topic + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("用户:" + topic + ",网络异常!!!!!!");
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(topic)) {
            webSocketMap.remove(topic);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:" + topic + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("用户:" + topic + ",信息:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //存到chat_detail数据库中
                ChatService chatService=applicationContext.getBean(ChatService.class);
                chatService.insertChat(Integer.parseInt(this.chatId),Integer.parseInt(topic), jsonObject.getString("content"));
                //追加发送人(防止串改)
                jsonObject.put("from_topic", topic);
                String to_topic = jsonObject.getString("to_topic");
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(to_topic) && webSocketMap.containsKey(to_topic)) {
                    webSocketMap.get(to_topic).sendMessage(jsonObject.toJSONString());
                } else {
                    logger.error("请求的to_topic:" + to_topic + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:" + this.topic + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("topic") String topic) throws IOException {
        logger.info("发送消息到:" + topic + "，信息:" + message);
        if (StringUtils.isNotBlank(topic) && webSocketMap.containsKey(topic)) {
            webSocketMap.get(topic).sendMessage(message);
        } else {
            logger.error("用户" + topic + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

