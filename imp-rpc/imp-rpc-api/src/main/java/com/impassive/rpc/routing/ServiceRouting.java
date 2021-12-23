package com.impassive.rpc.routing;

import com.impassive.imp.common.Url;
import com.impassive.imp.common.DiscoverService;
import java.util.List;

public interface ServiceRouting {

  /**
   * 通过服务发现的时候，进行服务路由
   *
   * @param discoverServices 发现的服务信息
   * @return 路由结果
   */
  DiscoverService routing(Url url, List<DiscoverService> discoverServices);

}
