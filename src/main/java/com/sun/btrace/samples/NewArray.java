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
 *     本脚本演示了拦截数组创建的可能性，它会在特定的方法体内被执行。
 *     这里通过使用“{@linkplain Kind#NEWARRAY}位置值”来完成。
 * </p>
 * This script demonstrates the possibility to intercept array creations
 * that are about to be executed from the body of a certain method.
 * This is achieved by using the {@linkplain Kind#NEWARRAY} location value.
 */
@BTrace
public class NewArray {

    // component count (组件计数器)
    private static volatile long count;

    @OnMethod(
      clazz="/.*/", // tracking in all classes; can be restricted to specific user classes (追踪所有类型)
      method="/.*/", // tracking in all methods; can be restricted to specific user methods (追踪所有方法)
      location=@Location(value=Kind.NEWARRAY, clazz="char") // 在“新的字符数组”创建时
    )
    public static void onNew(@ProbeClassName String pcn, @ProbeMethodName String pmn,
                             String arrType, int dim) {
        // pcn - allocation place class name
        // pmn - allocation place method name
        // **** following two parameters MUST always be in this order (下面的两个参数必须是这样的顺序)
        // arrType - the actual array type (真实的数组类型)
        // dim - the array dimension (数组维度)

        // increment counter on new array (新数组的递增计数器)
        count++;
    }

    @OnTimer(2000)
    public static void print() {
        // print the counter
        println(Strings.strcat("char[] count = ", str(count)));
    }

}
