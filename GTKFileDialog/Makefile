#Destination folder for compiled C files
C_BIN=native/bin

#Source folder for C
C_SRC=native/src

#Destination folder for compiled Java
JAVA_BIN=bin

#Source folder for Java
JAVA_SRC=src

#C++ Compiler
CC=gcc

JAVA_HOME=/usr/java/default

all: GtkFileDialog libGtkFileDialogPeer.so

init:
	mkdir -p $(C_BIN)
	
clean:
	rm -f $(C_BIN)/*

GtkFileDialog: init
	$(CC) -Wall -g $(C_SRC)/GtkFileDialog.c -o $(C_BIN)/GtkFileDialog `pkg-config --cflags gtk+-2.0` \
	`pkg-config --libs gtk+-2.0`
	
libGtkFileDialogPeer.so:
	$(CC) -o $(C_BIN)/libGtkFileDialogPeer.so -shared -Wl,-soname,$(C_BIN)/libGtkFileDialogPeer.so \
	`pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` `pkg-config --libs gthread-2.0` \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	$(C_SRC)/GtkFileDialogPeer.c \
	-static -lc

GtkFileDialogPeer.h:
	${JAVA_HOME}/bin/javah -classpath $(JAVA_BIN) -o native/src/GtkFileDialogPeer.h sun.awt.X11.GtkFileDialogPeer
	