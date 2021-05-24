//
// Created by misha on 10/31/19.
//

#ifndef AS2_UDPSERVER_H
#define AS2_UDPSERVER_H

void init_server();
void* udp_server_run(void* args);
void update_volume(int curr_volume);
void update_tempo(int curr_tempo);

#endif //AS2_UDPSERVER_H
