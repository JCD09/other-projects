  # based on: https://web.stanford.edu/class/archive/cs/cs107/cs107.1166/guide_make.html
# CROSS_TOOL is armv7l-unknown-linux-gnueabihf-gcc on my machine

C_TOOL = arm-linux-gnueabihf-gcc
CFLAGS = -Wall -g -std=c99 -D _POSIX_C_SOURCE=200809L -Werror
CFLAGS_NW = -U_FORTIFY_SOURCE -D_FORTIFY_SOURCE=0 -O0
# `pkg-config --cflags glib-2.0`
# SOURCES = Display.c Definitions.c segDisplay.c
SOURCES = Definitions.c Sorter.c Listener.c Display.c Potentiometer.c main.c
OBJECTS = $(SOURCES:.c=.o)
TARGET = pot2
LIBS = -lpthread


$(TARGET): $(OBJECTS)
	$(C_TOOL) $(CFLAGS) $(SOURCES) -o $(TARGET) $(LIBS)
#	patchelf --set-interpreter /lib/ld-linux-armhf.so.3 $(TARGET)
#	cp $(TARGET)  /home/misha/NFS/public/
	cp $(TARGET) $(HOME)/cmpt433/public/myApps/

noworky: 
	$(C_TOOL) $(CFLAGS) $(CFLAGS_NW) noworky.c -o noworky 
#	patchelf --set-interpreter /lib/ld-linux-armhf.so.3 noworky
#	cp noworky  /home/misha/NFS/public/
	cp noworky $(HOME)/cmpt433/public/myApps/

all: $(TARGET) noworky

clean :
	rm -r $(OBJECTS) $(TARGET) noworky