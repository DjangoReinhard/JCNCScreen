# JCNCScreen
JCNCScreen is a frontend application for linuxcnc (http://linuxcnc.org) which
is published according to GPL 2.0 or later. See file COPYING for details.

JCNCScreen contains a tool-manager with plugins for exporting tooltables for
different applications. These Plugins are separate projects.

JCNCScreen is build with java-language and swing for gui, so building and
installing might be different to usual linux packages.
Running JCNCScreen might be different for java applications, as linuxcnc
uses shared memory buffers for communication. So JCNCScreen contains a
little C-stub that handles all shared memory access. That stub needs to be
loaded by the java runtime, so starting JCNCScreen needs special parameters.

All Projects must reside in parallel directories, i.e. like this:
    /usr/share/src
          |
          +---  JCNCScreen              (mandatory)
          +---  LCExportHandler         (mandatory)
          +---  LinuxCNCExportHandler   (optional)
          +---  CamBamExportHandler     (optional)
          +---  SRExportHandler         (optional)

subproject LCExportHandler should be build first.
But before building it, JCNCScreen should already have been downloaded.
Dive into the projects directory and call

        ant dist

That will build the library and copy it into the lib directory of JCNCScreen.

Next step is to build JCNCScreen.
As already mentioned, JCNCScreen consists of two parts, the java components
and the C-stub. To compile the C-Stub you need a development installation of
linuxcnc. Best is a RIP-installation.
Go into the directory 'native' from JCNCScreen and add a softlink to linuxcnc
like this:

        ln -s /usr/local/src/linuxcnc-dev lc

Change the path of linuxcnc to your corresponding location.
Be sure, that you built linuxcnc successfully.

Now you can build JCNCScreen. Go to the main directory of JCNCScreen and call

        ant dist

That will compile the C-stub and the java application.
On successful build, you find a zip-file JCNCScreen-bin.zip in the main
directory of JCNCScreen. That archive contains everything to run JCNCScreen
from where ever you want.

Extract the archive to the wanted location, move into it and create the same
softlink to linuxcnc installation as before from directory 'native'. Then
look at subdirectory 'misc' - it contains a little bash-script for application
startup.
Edit the file and change the path from first line to the directory, where you
extracted the binary archive. Then copy the script to a directory that is
contained in your PATH environment variable (i.e. /usr/local/bin).

Finally you have to tell linuxcnc, that you want to use JCNCScreen as frontend.
Edit the ini-file for your machine and search the DISPLAY variable from
DISPLAY section.
Change it to JCNCScreen and you're done.

Inifile should look like this:

    [DISPLAY]
    #DISPLAY = axis
    DISPLAY = JCNCScreen


Have fun!

