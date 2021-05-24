#include <stdlib.h>
#include <alsa/asoundlib.h>
#include <alloca.h>
#include <pthread.h>
#include <stdbool.h>
#include <limits.h>
#include <semaphore.h>

#include "AudioMixer.h"
#include "SoundBite.h"
#include "UDPServer.h"


static snd_pcm_t *handle;

#define DEFAULT_VOLUME 80
#define SAMPLE_RATE 44100
#define NUM_CHANNELS 1
#define SAMPLE_SIZE (sizeof(short)) 			// bytes per sample
// Sample size note: This works for mono files because each sample ("frame') is 1 value.
// If using stereo files then a frame would be two samples.
static unsigned long playbackBufferSize = 0;
static int16_t *playbackBuffer = NULL;
static int curr_volume = 0;


void AudioMixer_setVolume(int newVolume);
void init_aggregator();
void play_audio();
void play_track();
void zero_buffer();
void readWaveFileIntoMemory1(char *fileName, Beat* beat);
bool empty();
bool full();
int get_num_evict();

////
//// Created by misha on 10/28/19.
////

#define NUM_SOUNDBITES 30
SoundBite soundBites[NUM_SOUNDBITES];
pthread_mutex_t mutexes[NUM_SOUNDBITES];

pthread_mutex_t audio_mtx=PTHREAD_MUTEX_INITIALIZER;


void fill_playback_buffer() {
//    printf("playing audio \n");
    int index = 0;
    zero_buffer();
//    pthread_mutex_lock(&audio_mtx);
    while (index < playbackBufferSize) {
        long sample = 0;
        for (int i = 0; i < NUM_SOUNDBITES; i++) {
            if(soundBites[i].offset<soundBites[i].size){
                int offset_value = soundBites[i].sound[soundBites[i].offset];
                sample=sample + offset_value;
                pthread_mutex_lock(&mutexes[i]);
                soundBites[i].offset=soundBites[i].offset+1;
                pthread_mutex_unlock(&mutexes[i]);
            }
        }
        if(sample > INT16_MAX){
            playbackBuffer[index]=INT16_MAX;
        }
        else if(sample<INT16_MIN){
            playbackBuffer[index]=INT16_MIN;
        }
        else{
            playbackBuffer[index]=sample;
        }
        index++;
    }
//    pthread_mutex_unlock(&audio_mtx);
}

void enqueue_soundbite(Beat* beat){

    for(int i = 0; i < NUM_SOUNDBITES; i++){
        if(is_evictable(&soundBites[i])){
//            pthread_mutex_lock(&audio_mtx);
            pthread_mutex_lock(&mutexes[i]);
            soundBites[i].sound=beat->sound;
            soundBites[i].offset=0;
            soundBites[i].size=beat->size;
            pthread_mutex_unlock(&mutexes[i]);
//            pthread_mutex_unlock(&audio_mtx);
            break;
        }
    }

}

void* play_beat(void* args){
    while(true){
        fill_playback_buffer();
        play_audio();
        }

return NULL;
}

void zero_buffer(){
    for(int j = 0; j < playbackBufferSize; j++){
        playbackBuffer[j]=0;
    }
}
void play_audio(){
    // Output the audio

    snd_pcm_sframes_t frames = snd_pcm_writei(handle,
                                              playbackBuffer, playbackBufferSize);


    // Check for (and handle) possible error conditions on output
    if (frames < 0) {
        fprintf(stderr, "AudioMixer: writei() returned %li\n", frames);
        frames = snd_pcm_recover(handle, frames, 1);
    }
    if (frames < 0) {
        fprintf(stderr, "ERROR: Failed writing audio with snd_pcm_writei(): %li\n",
                frames);
        exit(EXIT_FAILURE);
    }
if (frames > 0 && frames < playbackBufferSize) {
printf("Short write (expected %li, wrote %li)\n",
playbackBufferSize, frames);
}
}

void init_audio_mixer(){
    AudioMixer_setVolume(DEFAULT_VOLUME);
    init_aggregator();

    // Open the PCM output
    int err = snd_pcm_open(&handle, "default", SND_PCM_STREAM_PLAYBACK, 0);
    if (err < 0) {
        printf("Playback open error: %s\n", snd_strerror(err));
        exit(EXIT_FAILURE);
    }
    // Configure parameters of PCM output
    err = snd_pcm_set_params(handle,
                             SND_PCM_FORMAT_S16_LE,
                             SND_PCM_ACCESS_RW_INTERLEAVED,
                             NUM_CHANNELS,
                             SAMPLE_RATE,
                             1,			// Allow software resampling
                             50000);		// 0.05 seconds per buffer
    if (err < 0) {
        printf("Playback open error: %s\n", snd_strerror(err));
        exit(EXIT_FAILURE);
    }

    // Allocate this software's playback buffer to be the same size as the
    // the hardware's playback buffers for efficient data transfers.
    // ..get info on the hardware buffers:
    unsigned long unusedBufferSize = 0;
    snd_pcm_get_params(handle, &unusedBufferSize, &playbackBufferSize);
    // ..allocate playback buffer:
    playbackBuffer = malloc(playbackBufferSize * sizeof(*playbackBuffer));

    // Launch playback thread:
    //    pthread_create(&playbackThreadId, NULL, playbackThread, NULL);
}


void AudioMixer_setVolume(int newVolume)
{
    // Ensure curr_volume is reasonable; If so, cache it for later getVolume() calls.
    if (newVolume < 0 || newVolume > 100) {
        printf("ERROR: Volume must be between 0 and 100.\n");
        return;
    }
    curr_volume = newVolume;

    long min, max;
    snd_mixer_t *handle;
    snd_mixer_selem_id_t *sid;
    const char *card = "default";
    const char *selem_name = "PCM";

    snd_mixer_open(&handle, 0);
    snd_mixer_attach(handle, card);
    snd_mixer_selem_register(handle, NULL, NULL);
    snd_mixer_load(handle);

    snd_mixer_selem_id_alloca(&sid);
    snd_mixer_selem_id_set_index(sid, 0);
    snd_mixer_selem_id_set_name(sid, selem_name);
    snd_mixer_elem_t* elem = snd_mixer_find_selem(handle, sid);

    snd_mixer_selem_get_playback_volume_range(elem, &min, &max);
    snd_mixer_selem_set_playback_volume_all(elem, curr_volume * max / 100);

    snd_mixer_close(handle);
}

void init_aggregator(){
    printf("init agregator \n");
    SoundBite empty;
    empty.sound=NULL;
    empty.size=0;
    empty.offset=0;

    // populate array with evictable soundbites;
    for(int i = 0; i < NUM_SOUNDBITES; i++){
        soundBites[i]=empty;
        pthread_mutex_init(&mutexes[i], NULL);
    }

}

void inc_volume(){
    curr_volume= curr_volume + 5;
    if(curr_volume > 100){ curr_volume=100;}
    AudioMixer_setVolume(curr_volume);
    update_volume(curr_volume);

    // send the update to listener;
};
void dec_volume(){
    curr_volume= curr_volume - 5;
    if(curr_volume < 0){ curr_volume=0;}
    AudioMixer_setVolume(curr_volume);
    update_volume(curr_volume);

    // must send the updated to UDP Server
};

void set_new_volume(int volume){
    printf("setting new volume \n");
    curr_volume=volume;
    AudioMixer_setVolume(curr_volume);
    update_volume(curr_volume);

}