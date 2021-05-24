#include "SoundBite.h"

bool is_evictable(SoundBite* sb){
    return sb->offset >= sb->size;
}
void increment_offset(SoundBite* sb){
    sb->offset=sb->offset+1;
}