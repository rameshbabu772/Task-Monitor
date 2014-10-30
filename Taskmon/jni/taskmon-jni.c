#include <jni.h>
#include <stdio.h>
#include <time.h>
#include <android/log.h>

#define __NR_set_reserve 379
#define __NR_cancel_reserve 380
//#define __NR_set_reserve 379
//#define __NR_cancel_reserve 380


//Thread information structure
struct thread_info{
        pid_t pid;
        pid_t tgid;
        unsigned int rt_priority;
        char* name; //16 bytes
};



JNIEXPORT jint JNICALL 
Java_com_taskmon_MainActivity_JniSetReserve(JNIEnv *env,jobject obj,jobject data) {

	int x=0;
	jclass MainActivity =(*env)->GetObjectClass(env,data);
	jmethodID getC =(*env)->GetMethodID(env,MainActivity,"getC","()J");
	long c = (*env)->CallLongMethod(env,data,getC);


	jmethodID getTime =(*env)->GetMethodID(env,MainActivity,"getT","()J");
	long t = (*env)->CallLongMethod(env,data,getTime);


	jmethodID getPid =(*env)->GetMethodID(env,MainActivity,"getPid","()I");
	pid_t pid = (*env)->CallIntMethod(env,data,getPid);

	/*jmethodID getcpuid =(*env)->GetMethodID(env,MainActivity,"getcpuid","()I");
	int cpuid = (*env)->CallIntMethod(env,data,getcpuid);*/
	int cpuid = 0;

	struct timespec C;
	C.tv_sec = c/1000; //msec -> sec
	C.tv_nsec = ((long)(c%1000))*1000000;//msec -> nsec

	struct timespec T;
	T.tv_sec = t/1000; //msec -> sec
	T.tv_nsec = ((long)(t%1000))*1000000;//msec -> nsec

	__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Pid: %ld, C: %ld, T: %ld\n",(long)pid,c,t);

	x= syscall(__NR_set_reserve, pid, &C, &T, cpuid);

	return x;
}
JNIEXPORT jint JNICALL 
Java_com_taskmon_MainActivity_JniCancelReserve(JNIEnv *env,jobject obj,jobject data) {

	int x=0;
	jclass MainActivity =(*env)->GetObjectClass(env,data);

	jmethodID getPid =(*env)->GetMethodID(env,MainActivity,"getPid","()I");
	pid_t pid = (*env)->CallIntMethod(env,data,getPid);
	x= syscall(__NR_cancel_reserve, pid);
	return x;
}

/*JNIEXPORT jint JNICALL
Java_com_taskmon_MainActivity_JNICountRtThreads(JNIEnv *env,jobject obj) {

	int x=0;
	x= syscall(__NR_count_rt_threads);
	return x;
}*/

/*JNIEXPORT jobjectArray  JNICALL
Java_com_taskmon_MainActivity_JNIlistRtThreads(JNIEnv *env,jobject obj,jint count) {
	struct thread_info* thread_list;
	 int i,listnum;
	 jobjectArray pidlist;
	 thread_list = malloc(num_threads * sizeof(struct thread_info));
	listnum= syscall(__NR_list_rt_threads,thread_list,count);

	char* pid[listnum];
	char temp[10];
	for(i=0;i<listnum;i++)
	{
	 temp=itoa(thread_list[i]->pid);
	 strcpy(pid[i],temp);
	}


	pidlist= (jobjectArray)env->NewObjectArray(listnum, env->FindClass("java/lang/String"),env->NewStringUTF(""));

	    for(i=0;i<listnum;i++) {
	      env->SetObjectArrayElement( ret,i,env->NewStringUTF(pid[i]));
	    }
	    return(pidlist);
}*/
