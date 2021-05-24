#include "NodeArray.h"

typedef struct LIST {
  Node *currentNode;
  Node *head;
  Node *tail;
  int posIndex; // 0 when list is empty; 1 when outOFB -1; when out
  int listSize;
} LIST;

LIST *ListCreate();

int ListCount(LIST *list);

void* ListFirst(LIST *list);

void* ListLast(LIST *list);

void* ListNext(LIST *list);

void* ListPrev(LIST *list);

void* ListCurr(LIST *list);

int ListAdd(LIST *list, void* item);

int ListInsert(LIST *list, void* item);
//
int ListAppend(LIST* list, void* item);

int ListPrepend(LIST* list, void* item);

void* ListRemove(LIST *list);

void ListConcat(LIST* list1, LIST* list2);

void ListFree(LIST* list, void (*f_free)(void *));

void* ListTrim(LIST* list);


// getting functions

int getPositionIndex(LIST *list);
void *ListSearch(LIST* list, int (*comparator)(void*, void*), void* comparisonArg);
