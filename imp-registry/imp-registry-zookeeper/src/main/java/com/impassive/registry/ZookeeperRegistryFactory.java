package com.impassive.registry;

import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.Activity;
import com.impassive.registry.registry.Registry;
import javax.annotation.Nullable;

/** @author impassivey */
@Activity
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
