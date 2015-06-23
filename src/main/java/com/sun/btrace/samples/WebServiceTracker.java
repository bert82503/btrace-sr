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

import static com.sun.btrace.BTraceUtils.*;
import com.sun.btrace.annotations.*;

/**
 * <p>
 *     简单的BTrace程序：在“Web服务”被调用时打印类名和方法名，同时打印“服务方法的耗时”。
 *     “Web服务入口点”被注释为@WebService和@WebMethod，我们插入“追踪行为“到每一个使用了这些注解的类和方法。
 *     这种方式，我们无需知道真实的“Web服务服务实现类名”。
 * </p>
 * A simple BTrace program that prints a class name and method name whenever a webservice is called
 * and also prints time taken by service method.
 * WebService entry points are annotated javax.jws.WebService and javax.jws.WebMethod.
 * We insert tracing actions into every class and method annotated by these annotations.
 * This way we don't need to know actual WebService implement class name.
 */
@BTrace
public class WebServiceTracker {

    /**
     * 追踪所有“@WebService”注释的类中使用了“@WebMethod”注释的方法。
     *
     * @param pcn 探测的类名
     * @param pmn 探测的方法名
     */
    @OnMethod(
            clazz = "@javax.jws.WebService", // 匹配所有使用“@WebService”注释的类
            method = "@javax.jws.WebMethod" // 匹配所有使用“@WebMethod”注释的方法
    )
    public static void onWebServiceEntry(
            @ProbeClassName String pcn, @ProbeMethodName String pmn) {
        print("entering web service "); // 进入“Web服务”
        println(Strings.strcat(Strings.strcat(pcn, "."), pmn)); // 打印“类名.方法名”信息
    }

    @OnMethod(
            clazz = "@javax.jws.WebService", // 匹配所有使用“@WebService”注释的类
            method = "@javax.jws.WebMethod", // 匹配所有使用“@WebMethod”注释的方法
            location = @Location(Kind.RETURN) // 插入到方法返回时
    )
    public static void onWebServiceReturn(
            @ProbeClassName String pcn, @ProbeMethodName String pmn,
            @Duration long d // 方法调用时间
    ) {
        print("leaving web service "); // 离开“Web服务”
        println(Strings.strcat(Strings.strcat(pcn, "."), pmn)); // 打印“类名.方法名”信息
        println(Strings.strcat("Time taken (msec) ", Strings.str(d / 1000))); // 服务方法耗时毫秒数
        println("==========================");
    }

}
