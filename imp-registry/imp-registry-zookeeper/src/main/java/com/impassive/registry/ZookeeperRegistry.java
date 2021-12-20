package com.impassive.registry;

import com.impassive.imp.common.Url;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
      createPath(path, data);
    } catch (KeeperException | InterruptedException e) {
      throw new RuntimeException("zookeeper registry error ", e);
    }
  }

  private void createPath(String path, String data) throws InterruptedException, KeeperException {
    int i = path.lastIndexOf("/");
    if (i > 0) {
      final String substring = path.substring(0, i);
      createPath(substring, substring);
    }
    zooKeeperClient.setData(path, data.getBytes(StandardCharsets.UTF_8), -1);
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
