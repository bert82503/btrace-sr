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
import com.sun.btrace.AnyType; // 表示参数可以是“任何类型”

/**
 * <p>
 *     本示例追踪“所有服务器套接字的创建和客户端套接字的接收”信息。
 *     与SocketTracker.java不同的是，本脚本仅使用公共的API类和@OnProbe探测点（可以去除重复的代码）。
 *     <code>@OnProbe</code>探测点可以在BTrace代理时通过一个XML描述符被映射到内部的实现类。
 *
 *     例如，XML探测点描述符是“java.net.socket.xml”。
 * </p>
 * This example tracks all server socket creations and client socket accepts.
 * Unlike SocketTracker.java, this script uses only public API classes and @OnProbe probes
 * - which would be mapped to internal implementation classes by a XML descriptor at BTrace agent.
 *
 * For this sample, XML probe descriptor is "java.net.socket.xml".
 */
@BTrace
public class SocketTracker1 {

    @TLS private static int port = -1;
    @TLS private static InetAddress inetAddr;
    @TLS private static SocketAddress sockAddr;

    /**
     * 追踪“服务端的套接字请求连接信息”（new ServerSocket(int port, int backlog, InetAddress bindAddr)）。
     *
     * @param self 服务端套接字对象
     * @param p 服务端的请求端口号
     * @param backlog 请求队列长度
     * @param bindAddr 服务端的互联网协议（IP）地址
     */
    @OnMethod(
        clazz="java.net.ServerSocket",
        method="<init>"
    )
    public static void onServerSocket(@Self ServerSocket self, 
        int p, int backlog, InetAddress bindAddr) {
        port = p;
        inetAddr = bindAddr;
    }

    @OnMethod(
        clazz="java.net.ServerSocket",
        method="<init>",
        type="void (int, int, java.net.InetAddress)",
        location=@Location(Kind.RETURN)
    )
    public static void onSockReturn() {
        if (port != -1) {
            println(Strings.strcat("server socket at ", Strings.str(port)));
            port = -1;
        }
        if (inetAddr != null) {
            println(Strings.strcat("server socket at ", Strings.str(inetAddr)));
            inetAddr = null;
        }
    }

    /**
     * 追踪“java.net.socket.server-socket-creator”探测点的行为。
     */
    // @OnProbe：本注解以一种更抽象的方式来指定“一个BTrace探测点”
    @OnProbe(
        namespace="java.net.socket",
        name="server-socket-creator" // 服务端套接字创建者
    )
    public static void onSocket(@Return ServerSocket serverSock) {
        println(Strings.strcat("server socket at ", Strings.str(serverSock)));
    }

    /**
     * 追踪“java.net.socket.bind”探测点的行为。
     */
    @OnProbe(
        namespace="java.net.socket",
        name="bind" // 服务端套接字绑定行为
    )
    public static void onBind(@Self Object self, SocketAddress addr, int backlog) {
        sockAddr = addr;
    }

    /**
     * 追踪“java.net.socket.bind-return”探测点的行为。
     */
    @OnProbe(
        namespace="java.net.socket",
        name="bind-return" // 服务端套接字绑定返回行为
    )
    public static void onBindReturn() {
        if (sockAddr != null) {
            println(Strings.strcat("server socket bind ", Strings.str(sockAddr)));
            sockAddr = null;
        }
    }

    /**
     * 追踪“java.net.socket.accept-return”探测点的行为。
     */
    @OnProbe(
        namespace="java.net.socket",
        name="accept-return" // 客户端套接字接收返回行为
    )
    public static void onAcceptReturn(AnyType sock) { // AnyType：表示参数可以是“任何类型”
        if (sock != null) {
            println(Strings.strcat("client socket accept ", Strings.str(sock)));
        }
    }

}
