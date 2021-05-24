#include <sys/socket.h>
#include <netinet/in.h>
#include <stdbool.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h> // to close socket;
#include <string.h> // for memset;

#include "Sorter.h"

#define MAX_LEN 1024 
#define PORT 12345

int socket_descriptor;

void listener_cleanup(){
    printf("1.0 Executing listener cleanup routine\n");
    printf("1.1 Closing socket\n");
    close(socket_descriptor);
}

void* listener_run(void* args){
    struct sockaddr_in sin; 
    memset(&sin, 0, sizeof(sin));

    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = htonl(INADDR_ANY);
    sin.sin_port = htons(PORT);

    socket_descriptor = socket(PF_INET, SOCK_DGRAM, 0);
    bind(socket_descriptor, (struct sockaddr*) &sin, sizeof(sin));

    struct sockaddr_in sin_remote;
    unsigned int sin_len = sizeof(sin_remote);

    char message[MAX_LEN]={};
    char reply[MAX_LEN]={};

    pthread_cleanup_push(listener_cleanup,NULL);

    while(true){

        int bytes_recieved = recvfrom(socket_descriptor, message, MAX_LEN,
        0, (struct sockaddr*) &sin_remote, &sin_len);

        int term_index = (bytes_recieved < MAX_LEN) ? bytes_recieved-1 : MAX_LEN-1;
        message[term_index] = 0;

        char* first = strtok(message," ");
        char* second = strtok(NULL," ");

        sin_len = sizeof(sin_remote);
        if(strcmp(first,"stop")==0){
            sprintf(reply,"Terminating\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
            break;
        }
        else if(strcmp(first,"help")==0){
            sprintf(reply,"Accepted command examples:\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
            sprintf(reply,"count -- display number arrays sorted.\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
            sprintf(reply,"get length -- display length of array currently being sorted.\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
            sprintf(reply,"get array -- display the full array being sorted.\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
            sprintf(reply,"get 10 -- display the tenth element of array\n");
            sendto(socket_descriptor,reply,strlen(reply),0,
                (struct sockaddr*) &sin_remote,sin_len);
        }
        else if(strcmp(first,"count")==0){
            sprintf(reply,"Numbe of arrays sorted is %d\n", get_arrays_sorted());
            sendto(socket_descriptor,reply,strlen(reply),0,
                (struct sockaddr*) &sin_remote,sin_len);
        }
        else if(strcmp(first,"get")==0){
            if(strcmp(second,"length")==0){
                sprintf(reply,"Current array length is %d\n", get_curr_array_size());
                sendto(socket_descriptor,reply,strlen(reply),0,
                    (struct sockaddr*) &sin_remote,sin_len);
            }
            else if(strcmp(second,"array")==0){
                Array curr = get_curr_array();
                int index = 0;
                int line_size = 62;

                while(index < curr.size){
                    memset(reply,0,MAX_LEN);
                    printf("inside index loop %d\n", curr.array[index]);
                    // index++;
                    int buffer_index = 0;
                    
                    while((MAX_LEN-buffer_index)/64 > 0 ){
                        if(index >= curr.size){break;}
                        int line_index = 0;
                        char line[64] = {}; 

                        while(line_index <= line_size){
                            if(index >= curr.size){
                              sprintf(line+line_index-2,"\n");
                              break;}
                            int bw = sprintf(line+line_index,"%d ,",curr.array[index]);
                            if(line_index+bw>line_size){
                                bw = sprintf(line+line_index,"\n"); 
                                line_index=line_index+bw;
                                break;   
                            }
                            else{
                                bw = sprintf(line+line_index,"%d, ",curr.array[index]);
                                line_index=line_index+bw;
                                index++;
                            }  
                        }
                        int bw = sprintf(reply+buffer_index,"%s",line);
                        buffer_index=buffer_index+bw;
                    }
                    sendto(socket_descriptor,reply,strlen(reply),0,
                    (struct sockaddr*) &sin_remote,sin_len);
                }
                free(curr.array);
            }
            else{
                if(second==NULL||atoi(second)==0){
                    sprintf(reply,"Incorrect command \n");
                    sendto(socket_descriptor,reply,strlen(reply),0,
                    (struct sockaddr*) &sin_remote,sin_len);
                }
                else{
                    int index = atoi(second); 
                    Element el = get_element(index);
                    if(el.value==-1){
                        sprintf(reply,"Invalid argument. Must be between 1 and %d (array length).\n",
                        el.max
                        );
                    }
                    else{
                        sprintf(reply,"Value %d = %d \n", index, el.value);
                    }
                    sendto(socket_descriptor,reply,strlen(reply),0,
                    (struct sockaddr*) &sin_remote,sin_len);
                }
            }
        }
        else{
            sin_len = sizeof(sin_remote);
            sprintf(reply,"Incorrect command \n");
            sendto(socket_descriptor,reply,strlen(reply),0,
            (struct sockaddr*) &sin_remote,sin_len);
        }
    }
    pthread_cleanup_pop(1);

    return NULL;
}