package com.impassive.rpc.routing.impl;

import com.impassive.imp.common.DiscoverService;
import com.impassive.imp.common.ServiceRoutingType;
import com.impassive.rpc.routing.AbstractServiceRouting;
import java.util.List;
import java.util.Random;

public class RandomServiceRouting extends AbstractServiceRouting {

  private final Random random = new Random();

  @Override
  public ServiceRoutingType serviceRoutingType() {
    return ServiceRoutingType.random;
  }

  @Override
  protected DiscoverService doRouting(List<DiscoverService> discoverServices) {
    return discoverServices.get(random.nextInt(discoverServices.size() - 1));
  }
}
