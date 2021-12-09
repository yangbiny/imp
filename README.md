# imp RPC 框架

# 简介

Imp是一个使用 Java 实现的 RPC框架，目前支持 Netty进行网络交互，使用Zookeeper作为
注册中心。

# 开始

在Maven中引入下述的代码即可
```
<dependency>
  <groupId>com.impassive</groupId>
  <artifactId>imp-all</artifactId>
  <version>1.0.0</version>
</dependency>
```


# TODO
1. 支持服务负载均衡
2. 支持监控
3. 服务调用失败重试
4. 跨进程参数传递
5. Filter机制