#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>

#include "Joystick.h"
#include "BeatGenerator.h"

typedef struct gpio_pin{
  _Bool is_valid;
  string pin_number;
  char * pin_folder_path;
  Direction pin;
}gpio_pin;


gpio_pin joystick_pins[] = {
  {.is_valid=true, .pin_number = "26", .pin_folder_path ="/sys/class/gpio/gpio26" , .pin = UP},
  {.is_valid=true, .pin_number = "47", .pin_folder_path ="/sys/class/gpio/gpio47" , .pin = RIGHT},
  {.is_valid=true, .pin_number = "46", .pin_folder_path ="/sys/class/gpio/gpio46" , .pin = DOWN},
  {.is_valid=true, .pin_number = "65",.pin_folder_path ="/sys/class/gpio/gpio65" , .pin = LEFT},
  {.is_valid=true, .pin_number = "27", .pin_folder_path ="/sys/class/gpio/gpio27", .pin = CENTER},
  {.is_valid=false, .pin_number = "", .pin_folder_path = NULL , .pin = CENTER}
};

Joystick_State get_joystick_state();

void* jystk_run(void* args){
    Joystick_State j_state;
    while(true){
        j_state=get_joystick_state();
        if(j_state.up){
            inc_volume();
        }
        else if(j_state.down){
            dec_volume();
        }
        else if(j_state.left){
           dec_tempo();
        }
        else if(j_state.right){
            inc_tempo();
        }
        if(j_state.center){
            change_beat();
        }

        struct timespec sleep_interval;
        sleep_interval.tv_sec=0;
        sleep_interval.tv_nsec=200000000;
        nanosleep(&sleep_interval,NULL);
    }
    return NULL;
}

void export_pins(){
    char* export_file_path = "/sys/class/gpio/export";
    //47 46 65 27
    write_to_file(export_file_path,"26");
    write_to_file(export_file_path,"46");
    write_to_file(export_file_path,"65");
    write_to_file(export_file_path,"27");
    write_to_file(export_file_path,"47");
    struct timespec sleep_interval;
    sleep_interval.tv_sec=0;
    sleep_interval.tv_nsec=400000000;
    nanosleep(&sleep_interval,NULL);
}

void enable_pins(){
    for(gpio_pin* pin = joystick_pins; (*pin).is_valid != false; pin++){
        char* dir_file_path;
        int ret = asprintf(&dir_file_path, "%s%s",(*pin).pin_folder_path, "/direction");
        if(ret==-1){exit(1);}
        write_to_file(dir_file_path,"in");
    }
}


void init_jystk(){
    export_pins();
    enable_pins();
}


_Bool is_pressed(char* file_path){

    char* value_file_path;
    int ret = asprintf(&value_file_path, "%s%s",file_path, "/value");
    if(ret==-1){exit(1);}

    FILE *file = fopen(value_file_path, "r");
    if (file == NULL) {
        printf("ERROR: Unable to open file (%s) for read\n", value_file_path);
        exit(-1);
    }

    // set up buffer
    const int max_length = 1024;
    char buff[max_length];
    char* res = fgets(buff, max_length, file);
    if(res==NULL){
        exit(1);
    }
      // Close
    fflush(file);
    fclose(file);
    free(value_file_path);

    if(buff[0]=='0'){return true;}
    else if(buff[0]=='1'){return false;}
    else{printf("ERROR occured \n");exit(1);}
    return true;
}

Joystick_State get_joystick_state(){
    Joystick_State state;
    state.up = is_pressed(joystick_pins[0].pin_folder_path);
    state.right = is_pressed(joystick_pins[1].pin_folder_path);
    state.down = is_pressed(joystick_pins[2].pin_folder_path);
    state.left = is_pressed(joystick_pins[3].pin_folder_path);
    state.center = is_pressed(joystick_pins[4].pin_folder_path);
    return state;
}

_Bool is_released(Joystick_State state){
    // Joystick_State state = get_joystick_state();
    if(state.up==true){return false;}
    else if(state.down == true){return false;}
    else if(state.left == true){return false;}
    else if(state.right == true){return false;}
    else if(state.center == true){return false;}
    else{return true;}
}

Direction wait_for_input(){
    Joystick_State state;
    while(1){
        state = get_joystick_state();
        _Bool released = is_released(state);
        if(released==false){break;}
        else{
            struct timespec reqDelay = {0, 100000000};
            nanosleep(&reqDelay, (struct timespec *) NULL);
        }
    }
    if((state.left==true)){return LEFT;}
    else if(state.right==true){return RIGHT;}
    else if(state.up==true){return UP;}
    else if (state.down == true){return DOWN;}
    else  {return CENTER;}

}

void wait_until_released(){
    while(is_released(get_joystick_state())==false){
    }
}
