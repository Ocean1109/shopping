#include <iostream>

using namespace std;

 struct ListNode {
     int val;
     ListNode *next;
     ListNode() : val(0), next(nullptr) {}
     ListNode(int x) : val(x), next(nullptr) {}
     ListNode(int x, ListNode *next) : val(x), next(next) {}
 };

class Solution {
public:
    ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        int num1 = 0; 
        int num2 = 0;

        while(l1->next != nullptr){
            num1 = num1*10 + l1->val;
            l1 = l1->next;
        }
        while(l2->next != nullptr){
            num2 = num2*10 + l2->val;
            l2 = l2->next;
        }

        int sum = num1 + num2;

        ListNode * l3 = new ListNode();
        ListNode * front = l3;
        while(sum != 0){
            sum = sum%10;
            l3->val = sum;
            ListNode * ln = new ListNode();
            l3->next = ln;
            l3 = ln;
        }

        return front;
    }
};

int main(){

    int i = 123;

    cout << i%10;

    return 0;
}