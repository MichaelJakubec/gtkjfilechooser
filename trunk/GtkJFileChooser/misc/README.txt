#-------------------------------------------------------------------------------
# Copyright (c) 2010 Costantino Cerbo.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the GNU Lesser Public License v2.1
# which accompanies this distribution, and is available at
# http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
# 
# Contributors:
#     Costantino Cerbo - initial API and implementation
#-------------------------------------------------------------------------------
*** Install the new FileChooserUI ***

The library gtkjfilechooser.jar contains the new GTKFile. Ensure that you're 
using the GTK Laf and then simply set the UI Property "FileChooserUI" to 
"eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI":

  if ("GTK look and feel".equals(UIManager.getLookAndFeel().getName())){
    UIManager.put("FileChooserUI", "eu.kostia.gtkjfilechooser.ui.GtkFileChooserUI");
  }

You're now ready to start the file chooser:

  JFileChooser fileChooser = new JFileChooser();
  fileChooser.showOpenDialog(null);

*** Run a demo ***

The executable jar gtkjfilechooser-demo.jar demonstrates some of the 
capabilities of the JFileChooser object.  It brings up a window displaying 
several configuration controls that allow you to play with the JFileChooser 
options dynamically.
 
To run the gtkjfilechooser-demo demo:

  java -jar gtkjfilechooser-demo.jar

These instructions assume that this installation's version of the java
command is in your path.  If it isn't, then you should either
specify the complete path to the java command or update your
PATH environment variable as described in the installation
instructions for the Java(TM) SE Development Kit.
