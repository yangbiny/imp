package com.impassive.rpc.discover;

import com.impassive.imp.common.Url;
import java.util.List;

/**
 * 服务发现。
 */
public interface ServiceDiscover {

  List<DiscoverService> discover(Url url);


}
