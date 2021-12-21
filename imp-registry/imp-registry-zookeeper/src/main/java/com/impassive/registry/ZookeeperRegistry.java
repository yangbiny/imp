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
  protected void doRegister(Url url) {
    try {
      final String path = ZookeeperUtils.buildPath(url);
      final String data = ZookeeperUtils.buildData(url);
      createPath(path);
      saveData(path, data);
    } catch (KeeperException | InterruptedException e) {
      throw new RuntimeException("zookeeper registry error ", e);
    }
  }

  @Override
  protected void doUnRegistry(Url url) {

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
    zooKeeperClient.create(path, path.getBytes(StandardCharsets.UTF_8), Ids.OPEN_ACL_UNSAFE,
        CreateMode.PERSISTENT);
  }

  private void saveData(String path, String data) throws KeeperException, InterruptedException {
    byte[] existData = zooKeeperClient.getData(path, false, null);
    List<String> strings = new ArrayList<>();
    if (existData.length > 0) {
      strings = JsonTools.readFromJsonList(new String(existData), String.class);
      strings.add(data);
    }
    List<String> collect = strings.stream().distinct().collect(Collectors.toList());
    String dataValue = JsonTools.writeToJson(collect);
    zooKeeperClient.setData(path, dataValue.getBytes(StandardCharsets.UTF_8), -1);
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