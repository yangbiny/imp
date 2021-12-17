package com.impassive.registry.discover;

import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import java.util.List;

public class ZookeeperServiceDiscover extends AbstractServiceDiscovery {

  @Override
  public List<DiscoverService> doDiscover(Url url) {
    return null;
  }

  @Override
  protected RegistryType registryType() {
    return RegistryType.ZOOKEEPER;
  }
}
