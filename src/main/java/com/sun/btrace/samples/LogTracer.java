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
import java.util.logging.*;
import java.lang.reflect.Field;

/**
 * <p>
 *     简单的日志信息追踪类，打印任意日志级别的所有日志信息。
 *
 *     注意：我们使用内建的“field()”和“objectValue()”方法来读取 LogRecord 对象的私有“message”字段。
 * </p>
 * Simple log message tracer class.
 * This class prints all log messages regardless of log Level.
 *
 * Note that we read LogRecord's private "message" field using "field()" and "objectValue()" built-ins.
 */
@BTrace
public class LogTracer {

    // Reflective.field(clazz, name, true)：返回给定类中的指定“字段”对象
    private static Field msgField = Reflective.field("java.util.logging.LogRecord", "message");

    /**
     * 追踪“Logger.log(...)”的行为。
     *
     * @param self 日志实例（<code>Logger</code>）
     * @param record 日志记录实例（<code>LogRecord</code>）
     */
    @OnMethod(
        clazz="+java.util.logging.Logger",
        method="log"
    )
    public static void onLog(@Self Logger self, LogRecord record) {
        println(Reflective.get(msgField, record)); // 获取“给定实例引用字段”的值
    }

}
