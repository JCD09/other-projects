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

void purgeList(LIST* list){
  int size = ListCount(list);
  while(size > 0){
    ListTrim(list);
    size = ListCount(list);
  }
  printf("\tList Purged\n");
}

int main(){
  LIST* listOne;
  LIST* listTwo;

  listOne = ListCreate();
  listTwo = ListCreate();
  printf("RUNNING TESTS ON ListInsert, ListAppend, ListPrepend\n");

  printf("TEST ONE: testing ListInsert.\n");

  int aOne[10] = {0,1,2,3,4,5,6,7,8,9};

  printf("\tInserting elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListInsert(listOne,&aOne[i]);
    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("ListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST TWO. Testing using listInsert only into the left End of the list. ListCurr = head\n");
  printf("\tInserting elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListFirst(listOne);
    ListInsert(listOne,&aOne[i]);
    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST THREE. Testing using listInsert only into the left End of the list. When ListCurr is out of bounds. PosIndex < 0\n");
  printf("\tInserting elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListFirst(listOne);
    ListPrev(listOne);
    assert(ListCurr(listOne) == NULL);
    ListInsert(listOne,&aOne[i]);

    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST FOUR. Testing using listAdd only into the left End of the list. When ListCurr is out of bounds. PosIndex > 0\n");
  printf("\tInserting elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListLast(listOne);
    ListNext(listOne);
    assert(ListCurr(listOne) == NULL);
    ListInsert(listOne,&aOne[i]);

    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST FIVE. Testing using listAdd only into the left End of the list. When ListCurr is Tail\n");
  printf("\tInserting elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListLast(listOne);
    ListInsert(listOne,&aOne[i]);

    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST SIX. Adding elemnts only to the middle of the list\n");

  int aTwo[10] = {7,7,7,7,7,7,7,7,7,7};
  int* item;
  for(int i = 0; i < 10; i++){
      //printf("iteration %d\n",i);
      ListInsert(listTwo, &aTwo[i]);
      //printf("we are here%p \n", ListCurr(listTwo));
      item = ListCurr(listTwo);
      assert(*item = 7);

      if(i > 2){
        ListLast(listTwo);
        ListPrev(listTwo);
        ListPrev(listTwo);
      }
  }
  int size = ListCount(listTwo);
  assert(size = 10);
  printf("\tListCreated. Testing Node Connectivity\n");
  testNodeConnectivity(listTwo);
  printf("\tPurging List\n");
  purgeList(listTwo);
  testNodeConnectivity(listTwo);

  printf("TEST SEVEN. Running Additional Tests\n");
  printf("\tChecking propper insetion\n");
  printf("\tInsering number one\n");
  ListInsert(listOne,&aOne[1]);
  printf("\tInsering number one\n");
  ListInsert(listOne,&aOne[7]);
  printf("\tMoving current node towards number One\n");
  ListNext(listOne);
  printf("\tInserting Number 3 before Node One \n");
  ListInsert(listOne, &aOne[3]);
  printf("\tMoving current node towards number One\n");
  ListNext(listOne);
  printf("\tInserting Number 3 before Node One \n");
  ListInsert(listOne, &aOne[4]);
  printf("\tExpected Sequence: 7 3 4 1\n");
  printf("\tRunning assertions\n");
  item = ListFirst(listOne);
  assert(*item = 7);
  item = ListNext(listOne);
  assert(*item = 3);
  item = ListNext(listOne);
  assert(*item = 4);
  item = ListNext(listOne);
  assert(*item = 1);
  printf("\tAssertions done. Sequnce 7 3 4 1 encountered\n");
  purgeList(listOne);

  printf("TEST EIGHT. Running List Append \n");


  printf("\tAppending elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListAppend(listOne,&aOne[i]);
    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tTraversing List: expected sequence 0123456789\n");
  ListFirst(listOne);
  printf("\tOutput List Squece: " );
  for (int i = 0; i < 10; i ++){
    int* item = ListCurr(listOne);
    printf("%d",*item );
    ListNext(listOne);
  }
  printf("\n");

  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TEST NINE. Running List Prepend \n");


  printf("\tAppending elements into array list One \n");
  for (int i = 0; i < 10; i ++){
    ListPrepend(listOne,&aOne[i]);
    int* item = ListCurr(listOne);
    assert(*item == i);
  }
  printf("\tTraversing List from head: expected sequence 9876543210\n");
  ListFirst(listOne);
  printf("\tOutput List Squece: " );
  for (int i = 0; i < 10; i ++){
    int* item = ListCurr(listOne);
    printf("%d",*item );
    ListNext(listOne);
  }
  printf("\n");

  printf("\tListCreated. Testign Node Connectivity\n");
  testNodeConnectivity(listOne);
  printf("\tPurging List\n");
  purgeList(listOne);
  testNodeConnectivity(listOne);

  printf("TESTING COMPLETE");


}







// int main(){
//
//
//   // LIST* listOne;
//   // LIST* listTwo;
//   // LIST* listThree;
//   // LIST* listFour;
//   // LIST* listFive;
//   //
//   // printf("TEST ONE. Testing ListCreate() routine\n");
//   // printf("creating an list of three elements\n");
//   // listOne=ListCreate();
//   // listTwo=ListCreate();
//   // listThree=ListCreate();
//   // printf("List One Two and Three must be non-NULL\n");
//   // assert(listOne != NULL);
//   // assert(listTwo != NULL);
//   // assert(listThree != NULL);
//   // printf("all assertions passed\n");
//   // listFour=ListCreate();
//   // printf("list Four must be null\n");
//   // assert(listFour == NULL);
//   // printf("assertin passed\n");
//   // printf("availability index should be negative \n");
//   // assert(getAvailabilityIndex() < 0);
//   // printf("assertion passed. Availability index equals to: %d\n", getAvailabilityIndex());
//   //
//   // int array_of_intsOne[10] = {1,2,3,4,5,6,7,8,9,10};
//   // int array_of_intsTwo[10] = {2,2,2,2,2,2,2,2,2,2};
//   // int array_of_intsThree[10] = {5,5,5,5,5,5,5,5,5,5};
//   //
//   // printf("inserting elements of the array into ListOne using function ListAdd()\n" );
//   // for(int i = 0; i < 10; i ++){
//   //   ListAdd(listOne, &array_of_intsOne[i]);
//   //   int* listCurr =  ListCurr(listOne);
//   //   printf("Checking inserted items. Expected value %d. Inserted Value %d\n",*listCurr, array_of_intsOne[i]);
//   //   assert(*listCurr=array_of_intsOne[i]);
//   // }
//   // printf("Testing ListOne Size. Expected value 10. Actual Value %d\n", ListCount(listOne) );
//   // printf("All assertions Passed. Elements succesfully added to the list\n");
//   //
//   // printf("TEST THREE. TESTING INSERTION ONLY INTO THE LEFT END OF THE LIST\n");
//   //
//   // for(int i = 0; i < 10; i++){
//   //   //printf("iteration  %d\n", i);
//   //   ListAdd(listTwo, &array_of_intsTwo[i]);
//   //   int* listCurr = ListCurr(listTwo);
//   //   assert(*listCurr == 2);
//   //   int* listPrev = ListPrev(listTwo);
//   //   int posIndex = getPositionIndex(listTwo);
//   //   assert(listPrev==NULL);
//   //   assert(posIndex < 0);
//   //   listCurr = ListCurr(listTwo);
//   //   assert(*listCurr == 2);
//   // }
//   //
//   // printf("All assertions passed elements are successfully added to the beginning of the list Two\n");
//   //
//   // printf("TEST FOUR. TESTING INSERTION ONLY INTO THE RIGHT END OF THE LIST\n");
//   //
//   // for(int i = 0; i < 10; i++){
//   //   ListAdd(listThree, &array_of_intsThree[i]);
//   //   int* listCurr = ListCurr(listThree);
//   //   assert(*listCurr == 5);
//   //   int* listNext = ListNext(listThree);
//   //   int posIndex = getPositionIndex(listThree);
//   //   assert(listNext==NULL);
//   //   assert(posIndex > 0);
//   //   listCurr = ListCurr(listThree);
//   //   assert(*listCurr == 5);
//   // }
//   //
//   // printf("ALL assertions passed elements are successfully added to the end the list\n");
//
//
//   }
