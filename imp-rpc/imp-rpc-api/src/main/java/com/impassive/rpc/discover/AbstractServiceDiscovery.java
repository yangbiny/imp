package com.impassive.rpc.discover;

import com.impassive.imp.common.DiscoverService;
import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import java.util.List;

public abstract class AbstractServiceDiscovery implements ServiceDiscover {

  protected abstract List<DiscoverService> doDiscover(Url url);

  public abstract RegistryType registryType();

  @Override
  public List<DiscoverService> discover(Url url) {
    return doDiscover(url);
  }
}
