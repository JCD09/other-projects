#ifndef _SORTER_H_
#define _SORTER_H_ "Sorter.h"

typedef struct Array{
    int size;
    int* array;
} Array;


typedef struct Element{
    int max;
    int value;
} Element;


void* sorter_run(void* args);
int get_curr_array_size();
int get_arrays_sorted();
Array get_curr_array();
Element get_element(int index);


#endif
