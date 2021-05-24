#include "LIST.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

#define Header_Array_Size 3
LIST headerList[Header_Array_Size];

int availabilityIndex = Header_Array_Size;
NodeArray nodeArray;

LIST create(){
  LIST newList;
  newList.currentNode = NULL;
  newList.tail = NULL;
  newList.head = NULL;
  newList.listSize = 0;
  newList.posIndex;
  return newList;
}

LIST *ListCreate(){
  if(availabilityIndex==Header_Array_Size){
    nodeArray=createNodeArray();
    linkNodes(&nodeArray);
    availabilityIndex=availabilityIndex-1;
  }
  if(availabilityIndex<0){
    return NULL;
  }
  headerList[availabilityIndex]=create();
  availabilityIndex=availabilityIndex-1;
  return &headerList[availabilityIndex+1];

}


int ListCount(LIST *list){
  return list->listSize;
}

// Returns pointer to the first item in the list;
// Makes the first item the current item;
void* ListFirst(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  else{
    list->currentNode=list->head;
    list->posIndex = 0;
    return list->currentNode->item;
  }
}


void* ListLast(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  else{
    list->currentNode=list->tail;
    list->posIndex = 0;
    return list->currentNode->item;
  }
}

void* ListNext(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  if(list->currentNode->next==NULL){
    list->posIndex = 1;
    return NULL;

  }
  else{
    list->currentNode=list->currentNode->next;
    list->posIndex = 0;
    return list->currentNode->item;
  }

}

void* ListPrev(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  if(list->currentNode->previous==NULL){
    list->posIndex=-1;
    return NULL;
  }
  else{
    list->currentNode=list->currentNode->previous;
    list->posIndex = 0;
    return list->currentNode->item;
  }
}

void* ListCurr(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  if(list->posIndex != 0){
    return NULL;
  }
  else{
    return list->currentNode->item;
  }
}

int ListAdd(LIST *list, void* item){
  Node* newNode = getNode(&nodeArray);
  if(newNode == NULL){return -1;}
  if(list->listSize == 0){
    list->posIndex=0;
    list->currentNode=newNode;
    list->head = newNode;
    list->tail = newNode;
    list->currentNode->item = item;
    list->listSize=list->listSize+1;
    return 0;
  }
  else {
    if(list->posIndex < 0){
      //Prependnode to the front of the list;
      assert(list->currentNode==list->head);
      nodePrepend(list->currentNode,newNode);
      assert(list->currentNode->previous==newNode);
      list->currentNode=list->currentNode->previous;
      list->currentNode->item = item;
      list->head=list->currentNode;
      list->posIndex = 0;
      list->listSize=list->listSize+1;
      return 0;
    }
    else if(list->posIndex > 0){
      //Prependnode to the front of the list;
      assert(list->currentNode==list->tail);
      nodeAppend(list->currentNode,newNode);
      assert(list->currentNode->next==newNode);
      assert(newNode->previous = list->currentNode);
      list->currentNode=list->currentNode->next;
      list->currentNode->item = item;
      list->tail = list->currentNode;
      list->posIndex = 0;
      list->listSize=list->listSize+1;
      return 0;
    }
    else{
      Node* breakNode = breakAfter(list->currentNode); //
      // Insert the newNode afte the current node;
      if(breakNode != NULL){nodePrepend(breakNode,newNode);}
      nodeAppend(list->currentNode,newNode);
      if(breakNode == NULL){
        list->tail = list->currentNode->next;
      }
      list->currentNode = list->currentNode->next;
      list->currentNode->item = item;
      list->listSize=list->listSize+1;
      return 0;
    // Place item inside the node;
  }
}
}

int ListInsert(LIST *list, void* item){
  ListPrev(list);
  int val = ListAdd(list,item);
  return val;
}

int ListAppend(LIST* list, void* item){
  ListLast(list);
  int val = ListAdd(list, item);
  return val;
}

int ListPrepend(LIST* list, void* item){
  ListFirst(list);
  int val = ListInsert(list,item);
  return val;
}

