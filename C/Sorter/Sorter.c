#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

#include "Potentiometer.h"
#include "Display.h"
#include "Sorter.h"

int* current_array; 
int  size; 

int arrays_sorted; 

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
//pthread_cond_t cvar =  PTHREAD_COND_INITIALIZER;

int* genenrate_randomized_array(int array_size){
    int* new = malloc(array_size*sizeof(int));
    for(int i = 0; i < array_size; i++){
        new[i]=i;
    }
    // randomize array; 
    for(int i = 1; i < array_size; i++){
        int j = rand() % (i+1);
        int temp = new[i];
        new[i]=new[j];
        new[j] = temp;
    }

    return new;
}

void sorter_cleanup(){
    printf("2.0 Executing sorter cleanup routine \n");
    printf("2.1 Freeing memory(current array)\n");
    free(current_array);
    printf("2.2 Unlocking mutex\n");
    pthread_mutex_unlock(&mtx);
}


void sort(){
    _Bool swapped;
    do{
        swapped=false;
        for(int i = 1; i < size; i++){

            pthread_mutex_lock(&mtx);
            if(current_array[i-1] > current_array[i]){
                int temp = current_array[i-1];
                current_array[i-1]=current_array[i];
                current_array[i] = temp;
                swapped = true; 
            }
            pthread_mutex_unlock(&mtx);
            pthread_testcancel();
            
        }
    }
    while(swapped);
}

void* sorter_run(void* args){
    //  create dummy array; 
    pthread_cleanup_push(sorter_cleanup, NULL);
    current_array = malloc(sizeof(int));
    current_array[0] = 0;
    size = 1;
    arrays_sorted = 0;

    while(true){
        pthread_mutex_lock(&mtx);
        size = get_array_size();
        free(current_array);
        current_array = genenrate_randomized_array(size);

        pthread_mutex_unlock(&mtx);
        sort();

        pthread_testcancel();
        arrays_sorted++;
    }
    pthread_cleanup_pop(1);
}

int get_arrays_sorted(){
    return arrays_sorted;
}

int get_curr_array_size(){
    return size;
}

Array get_curr_array(){
    Array a; 
    
    pthread_mutex_lock(&mtx);
    int* new = malloc(size*sizeof(int));
    a.array = new;
    for(int i = 0; i < size; i++){
        a.array[i]=current_array[i];
    };
    a.size = size; 
    pthread_mutex_unlock(&mtx);

    return a;

}

Element get_element(int index){
    Element el;
    if(index>size){
        el.value = -1;
        el.max = size;
    }
    else{
        el.max = size;
        el.value = current_array[index-1];
    }
    return el;
}