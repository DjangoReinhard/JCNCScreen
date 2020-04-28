/*
 * **************************************************************************
 *
 *  file:       de_schwarzrot_system_StatusReader.cc
 *  project:    GUI for linuxcnc
 *  subproject: C-stub to access nml-structure in shared memory
 *  purpose:    read status information from linuxcnc backend
 *              and allow the java application access to that buffer.
 *  created:    19.10.2019 by Django Reinhard
 *              followed code from emcmodule.cc (linuxcnc), which was
 *              written by Jeff Epler and Chris Radek
 *  copyright:  all rights reserved
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * **************************************************************************
 */
/*
 * JNI implementation of a bridge between linuxcnc and java (ui)
 *
 * compile with:
g++ -c -I. \
    -Ilc/src/libnml/linklist \
    -Ilc/src/libnml/cms \
    -Ilc/src/libnml/rcs \
    -Ilc/src/libnml/inifile \
    -Ilc/src/libnml/os_intf \
    -Ilc/src/libnml/nml \
    -Ilc/src/libnml/buffer \
    -Ilc/src/libnml/posemath \
    -Ilc/src/rtapi \
    -Ilc/src/hal \
    -Ilc/src/emc/nml_intf \
    -Ilc/src/emc/kinematics \
    -Ilc/src/emc/tp \
    -Ilc/src/emc/motion \
    -Ilc/src/emc/ini \
    -Ilc/src/emc/rs274ngc \
    -Ilc/src/emc/sai \
    -Ilc/src/emc/pythonplugin \
    -Ilc/include \
    -I/usr/include/python2.7 \
    -I/usr/lib/jvm/java-8-openjdk-amd64/include \
    -g -O2    -DULAPI  -g -Wall -Os -fwrapv -Woverloaded-virtual -fPIC -fno-strict-aliasing \
    -MP -MD \
    de_schwarzrot_status_StatusReader.cc \
    -o jniLinuxCNC.o

 * then link as shared lib with:
g++ -Llc/lib \
    -Wl,-rpath,lc/lib \
    -shared \
    -o jniLinuxCNC.so \
    jniLinuxCNC.o \
    lc/lib/liblinuxcnc.a \
    lc/lib/libnml.so.0 \
    lc/lib/liblinuxcncini.so \
    -L/usr/X11R6/lib \
    -lm -lGL
 *
 */
#define __STDC_FORMAT_MACROS

#include <Python.h>
#include "config.h"
#include "rcs.hh"
#include "emc.hh"
#include "emc_nml.hh"
#include "kinematics.h"
#include "config.h"
#include "inifile.hh"
#include "timer.hh"
#include "nml_oi.hh"
#include "rcs_print.hh"

#include  <de_schwarzrot_system_StatusReader.h>

/*
 * ini-file: /usr/local/src/linuxcnc-dev/configs/sim/axis/axis.ini
 */
#define EMC_COMMAND_TIMEOUT 5.0  // how long to wait until timeout
#define EMC_COMMAND_DELAY   0.01 // how long to sleep between checks


struct StatusChannel {
    PyObject_HEAD
    RCS_STAT_CHANNEL    *c;
    EMC_STAT             status;
    };

static StatusChannel  sc = {0};

static int poll(StatusChannel *s) {
    if (!s->c->valid()) return -1;
    int rv = s->c->peek();

    if (rv == EMC_STAT_TYPE) {
       EMC_STAT *estat = static_cast<EMC_STAT*>(s->c->get_address());
       memcpy(&s->status, estat, sizeof(EMC_STAT));

       return 0;
       }
    return rv;
    }


#ifdef __cplusplus
extern "C" {
#endif


/*
 * Class:     de_schwarzrot_status_StatusReader
 * Method:    init
 * Signature: ()Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_de_schwarzrot_system_StatusReader_init(JNIEnv *env
                                                                    , jobject thisObject) {
    const char*   nmlFile    = EMC2_DEFAULT_NMLFILE;
    jobject       byteBuffer = env->NewDirectByteBuffer((void*)&sc.status, sizeof(EMC_STAT));
    unsigned long bufSize    = env->GetDirectBufferCapacity(byteBuffer);

    if (bufSize < sizeof(EMC_STAT)) {
       fprintf(stderr, "ERROR: byteBuffer is too small!!!");
       }
    sc.c = new RCS_STAT_CHANNEL(emcFormat, "emcStatus", "xemc", nmlFile);

    if (sc.c) {
       fprintf(stderr, "ok, wi got a status channel ...\n");
       }
    else {
       fprintf(stderr, "OUPS, failed to create status channel!\n");
       }
    void *buf = env->GetDirectBufferAddress(byteBuffer);

    fprintf(stderr, "byteBuffer is located at 0x%lX\n", (unsigned long)buf);
    fprintf(stderr, "byteBuffer is located at #%ld\n",  (unsigned long)buf);

    return byteBuffer;
    }

/*
 * Class:     de_schwarzrot_status_StatusReader
 * Method:    getString
 * Signature: (II)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_schwarzrot_system_StatusReader_getString(JNIEnv *env
                                                                         , jobject thisObject
                                                                         , jint offset
                                                                         , jint length) {
    std::string fileName = ((const char *)&sc.status) + offset;

    return env->NewStringUTF(fileName.c_str());
    }


/*
 * Class:     de_schwarzrot_status_StatusReader
 * Method:    readStatus
 * Signature: ()Ljava/nio/ByteBuffer;
 */
JNIEXPORT void JNICALL Java_de_schwarzrot_system_StatusReader_readStatus(JNIEnv *env
                                                                       , jobject thisObject) {
    int rv = poll(&sc);

    if (rv) {
       // ERROR: could not fetch status message
       fprintf(stderr, "ERROR %d: could not fetch status message!\n", rv);
       }
    }


#ifdef __cplusplus
}
#endif
