//
// Created by misha on 10/28/19.
//
#ifndef AS2_AUDIOMIXER_H
#define AS2_AUDIOMIXER_H

#include "Beat.h"

void initiate_playback();
void init_audio_mixer();
void* play_beat(void* args);
void enqueue_soundbite(Beat* beat);

void inc_volume();
void dec_volume();
void set_new_volume(int volume);

#endif //AS2_AUDIOMIXER_H
