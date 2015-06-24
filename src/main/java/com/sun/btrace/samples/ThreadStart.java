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
 *     本脚本插入一个探测点到“Thread.start()”方法的方法入口点。
 *     在每一次Thread.start()被调用时，它会触发一个“DTrace探测点”并打印“线程的名称”。
 *     一个“DTrace脚本文件”（jthread.d）可能被用于获取相关的“DTrace探测点事件”。
 * </p>
 * This BTrace script inserts a probe into method entry of java.lang.Thread.start() method.
 * At each Thread.start(), it raises a DTrace probe in addition to printing the name of the thread.
 * A D-script like jthread.d may be used to get the associated DTrace probe events.
 */
@DTraceRef("jthread.d") // 关联一个“DTrace脚本文件（D-script）”
@BTrace
public class ThreadStart {

    /**
     * 追踪线程开始执行的“Thread.start()”行为。
     */
    @OnMethod(
        clazz="java.lang.Thread",
        method="start"
    ) 
    public static void onNewThread(@Self Thread t) {
        D.probe("jthreadstart", Threads.name(t));
        println(Strings.strcat("starting ", Threads.name(t))); // 验证“DTrace脚本文件”打印的线程名称是否正确
    }

}
