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
import com.sun.btrace.annotations.Export; // 暴露“BTrace字段”

/**
 * <p>
 *     本示例创建了一个“jvmstat计数器”并在每次“Thread.start()”被调用时递增。
 *     本线程计数器可以在本进程之外被访问，被“@Export”注解的字段被映射到“jvmstat计数器”中。
 *     “jvmstat计数器名称”是“"btrace." + <className> + "." + <fieldName>”。
 * </p>
 * This sample creates a jvmstat counter and increments it everytime Thread.start() is called.
 * This thread count may be accessed from outside the process.
 * The @Export annotated fields are mapped to jvmstat counters.
 * The counter name is "btrace." + <className> + "." + <fieldName>
 */ 
@BTrace
public class ThreadCounter {

    // create a jvmstat counter using @Export
    // 使用“@Export”创建一个jvmstat计数器
    // @Export：使用本注解的“BTrace字段”会使用这种机制将自己暴露给进程之外的工具（例如jvmstat）
    @Export private static long count;

    /**
     * 追踪线程的“Thread.start(...)”开始运行行为。
     *
     * @param t 新创建的线程
     */
    @OnMethod(
        clazz="java.lang.Thread",
        method="start"
    ) 
    public static void onNewThread(@Self Thread t) {
        // updating counter is easy. Just assign to the static field!
        // 更新计数器很容易，只需分配给静态字段！
        count++;
    }

    @OnTimer(2000) 
    public static void onTimer() {
        // we can access counter as "count" as well as from jvmstat counter directly.
        // 我们可以访问“计数器”作为“计数”或者直接从“jvmstat计数器”读取
        println(count);
        // or equivalently ... (等价于...)
        println(Counters.perfLong("btrace.com.sun.btrace.samples.ThreadCounter.count")); // 访问“jvmstat计数器”
    }

}
