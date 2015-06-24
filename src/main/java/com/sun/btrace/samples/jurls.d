
/*
 * 只要“btrace:::event”探测点作为第一个参数被“java-url-open”触发时，
 * 本“DTrace脚本文件”都会维护一个聚合功能。
 *
 * This D-script maintains a aggregation whenever
 * btrace:::event probe is raised with "java-url-open" as first argument.
 */

btrace$target:::event
/ copyinstr(arg0) == "java-url-open" /
{
    @[copyinstr(arg1)] = count();
}
