package com.impassive.imp.registry;

import com.impassive.imp.protocol.Url;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;

/** @author impassivey */
public abstract class AbstractRegistryFactory implements RegistryFactory {

  private static final Map<String, Registry> REGISTRY_MAP = new ConcurrentHashMap<>();

  private static final String REGISTRY_KEY_TPL = "%s_%s_%s";

  private static final ReentrantLock LOCK = new ReentrantLock();

  private static final Map<RegistryType, RegistryFactory> REGISTRY_FACTORY_MAP =
      new ConcurrentHashMap<>();

  public static RegistryFactory getRegistryFactory(RegistryType registryType) {
    final RegistryFactory registryFactory = REGISTRY_FACTORY_MAP.get(registryType);
    if (registryFactory != null) {
      return registryFactory;
    }
    LOCK.lock();
    try {
      RegistryFactory factory = null;
      if (registryType == RegistryType.ZOOKEEPER) {
        factory = new ZookeeperRegistryFactory();
      }
      if (factory == null) {
        throw new IllegalArgumentException(
            "can not find registryFactory of type : " + registryType.name());
      }
      return factory;
    } finally {
      LOCK.unlock();
    }
  }

  @Override
  public Registry getRegistry(Url url) {
    if (url == null) {
      throw new IllegalArgumentException("url can not be null");
    }
    String key = url.buildRegistryKey(REGISTRY_KEY_TPL);
    final Registry registry = REGISTRY_MAP.get(key);
    if (registry != null) {
      return registry;
    }
    LOCK.lock();
    try {
      final Registry registryOld = REGISTRY_MAP.get(key);
      if (registryOld != null) {
        return registryOld;
      }
      final Registry newRegistry = createRegistry(url);
      if (newRegistry == null) {
        throw new IllegalStateException("can not create registry：" + url);
      }
      REGISTRY_MAP.put(key, newRegistry);
      return newRegistry;
    } finally {
      LOCK.unlock();
    }
  }

  /**
   * 每一个具体的注册中心创建对象的方式
   *
   * @param url url
   * @return 创建的对象
   */
  @Nullable
  protected abstract Registry createRegistry(Url url);

  /**
   * 返回注册中心的类型
   *
   * @return 注册中心的类型
   */
  protected abstract RegistryType registryType();
}
