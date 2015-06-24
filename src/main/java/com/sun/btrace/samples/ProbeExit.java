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
 *     本程序演示了“@OnExit探测点”。
 *     当一些“BTrace行为方法”调用“exit(int)”内建方法时，被“@OnExit”注释的方法会被调用。
 *     在这个方法中，“BTrace脚本”打印了“追踪/清理”的概要信息。
 * </p>
 * This program demonstrates OnExit probe.
 * When some BTrace action method calls "exit(int)" built-in function,
 * method annotated by @OnExit (if found) is called.
 * In this method, BTrace script print summary information of tracing and/or do clean-up.
 */

@BTrace
public class ProbeExit {

    private static volatile int timeCounter;

    // @OnExit is called when some BTrace method calls exit(int) method
    // 当一些BTrace方法调用“exit(int)”方法时，被“@OnExit”注释的方法会被调用
    @OnExit
    public static void onExit(int code) {
        println("BTrace program exits!"); // 退出“BTrace程序”
    }

    // We just put @OnTimer probe and exit BTrace program when the count reaches 5.
    // 插入“@OnTimer探测点”，并在计数器到达5次时退出“BTrace程序”
    @OnTimer(1000)
    public static void onTime() {
        println("hello");
        timeCounter++;
        if (timeCounter == 5) {
            // note that this exits the BTrace client
            // and not the traced program (which would be a destructive action!).
            // 注意：这将退出“BTrace客户端”，而不是“追踪的程序”！
            // Sys: Wraps the OS related BTrace utility methods
            // Sys：包装与OS相关的BTrace辅助方法
            Sys.exit(0); // Exits the BTrace session (退出“BTrace会话”)
        }
    }

}
