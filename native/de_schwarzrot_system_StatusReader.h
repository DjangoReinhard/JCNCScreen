/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_schwarzrot_system_StatusReader */

#ifndef _Included_de_schwarzrot_system_StatusReader
#define _Included_de_schwarzrot_system_StatusReader
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_schwarzrot_system_StatusReader
 * Method:    getString
 * Signature: (II)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_schwarzrot_system_StatusReader_getString
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     de_schwarzrot_system_StatusReader
 * Method:    readStatus
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_de_schwarzrot_system_StatusReader_readStatus
  (JNIEnv *, jobject);

/*
 * Class:     de_schwarzrot_system_StatusReader
 * Method:    init
 * Signature: ()Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_de_schwarzrot_system_StatusReader_init
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
