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

// import statics from BTraceUtils class (导入BTraceUtils类的所有静态可视成员)
import static com.sun.btrace.BTraceUtils.*;
// import all BTrace annotations (导入所有的BTrace注解)
import com.sun.btrace.annotations.*;

/**
 * <p>
 *     当"一个类"被"用户定义的类加载器"加载时，则打印"栈踪迹"信息。
 *     在"ClassLoader.defineClass()"方法里插入一个"返回的探测点"，
 *     以便检测类被成功加载了。
 * </p>
 * A simple BTrace program that prints stack trace
 * whenever a class is loaded by a user-defined
 * class loader. We insert a return point probe in
 * ClassLoader.defineClass method to detect successful
 * class load.
 */
@BTrace // 标识一个“BTrace类”
public class Classload {

    @OnMethod( // 指定一个"探测点"（追踪行为方法）
            clazz = "+java.lang.ClassLoader", // 待探测或追踪的“全路径类名/正则匹配/注解类”
            method = "defineClass", // 待探测或追踪的“方法名/正则匹配/注解方法”
            location = @Location(Kind.RETURN)) // 标识精确的“位置”或感兴趣的“探测点”，以便在“方法集”中进行探测
    public static void defineclass(@Return Class<?> cl) { // 表示“被探测方法的返回值”
        println(Strings.strcat("loaded ", Reflective.name(cl))); // 返回“给定类型对象的名称”
//        println(concat("loaded ", Reflective.name(cl)));
        Threads.jstack(); // 打印“当前线程的Java调用栈信息”
        println("==========================");
    }

}
