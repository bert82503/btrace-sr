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
import static com.sun.btrace.BTraceUtils.Sys.*;
import static com.sun.btrace.BTraceUtils.Threads.*;

/**
 * <p>
 *     一个简单的示例：打印所有线程的“调用栈信息”并退出“追踪程序”。
 *
 *     本BTrace程序模仿JDK的“jstack”命令行工具。
 * </p>
 * A simple sample prints stack traces and exits.
 * This BTrace program mimics the jstack command line tool in JDK.
 */
@BTrace
public class JStack {

    // “静态语句块”保证代码只被执行一次
    static {
        deadlocks(false); // 只打印“检测到的死锁线程信息”，不打印“死锁线程的调用栈信息”
        jstackAll(); // 打印“所有Java线程的调用栈信息”
        exit(0);
    }

}
