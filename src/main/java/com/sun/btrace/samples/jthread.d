#!/usr/sbin/dtrace -s

/*
 * 只要“btrace:::event”探测点作为第一个参数被“jthreadstart”触发时，
 * 本“DTrace脚本文件”就会打印“Java的线程名称”。
 */

btrace$1:::event 
/
    copyinstr(arg0) == "jthreadstart" &&
    arg1 != NULL
/
{
    printf("From DTrace: Java Thread '%s' started\n", copyinstr(arg1));
}
