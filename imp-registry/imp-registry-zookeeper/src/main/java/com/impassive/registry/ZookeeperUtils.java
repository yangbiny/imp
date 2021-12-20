package com.impassive.registry;

import com.impassive.imp.common.Url;

public class ZookeeperUtils {

  public static String buildData(Url url) {
    return String.format(
        ZookeeperConstant.ZK_DATA,
        url.getProtocol(),
        url.getHost(),
        url.getPort(),
        url.getInterfaceName(),
        url.getGroupName());
  }

  public static String buildPath(Url url) {
    return String.format(
        ZookeeperConstant.ZK_PATH,
        url.getProtocol(),
        url.getApplicationName(),
        url.getGroupName(),
        url.getInterfaceName());
  }

}
