#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "LIST.h"


void testNodeConnectivity(LIST* list){
  printf("\tTesting List Nodes\n");
  if(list->listSize==0){
    assert(list->head == NULL);
    assert(list->tail == NULL);
    assert(list->currentNode == NULL);
    printf("\tList is empty\n");}
  if(list->listSize==1){printf("List is of the size one\n");}
  if(list->listSize >= 2){
    printf("\ttesting Node connectvity: traversing from head to tail\n" );
    Node *node = list->head;
    Node *nextNode = list->head->next;
    assert(node = nextNode->previous);
    //printf("first assertion passed\n");
    while (nextNode->next!=NULL){
      node = node->next;
      nextNode = nextNode -> next;
      assert(node = nextNode->previous);
    }
    printf("\tnodes are forward and backwards connected\n");
  }
}

int main(){
  LIST* listOne;
  LIST* listTwo;

  printf("TEST ONE: adding and removing one element array.\n");

  int intArrayOne[10] = {7,7,7,7,7,7,7,7,7,7};
  listOne = ListCreate();

  printf("TEST ONE: adding and removing one element array.\n");
  int listSize = ListCount(listOne);
  assert(listSize == 0);
  int *itemCurr = NULL;
  itemCurr=ListRemove(listOne);
  assert(itemCurr==NULL);

  printf("\tadding one element to the empty listOne\n");
  ListAdd(listOne,&intArrayOne[1]);
  assert(ListCount(listOne) == 1);

  printf("removing using ListRemove\n");
  int *item = ListRemove(listOne);
  assert(*item = 7);
  assert(ListCount(listOne) == 0);
  printf("\tTest Passed. List is empty\n");

  printf("\tadding one element to the empty listOne\n");
  ListAdd(listOne,&intArrayOne[1]);
  assert(ListCount(listOne) == 1);
  testNodeConnectivity(listOne);

  printf("Removing using trim\n");
  item = ListTrim(listOne);
  assert(*item = 7);
  assert(ListCount(listOne) == 0);
  printf("\tTest Passed\n");
  testNodeConnectivity(listOne);


  printf("TEST TWO: adding or removing elements from the array when the current Node is NULL(posIndex < 0) or (>0).\n");

  printf("\tInsert five elements into array listOne\n");
  for (int i = 0; i < 5; i ++){
    ListAdd(listOne,&intArrayOne[i]);
    int* returnItem = ListCurr(listOne);
    assert(*returnItem == 7);
  }
  printf("\tList is populatined\n");
  testNodeConnectivity(listOne);
  assert(ListCount(listOne) == 5);
  printf("\tRemoving elements from the listOne using ListRemove\n");
  printf("\tMoving index out of bounds. PosIndex < 0\n" );
  ListFirst(listOne);
  ListPrev(listOne);
  item = ListRemove(listOne);
  assert(item == NULL);
  printf("\tItem is not removed List Size is %d. Expected Value 5\n", ListCount(listOne));
  printf("\tMoving index out of bounds. PosIndex > 0\n" );
  ListLast(listOne);
  ListNext(listOne);
  item = ListRemove(listOne);
  assert(item == NULL);
  printf("\tItem is not removed List Size is %d. Expected Value 5\n", ListCount(listOne));

  printf("TEST THREE: removing elements from the array when the current Node is at Head of the list.\n");
  printf("\tremoving nodes from the list Using using listRemove\n");

  int* removeItem;
  for(int i = 0; i < 5; i++){
    //printf("printing I %d\n", i);
    ListFirst(listOne);
    removeItem = ListRemove(listOne);
    assert(*removeItem == 7);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  //printf("\tListCount %d\n",ListCount(listOne));
  assert(ListCount(listOne)==0);
  testNodeConnectivity(listOne);
  printf("\tAll Elements Successfully Removed\n");


  printf("TEST FOUR: removing elements from the array when the current Node is at the Tail of the LIST.\n");

  printf("\tInsert five elements into array listOne\n");
  for (int i = 0; i < 5; i ++){
    ListAdd(listOne,&intArrayOne[i]);
    int* returnItem = ListCurr(listOne);
    assert(*returnItem == 7);
  }
  printf("\tList is populatined\n");

  printf("\tremoving nodes from the list Using using listRemove\n");
  for(int i = 0; i < 5; i++){
    //printf("printing I %d\n", i);
    ListLast(listOne);
    removeItem = ListRemove(listOne);
    assert(*removeItem == 7);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  //printf("\tListCount %d\n",ListCount(listOne));
  assert(ListCount(listOne)==0);
  testNodeConnectivity(listOne);
  printf("\tAll Elements Successfully Removed\n");


  printf("TEST FIVE: Adding and removing elements when currentNode is in the middle\n");

  printf("\tInsert five elements into array listOne\n");
  for (int i = 0; i < 5; i ++){
    ListAdd(listOne,&intArrayOne[i]);
    int* returnItem = ListCurr(listOne);

    assert(*returnItem == 7);
  }
  printf("\tList is populatined\n");

  ListFirst(listOne);
  ListNext(listOne);
  ListNext(listOne);
  int* itemm;
  for(int i = 0; i < 5; i++){
    //printf("iteration i = %d\n", i);
    itemm =ListCurr(listOne);
    removeItem = ListRemove(listOne);
    assert(*removeItem == 7);
    assert(ListCount(listOne) == 4-i);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  assert(ListCount(listOne)==0);
  printf("\tTest Five Passed. Elements Successfully Removed. \n");

  printf("TEST SiX: Removing elements using TRIM. when pointer is out of bounds < 0\n");
  printf("\tInsert five elements into array listOne\n");
  for (int i = 0; i < 5; i ++){
    ListAdd(listOne,&intArrayOne[i]);
    int* returnItem = ListCurr(listOne);

    assert(*returnItem == 7);
  }
  printf("\tList is populatined\n");

  for(int i = 0; i < 5; i++){
    //printf("iteration i = %d\n", i);
    itemm =ListFirst(listOne);
    ListPrev(listOne);
    removeItem = ListTrim(listOne);
    assert(*removeItem == 7);
    assert(ListCount(listOne) == 4-i);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  testNodeConnectivity(listOne);

  printf("TEST SEVEN: Removing elements using TRIM. when pointer is out of bounds > 0\n");
  printf("\tInsert five elements into array listOne\n");
  for (int i = 0; i < 5; i ++){
    ListAdd(listOne,&intArrayOne[i]);
    int* returnItem = ListCurr(listOne);

    assert(*returnItem == 7);
  }
  printf("\tList is populatined\n");

  for(int i = 0; i < 5; i++){
    //printf("iteration i = %d\n", i);
    itemm =ListFirst(listOne);
    ListPrev(listOne);
    removeItem = ListTrim(listOne);
    assert(*removeItem == 7);
    assert(ListCount(listOne) == 4-i);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  testNodeConnectivity(listOne);

  printf("All tests Passed. Done");






}
