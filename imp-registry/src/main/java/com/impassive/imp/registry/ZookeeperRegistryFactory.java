package com.impassive.imp.registry;

import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import javax.annotation.Nullable;

/** @author impassivey */
public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

  @Nullable
  @Override
  protected Registry createRegistry(Url url) {
    return new ZookeeperRegistry(url);
  }

  @Override
  protected RegistryType registryType() {
    return RegistryType.ZOOKEEPER;
  }
}
