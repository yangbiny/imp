package com.impassive.rpc.routing;

import com.impassive.rpc.discover.DiscoverService;
import java.util.List;

public interface ServiceRouting {

  /**
   * 通过服务发现的时候，进行服务路由
   *
   * @param discoverServices 发现的服务信息
   * @return 路由结果
   */
  DiscoverService routing(List<DiscoverService> discoverServices);

}
