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
 *     一个简单的示例：在启动时转储“目标JVM的堆内存”并退出“追踪程序”。
 *
 *     本BTrace程序模仿JDK的“jmap -dump”命令行工具。
 * </p>
 * A simple sample that dumps heap of the target at start and exits.
 * This BTrace program mimics the jmap tool (with -dump option).
 */
@BTrace
public class JMap {

    static { // 静态语句块
        String dumpedHeapFileName; // 保存“Java内存堆快照”的文件名
        if (Sys.$length() == 3) { // 命令行参数的数量
            dumpedHeapFileName = Sys.$(2); // 返回第2个命令行参数
        } else {
            dumpedHeapFileName = "heap.bin";
        }

        // 转储“Java堆的快照”到一个以hprof二进制格式的文件
        // 在追踪的应用的当前目录下，"./btrace<pid>/<btrace-class>/"目录会被创建.
        Sys.Memory.dumpHeap(dumpedHeapFileName);

        println("heap dumped!");
        Sys.exit(0);
    }

}
