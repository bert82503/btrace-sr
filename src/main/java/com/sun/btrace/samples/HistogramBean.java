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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *     本示例收集了由追踪的程序创建的JComponets的历史记录。
 *     历史记录会每隔4秒被打印一次。
 *     同样，本示例将追踪的类暴露为一个JMX实体。
 *     在连接BTrace到目标应用之后，然后再连接VisualVM/jconsole/其它客户端到同样的应用。
 * </p>
 * This sample collects histogram of javax.swing.JComponets created by traced app.
 * The histogram is printed once every 4 seconds.
 * Also, this example exposes the trace class as a JMX bean.
 * After connecting BTrace to the target application, connect VisualVM or jconsole or any other
 * JMX client to the same application.
 */
@BTrace
public class HistogramBean {

    // @Property exposes this field as MBean attribute
    // @Property：暴露本字段作为MBean的属性
   @Property
   private static Map<String, AtomicInteger> history = newHashMap();

    @OnMethod(
        clazz="javax.swing.JComponent",
        method="<init>"
    ) 
    public static void onNewObject(@Self Object obj) {
        String className = name(classOf(obj));
        AtomicInteger classNameCounter = get(history, className);
        if (classNameCounter == null) {
            classNameCounter = newAtomicInteger(1);
            put(history, className, classNameCounter);
        } else {
            incrementAndGet(classNameCounter);
        }     
    }

    @OnTimer(4000) 
    public static void print() {
//        if (size(history) != 0) {
        if (!isEmpty(history)) {
            printNumberMap("Component Histogram", history);
        }
    }

}
