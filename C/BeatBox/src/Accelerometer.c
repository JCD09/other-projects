#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <linux/i2c.h>
#include <linux/i2c-dev.h>
#include <assert.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <signal.h>
#include <stdbool.h>
#include "Accelerometer.h"
#include "AudioMixer.h"

#define I2CDRV_LINUX_BUS0 "/dev/i2c-0"
#define I2CDRV_LINUX_BUS1 "/dev/i2c-1"
#define I2CDRV_LINUX_BUS2 "/dev/i2c-2"

#define I2C_DEVICE_ADDRESS 0x1c
#define I2C_DEVICE_ADDRESS 0x1c

#define Z "./beatbox-wav-files/Acclr/100066__menegass__gui-drum-tom-mid-hard.wav"
#define Y "./beatbox-wav-files/Acclr/100056__menegass__gui-drum-cyn-hard.wav"
#define X "./beatbox-wav-files/Acclr/100055__menegass__gui-drum-co.wav"

int i2c_file_desc;
Beat acclr_beats[3];
void init_acclr(){


    readWaveFileIntoMemory(Z,&acclr_beats[0]);
    readWaveFileIntoMemory(Y,&acclr_beats[1]);
    readWaveFileIntoMemory(X,&acclr_beats[2]);

    i2c_file_desc = initI2cBus(I2CDRV_LINUX_BUS1, I2C_DEVICE_ADDRESS);
    // set device into stand by mode
    writeI2cReg(i2c_file_desc,0x2A,0x00);
    // set +/- 2g;
    writeI2cReg(i2c_file_desc,0x0E,0x10);
    // set high pass filter;
    writeI2cReg(i2c_file_desc,0x0F,0x10);
    //    // set F_READ=1 to one
    writeI2cReg(i2c_file_desc,0x2A,0x01);
    // turn on active mode
    //    writeI2cReg(i2c_file_desc, 0x2A, 0x01);
    // write to 0x01 register to trigger signal;
}

// adopted from:
// https://github.com/ControlEverythingCommunity/
// MMA8452Q/blob/master/C/MMA8452Q.c
//
void* acclr_run(void* args){
    int prev_z = 0;
    int prev_y = 0;
    int prev_x = 0;
    int threshold_z = 4000;
    int threshold_xy = 8000;
    int i = 0;
    while (true) {
        char reg[1] = {0x00};
        int b = write(i2c_file_desc, reg, 1);
        //    char reg[1] = {0x00};
        if (b != 1) {
            exit(0);
        }
        char data[7] = {0};
        if (read(i2c_file_desc, data, 7) != 7) {
            exit(0);
        }

        int16_t a_x = (data[1] << 8) | (data[2]);
        int16_t a_y = (data[3] << 8) | (data[4]);
        int16_t a_z = (data[5] << 8) | (data[6]);

//        printf("x: %d, y %d, z: %d \n",a_x,a_y,a_z);
        if(abs(a_z-prev_z)>threshold_z){
            //printf("playing sound %d \n", a_z);
            enqueue_soundbite(&acclr_beats[0]);
        }
        if(abs(a_y-prev_y)>threshold_xy){
            //printf("playing sound %d \n", a_y);
            enqueue_soundbite(&acclr_beats[1]);
        }
        if(abs(a_x-prev_x)>threshold_xy){
            //printf("playing sound %d \n", a_x);
            enqueue_soundbite(&acclr_beats[2]);
        }

        prev_z=a_z;
        prev_y=a_y;
        prev_x=a_x;//        printf("%d %d %d \n", (int8_t )data[1], (int8_t )data[2], (int8_t )data[3]);

        struct timespec sleep_interval;
        sleep_interval.tv_sec=0;
        sleep_interval.tv_nsec=100000000;
        nanosleep(&sleep_interval,NULL);
        i++;
    }
}

int initI2cBus(char* bus, int address)
{
    int i2cFileDesc = open(bus, O_RDWR);
    if (i2cFileDesc < 0) {
        printf("I2C DRV: Unable to open bus for read/write (%s)\n", bus);
        perror("Error is:");
        exit(-1);
    }

    int result = ioctl(i2cFileDesc, I2C_SLAVE, address);
    if (result < 0) {
        perror("Unable to set I2C device to slave address.");
        exit(-1);
    }
    return i2cFileDesc;
}

void writeI2cReg(int i2cFileDesc, unsigned char regAddr, unsigned char value)
{
    unsigned char buff[2];
    buff[0] = regAddr;
    buff[1] = value;
    int res = write(i2cFileDesc, buff, 2);
    if (res != 2) {
        perror("Unable to write i2c register");
        exit(-1);
    }
}

unsigned char readI2cReg(int i2cFileDesc, unsigned char regAddr){
    // To read a register, must first write the addressint
    int res = write(i2cFileDesc, &regAddr, sizeof(regAddr));
    if (res != sizeof(regAddr)) {
        perror("I2C: Unable to write to i2c register.");
        exit(1);}
    // Now read the value and return it
    char value = 0;
    res = read(i2cFileDesc, &value, sizeof(value));
    if (res != sizeof(value)) {
        perror("I2C: Unable to read from i2c register");
        exit(1);}
    return value;
}


void cleanup_routine() {
}

// Created by misha on 10/29/19.
//

