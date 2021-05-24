//
// Created by misha on 10/28/19.
//
#include <string.h>
#include <stdlib.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <pthread.h>
#include <stdbool.h>
#include <unistd.h>

#include "BeatGenerator.h"
#include "AudioMixer.h"
#include "UDPServer.h"


#define NUM_BEATS 8
#define CC "./beatbox-wav-files/Rock/cc.wav"
#define BD_HARD "./beatbox-wav-files/Rock/bd-hard.wav"
#define SNARE_SOFT "./beatbox-wav-files/Rock/snare-soft.wav"

#define One "./beatbox-wav-files/Custom/100066__menegass__gui-drum-tom-mid-hard.wav"
#define TWO "./beatbox-wav-files/Custom/100056__menegass__gui-drum-cyn-hard.wav"
#define THREE "./beatbox-wav-files/Custom/100061__menegass__gui-drum-splash-soft.wav"

void readWaveFileIntoMemory(char *fileName, Beat* beat);
void* generate_beats(void* args);
void add_sounds_to_playback(uint8_t pattern);

pthread_t generator_thread;

enum Name current_beat;
static Track tracks[8];
static Beat beats[NUM_BEATS];

double BPM = 120;

Track new_track(enum Name name){
    Track track;
    if(name == None){
        track.name=None;
        track.interval=0;
        track.pattern=NULL;
        return track;
    }
    else if (name == Rock) {
        track.name=Rock;
        track.interval=4;
        uint8_t* pattern = malloc(4*sizeof(uint8_t));
        pattern[0]=0x03;  // 0000 0011;
        pattern[1]=0x01;  // 0000 0001;
        pattern[2]=0x05;  // 0000 0101;
        pattern[3]=0x01;  // 0000 0001;
        track.pattern=pattern;
        return track;
    }
    else{
        track.name=Custom;
        track.interval=4;
        uint8_t* pattern = malloc(4*sizeof(uint8_t));
        pattern[0]=0x18;  // 0001 1000;
        pattern[1]=0x08;  // 0000 1000;
        pattern[2]=0x28;  // 0010 1000;
        pattern[3]=0x08;  // 0000 1000;
        track.pattern=pattern;
        return track;
    }
}
//static enum Tracks current_track;


void init_beat_generator(){
    readWaveFileIntoMemory(CC,&beats[0]);
    readWaveFileIntoMemory(BD_HARD,&beats[1]);
    readWaveFileIntoMemory(SNARE_SOFT,&beats[2]);

    readWaveFileIntoMemory(One,&beats[3]);
    readWaveFileIntoMemory(TWO,&beats[4]);
    readWaveFileIntoMemory(THREE,&beats[5]);

    printf("%d %d %d \n", beats[0].size,beats[1].size,beats[2].size);
    current_beat=Rock;
}

void change_beat(){
    if(current_beat==None){
        current_beat=Rock;
    }
    else if(current_beat==Rock){
        current_beat=Custom;
    }
    else{
        current_beat=None;
    }
}

void set_new_beat(enum Name new_beat){
    current_beat=new_beat;
}

void* generate_beats(void* args) {
    tracks[0] = new_track(None);
    tracks[1] = new_track(Rock);
    tracks[2] = new_track(Custom);
//    printf("starting play beat generator\n");
    while (true) {
        printf("current beat is %d",current_beat);
        if (current_beat == None) {
            for (;;) {
                struct timespec sleep_interval;
                sleep_interval.tv_sec = 0;
                sleep_interval.tv_nsec = 300000000;
                nanosleep(&sleep_interval, NULL);
                if (current_beat != None) { break; }
            }
        } else if (current_beat == Rock) {
            printf("Playing Rock Beat\n");
            uint32_t index = 0;
            uint32_t interval = tracks[1].interval;
            for (;;) {
//                printf("adding sound to playback %d %d \n", index, interval);
                add_sounds_to_playback(tracks[1].pattern[index % interval]);
                struct timespec sleep_interval;

                uint64_t val = (uint64_t) ((30 / BPM) * 1000000000);
                sleep_interval.tv_sec = val / 999999999;
                sleep_interval.tv_nsec = val % 999999999;
                nanosleep(&sleep_interval, NULL);

                if (current_beat != Rock) { break; }
                index++;
            }
        } else {
            printf("Playing Custom Beat\n");
            uint32_t index = 0;
            uint32_t interval = tracks[2].interval;
            for (;;) {
//                printf("adding sound to playback %d %d \n", index, interval);
                add_sounds_to_playback(tracks[2].pattern[index % interval]);
                struct timespec sleep_interval;

                uint64_t val = (uint64_t) ((30 / BPM) * 1000000000);
                sleep_interval.tv_sec = val / 999999999;
                sleep_interval.tv_nsec = val % 999999999;
                nanosleep(&sleep_interval, NULL);

                if (current_beat != Custom) { break; }
                index++;
            }
        }

    }
    return NULL;
}

void inc_tempo(){
    double temp = BPM+5;
    if(temp>200){BPM=200;}
    else{BPM=temp;}
    update_tempo((int) BPM);
}
void dec_tempo(){
    double temp = BPM-5;
    if(temp<40){BPM=40;}
    else{BPM=temp;}
    update_tempo((int) BPM);
}

void set_new_tempo(int new_tempo){
//    printf("new tempo is %d \n", new_tempo);
    BPM = new_tempo;
}

void add_sounds_to_playback(uint8_t pattern){
    for(int i = 0; i < NUM_BEATS; i++){
        if(pattern & (0x1 << i)){
//           printf("adding beat at index %i with pattern %d is \n",i, pattern);
           enqueue_soundbite(&beats[i]);
        }
    }
}

void readWaveFileIntoMemory(char *fileName, Beat* beat)
{
    // The PCM data in a wave file starts after the header:
    const int PCM_DATA_OFFSET = 44;

    // Open the wave file
    FILE *file = fopen(fileName, "r");
    if (file == NULL) {
        fprintf(stderr, "ERROR: Unable to open file %s.\n", fileName);
        exit(EXIT_FAILURE);
    }

    // Get file size
    fseek(file, 0, SEEK_END);
    int sizeInBytes = ftell(file) - PCM_DATA_OFFSET;
    beat->size = sizeInBytes / 2;

    // Search to the start of the data in the file
    fseek(file, PCM_DATA_OFFSET, SEEK_SET);

    // Allocate space to hold all PCM data
    beat->sound = malloc(sizeInBytes);
    if (beat->sound == 0) {
        fprintf(stderr, "ERROR: Unable to allocate %d bytes for file %s.\n",
                sizeInBytes, fileName);
        exit(EXIT_FAILURE);
    }

    // Read PCM data from wave file into memory
    int samplesRead = fread(beat->sound, 2, beat->size, file);
    if (samplesRead != beat->size) {
        fprintf(stderr, "ERROR: Unable to read %d samples from file %s (read %d).\n",
                beat->size, fileName, samplesRead);
        exit(EXIT_FAILURE);
    }
}