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
 *     每隔4秒打印“JVM内存”使用情况。
 *
 *     可以通过调整“已使用的内存阈值或一些其它条件”来改变转储“堆”的行为。（dumpHeap 是一个内建的方法）
 * </p>
 * Simple BTrace program that prints memory usage once every 4 seconds.
 * It is possible to modify this to dump heap depending on
 * used memory crossing a threshold or some other such condition. [dumpHeap is a built-in function].
 */
@BTrace
public class Memory {

    @OnTimer(4000) // 定时器（每隔4秒触发一次）
    public static void printMem() {
        println("Heap:");
        println(Sys.Memory.heapUsage()); // 打印“堆内存”使用情况
        println("Non-Heap:");
        println(Sys.Memory.nonHeapUsage()); // 打印“非堆内存”使用情况
    }

}
