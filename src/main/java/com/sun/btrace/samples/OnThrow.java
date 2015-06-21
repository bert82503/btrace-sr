/*
 * Copyright 2008-2010 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.btrace.samples;

import com.sun.btrace.annotations.*;
import static com.sun.btrace.BTraceUtils.*;

/**
 * <p>
 *     本示例演示了打印“一个异常的调用栈信息”和“线程本地变量”。
 *     本追踪脚本会在“Throwable构造器返回”时打印“异常的调用栈信息”。
 *     这样，你就可以通过“追踪程序”来追踪“所有可能被捕获但又被悄无声息地‘吃掉’的异常”。
 *
 *     注意：这里假设“异常对象”在创建后就被抛出（像“throw new FooException();”），而非被存储起来并后面再抛出。
 * </p>
 * This example demonstrates printing stack trace of an exception and thread local variables.
 * This trace script prints exception stack trace whenever java.lang.Throwable's constructor returns.
 * This way you can trace all exceptions that may be caught and "eaten" silently by the traced program.
 *
 * Note that the assumption is that the exceptions are thrown soon after creation
 * [like in "throw new FooException();"] rather that be stored and thrown later.
 */
@BTrace
public class OnThrow {

    // store current exception in a thread local variable (@TLS annotation).
    // Note that we can't store it in a global variable!
    // 存储“当前异常信息”到“一个线程本地变量”中（@TLS 注解）
    // 注意：不能存储它到“一个全局变量”中！
    @TLS static Throwable currentException;

    // introduce probe into every constructor of java.lang.Throwable
    // class and store "this" in the thread local variable.
    // 在“java.lang.Throwable”类的每个构造器中引入“探测点”，
    // 并存储“当前对象”到“线程本地变量”中。
    @OnMethod(
            clazz="java.lang.Throwable",
            method="<init>" // 匹配“构造器”方法
//        location=@Location(Kind.ENTRY) // “构造器方法进入”的探测位置
    ) // Throwable()
    public static void onConstructor(@Self Throwable self) {
        currentException = self;
    }

    @OnMethod(
            clazz="java.lang.Throwable",
            method="<init>"
    ) // Throwable(String message)
    public static void onConstructor(@Self Throwable self, String message) {
        currentException = self;
    }

    @OnMethod(
            clazz="java.lang.Throwable",
            method="<init>"
    ) // Throwable(String message, Throwable cause)
    public static void onConstructor(@Self Throwable self, String message, Throwable cause) {
        currentException = self;
    }

    @OnMethod(
            clazz="java.lang.Throwable",
            method="<init>"
//        location=@Location(Kind.ENTRY) // “构造器方法进入”的探测位置
    ) // Throwable(Throwable cause)
    public static void onConstructor(@Self Throwable self, Throwable cause) {
        currentException = self;
    }

    // when any constructor of java.lang.Throwable returns
    // print the currentException's stack trace.
    @OnMethod(
            clazz="java.lang.Throwable",
            method="<init>",
            location=@Location(Kind.RETURN) // “构造器方法返回”的探测位置
    )
    public static void onConstructorReturn() {
        if (currentException != null) {
            Threads.jstack(currentException); // 打印“给定异常对象的调用栈信息”
            println("=====================");
            currentException = null;
        }
    }

}
