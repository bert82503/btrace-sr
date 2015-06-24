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
import java.lang.management.MemoryUsage; // “内存使用情况”的快照信息

/**
 * <p>
 *     本示例追踪“超过内存阈值的预警功能”。
 *     你需要指定“待观察的内存池名称”和“内存使用阈值”。
 *     你可以写脚本在跨“内存阈值”上通过“dumpHeap()”来转储“堆内存”，而非仅打印一条信息。
 *
 *     注意：“老年代”的名称与GC算法是独立的。
 *     使用“ParallelGC”算法，“内存池名称”是“PS Old Gen”。
 * </p>
 * This sample traces memory threshold exceeds.
 * You need to specify the memory pool to watch out and the usage threshold.
 * You can write script that dumps heap by dumpHeap on crossing the threshold instead of just printing a message.
 *
 * Note that the name of the old gen is dependent on GC algorithm.
 * With ParallelGC, the name is "PS Old Gen".
 */
@BTrace 
public class MemAlerter {

    /**
     * OnLowMemory：BTrace methods annotated by this annotation are called when
     * the traced JVM's specified memory pool exceeds specified threshold size.
     *
     * 当指定的“JVM内存池”超过“给定的阈值大小”时，被“@OnLowMemory”注释的BTrace方法就会被调用。
     *
     * @param mu 当前“内存使用情况”的快照信息
     */
    @OnLowMemory(
            // Young Gen：年轻代，Perm Gen：持久代
            pool = "Tenured Gen", // “老年代”内存池名称
            threshold = 6000000
    )
    public static void onLowMem(MemoryUsage mu) {
        println(mu);
    }

}
