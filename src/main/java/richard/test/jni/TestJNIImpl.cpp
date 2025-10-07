#include </home/rd/software/jdk1.8.0_333/include/jni.h>
#include "richard_test_jni_TestJNI.h"
#include <stdio.h>


/*
 * Class:     TestJNI
 * Method:    go
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_richard_test_jni_TestJNI_go
  (JNIEnv * env, jobject jobj){

    printf("I am going....\n");
}

/*
 * Class:     TestJNI
 * Method:    run
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_richard_test_jni_TestJNI_run
  (JNIEnv * env, jobject jobj){

    printf("I am running....\n");
}

/*
 * Class:     TestJNI
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_richard_test_jni_TestJNI_getName
  (JNIEnv * env, jobject job){

   printf("I am GDUTtiantian, go with me.\n");
   //将字符串转化为jstring类型
   //jstring就是对应java的String类型
   jstring p = env->NewStringUTF("GDUTtiantian");
   return p;
}

/*
 * Class:     TestJNI
 * Method:    sort
 * Signature: ([I)[I
 */
JNIEXPORT jintArray JNICALL Java_richard_test_jni_TestJNI_sort
  (JNIEnv * env, jobject jobj, jintArray array){

jint* arr;//定义一个整形指针
    int sum=0;
    //对于整形数组的处理，主要有GetIntArrayElements与GetIntArrayRegion
    //第一种方法
    arr = env->GetIntArrayElements(array, NULL);//得到一个指向原始数据类型内容的指针
    jint length = env->GetArrayLength(array);//得到数组的长度

    for(int i=0; i<length; i++){
        for(int j=i+1; j<length; j++){
            if(arr[i] > arr[j]){
                jint temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }


    for(int i=0; i<length; i++){
        printf("%d ", arr[i]);
    }

    printf("\n排序完成\n");
    printf("hello")

    jintArray javaArray = env->NewIntArray(length);
    env->SetIntArrayRegion(javaArray, 0, length, arr);

    return javaArray;//返回排序后的数组
}