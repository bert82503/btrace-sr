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
 *     本脚本演示了追踪系统引导类是可行的，并在追踪行为方法中以相同的方式调用。
 *
 *     在本示例中，我们在System.getProperty()方法中插入一个探测点，
 *     并调用内建的Sys.Env.property()方法，而无须无限地递归获取。
 *     一个线程本地标识在这里被用于避免无限地递归。
 * </p>
 * This BTrace script demonstrates that it is okay to trace bootstrap classes
 * and call the same inside the trace actions.
 *
 * In this example, we insert a probe into System.getProperty() method and
 * call System.getProperty [through Sys.Env.property() built-in function]
 * without getting into infinite recursion.
 * A thread local flag is used by BTrace to avoid infinite recursion here.
 */
@BTrace
public class SysProp {

    /**
     * 通过追踪“System.getProperty(String key)”的行为来打印指定的系统属性。
     *
     * @param name 属性名
     */
    @OnMethod(
        clazz="java.lang.System",
        method="getProperty"
    )
    public static void onGetProperty(String name) {
        println(name);
        // call property safely here. (安全地调用属性方法)
        println(Sys.Env.property(name)); // 打印“指定键的系统属性”
    }

}	
