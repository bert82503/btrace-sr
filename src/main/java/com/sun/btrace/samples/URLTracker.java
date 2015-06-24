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
import java.net.*;

/**
 * <p>
 *     本示例打印每次“URL.openConnection(...)”成功返回的行为。
 *     此外，在DTrace可用的平台上，它会运行“jurls.d”DTrace脚本文件，
 *     该脚本会收集通过“btrace:::event探测点”访问的URL请求历史记录。
 *     通过本BTrace程序，我们打开了“DTrace探测点”（调用dtraceProbe(...)方法）。
 *
 *     注意：这可能与直接使用BTrace程序本身实现历史记录的功能很相似（见 Histogram.java）。
 *     但是，本示例展示了“DTrace/BTrace”集成的功能。
 *     在程序退出时，所有“DTrace”脚本会聚合到BTrace进行打印（不会显示地调用DTrace的打印功能）。
 * </p>
 * This sample prints every Java URL openURL and openConnection (successful) attempts.
 * In addition, on platforms where DTrace is available, it runs
 * the D-script jurls.d -- which collects a histogram of URL accesses by a btrace:::event probe.
 * From this BTrace program we raise that DTrace probe (dtraceProbe call).
 *
 * Note that it is possible to do similar histogram in BTrace itself (see Histogram.java).
 * But, this sample shows DTrace/BTrace integration as well.
 * On exit, all DTrace aggregates are printed by BTrace (i.e., the ones
 * that are not explicitly printed by DTrace print call).
 */
@DTraceRef("jurls.d") // 关联一个“DTrace脚本文件（D-script）”
@BTrace
public class URLTracker {

    @TLS private static URL url; // 请求URL

    /**
     * 追踪URL打开请求连接的“URL.openConnection()”行为。
     */
    @OnMethod(
        clazz="java.net.URL",
        method="openConnection"
    )
    public static void openURL(URL self) {
        url = self;
    }

    /**
     * 追踪URL打开代理请求连接的“URL.openConnection(Proxy proxy)”行为。
     */
    @OnMethod(
        clazz="java.net.URL",
        method="openConnection"
    )
    public static void openURL(URL self, Proxy p) {
        url = self;
    }

    @OnMethod(
        clazz="java.net.URL",
        method="openConnection",
        location=@Location(Kind.RETURN) // 连接成功后返回
    )
    public static void openURLReturn() {
        if (url != null) {
            println(Strings.strcat("open ", Strings.str(url)));
            // D：Wraps the dtrace related BTrace utility methods
            // D：包装与“DTrace”相关的BTrace辅助方法
            // D.probe(...)：BTrace到DTrace通信通道
            D.probe("java-url-open", Strings.str(url));
            url = null;
        }
    }

}
