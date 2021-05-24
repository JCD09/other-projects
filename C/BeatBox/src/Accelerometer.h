//
// Created by misha on 10/29/19.
//

#ifndef AS2_ACCELEROMETER_H
#define AS2_ACCELEROMETER_H

#include "Beat.h"

unsigned char readI2cReg(int i2cFileDesc, unsigned char regAddr);
int initI2cBus(char* bus, int address);
void writeI2cReg(int i2cFileDesc, unsigned char regAddr, unsigned char value);
void readWaveFileIntoMemory(char *fileName, Beat* beat);
unsigned char readI2cReg(int i2cFileDesc, unsigned char regAddr);

void init_acclr();
void* acclr_run(void* args);



#endif //AS2_ACCELEROMETER_H
