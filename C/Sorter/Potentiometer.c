#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

#include "Sorter.h"

#define A2D_FILE_VOLTAGE0 "/sys/bus/iio/devices/iio:device0/in_voltage0_raw"
#define A2D_VOLTAGE_REF_V 1.8
#define A2D_MAX_READING 4095

int a2d_readings[] = {0,500,1000,1500,2000,2500,3000,3500,4000,4100};
int array_sizes[] = {1,20,60,120,250,300,500,800,1200,2100};

int array_size;
int arrays_sorted_per_sec;

typedef struct Indices{
    int left;
    int right; 
} Indices;


int get_voltage_reading()
{
    // Open file
    FILE *f = fopen(A2D_FILE_VOLTAGE0, "r");
    if (!f) {
        printf("ERROR: Unable to open voltage input file. Cape loaded?\n");
        printf(" Check /boot/uEnv.txt for correct options.\n");
        exit(-1);
    }
    // Get reading
    int a2dReading = 0;
    int itemsRead = fscanf(f, "%d", &a2dReading);
    if (itemsRead <= 0) {
        printf("ERROR: Unable to read values from voltage input file.\n");
        exit(-1);
    }
    // Close file
    fclose(f);
    return a2dReading;
}

Indices get_indices(int a2d_reading){
    for(int i = 0; i <= 8; i ++){
        if(a2d_reading>a2d_readings[i]&&a2d_reading<a2d_readings[i+1]){          
            Indices idx = {.left=i,.right=i+1};
            return idx;
        }
        else if(a2d_reading==a2d_readings[i]||a2d_reading==a2d_readings[i+1]){      
            Indices idx = {.left=i,.right=i};
            return idx;
        }
        else{}
    }
    Indices idx = {.left=-1,.right=-1};
    return idx;
}


int calculate_array_size(int a2d_reading){

    Indices indices = get_indices(a2d_reading);
    if(indices.left == indices.right){
        return array_sizes[indices.left];
    }
    int del_y = (array_sizes[indices.right]-array_sizes[indices.left]);
    int del_x = (a2d_readings[indices.right]-a2d_readings[indices.left]);
    double slope = del_y/(double)del_x;

    double size = slope*(a2d_reading-a2d_readings[indices.left])+array_sizes[indices.left];
    return (int) size;
}

void potentiometer_cleanup(){
    printf("4.0 Executing potentiometer sorter_cleanup routine\n");
}

void* potentiometer_run(void *args){
    int arr_sorted;
    pthread_cleanup_push(potentiometer_cleanup, NULL);
     while(true){
        array_size=calculate_array_size(get_voltage_reading());
        printf("current array size is %d\n", array_size);
        arr_sorted = get_arrays_sorted();
        sleep(1);
        arrays_sorted_per_sec = get_arrays_sorted()-arr_sorted;
        pthread_testcancel();
    }
    pthread_cleanup_pop(1);
}

int get_array_size(){
    return array_size;
}

int get_arrays_per_sec(){
    return arrays_sorted_per_sec;
}