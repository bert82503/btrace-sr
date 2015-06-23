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
import java.net.*; // 与网络相关的包
import java.nio.channels.SocketChannel; // 一个可选择的“面向流连接的套接字通道”

/**
 * <p>
 *     本示例追踪“所有服务器套接字的创建和客户端套接字的接收”信息。
 *     这里也展示了“如何使用共享的方法”。
 * </p>
 * This example tracks all server socket creations and client socket accepts.
 * <br/>
 * Also, it shows how to use shared methods.
 */
@BTrace
public class SocketTracker {

    @TLS private static int port = -1; // 服务端的请求端口号
    @TLS private static InetAddress inetAddr; // 服务端的互联网协议（IP）地址
    @TLS private static SocketAddress sockAddr; // 客户端的套接字地址

    /**
     * 追踪“服务端的套接字请求连接信息”（new ServerSocket(int port, int backlog, InetAddress bindAddr)）。
     *
     * @param self 服务端套接字对象
     * @param p 服务端的请求端口号
     * @param backlog 请求队列长度
     * @param bindAddr 服务端的互联网协议（IP）地址
     */
    @OnMethod(
        clazz="java.net.ServerSocket", // 服务端套接字请求连接
        method="<init>" // 构造器方法
    )
    public static void onServerSocket(@Self ServerSocket self,
        int p, int backlog, InetAddress bindAddr) {
        port = p;
        inetAddr = bindAddr;
    }

    @OnMethod(
        clazz="java.net.ServerSocket",
        method="<init>", // 构造器方法
        type="void (int, int, java.net.InetAddress)", // 方法类型声明
        location=@Location(Kind.RETURN) // 方法返回时执行
    )
    public static void onSockReturn() {
        if (port != -1) {
            println(Strings.strcat("server socket port at ", Strings.str(port)));
            port = -1;
        }
        if (inetAddr != null) {
            println(Strings.strcat("server socket address at ", Strings.str(inetAddr)));
            inetAddr = null;
        }
    }

    /**
     * 追踪“ServerSocket.bind(SocketAddress endpoint, int backlog)”的行为。
     */
    @OnMethod(
        clazz="java.net.ServerSocket",
        method="bind" // 绑定到服务端的IP地址和端口号
    )
    public static void onBind(@Self ServerSocket self, SocketAddress addr, int backlog) {
        sockAddr = addr;
    }

    @OnMethod(
        clazz="java.net.ServerSocket",
        method="bind",
        type="void (java.net.SocketAddress, int)", // 方法类型声明
        location=@Location(Kind.RETURN) // 方法返回时执行
    )
    public static void onBindReturn() {
        socketBound();
    }

    /**
     * 追踪“ServerSocketChannelImpl.bind(SocketAddress endpoint, int backlog)”的行为。
     */
    @OnMethod(
        clazz="sun.nio.ch.ServerSocketChannelImpl", // “客户端套接字通道”实现类
        method="bind" // 绑定到客户端的IP地址和端口号
    )
    public static void onBind(@Self Object self, SocketAddress addr, int backlog) {
        sockAddr = addr;
    }

    @OnMethod(
        clazz="sun.nio.ch.ServerSocketChannelImpl",
        method="bind",
        type="void (java.net.SocketAddress, int)",
        location=@Location(Kind.RETURN)
    )
    public static void onBindReturn2() {
        socketBound();
    }

    /**
     * 追踪“ServerSocket.accept()”的行为。
     */
    @OnMethod(
        clazz="java.net.ServerSocket",
        method="accept", // 服务端接收到一个来自客户端的请求
        location=@Location(Kind.RETURN)
    )
    public static void onAcceptReturn(@Return Socket sock) { // 标记“一个方法参数”作为“被探测方法的返回值”
        clientSocketAccept(sock);
    }

    /**
     * 追踪“ServerSocketChannelImpl.socket()”的行为。
     */
    @OnMethod(
        clazz="sun.nio.ch.ServerSocketChannelImpl",
        method="socket", // 创建客户端到服务端的连接
        location=@Location(Kind.RETURN)
    )
    public static void onSocket(@Return ServerSocket serverSock) { // 标记“一个方法参数”作为“被探测方法的返回值”
        println(Strings.strcat("server socket at ", Strings.str(serverSock)));
    }

    /**
     * 追踪“ServerSocketChannelImpl.accept()”的行为。
     */
    @OnMethod(
        clazz="sun.nio.ch.ServerSocketChannelImpl",
        method="accept", // 客户端接收到来自服务端的响应内容
        location=@Location(Kind.RETURN)
    )
    public static void onAcceptReturn(@Return SocketChannel sockChannel) { // 标记“一个方法参数”作为“被探测方法的返回值”
        clientSocketAccept(sockChannel);
    }

    /**
     * 打印“套接字成功绑定的信息（IP地址和端口号）”。
     */
    private static void socketBound() {
        if (sockAddr != null) {
            println(Strings.strcat("server socket bind ", Strings.str(sockAddr)));
            sockAddr = null;
        }
    }

    /**
     * 打印“服务端成功接收到客户端套接字的请求信息”。
     */
    private static void clientSocketAccept(Object obj) {
        if (obj != null) {
            println(Strings.strcat("client socket accept ", Strings.str(obj)));
        }
    }

}
