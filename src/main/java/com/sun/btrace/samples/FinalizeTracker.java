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
import java.lang.reflect.Field;

/**
 * <p>
 *     本示例演示打印对象的所有字段状态信息和访问[私有]字段。
 *     这在调试/故障排除中非常有用！
 *
 *     本示例打印在“FileInputStream类的close() /finalize()方法”上的信息。
 * </p>
 * This sample demonstrates that we can print all fields of an object and access (private) fields (read-only) as well.
 * This is useful in debugging / troubleshooting scenarios.
 * This sample prints info on close() /finalize() methods of java.io.FileInputStream class.
 */
@BTrace
public class FinalizeTracker {

    private static final String probeClassName = "java.io.FileInputStream";

    // 访问私有字段
    private static Field fdField =
            field(probeClassName, "fd"); // File Descriptor - handle to the open file
    // Reflective.field(...)：返回指定类中的指定“字段”对象

    @OnTimer(4000) // 定时器
    public static void onTimer() {
        runFinalization(); // 定期地运行任何“对象的终止方法”（禁止在线上操作！）
    }

    /**
     * 追踪“FileInputStream.finalize(...)”的行为。
     */
    @OnMethod(
            clazz = probeClassName,
            method = "finalize" // 当无实例引用它时，确保“文件输入流的close方法”被调用。（垃圾回收机制）
    )
    public static void onFinalize(@Self Object me) {
        // @Self：标记一个方法参数表示 *this*实例
        // str(obj)：返回表示“给定对象”的字符串
        // printFields(obj)：以“名称-值”对形式打印“给定对象”的所有实例字段
        // get(field, obj)：获取“给定实例引用字段”的值
        println(concat("finalizing ", str(me)));
        printFields(me); // 打印“探测目标实例”持有的所有字段状态
        printFields(get(fdField, me)); // 打印“探测的字段实例”持有的所有字段状态
        println("==========");
    }

    /**
     * 追踪“FileInputStream.close(...)”的行为。
     */
    @OnMethod(
            clazz = probeClassName,
            method = "close" // 关闭本文件输入流，并释放任何与该文件流相关的系统资源。
    )
    public static void onClose(@Self Object me) {
        // currentThread()：返回“当前正在执行的线程对象”的一个引用
        println(concat("closing ", str(me)));
        println(concat("thread: ", str(currentThread()))); // 打印“当前线程”的字符串表示（包括 线程的名称、优先级、线程组）
        printFields(me);
        printFields(get(fdField, me));
        jstack(); // 打印“当前线程的Java调用栈信息”
        println("=============");
    }

}
