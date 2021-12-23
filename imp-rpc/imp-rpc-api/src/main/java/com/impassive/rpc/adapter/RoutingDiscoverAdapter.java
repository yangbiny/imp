package com.impassive.rpc.adapter;

import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.common.DiscoverService;
import com.impassive.rpc.discover.impl.ServiceDiscoverManager;
import com.impassive.rpc.routing.impl.ServiceRoutingManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class RoutingDiscoverAdapter {

  private static final ServiceRoutingManager SERVICE_ROUTING_MANAGER = ServiceRoutingManager.getInstance();

  private static final ServiceDiscoverManager SERVICE_DISCOVER_MANAGER = ServiceDiscoverManager.getInstance();

  public static DiscoverService discoverAndRouting(Url url) {
    List<DiscoverService> discover = SERVICE_DISCOVER_MANAGER.discover(url);
    if (CollectionUtils.isEmpty(discover)) {
      log.error("can not find service : {}, {}", url.getGroupName(), url.getInterfaceName());
      throw new ImpCommonException(
          "can not find service " + url.getGroupName() + " " + url.getInterfaceName());
    }
    DiscoverService routing = SERVICE_ROUTING_MANAGER.routing(url, discover);
    if (routing == null) {
      throw new ImpCommonException("no instance can be use " + url);
    }
    return routing;
  }
}
