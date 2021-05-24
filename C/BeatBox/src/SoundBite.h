#ifndef AS2_SOUNDBITE_H
#define AS2_SOUNDBITE_H

#include <stdint.h>
#include <stdbool.h>

typedef struct SoundBite{
    int16_t* sound;
    uint32_t offset;
    uint32_t size;
}SoundBite;

typedef struct ModSoundBite{
    int16_t* sound;
    uint32_t offset;
    uint32_t size;
}ModSoundBite;

bool is_evictable(SoundBite* sb);
void increment_offset(SoundBite* sb);
#endif //AS2_SOUNDBITE_H
