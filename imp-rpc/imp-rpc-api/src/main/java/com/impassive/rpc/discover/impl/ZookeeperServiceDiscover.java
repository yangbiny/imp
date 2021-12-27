package com.impassive.rpc.discover.impl;

import com.impassive.imp.common.DiscoverService;
import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.util.json.JsonTools;
import com.impassive.registry.ZookeeperUtils;
import com.impassive.rpc.discover.AbstractServiceDiscovery;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

@Slf4j
public class ZookeeperServiceDiscover extends AbstractServiceDiscovery {

  private static final Map<String, ZooKeeper> zooKeeperClientMap = new HashMap<>();

  @Override
  public List<DiscoverService> doDiscover(Url url) {
    String address = buildDiscoverAddress(url);
    if (!zooKeeperClientMap.containsKey(address)) {
      buildZookeeperClient(address);
    }
    ZooKeeper zooKeeper = zooKeeperClientMap.get(address);
    if (zooKeeper == null) {
      throw new ImpCommonException("can not find zookeeper client : " + address);
    }
    String path = ZookeeperUtils.buildPath(url);
    byte[] dataByte;
    try {
      dataByte = zooKeeper.getData(path, false, null);
    } catch (KeeperException | InterruptedException e) {
      throw new ImpCommonException(e);
    }
    String data = new String(dataByte);
    List<String> urls = JsonTools.readFromJsonList(data, String.class);
    List<DiscoverService> discoverServiceList = new ArrayList<>();
    for (String urlStr : urls) {
      DiscoverService discoverService = buildDiscover(urlStr);
      discoverServiceList.add(discoverService);
    }
    return discoverServiceList;
  }

  private DiscoverService buildDiscover(String data) {
    URI uri;
    try {
      uri = new URI(data);
    } catch (URISyntaxException e) {
      throw new ImpCommonException(e);
    }
    String host = uri.getHost();
    String scheme = uri.getScheme();
    String urlPath = uri.getPath();
    int port = uri.getPort();
    DiscoverService discoverService = new DiscoverService();
    discoverService.setPort(port);
    discoverService.setClassName(urlPath.replaceAll("/", ""));
    discoverService.setHost(host);
    discoverService.setProtocol(scheme);
    return discoverService;
  }

  private void buildZookeeperClient(String address) {
    try {
      ZooKeeper zooKeeper = new ZooKeeper(address, 10000, new Watcher());
      zooKeeperClientMap.putIfAbsent(address, zooKeeper);
    } catch (IOException e) {
      throw new ImpCommonException(e);
    }
  }

  private String buildDiscoverAddress(Url url) {
    return String.format("%s:%s", url.getRegistryIp(), url.getRegistryPort());
  }

  @Override
  public RegistryType registryType() {
    return RegistryType.ZOOKEEPER;
  }

  @Override
  public void destroy() {
    for (ZooKeeper value : zooKeeperClientMap.values()) {
      try {
        value.close();
      } catch (InterruptedException e) {
        log.error("close zk has error ", e);
      }
    }
  }

  final static private class Watcher implements org.apache.zookeeper.Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
  }
}
