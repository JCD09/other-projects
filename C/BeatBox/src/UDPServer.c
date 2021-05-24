//
// Created by misha on 10/31/19.
//

#include <sys/socket.h>
#include <netinet/in.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h> // to close socket;
#include <string.h> // for memset;
#include "BeatGenerator.h"

#define MAX_LEN 1024
#define PORT 12345

struct sockaddr_in sin_local;
struct sockaddr_in sin_third;
struct sockaddr_in sin_remote;
unsigned int sin_len_remote = sizeof(sin_remote);


int socket_descriptor;
char message[MAX_LEN]={};
char reply[MAX_LEN]={};

void init_server(){

    memset(&sin_local, 0, sizeof(sin_local));
    sin_local.sin_family = AF_INET;
    sin_local.sin_addr.s_addr = htonl(INADDR_ANY);
    sin_local.sin_port = htons(PORT);

    socket_descriptor = socket(PF_INET, SOCK_DGRAM, 0);
    bind(socket_descriptor, (struct sockaddr*) &sin_local, sizeof(sin_local));
}

void listener_cleanup(){
    printf("1.0 Executing listener cleanup routine\n");
    printf("1.1 Closing socket\n");
    close(socket_descriptor);
}

// http://blockofcodes.blogspot.com/2013/07/how-to-convert-string-to-integer-in-c.html//
int to_int(char* start, uint16_t size){
    int val = 0;
    int ten=1;
    for(int i=size-1; i>=0; i--){
        printf("val %d", val);
        val = val + (start[i]-'0') * ten;
        ten= ten * 10;
    }
    return val;
}

void* udp_server_run(void* args){

    pthread_cleanup_push(listener_cleanup,NULL);
    while(true){
        int bytes_recieved = recvfrom(socket_descriptor, message, MAX_LEN,
                0, (struct sockaddr*) &sin_remote, &sin_len_remote);
        sin_third=sin_remote;
        int term_index = (bytes_recieved < MAX_LEN) ? bytes_recieved-1 : MAX_LEN-1;
                message[term_index] = 0;

        char* first = strtok(message," ");
        char* second = strtok(NULL," ");

        sin_len_remote = sizeof(sin_remote);
        if(strcmp(first,"stop")==0){
            // terminating application
        }
        else if(strcmp(first,"beat")==0){
            if(strcmp(second,"rock")==0){
                set_new_beat(Rock);
            }
            else if(strcmp(second,"none")==0){
                set_new_beat(None);
            }
            else if(strcmp(second,"custom")){
                printf("custom\n");
            }
            else{}
        }
        else if(strcmp(first,"volume")==0){
            set_new_volume(to_int(second,strlen(second)));
        }
        else if(strcmp(first,"tempo")==0){
            set_new_tempo(to_int(second,strlen(second)));
        }
        else if(strcmp(first,"commandReply")){
            printf("command reply \n");
        }
        else{
            printf("message is %s\n",message);
        }
    }
    pthread_cleanup_pop(1);

    return NULL;
}

void update_volume(int curr_volume){
    memset(reply,'\0',MAX_LEN);
    sprintf(reply,"volume %d",curr_volume);
    sendto(socket_descriptor,reply,strlen(reply),0,
           (struct sockaddr*) &sin_remote,sin_len_remote);
}

void update_tempo(int curr_volume){
    printf("sending update tempo \n");
    memset(reply,'\0',MAX_LEN);
    sprintf(reply,"tempo %d",curr_volume);
    sendto(socket_descriptor,reply,strlen(reply),0,
           (struct sockaddr*) &sin_remote,sin_len_remote);
    sprintf(reply,"tempo %d",curr_volume);
    sendto(socket_descriptor,reply,strlen(reply),0,
           (struct sockaddr*) &sin_remote,sin_len_remote);
}