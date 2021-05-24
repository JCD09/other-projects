# based on: https://web.stanford.edu/class/archive/cs/cs107/cs107.1166/guide_make.html
# CROSS_TOOL is armv7l-unknown-linux-gnueabihf-gcc on my machine

C_TOOL = arm-linux-gnueabihf-gcc
CFLAGS = -Wall -g -std=c99 -D _POSIX_C_SOURCE=200809L -Werror
CFLAGS_NW = -U_FORTIFY_SOURCE -D_FORTIFY_SOURCE=0 -O0

# `pkg-config --cflags glib-2.0`

SOURCES = src/main.c src/BeatGenerator.c src/SoundBite.c src/AudioMixer.c src/Accelerometer.c src/Joystick.c src/Definitions.c src/UDPServer.c
OBJECTS = $(SOURCES:.c=.o)
TARGET = src/as3
LIBS = -lasound -lpthread -lrt


all : $(TARGET) node
	cp -r beatbox-wav-files $(HOME)/NFS/public/as3

$(TARGET): $(OBJECTS)
	$(C_TOOL) $(CFLAGS) $(SOURCES) -o $(TARGET) $(LIBS)
#	patchelf --set-interpreter /lib/ld-linux-armhf.so.3 $(TARGET)
#	cp $(TARGET)  /home/misha/NFS/public/as3
	cp $(TARGET) $(HOME)/cmpt433/public/myApps/

node :
	make --directory=beatbox-server-copy
clean :
	rm -r $(OBJECTS) $(TARGET)
