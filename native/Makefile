# make file
#
ifeq ($(TOOL_NML),yes)
  TOOL_NML_FLAG := -DTOOL_NML
  $(info TOOL_NML_FLAG = $(TOOL_NML_FLAG))
endif
O_HAL   = de_schwarzrot_hal_ItemList.new.o
O_NML   = de_schwarzrot_system_CommandWriter.o \
 de_schwarzrot_system_ErrorReader.o \
 de_schwarzrot_system_StatusReader.o \
 emcDecode.o
O_LC    = lc/src/objects/libnml/buffer/*.o \
 lc/src/objects/libnml/rcs/*.o \
 lc/src/objects/libnml/cms/*.o \
 lc/src/objects/libnml/nml/*.o
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

# -fPIC -fno-strict-aliasing \
# -lrt \

LC_FLAGS = $(INCLUDE) \
 -DLOCALE_DIR=\"/usr/share/locale\" \
 -DPACKAGE=\"linuxcnc\" \
 $(TOOL_NML_FLAG) \
 -L$(abspath lc/lib) \
 -Wl,-rpath,$(abspath lc/lib) \
 -Xlinker -Map=libLinuxCNC.map \
  $(abspath lc/lib/liblinuxcnc.a) \
  $(abspath lc/lib/liblinuxcncini.so.0) \
  $(abspath lc/lib/libnml.so.0) \
  $(abspath lc/lib/liblinuxcnchal.so.0) \
  $(abspath lc/lib/libtooldata.so.0) \
 -L/usr/X11R6/lib -lm -lGL
GCC = g++


all: 	libLinuxCNC.so DumpBufDesc

clean:
	rm *.o
	rm *.d
	rm *.so
	rm DumpBufDesc

%.o : %.cc
	$(GCC) -c $(CC_FLAGS) -o $@ $<

libLinuxCNC.so: $(O_NML) $(abspath lc/src/objects/emc/rs274ngc/modal_state.o)
	$(GCC) -shared $(LC_FLAGS) -o $@ $^

DumpBufDesc:	dumpBufDesc.o
	$(GCC) $(LC_FLAGS) -o $@ $^