void* ListRemove(LIST *list){
  if(list->listSize == 0){
    return NULL;
  }
  if(list->posIndex != 0){
    return NULL;
  }
  if(list->listSize == 1){
    void* item = list->currentNode->item;
    Node* freeNode = list->currentNode;
    list->currentNode->item = NULL;
    list->head = NULL;
    list->tail = NULL;
    list->currentNode=NULL;
    putNode(&nodeArray,freeNode);
    list->listSize=list->listSize-1;
    list->posIndex == 0;
    return item;
  }
  else{
    if(list->currentNode==list->head){
      // node is out of bounds or node is at Head returning the headNode;
      assert(list->currentNode->previous==NULL);
      Node *freeNode = list->currentNode;
      list->head=list->head->next;
      list->currentNode=list->currentNode->next;
          
      breakAfter(freeNode);
      assert(freeNode->previous == NULL);
      assert(freeNode->next ==NULL);
      void* item = freeNode->item;
      freeNode->item = NULL;
      putNode(&nodeArray,freeNode);
      list->posIndex=0;
      list->listSize=list->listSize-1;
        return item;
    }
    else if(list->currentNode==list->tail){
      // node is out of bounds returning the tailNode;
      assert(list->currentNode->next==NULL);
      Node *freeNode = list->currentNode;
      list->tail=list->tail->previous;
      list->currentNode=list->currentNode->previous;
      breakBefore(freeNode);
      assert(freeNode->previous == NULL);
      assert(freeNode->next ==NULL);
      void* item = freeNode->item;
      freeNode->item = NULL;
      putNode(&nodeArray,freeNode);
      list->posIndex=0;
      list->listSize=list->listSize-1;
      return item;
    }
    else{
    // Make the next Item the current One
      Node* freeNode = list->currentNode;
      assert(freeNode->next != NULL);
      assert(freeNode->previous != NULL);
      void* item = freeNode->item;
      //Break the Node from Both Ends
      Node* ptrToLeftEnd = breakBefore(list->currentNode);
      assert(ptrToLeftEnd->next == NULL);
      Node* ptrToRightEnd = breakAfter(list->currentNode);
      assert(ptrToRightEnd->previous==NULL);    
      list->currentNode=ptrToRightEnd;
      nodeAppend(ptrToLeftEnd,ptrToRightEnd);
      freeNode->item=NULL;
      assert(freeNode->next == NULL);
      assert(freeNode->previous == NULL);
      putNode(&nodeArray,freeNode);
      list->listSize=list->listSize-1;
      list->posIndex=0;
        return item;
    }
  }
}

void ListConcat(LIST* list1, LIST* list2){
  int list1_Size = ListCount(list1);
  int list2_Size = ListCount(list2);
  if((list1_Size==0)&&(list2_Size==0)){
    // Nothing to join
    return;
  }
  else if(list1_Size==0){
    // Copy the elements from List Two into ListOne;
    list1->head = list2->head;
    list2->head = NULL;
    list1->tail = list2->tail;
    list2->tail = NULL;

    list1->currentNode=list2->currentNode;
    list2->currentNode=NULL;
    list1->posIndex=list2->posIndex;
    list1->listSize = list2->listSize;
    list2->posIndex=0;
    list2->listSize= 0;
    return;
  }
  else if(list2_Size == 0){
    // lis2 is empty/ do nuthing;
    return;
  }
  else{
    // Join Nodes between head and tail
    assert(list1->tail->next == NULL);
    assert(list2->head->previous == NULL);
    nodeAppend(list1->tail,list2->head);
    // Update list1 parameters
    list1->tail = list2->tail;
    list2->tail = NULL;
    list2->head = NULL;
    list2->currentNode=NULL;
    list1->listSize=list1->listSize+list2->listSize;
    list2->listSize=0;
    list2->posIndex=0;
    if(list1->posIndex > 0){
      list1->currentNode=list1->tail;
    }
    return;
  }

}

void ListFree(LIST* list, void (*itemFree)(void *)){
  void* item;
  while(list->listSize > 0){
    item = ListTrim(list);
    //printf("freeing item using supplied routine\n");
    (*itemFree)(item);
  }
}

void* ListTrim(LIST *list){
  if(list->listSize==0){}
  else{
    ListLast(list);
    return ListRemove(list);
  }
}
int getAvailabilityIndex(){
  return availabilityIndex;
}

int getPositionIndex(LIST *list){
  return list->posIndex;
}


void *ListSearch(LIST* list, int (*comparator)(void*, void*), void* comparisonArg){
  ListFirst(list);
  int size = ListCount(list);
  void* item;
  int compare;
  while(ListCurr(list) != NULL){
    item = ListCurr(list);
    compare = (*comparator)(item,comparisonArg);
    int* itemInt = item;
    //printf("printing compare value %d and current int %d \n",compare,*itemInt);
    if(compare == 1){
      return item;
    }
    ListNext(list);
  }
  return NULL;
}
