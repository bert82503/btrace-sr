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
 *     本示例演示了可以将一个BTrace类暴露为一个JMX MBean。
 *     在连接BTrace程序到目标应用程序后，再连接VisualVM或jconsole或其他JMX客户端到相同的应用。
 * </p>
 * This sample demonstrates that you can expose a BTrace class as a JMX MBean.
 * After connecting BTrace to the target application,
 * connect VisualVM or jconsole or any other JMX client to the same application.
 */ 
@BTrace
public class ThreadCounterBean {

    // @Property makes the count field to be exposed as an attribute of this MBean.
    // @Property使得“count字段”被暴露为本MBean对象的一个属性
    @Property // 使用本注解的“BTrace字段”会作为“动态JMX实体”的属性暴露给外界
    private static long count;

    /**
     * 追踪线程的“Thread.start()”开始运行行为。
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
        println(count);
    }

}
