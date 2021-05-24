#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>


#include "Listener.h"
#include "Sorter.h"
#include "Potentiometer.h"
#include "Display.h"

int main(){

    pthread_t listener_th; 
    pthread_t sorter_th;
    pthread_t potentiometer_th;
    pthread_t display_th;

    pthread_create(&listener_th, NULL, listener_run, NULL);
    pthread_create(&sorter_th,NULL,sorter_run,NULL);
    pthread_create(&potentiometer_th, NULL, potentiometer_run, NULL);
    pthread_create(&display_th,NULL,display_run,NULL);
    pthread_join(listener_th,NULL);
    pthread_cancel(sorter_th);
    pthread_join(sorter_th,NULL);
    pthread_cancel(display_th);
    pthread_join(display_th,NULL);
    pthread_cancel(potentiometer_th);
    pthread_join(potentiometer_th,NULL);

}