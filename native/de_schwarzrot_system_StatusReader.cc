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
 *              rewrite to minimize dependencies 10.7.2021
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
#include <stat_msg.hh>
#include <cmd_msg.hh>
#include <emc_nml.hh>
#include <stdio.h>
#include <sys/time.h>
#include  <de_schwarzrot_system_StatusReader.h>


static RCS_STAT_CHANNEL*   cStat;
static EMC_STAT*           status;


/*
 * Class:     de_schwarzrot_status_StatusReader
 * Method:    init
 * Signature: ()Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_de_schwarzrot_system_StatusReader_init(JNIEnv *env
                                                                    , jobject thisObject) {
  cStat = new RCS_STAT_CHANNEL(emcFormat, "emcStatus", "xemc", EMC2_DEFAULT_NMLFILE);
  if (!cStat || !cStat->valid()) {
     delete cStat;
     cStat = NULL;

     return NULL;
     }
  else {
     status = static_cast<EMC_STAT*>(cStat->get_address());

     return env->NewDirectByteBuffer((void*)status, sizeof(EMC_STAT));
     }
  return NULL;
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
  std::string fileName = ((const char *)status) + offset;

  return env->NewStringUTF(fileName.c_str());
  }


/*
 * Class:     de_schwarzrot_status_StatusReader
 * Method:    readStatus
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_de_schwarzrot_system_StatusReader_readStatus(JNIEnv *env
                                                                       , jobject thisObject) {
  if (!cStat || !cStat->valid()) return -1;
  int rv = cStat->peek();

  if (!rv || rv == EMC_STAT_TYPE) return 0;
  return rv;
  }

