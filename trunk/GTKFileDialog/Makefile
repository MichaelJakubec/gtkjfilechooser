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

NATIVE_SUN_AWT=/home/c.cerbo/openjdk/jdk7/mytl/jdk/src/solaris/native/sun/awt
SUN_AWT_X11=/home/c.cerbo/openjdk/jdk7/mytl/jdk/src/solaris/classes/sun/awt/X11

ALT_JDK_IMPORT_PATH=/home/c.cerbo/openjdk/jdk7/mytl/build/linux-i586

all: build

init:
	mkdir -p $(C_BIN)
	mkdir -p $(JAVA_BIN)
	
clean:
	rm -rf $(C_BIN)/*
	rm -rf $(JAVA_BIN)/*

GtkFileDialog: init
	$(CC) -Wall -g $(C_SRC)/GtkFileDialog.c -o $(C_BIN)/GtkFileDialog `pkg-config --cflags gtk+-2.0` \
	`pkg-config --libs gtk+-2.0` -lgthread-2.0
	
libGtkFileDialogPeer.so.old:
	$(CC) -o $(C_BIN)/libGtkFileDialogPeer.so -shared -Wl,-soname,$(C_BIN)/libGtkFileDialogPeer.so \
	`pkg-config --cflags gtk+-2.0` `pkg-config --libs gtk+-2.0` `pkg-config --libs gthread-2.0` \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	$(C_SRC)/sun_awt_X11_GtkFileDialogPeer.c \
	-static -lc

libGtk2Interface.so:
	$(CC) -o $(C_BIN)/libGtk2Interface.so -shared -Wl,-soname,$(C_BIN)/libGtk2Interface.so \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -I/usr/include/ \
	$(C_SRC)/gtk2_interface.c \
	
	
libGtkFileDialogPeer.so: libGtk2Interface.so
	$(CC) -o $(C_BIN)/libGtkFileDialogPeer.so -shared -Wl,-soname,$(C_BIN)/libGtkFileDialogPeer.so \
	-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux \
	$(C_BIN)/libGtk2Interface.so \
	$(C_SRC)/sun_awt_X11_GtkFileDialogPeer.c
	
sun_awt_X11_GtkFileDialogPeer.h:
	${JAVA_HOME}/bin/javah -classpath $(JAVA_BIN) -o native/src/sun_awt_X11_GtkFileDialogPeer.h sun.awt.X11.GtkFileDialogPeer
	
build: init libGtkFileDialogPeer.so
	${JAVA_HOME}/bin/javac -sourcepath $(JAVA_SRC) -d $(JAVA_BIN) `find $(JAVA_SRC) -name *.java`

build.test: build	
	${JAVA_HOME}/bin/javac -sourcepath $(JAVA_SRC):$(JAVA_TEST) -d $(JAVA_BIN) `find $(JAVA_TEST) -name *.java`
	
test: build.test
	${JAVA_HOME}/bin/java -cp $(JAVA_BIN) sun.awt.X11.GtkFileDialogTest

copy:	
	cp $(C_SRC)/sun_awt_X11_GtkFileDialogPeer.h $(NATIVE_SUN_AWT)
	cp $(C_SRC)/sun_awt_X11_GtkFileDialogPeer.c $(NATIVE_SUN_AWT)
	cp $(C_SRC)/gtk2_interface.h $(NATIVE_SUN_AWT)
	cp $(C_SRC)/gtk2_interface.c $(NATIVE_SUN_AWT)
	cp $(JAVA_SRC)/sun/awt/X11/GtkFileDialogPeer.java $(SUN_AWT_X11)
		
test_file_dialog:
	$(ALT_JDK_IMPORT_PATH)/bin/javac -d $(JAVA_BIN) test/sun/awt/X11/FileDialogTest.java
	$(ALT_JDK_IMPORT_PATH)/bin/java -cp $(JAVA_BIN) sun.awt.X11.FileDialogTest
