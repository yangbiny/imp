package com.impassive.registry;

import com.impassive.imp.common.Url;
import com.impassive.imp.util.json.JsonTools;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author impassivey
 */
@Slf4j
public class ZookeeperRegistry extends AbstractRegistry {

  private ZooKeeper zooKeeperClient;

  public ZookeeperRegistry(Url url) {
    super(url);

    try {
      this.zooKeeperClient = new ZooKeeper(buildZookeeperRegistry(url), 10000,
          new ZookeeperWatcher());
    } catch (IOException e) {
      throw new RuntimeException("create zookeeper Client error", e);
    }
  }

  private String buildZookeeperRegistry(Url url) {
    return String.format("%s:%s", url.getRegistryIp(), url.getRegistryPort());
  }

  @Override
  protected void doSubscribe(Url url) {
    final String path = ZookeeperUtils.buildConsumerPath(url);
    final String data = ZookeeperUtils.buildConsumerData(url);
    createPathAndSaveData(path, data);
  }

  @Override
  protected void doRegister(Url url) {
    final String path = ZookeeperUtils.buildProvidePath(url);
    final String data = ZookeeperUtils.buildProviderData(url);
    createPathAndSaveData(path, data);
  }

  @Override
  protected void doUnSubscribe(Url url) {
    final String path = ZookeeperUtils.buildConsumerPath(url);
    final String data = ZookeeperUtils.buildConsumerData(url);
    removeData(path, data);
  }

  @Override
  protected void doUnRegistry(Url url) {
    final String path = ZookeeperUtils.buildProvidePath(url);
    final String data = ZookeeperUtils.buildProviderData(url);
    removeData(path, data);
  }

  private void createPathAndSaveData(String path, String data) {
    try {
      createPath(path);
      saveData(path, data, false);
    } catch (InterruptedException | KeeperException e) {
      throw new RuntimeException("zookeeper has exception ", e);
    }
  }

  private void removeData(String path, String data) {
    try {
      byte[] existData = zooKeeperClient.getData(path, false, null);
      if (existData.length <= 0) {
        return;
      }
      List<String> dataList = JsonTools.readFromJsonList(new String(existData), String.class);
      dataList.remove(data);
      saveData(path, JsonTools.writeToJson(dataList), true);
    } catch (KeeperException | InterruptedException e) {
      log.error("zookeeper un register has error : ", e);
      throw new RuntimeException("zookeeper un registry error ", e);
    }
  }

  private void createPath(String path) throws InterruptedException, KeeperException {
    int i = path.lastIndexOf("/");
    if (i > 0) {
      final String substring = path.substring(0, i);
      createPath(substring);
    }
    final Stat exists = zooKeeperClient.exists(path, false);
    if (exists != null) {
      return;
    }
    zooKeeperClient.create(path, "".getBytes(StandardCharsets.UTF_8), Ids.OPEN_ACL_UNSAFE,
        CreateMode.PERSISTENT);
  }

  private void saveData(String path, String data, boolean override)
      throws KeeperException, InterruptedException {
    if (override) {
      zooKeeperClient.setData(path, data.getBytes(StandardCharsets.UTF_8), -1);
      return;
    }
    byte[] existData = zooKeeperClient.getData(path, false, null);
    List<String> strings = new ArrayList<>();
    if (existData != null && existData.length > 0) {
      strings = JsonTools.readFromJsonList(new String(existData), String.class);
      strings.add(data);
    }
    List<String> collect = strings.stream().distinct().collect(Collectors.toList());
    String dataValue = JsonTools.writeToJson(collect);
    zooKeeperClient.setData(path, dataValue.getBytes(StandardCharsets.UTF_8), -1);
  }

  @Override
  public void destroy() {
    try {
      zooKeeperClient.close();
    } catch (InterruptedException e) {
      log.error("close zk has exception : ", e);
    }
  }


  final private class ZookeeperWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
      if (event.getState() == KeeperState.Expired) {
        log.error("zookeeper expire, will recreate : {}", event);
        try {
          zooKeeperClient = new ZooKeeper(buildZookeeperRegistry(url), 10000, this);
        } catch (IOException e) {
          throw new RuntimeException("create zookeeper Client error", e);
        }
      }
    }
  }
}
