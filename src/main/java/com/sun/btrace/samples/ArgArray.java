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
import com.sun.btrace.AnyType;
import static com.sun.btrace.BTraceUtils.*;

/**
 * <p>
 *     本示例演示“正则表达式探测点匹配”和获取输入参数作为一个数组。
 *     所以，任何类型的重载变量都可以追溯到“一个地方”。
 *     本示例追踪在java.io包中任何类的"readXXX"方法，
 *     探测的类、方法和参数数组在追踪行为中被打印。
 * </p>
 * This sample demonstrates regular expression probe matching and getting input arguments
 * as an array - so that any overload variant can be traced in "one place".
 * This example traces any "readXXX" method on any class in java.io package.
 * Probed class, method and arg array is printed in the action.
 */
@BTrace
public class ArgArray {

    @OnMethod(
        // 多个"类/方法"匹配
        clazz="/java\\.io\\..*/", // 匹配在"java.io"包中的任何类
        method="/read.*/" // 匹配任何"readXXX"方法
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        // @ProbeClassName：表示“探测目标类名”
        // @ProbeMethodName：表示“探测目标方法名”
        // AnyType：表示“任何引用类型(对象或数组)”都允许
        println(pcn);
        println(pmn);
        printArray(args); // 打印“输入参数数组”
    }

}
