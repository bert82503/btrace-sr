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

import java.sql.Statement;
import java.util.Map;

import com.sun.btrace.AnyType;
import com.sun.btrace.aggregation.Aggregation;
import com.sun.btrace.aggregation.AggregationFunction;
import com.sun.btrace.aggregation.AggregationKey;
import com.sun.btrace.annotations.*;

/**
 * <p>
 *     本脚本打印了所有执行的SQL语句的耗时信息，演示了不同类型的聚合函数。
 * </p>
 * BTrace script to print timings for all executed JDBC statements on an event.
 * Demonstrates different types of aggregation function.
 * <p>
 *
 * @author Christian Glencross 
 */
@BTrace
public class JdbcQueries {

    // “预编译SQL语句”描述信息本地缓存
    private static Map<Statement, String> preparedStatementDescriptions = Collections.newWeakMap();

    /*
     * Aggregation：存储"聚合函数"的结果
     * AggregationFunction：聚合函数的类型
     */
    // 计算“值的平方的频次分布”
    private static Aggregation histogram = Aggregations.newAggregation(AggregationFunction.QUANTIZE);

    // 计算“平均值”
    private static Aggregation average = Aggregations.newAggregation(AggregationFunction.AVERAGE);

    // 计算“最大值”
    private static Aggregation max = Aggregations.newAggregation(AggregationFunction.MAXIMUM);

    // 计算“最小值”
    private static Aggregation min = Aggregations.newAggregation(AggregationFunction.MINIMUM);

    // 计算“值的总和”
    private static Aggregation sum = Aggregations.newAggregation(AggregationFunction.SUM);

    // 统计“值的数量”
    private static Aggregation count = Aggregations.newAggregation(AggregationFunction.COUNT);

    // 全局统计“值的数量”
    private static Aggregation globalCount = Aggregations.newAggregation(AggregationFunction.COUNT);

    @TLS
    private static String preparingStatement; // 预编译SQL

    @TLS
    private static String executingStatement; // 执行SQL

    /**
     * <p>
     *     如果“--stack”参数通过命令行传入，则打印JDBC语句的Java调用栈追踪信息；
     *     否则，只打印SQL。
     * </p>
     * If "--stack" is passed on command line, print the Java stack trace of the JDBC statement.
     * Otherwise we print the SQL.
     */
    private static boolean useStackTrace = Sys.$(2) != null && Strings.strcmp("--stack", Sys.$(2)) == 0;

    // The first couple of probes capture whenever prepared statement and callable statements are
    // instantiated, in order to let us track what SQL they contain.
    // 当“预编译SQL语句”和“可执行的SQL语句”被实例化后，第一对探测点会被捕获。

    /**
     * Capture SQL used to create prepared statements.
     * 用于捕获创建的“预编译SQL”。
     * 追踪Connection及其子类的“prepareCall/prepareStatement”行为。
     * 
     * @param args
     *            the list of method parameters. args[0] is the SQL. (方法参数列表)
     */
    @OnMethod(
            clazz = "+java.sql.Connection",
            method = "/prepare(Call|Statement)/"
    )
    public static void onPrepare(AnyType[] args) {
        preparingStatement = useStackTrace ? Threads.jstackStr() : str(args[0]);
    }

    /**
     * Cache SQL associated with a prepared statement.
     * 缓存“预编译SQL”。
     * 
     * @param preparedStatement
     *            the return value from the prepareXxx() method. (prepareXxx()方法的返回SQL语句)
     */
    @OnMethod(
            clazz = "+java.sql.Connection",
            method = "/prepare(Call|Statement)/",
            location = @Location(Kind.RETURN)
    )
    public static void onPrepareReturn(@Return Statement preparedStatement) {
        if (preparingStatement != null) {
//             print("P"); // Debug Prepared
            Collections.put(preparedStatementDescriptions, preparedStatement, preparingStatement);
            preparingStatement = null;
        }
    }

    // The next couple of probes intercept the execution of a statement.
    // If it execute with no-args, then it must be a prepared statement or callable statement.
    // Get the SQL from the probes up above.
    // Otherwise the SQL is in the first argument.
    // 下一对探测点会拦截SQL语句的执行。
    // 如果未传入任何参数，则SQL语句必须是一个预编译或可执行的语句。
    // SQL可以从上面的探测点获得。
    // 否则，SQL就是第一个参数值。

    /**
     * 追踪SQL语句（Statement）的“execute/executeUpdate/executeQuery/executeBatch”行为。
     *
     * @param currentStatement 当前SQL执行语句
     * @param args 参数列表
     */
    @OnMethod(
            clazz = "+java.sql.Statement",
            method = "/execute($|Update|Query|Batch)/"
    )
    public static void onExecute(
            @Self Object currentStatement,
            AnyType[] args) {
        if (args.length == 0) {
            // No SQL argument; lookup the SQL from the prepared statement
            executingStatement = Collections.get(preparedStatementDescriptions, currentStatement);
        } else {
            // Direct SQL in the first argument
            executingStatement = useStackTrace ? Threads.jstackStr() : str(args[0]);
        }
    }

    @OnMethod(
            clazz = "+java.sql.Statement",
            method = "/execute($|Update|Query|Batch)/",
            location = @Location(Kind.RETURN)
    )
    public static void onExecuteReturn(
            @Duration long durationL) { // 方法执行时间
        if (executingStatement == null) {
            return;
        }

//        print("E"); // Debug Executed
        AggregationKey key = Aggregations.newAggregationKey(executingStatement);
        int duration = (int) durationL / 1000;

        Aggregations.addToAggregation(histogram, key, duration);
        Aggregations.addToAggregation(average, key, duration);
        Aggregations.addToAggregation(max, key, duration);
        Aggregations.addToAggregation(min, key, duration);
        Aggregations.addToAggregation(sum, key, duration);
        Aggregations.addToAggregation(count, key, duration);
        Aggregations.addToAggregation(globalCount, duration);

        executingStatement = null;
    }

    /**
     * 打印“SQL统计信息”。
     */
    @OnEvent
    public static void onEvent() {
        // Top 10 queries only (前10个最多的查询)
        Aggregations.truncateAggregation(histogram, 10);

        println("---------------------------------------------");
        Aggregations.printAggregation("Count", count);
        Aggregations.printAggregation("Min", min);
        Aggregations.printAggregation("Max", max);
        Aggregations.printAggregation("Average", average);
        Aggregations.printAggregation("Sum", sum);
        Aggregations.printAggregation("Histogram", histogram);
        Aggregations.printAggregation("Global Count", globalCount);
        println("---------------------------------------------");
    }

}
