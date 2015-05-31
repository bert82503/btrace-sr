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
 *     一个简单的示例：打印“所有系统属性”、“BTrace程序的输入参数列表”、“操作系统的所有环境变量值”并退出追踪程序。
 *
 *     本BTrace程序模仿JDK的“jinfo”命令行工具。
 * </p>
 * A simple sample that prints system properties, flags and exits.
 * This BTrace program mimics the jinfo command line tool in JDK.
 */
@BTrace
public class JInfo {

    static {
        println("System Properties:");
        printProperties(); // Sys.Env.printProperties()：打印“所有系统属性”信息
        println("VM Flags:");
        printVmArguments(); // Sys.VM.printVmArguments()：打印“BTrace程序的输入参数列表”
        println("OS Enviroment:");
        printEnv(); // Sys.Env.printEnv()：打印“操作系统的所有环境变量值”
        exit(0); // Sys.exit(exitCode)：退出“客户端追踪会话”，即终止“追踪行为方法”
    }

}
