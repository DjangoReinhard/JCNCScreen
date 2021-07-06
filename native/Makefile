# make file
#
O_HAL   = de_schwarzrot_hal_ItemList.new.o
O_NML   = de_schwarzrot_system_CommandWriter.o \
 de_schwarzrot_system_ErrorReader.o \
 de_schwarzrot_system_StatusReader.o \
 emcDecode.o
O_LC    = lc/src/objects/libnml/buffer/*.o \
 lc/src/objects/libnml/rcs/*.o \
 lc/src/objects/libnml/cms/*.o \
 lc/src/objects/libnml/nml/*.o
CC_FLAGS=-I. \
 -I/usr/include/python2.7 \
 -I$(abspath lc/src) \
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
 -I$(abspath lc/src/include) \
 -I/usr/lib/jvm/java-11-openjdk-amd64/include \
 -I/usr/lib/jvm/java-11-openjdk-amd64/include/linux \
 -DULAPI -g -Wall -Os -fwrapv -Woverloaded-virtual \
 -D_GNU_SOURCE -DTOOL_NML -Wno-psabi -std=gnu++11 \
 -fPIC -fno-strict-aliasing \
 -MP -MD


LC_FLAGS = -L$(abspath lc/lib) \
 -Wl,-rpath,$(abspath lc/lib) \
 -Xlinker -Map=libLinuxCNC.map \
 -shared \
  $(abspath lc/lib/liblinuxcnc.a) \
  $(abspath lc/lib/liblinuxcncini.so) \
  $(abspath lc/lib/liblinuxcnchal.so) \
  $(abspath lc/lib/libnml.so.0) \
  $(abspath lc/lib/libtooldata.so.0) \
 -L/usr/X11R6/lib -lm -lGL
GCC     = g++


all: 	libLinuxCNC.so

clean:
	rm *.o
	rm *.d
	rm *.so

%.o : %.cc
	$(GCC) -c $(CC_FLAGS) -o $@ $<

libLinuxCNC.so: $(O_NML)
	$(GCC) $(LC_FLAGS) -o $@ $^


