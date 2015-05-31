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
import static com.sun.btrace.BTraceUtils.Threads.*; // Threads：“死锁检测、线程的调用栈信息、线程”辅助工具类

/**
 * <p>
 *     演示内建的“死锁检测(deadlocks)”函数。
 *
 *     本示例会每隔4秒钟打印“检测到的死锁信息”。
 * </p>
 * This BTrace program demonstrates deadlocks built-in function.
 * This example prints deadlocks (if any) once every 4 seconds.
 */
@BTrace
public class Deadlock {

    @OnTimer(4000) // 定时器
    public static void print() {
        deadlocks(); // 如果“死锁”存在的话，则打印“检测到的死锁信息”
    }

}
