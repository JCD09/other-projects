#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "LIST.h"

void testNodeConnectivity(LIST* list){
  if(list->listSize >= 2){
    printf("\ttesting Node connectvity...\n" );
    Node *node = list->head;
    Node *nextNode = list->head->next;
    assert(node = nextNode->previous);
    while (nextNode->next!=NULL){
      node = node->next;
      nextNode = nextNode -> next;
      assert(node = nextNode->previous);
    }
    printf("\t\tnodes are forward and backwards connected\n");
    printf("\t\tPassed\n");
  }
}

// Traverses the nodes starting from Head and returns a total number of nodes
// Traversed;
int traverseNodes(LIST *list){
  int i = 0;
  Node *next=list->head;
  while(next !=NULL){
    next=next->next;
    i = i + 1;
  }
  return i;

}

int main(){
  LIST* listOne;
  LIST* listTwo;

  printf("Testing Concatenation of Two Lists\n");

  printf("Creating listOne and list Two\n");
  listOne = ListCreate();
  listTwo = ListCreate();

  int aOne[5] = {1,1,1,1,1};
  int aTwo[5] = {2,2,2,2,2};

  printf("TEST ONE: Concatenating two empty lists\n");
  ListConcat(listOne,listTwo);
  printf("\tPassed\n");

  printf("TEST TWO: First List is not Empty. Second list is empty\n");

  for(int i = 0; i<5; i++){
    ListAdd(listOne,&aOne[i]);
  }
  printf("\tElements added to the listOne\n");
  testNodeConnectivity(listOne);
  int listLength = traverseNodes(listOne);
  printf("\tNumber of Nodes traversed is %d\n ", listLength );
  assert(listLength == ListCount(listOne));
  ListConcat(listOne, listTwo);

  printf("TEST THREE: First List is not Empty. Second list is empty\n");
  ListConcat(listTwo,listOne);
  listLength = traverseNodes(listOne);
  printf("\tNumber of Nodes traversed is %d\n", listLength );
  assert(listLength == ListCount(listOne));
  testNodeConnectivity(listOne);

  ListConcat(listTwo,listOne);
  listLength = traverseNodes(listTwo);
  printf("\tNumber of Nodes traversed is %d\n", listLength );
  assert(listLength == ListCount(listTwo));
  testNodeConnectivity(listTwo);
  ListConcat(listOne,listTwo);
  testNodeConnectivity(listOne);

  printf("TEST FOUR: First List is not Empty. Second list is not Empty\n");
  printf("Initializing List Two with elements 2\n");
  for(int i = 0; i < 5; i++){
      ListAdd(listTwo,&aTwo[i]);
  }
  printf("Checking List Two Nodes\n");
  listLength = traverseNodes(listTwo);
  printf("Number of Nodes traversed is %d\n", listLength );
  assert(listLength == ListCount(listTwo));
  testNodeConnectivity(listTwo);
  int *listTwoItem = ListCurr(listTwo);
  printf("Value of the item in list two. Expected Value 2. Actual Value %d\n", *listTwoItem);
  printf("Setting the curretItem pointer in ListOne to out of bound(posIndex > 0)\n");
  ListLast(listOne);
  ListNext(listOne);
  ListConcat(listOne,listTwo);

  printf("Performing concatenation of listOne and listTwo\n");
  printf("Performing Checks\n");
  assert(ListCount(listTwo)==0);
  printf("\t 1.0: List Two should be empty... passed\n");
  printf("\t 2.0: List One should be of size 10... passed\n");
  assert(ListCount(listOne)==10);
  printf("\t 3.0: Returning Current Item, should be out of bounds... passed\n");
  assert(ListCurr(listOne)==NULL);
  printf("\t 4.0: Traversing list one and examining its contents....\n");
  listLength=traverseNodes(listOne);
  assert(listLength==ListCount(listOne));
  int *listOneItem = ListFirst(listOne);

  for(int i = 0; i<listLength; i++){
    if(i<5){
      assert(*listOneItem == 1);
      listOneItem = ListNext(listOne);
    }
    else {
      assert(*listTwoItem == 2);
      listTwoItem = ListNext(listOne);
    }
  }
  printf("\t ...passed\n");

  testNodeConnectivity(listOne);



}
