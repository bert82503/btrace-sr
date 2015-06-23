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
 *     演示多个由不同周期触发的定时器探测点。
 * </p>
 * Demonstrates multiple timer probes with different periods to fire.
 */
@BTrace
public class Timers {

    // “静态语句块”保证代码只在本类被加载时被执行一次
    // when starting print the target VM version and start time
    // 当程序启动时，打印目标Java虚拟机的版本和启动时间
    static {
        println(Strings.strcat("vm version ", Sys.VM.vmVersion())); // 打印JVM的实现版本
        println(Strings.strcat("vm startTime ", Strings.str(Sys.VM.vmStartTime()))); //打印JVM的启动时间（ms）
    }

    @OnTimer(1000)
    public static void vmUptime() {
        println(Strings.strcat("1000 msec: ", Strings.str(Sys.VM.vmUptime()))); // 打印JVM的运行时间（ms）
    }

    @OnTimer(3000)
    public static void currentTimeMillis() {
        println(Strings.strcat("3000 msec: ", Strings.str(Time.millis()))); // 打印系统的当前时间（ms）
    }

}
