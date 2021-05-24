#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "LIST.h"

void testNodeConnectivity(LIST* list){
  if(list->listSize==0){printf("List is empty\n");}
  if(list->listSize==1){printf("Lis is of size one\n");}
  if(list->listSize >= 2){
    printf("testing Node connectvity: traversing from head to tail\n" );
    Node *node = list->head;
    Node *nextNode = list->head->next;
    assert(node = nextNode->previous);
    printf("first assertion passed\n");
    while (nextNode->next!=NULL){
      node = node->next;
      nextNode = nextNode -> next;
      assert(node = nextNode->previous);
    }
    printf("nodes are forward and backwards connected\n");
  }
}




void f_freeInt(void* freeInt){
  free(freeInt);
}


int main(){
  LIST* listOne;

  listOne = ListCreate();

  printf("TESTING ListFree():\n");
  printf("Creating an array of pointers to int\n");
  int* ptrArrInt[10];
  printf("populating array\n");
  for (int i = 0; i < 10; i ++){
    ptrArrInt[i]=malloc(sizeof(int));
    *ptrArrInt[i] = i;
    //printf("%d\n", *ptrArrInt[i]);
  }
  printf("adding pointers to Ints into the list\n");

  int* currentItem;
  for(int i =0; i < 10; i ++){
    ListAdd(listOne, ptrArrInt[i]);
    currentItem = ListCurr(listOne);
    assert(*currentItem == i);
  }
  printf("Checking list connectivity/integrity\n");
  testNodeConnectivity(listOne);
  printf("Freeing list\n");
  ListFree(listOne,&f_freeInt);
  //assert(ListCount(listOne)==0);
  //testNodeConnectivity(listOne);
  assert(ListCount(listOne)==0);




  printf("Elements successfully Freed\n");
}
