package com.impassive.registry;

public class ZookeeperConstant {

  public static final String PROVIDER_ZK_PATH = "/%s/%s/%s/%s/provider";

  public static final String CONSUMER_ZK_PATH = "/%s/%s/%s/%s/consumer";

  public static final String PROVIDER_ZK_DATA = "%s://%s:%s/%s/?groupName=%s";

  public static final String CONSUMER_ZK_DATA = "%s://%s/%s/?groupName=%s&application=%s";

}
