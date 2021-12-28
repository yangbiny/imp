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
  <version>1.0.2</version>
</dependency>
```
# 注册中心
## 存储路径

/imp/{applicationName}/{groupName}/{interfaceName}
## 存储内容
imp://{providerIp}:{providerPort}/{interfaceName}/?groupName={groupName}

# TODO
1. 限流
2. 扩展点
3. Mesh架构
4. 注册中心结构改变
4.1 provider
4.2 consumer