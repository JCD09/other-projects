#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <pthread.h>
#include <unistd.h>
#include <stdbool.h>
#include <signal.h>

#include "BeatGenerator.h"
#include "AudioMixer.h"
#include "Accelerometer.h"
#include "SoundBite.h"
#include "Accelerometer.h"
#include "Joystick.h"
#include "UDPServer.h"

#define SNARE_SOFT "./Acclr/snare-soft.wav"

int main() {


//    pid_t pid;
//    pid = fork();
//
//    if (pid < 0) { perror("fork"); exit(0); }
//    if (pid == 0) {
//
//        printf("Child process created and will now wait for signal...\n");
//        int cdir = chdir("./node/10-UdpRelay-copy/");
//        int sys = system("node server.js");
//        printf("sstem %d %d", sys, cdir);
//
//    }
//    else {
//
//        //do
//        // some other work in parent process here
//        printf("Killing child (%ld) from parent process!", (long) pid);
////        kill(pid, SIGKILL);

        init_beat_generator();
        init_audio_mixer();
        init_acclr();
        init_jystk();
        init_server();

        //
        pthread_t beat_generator_th;
        pthread_t audio_mixer_th;
        pthread_t acclr_run_th;
        pthread_t jystk_run_th;
        pthread_t upd_server_th;
        //
        pthread_create(&beat_generator_th,NULL,&generate_beats,NULL);
        pthread_create(&audio_mixer_th,NULL,&play_beat,NULL);
        pthread_create(&acclr_run_th,NULL,&acclr_run,NULL);
        pthread_create(&jystk_run_th,NULL,&jystk_run,NULL);
        pthread_create(&upd_server_th,NULL,&udp_server_run,NULL);

        pthread_join(audio_mixer_th, NULL);

//        kill(pid,SIGCONT);

//    }
//    return 0;
}