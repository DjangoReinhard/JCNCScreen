/*
 * **************************************************************************
 *
 *  file:       de_schwarzrot_system_ErrorReader.cc
 *  project:    GUI for linuxcnc
 *  subproject: C-stub to access nml-structure in shared memory
 *  purpose:    read error messages and convert them into java elements
 *  created:    8.10.2019 by Django Reinhard
 *              followed code from linuxcnc
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

#include  <de_schwarzrot_system_ErrorReader.h>


struct ErrorChannel {
  PyObject_HEAD
  NML                *c;
  };

static ErrorChannel   ec = {0};


#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     de_schwarzrot_system_ErrorReader
 * Method:    fetchMessage
 * Signature: ()Lde/schwarzrot/system/SystemMessage;
 *
 * Sub Types: Lde/schwarzrot/system/SystemMessage;
 *            Lde/schwarzrot/system/SystemMessage/MessageType;
 */
JNIEXPORT jobject JNICALL Java_de_schwarzrot_system_ErrorReader_fetchMessage(JNIEnv *env, jobject thisObject) {
  if (!ec.c->valid()) {
     fprintf(stderr, "ERROR: error buffer is invalid!");
     return NULL;
     }
  NMLTYPE type = ec.c->read();
  if (type == 0) return NULL;
  char error_buffer[LINELEN];
  jclass clSysMsg  = env->FindClass("de/schwarzrot/system/SystemMessage");
  jclass clMsgType = env->FindClass("de/schwarzrot/system/SystemMessage$MessageType");
  jfieldID fEnumMsgType;

  switch (type) {
    case EMC_OPERATOR_ERROR_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "OperatorError"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((EMC_OPERATOR_ERROR*)ec.c->get_address())->error, LINELEN-1);
         } break;
    case EMC_OPERATOR_TEXT_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "OperatorText"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((EMC_OPERATOR_TEXT*)ec.c->get_address())->text, LINELEN-1);
         } break;
    case EMC_OPERATOR_DISPLAY_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "OperatorDisplay"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((EMC_OPERATOR_DISPLAY*)ec.c->get_address())->display, LINELEN-1);
         } break;
    case NML_ERROR_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "NMLError"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((NML_ERROR*)ec.c->get_address())->error, LINELEN-1);
         } break;
    case NML_TEXT_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "NMLText"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((NML_TEXT*)ec.c->get_address())->text, LINELEN-1);
         } break;
    case NML_DISPLAY_TYPE: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "NMLDisplay"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         strncpy(error_buffer, ((NML_DISPLAY*)ec.c->get_address())->display, LINELEN-1);
         } break;
    default: {
         fEnumMsgType = env->GetStaticFieldID(clMsgType
                                            , "NMLError"
                                            , "Lde/schwarzrot/system/SystemMessage$MessageType;");
         sprintf(error_buffer, "unrecognized error %" PRId32, type);
         } break;
    }
  error_buffer[LINELEN-1]  = 0;
  jstring  errorMessage    = env->NewStringUTF(error_buffer);
  jobject  enumMessageType = env->GetStaticObjectField(clMsgType, fEnumMsgType);
  jobject  newSystemMsg    = env->AllocObject(clSysMsg);
  jfieldID fMsg            = env->GetFieldID(clSysMsg
                                           , "message"
                                           , "Ljava/lang/String;");
  jfieldID fType           = env->GetFieldID(clSysMsg
                                           , "type"
                                           , "Lde/schwarzrot/system/SystemMessage$MessageType;");
  env->SetObjectField(newSystemMsg, fMsg,  errorMessage);
  env->SetObjectField(newSystemMsg, fType, enumMessageType);

  return newSystemMsg;
  }


/*
 * Class:     de_schwarzrot_system_ErrorReader
 * Method:    init
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_de_schwarzrot_system_ErrorReader_init(JNIEnv *env, jobject thisObject) {
  const char* nmlFile = EMC2_DEFAULT_NMLFILE;
  NML*        c       = new NML(emcFormat, "emcError", "xemc", nmlFile);

  if (!c) {
     fprintf(stderr, "ERROR: new NML failed!");
     return -1;
     }
  ec.c = c;

  return 0;
  }

#ifdef __cplusplus
}
#endif
