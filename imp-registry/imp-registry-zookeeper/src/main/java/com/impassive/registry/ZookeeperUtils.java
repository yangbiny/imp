package com.impassive.registry;

import com.impassive.imp.common.Url;
import com.impassive.imp.net.NetUtils;

public class ZookeeperUtils {

  public static String buildProviderData(Url url) {
    return String.format(
        ZookeeperConstant.PROVIDER_ZK_DATA,
        url.getProtocol(),
        url.getHost(),
        url.getPort(),
        url.getInterfaceName(),
        url.getGroupName());
  }

  public static String buildProvidePath(Url url) {
    return String.format(
        ZookeeperConstant.PROVIDER_ZK_PATH,
        url.getProtocol(),
        url.getApplicationName(),
        url.getGroupName(),
        url.getInterfaceName());
  }

  public static String buildConsumerData(Url url) {
    String address = NetUtils.getAddress();
    return String.format(ZookeeperConstant.CONSUMER_ZK_DATA,
        url.getProtocol(),
        address,
        url.getInterfaceName(),
        url.getGroupName(),
        url.getApplicationName());
  }

  public static String buildConsumerPath(Url url) {
    return String.format(
        ZookeeperConstant.CONSUMER_ZK_PATH,
        url.getProtocol(),
        url.getApplicationName(),
        url.getGroupName(),
        url.getInterfaceName());
  }

}
