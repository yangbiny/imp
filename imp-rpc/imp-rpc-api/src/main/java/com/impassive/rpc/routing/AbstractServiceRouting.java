package com.impassive.rpc.routing;

import com.impassive.imp.common.ServiceRoutingType;
import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.common.DiscoverService;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractServiceRouting implements ServiceRouting {

  public abstract ServiceRoutingType serviceRoutingType();

  protected abstract DiscoverService doRouting(List<DiscoverService> discoverServices);

  @Override
  public DiscoverService routing(Url url, List<DiscoverService> discoverServices) {
    if (CollectionUtils.isEmpty(discoverServices)) {
      throw new ImpCommonException("discover Service is empty");
    }
    if (discoverServices.size() == 1) {
      return discoverServices.get(0);
    }
    return doRouting(discoverServices);
  }
}
