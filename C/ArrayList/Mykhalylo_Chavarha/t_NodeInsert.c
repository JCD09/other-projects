#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "LIST.h"

int main(){
  LIST* listOne;
  LIST* listTwo;
  LIST* listThree;

  int aOne[10] = {1,2,3,4,5,6,7,8,9,10};
  int aTwo[10] = {2,2,2,2,2,2,2,2,2,2};
  //int array_of_intsThree[10] = {5,5,5,5,5,5,5,5,5,5};
  listOne = ListCreate();
  listTwo = ListCreate();
  listThree = ListCreate();

  printf("inserting elements of the array into ListOne using function ListInsert()\n" );
  for(int i = 0; i < 10; i ++){
    printf("Iteration %d\n", i);
    ListInsert(listOne, &aOne[i]);
    printf("Checking inserted items. Inserted Value %d\n", aOne[i]);
    int* listCurr =  ListCurr(listOne);
    printf("Checking inserted items. Expected value %d. Inserted Value %d\n",*listCurr, aOne[i]);
    assert(*listCurr=aOne[i]);
  }
  printf("Testing ListOne Size. Expected value 10. Actual Value %d\n", ListCount(listOne) );
  printf("All assertions Passed. Elements succesfully added to the list\n");

  printf("TEST TWO. Inserting elements only into left end of the list\n");

  for(int i = 0; i < 10; i++){
    //printf("iteration  %d\n", i);
    ListInsert(listTwo, &aTwo[i]);
    int* listCurr = ListCurr(listTwo);
    assert(*listCurr == 2);

    int* listPrevious = ListFirst(listTwo);
    listPrevious=ListPrev(listTwo);

    int posIndex = getPositionIndex(listTwo);
    assert(listPrevious==NULL);
    assert(posIndex < 0);

    listCurr = ListCurr(listTwo);
    assert(listCurr == NULL);

  }
  printf("removing elements from list Two\n");

  int* itemm;
  int* removeItem;
  for(int i = 0; i < 10; i++){
    //printf("iteration i = %d\n", i);
    itemm =ListFirst(listTwo);
    removeItem = ListRemove(listTwo);
    assert(*removeItem == 2);
    assert(ListCount(listTwo) == 9-i);
    //printf("ListCount %d\n",ListCount(listOne));
  }
  assert(ListCount(listTwo)==0);

  printf("All assertions passed elements are successfully inserted to the beginning of the list Two\n");
  //
   printf("TEST THREE. Inserting Items into the right end of the list\n");
   for(int i = 0; i < 10; i++){
     //printf("iteration  %d\n", i);
     ListInsert(listTwo, &aTwo[i]);
     int* listCurr = ListCurr(listTwo);
     assert(*listCurr == 2);

     int* listNext = ListLast(listTwo);
     listNext=ListNext(listTwo);

     int posIndex = getPositionIndex(listTwo);
     assert(listNext==NULL);
     assert(posIndex > 0);

     listCurr = ListCurr(listTwo);
     assert(listCurr == NULL);
   }

   assert(ListCount(listTwo)==10);

   printf("removing elements from list Two\n");

   for(int i = 0; i < 10; i++){
     //printf("iteration i = %d\n", i);
     itemm =ListFirst(listTwo);
     removeItem = ListRemove(listTwo);
     assert(*removeItem == 2);
     assert(ListCount(listTwo) == 9-i);
     //printf("ListCount %d\n",ListCount(listOne));
   }
   assert(ListCount(listTwo)==0);

  printf("ALL assertions passed elements are successfully added to the end the list\n");

  printf("TEST FOUR. Inserting Items into the right end of the list\n");

  int* listNext;
  for(int i = 0; i < 10; i++){
    //printf("iteration  %d\n", i);
    ListInsert(listTwo, &aTwo[i]);
    int* listCurr = ListCurr(listTwo);
    assert(*listCurr == 2);
    if(i>2){
      listNext = ListLast(listTwo);
      listNext=ListPrev(listTwo);
      listNext = ListCurr(listTwo);
      assert(*listNext==2);
    }

  }

  printf("ALL assertions passed elements are successfully added to the end the list\n");



  }
