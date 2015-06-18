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
 *     本类演示了我们通过在“类和方法名上”使用“正则表达式”规范定义的“单个探测点”，
 *     可以同时探测多个类和多个方法。
 *
 *     在本例中，我们将“探测点”植入到所有“InputStream”类的所有“readXXX”方法中。
 * </p>
 * This BTrace class demonstrates that we can probe into multiple classes and methods
 * by a single probe specification using regular expressions
 * for class and/or method names as given below.
 * In the example, we put probe into all readXXX methods of all InputStream classes.
 */
@BTrace
public class MultiClass {

    @OnMethod(
        clazz="/java\\.io\\..*Input.*/", // 匹配“java.io”包下的所有满足“*Input*”正则的类
        method="/read.*/" // 匹配所有满足“read*”正则的方法
    )
    public static void onread(@ProbeClassName String pcn) {
        // Strings.concat(str1, str2)：将“指定的两个字符串”连接在一起
        println(Strings.strcat("read on ", pcn)); // 打印使用了哪个JDK IO类
    }

}
