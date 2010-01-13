#Destination folder for compiled C files
C_BIN=native/bin

#Source folder for C
C_SRC=native/src

#Destination folder for compiled Java
JAVA_BIN=bin

#Source folder for Java
JAVA_SRC=src
JAVA_TEST=test

#C++ Compiler
CC=gcc

JAVA_HOME=/usr/java/default

all: build

init:
	mkdir -p $(C_BIN)
	mkdir -p $(JAVA_BIN)
	
clean:
	rm -rf $(C_BIN)/*
	rm -rf $(JAVA_BIN)/*

GtkFileDialog: init
	$(CC) -Wall -g $(C_SRC)/GtkFileDialog.c -o $(C_BIN)/GtkFileDialog `pkg-config --cflags gtk+-2.0` \
	`pkg-config --libs gtk+-2.0`
	
libGtkFileDialogPeer.so.old:
	$(CC) -o $(C_BIN)/libGtkFileDialogPeer.so -shared -Wl,-soname,$(C_BIN)/libGtkFileDialogPeer.so \
	`pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` `pkg-config --libs gthread-2.0` \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	$(C_SRC)/awt_GtkFileDialogPeer.c \
	-static -lc

libGtkFileDialogPeer.so:
	$(CC) -o $(C_BIN)/libGtkFileDialogPeer.so -shared -Wl,-soname,$(C_BIN)/libGtkFileDialogPeer.so \
	`pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` `pkg-config --libs gthread-2.0` \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	$(C_SRC)/awt_GtkFileDialogPeer.c \
	-static -lc
	
awt_GtkFileDialogPeer.h:
	${JAVA_HOME}/bin/javah -classpath $(JAVA_BIN) -o native/src/awt_GtkFileDialogPeer.h sun.awt.X11.GtkFileDialogPeer
	
build: init libGtkFileDialogPeer.so
	${JAVA_HOME}/bin/javac -sourcepath $(JAVA_SRC) -d $(JAVA_BIN) `find $(JAVA_SRC) -name *.java`

build.test: build	
	${JAVA_HOME}/bin/javac -sourcepath $(JAVA_SRC):$(JAVA_TEST) -d $(JAVA_BIN) `find $(JAVA_TEST) -name *.java`
	
test: build.test
	${JAVA_HOME}/bin/java -cp $(JAVA_BIN) sun.awt.X11.GtkFileDialogTest
	