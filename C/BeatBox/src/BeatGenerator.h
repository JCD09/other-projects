//
// Created by misha on 10/28/19.
//

#include <stdint.h>
#include "AudioMixer.h"
#include "Beat.h"

#ifndef AS2_BEATGENERATOR_H
#define AS2_BEATGENERATOR_H

void init_beat_generator();
void* generate_beats(void* args);
enum Name{
    None,
    Rock,
    Custom
};
typedef struct Track{
    enum Name name;
    uint8_t interval;
    uint8_t* pattern;
}Track;

void inc_tempo();
void dec_tempo();

void set_new_tempo(int new_tempo);

void change_beat();
void set_new_beat(enum Name new_beat);
#endif //AS2_BEATGENERATOR_H
