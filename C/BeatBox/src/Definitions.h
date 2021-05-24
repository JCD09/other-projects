#ifndef DEFINITIONS_H
#define DEFINITIONS_H "Definitions.h"
//the entire header file file

typedef char* string;

typedef enum Direction{
  UP, DOWN, LEFT, RIGHT, CENTER } Direction ;

typedef struct Joystick_State {
  _Bool up;
  _Bool down;
  _Bool left;
  _Bool right;
  _Bool center;
} Joystick_State;

typedef enum Led_Command {
    LED_ONE_ON,
    LED_THREE_ON,
    LED_CORRECT,
    LED_INCORRECT
} Led_Command;


void write_to_file(string file_path, string data);

#endif
