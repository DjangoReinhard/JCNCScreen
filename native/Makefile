# make file
#
ifeq ($(TOOL_NML),yes)
  TOOL_NML_FLAG := -DTOOL_NML
  $(info TOOL_NML_FLAG = $(TOOL_NML_FLAG))
endif
O_HAL   = de_schwarzrot_hal_ItemList.new.o
O_NML   = de_schwarzrot_system_CommandWriter.o \
 de_schwarzrot_system_ErrorReader.o \
 de_schwarzrot_system_StatusReader.o
INCLUDES=-I. \
 -I/usr/include/python2.7 \
 -I$(abspath lc/src) \
 -I$(abspath lc/src/include) \
 -I$(abspath lc/libnml/linklist) \
 -I$(abspath lc/libnml/cms) \
 -I$(abspath lc/libnml/rcs) \
 -I$(abspath lc/libnml/inifile) \
 -I$(abspath lc/libnml/os_intf) \
 -I$(abspath lc/libnml/nml) \
 -I$(abspath lc/libnml/buffer) \
 -I$(abspath lc/libnml/posemath) \
 -I$(abspath lc/rtapi) \
 -I$(abspath lc/hal) \
 -I$(abspath lc/emc/nml_intf) \
 -I$(abspath lc/emc/kinematics) \
 -I$(abspath lc/emc/tp) \
 -I$(abspath lc/emc/motion) \
 -I$(abspath lc/emc/ini) \
 -I$(abspath lc/emc/rs274ngc) \
 -I$(abspath lc/emc/sai) \
 -I$(abspath lc/emc/pythonplugin) \
 -I$(abspath lc/emc/tooldata) \
 -I$(abspath lc/include) \
 -I/usr/lib/jvm/java-11-openjdk-amd64/include \
 -I/usr/lib/jvm/java-11-openjdk-amd64/include/linux
CC_FLAGS=$(INCLUDES) \
 -DULAPI -g -Wall -Os -fwrapv -Woverloaded-virtual \
 -D_GNU_SOURCE $(TOOL_NML_FLAG) -Wno-psabi -std=gnu++11 \
 -MP -MD
LC_FLAGS = -DLOCALE_DIR=\"/usr/share/locale\" \
 -DPACKAGE=\"linuxcnc\" \
 $(TOOL_NML_FLAG) \
 -L$(abspath lc/lib) \
  $(abspath lc/lib/libnml.so.0) \
 -Wl,-rpath,$(abspath lc/lib) \
 -Xlinker -Map=libLinuxCNC.map \
 -L/usr/X11R6/lib -lm -lGL
GCC = g++
JLIBS = ../lib/derby.jar:../lib/derbyshared.jar:../lib/derbytools.jar:../lib/glazedlists-1.11.0.jar:../lib/jme3-core.jar:../lib/jme3-desktop.jar:../lib/jme3-lwjgl.jar:../lib/LCExportHandler.jar:../lib/lwjgl.jar:../lib/lwjgl-platform-natives-linux.jar
LC_OBJS = $(abspath lc/src/objects/emc/nml_intf/emc.o) \
 $(abspath lc/src/objects/emc/nml_intf/emcops.o) \
 $(abspath lc/src/objects/emc/rs274ngc/modal_state.o) \
 $(abspath lc/src/objects/emc/tooldata/tooldata_common.o) \
 $(abspath lc/src/objects/emc/tooldata/tooldata_mmap.o) \
 $(abspath lc/src/objects/emc/tooldata/tooldata_db.o)

all: 	libLinuxCNC.so DumpBufDesc

clean:
	rm *.o
	rm *.d
	rm *.so
	rm DumpBufDesc

headers:
	javac -h . -cp .:../src:$(JLIBS) ../src/de/schwarzrot/system/CommandWriter.java
	javac -h . -cp .:../src:$(JLIBS) ../src/de/schwarzrot/system/ErrorReader.java
	javac -h . -cp .:../src:$(JLIBS) ../src/de/schwarzrot/system/StatusReader.java

%.o : %.cc
	$(GCC) -c $(CC_FLAGS) -o $@ $<

libLinuxCNC.so: $(O_NML) $(LC_OBJS)
	$(GCC) -shared $(LC_FLAGS) -o $@ $^

DumpBufDesc:	dumpBufDesc.o
	$(GCC) $(LC_FLAGS) -o $@ $^

TestNML:	TestNML.o $(LC_OBJS)
	$(GCC) $(LC_FLAGS) -o $@ $^

