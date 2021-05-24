#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "LIST.h"

int main(){
  LIST* listOne;
  LIST* listTwo;
  LIST* listThree;

  printf("TEST ONE. ADDING elemnts only to the middle of the list\n");

  int intArrayOne[10] = {7,7,7,7,7,7,7,7,7,7};

  listOne = ListCreate();
    for(int i = 0; i < 10; i++){
      printf("iteration %d\n",i);
      ListAdd(listOne, &intArrayOne[i]);
      int* cItem = ListCurr(listOne);
      assert(*cItem = 7);
      if(i > 2){
        ListLast(listOne);
        ListPrev(listOne);
        ListPrev(listOne);
      }
    }
    int size = ListCount(listOne);
    assert(size = 10);

  printf("TEST ONE COMPLETE. ELEMENTS SUCCESSFULLY ADDED\n");
}
