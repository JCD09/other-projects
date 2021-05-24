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


#include "Definitions.h"
#include "Potentiometer.h"


#define I2CDRV_LINUX_BUS0 "/dev/i2c-0"
#define I2CDRV_LINUX_BUS1 "/dev/i2c-1"
#define I2CDRV_LINUX_BUS2 "/dev/i2c-2"

#define I2C_DEVICE_ADDRESS 0x20

#define REG_DIRA 0x00
#define REG_DIRB 0x01

#define REG_OUTA 0x14
#define REG_OUTB 0x15

#define LEFT_DISPLAY "/sys/class/gpio/gpio61/value"
#define RIGHT_DISPLAY "/sys/class/gpio/gpio44/value"

int i2c_file_desc;

char left_digit;
char right_digit; 

// delay 
struct timespec delay = {.tv_sec=0, .tv_nsec=5000000};

typedef struct Digit{
    unsigned char upper; 
    unsigned char lower; 
} Digit;

// TODO: Change numbers to single 
Digit digit_codes[] = {
    {.upper=0x86,.lower = 0xA1},
    {.upper=0x02|0x10,.lower=0x80},
    {.upper=(0x04|0x02)|0x08,.lower=0x01|0x20|0x10},
    {.upper=0x04|0x02, .lower=0x10|0x80|0x20},
    {.upper=0x80|0x08|0x02,.lower=0x10|0x80},
    {.upper=0x04|0x80|0x08,.lower=0x10|0x80|0x20},
    {.upper=0x8c,.lower=0xb1},
    {.upper=0x14,.lower = 0x04},
    {.upper=0x8E,.lower = 0xb1},
    {.upper=0x8E,.lower = 0xB0},
};

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

void configure_pins(){
    printf("configuring Pins \n");
    int pin_one = system("config-pin P9_18 i2c");
    int pin_two = system("config-pin P9_17 i2c");
    if(pin_one==-1||pin_two==-1){
        printf("error occurred\n");
        exit(1);
    }
}

void show(char digit){
    int index = digit-'0';
    writeI2cReg(i2c_file_desc, REG_OUTA, digit_codes[index].lower);
	writeI2cReg(i2c_file_desc, REG_OUTB, digit_codes[index].upper);

};

void cleanup_routine(){
    printf("3.0 Executing display cleanup routine \n");

    printf("3.1 Turning off left and right display \n");
    write_to_file(LEFT_DISPLAY,"0");
    write_to_file(RIGHT_DISPLAY,"0");

    printf("3.2 Closing file descriptor \n");
    close(i2c_file_desc);

}

// probably unnecessary step; 
void update(int arr_min){
    int value;
    if((value = (arr_min/100)) > 0){
        left_digit=9+'0';
        right_digit=9+'0';
    }
    else if((value = arr_min/10)>0){
        left_digit=arr_min/10 + '0';
        right_digit=arr_min % 10 + '0';
    }
    else{
        left_digit=0+'0';
        right_digit=arr_min % 10 + '0'; 
    }
}


void* display_run(void* args){

    // export pins 61 and 44
    write_to_file("/sys/class/gpio/export", "61");
    write_to_file("/sys/class/gpio/export", "44");

    // set direction of of the corresponding pins to out 
    write_to_file("/sys/class/gpio/gpio61/direction", "out");
    write_to_file("/sys/class/gpio/gpio44/direction", "out");

    configure_pins();

    i2c_file_desc = initI2cBus(I2CDRV_LINUX_BUS1, I2C_DEVICE_ADDRESS);

    // set registers to output; 
    writeI2cReg(i2c_file_desc, REG_DIRA, 0x00);
    writeI2cReg(i2c_file_desc, REG_DIRB, 0x00);

    pthread_cleanup_push(cleanup_routine,NULL);

    update(0);

    while(true){
        pthread_testcancel();
        write_to_file(LEFT_DISPLAY,"0");
        write_to_file(RIGHT_DISPLAY,"0");

        show(left_digit);
        write_to_file(LEFT_DISPLAY,"1"); 
        nanosleep(&delay, (struct timespec *) NULL);

        write_to_file(LEFT_DISPLAY,"0");
        write_to_file(RIGHT_DISPLAY,"0");

        show(right_digit);
        write_to_file(RIGHT_DISPLAY,"1");
        nanosleep(&delay, (struct timespec *) NULL);
        update(get_arrays_per_sec());

        pthread_testcancel();
    };
    pthread_cleanup_pop(1);

    return NULL;
  
}