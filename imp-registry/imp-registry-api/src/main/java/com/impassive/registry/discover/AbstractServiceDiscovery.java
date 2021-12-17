package com.impassive.registry.discover;

import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import java.util.List;

public abstract class AbstractServiceDiscovery implements ServiceDiscover {

  protected abstract List<DiscoverService> doDiscover(Url url);

  protected abstract RegistryType registryType();

  @Override
  public List<DiscoverService> discover(Url url) {
    return doDiscover(url);
  }
}
