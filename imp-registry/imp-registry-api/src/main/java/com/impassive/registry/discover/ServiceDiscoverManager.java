package com.impassive.registry.discover;

import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDiscoverManager implements ServiceDiscover {

  private static volatile ServiceDiscoverManager SERVICE_DISCOVER_MANAGER;

  private final Map<RegistryType, ServiceDiscover> discoverMap = new HashMap<>();

  private ServiceDiscoverManager() {
  }

  public static ServiceDiscoverManager getInstance() {
    if (SERVICE_DISCOVER_MANAGER != null) {
      return SERVICE_DISCOVER_MANAGER;
    }
    synchronized (ServiceDiscoverManager.class) {
      if (SERVICE_DISCOVER_MANAGER != null) {
        return SERVICE_DISCOVER_MANAGER;
      }
      SERVICE_DISCOVER_MANAGER = new ServiceDiscoverManager();
    }
    return SERVICE_DISCOVER_MANAGER;
  }

  @Override
  public List<DiscoverService> discover(Url url) {
    return discoverMap.get(url.getRegistryType()).discover(url);
  }

}
