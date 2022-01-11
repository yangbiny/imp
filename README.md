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

/imp/{groupName}/{interfaceName}
## 存储内容
imp://{providerIp}:{providerPort}/{interfaceName}/?groupName={groupName}&applicationName={applicationName}

# 扩展点
支持自动加载扩展点。需要在resource 下的 imp 文件夹中添加一个和接口全路径名相同的文件。添加到文件中对应的类会进行自动加载。

会根据SPI注解中配置的名称自动加载相关的类型。对于自适应类型的数据，会根据参数自动选择