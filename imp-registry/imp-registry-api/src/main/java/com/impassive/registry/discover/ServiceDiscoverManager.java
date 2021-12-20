package com.impassive.registry.discover;

import com.impassive.imp.common.ClassInfo;
import com.impassive.imp.common.ClassType;
import com.impassive.imp.common.ClassUtils;
import com.impassive.imp.common.RegistryType;
import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ServiceDiscoverManager implements ServiceDiscover {

  private static volatile ServiceDiscoverManager SERVICE_DISCOVER_MANAGER;

  private final Map<RegistryType, ServiceDiscover> discoverMap = new HashMap<>();

  private ServiceDiscoverManager() {

  }

  public static ServiceDiscoverManager getInstance() {
    if (SERVICE_DISCOVER_MANAGER != null) {
      return SERVICE_DISCOVER_MANAGER;
    }
    synchronized (ServiceDiscoverManager.class) {
      if (SERVICE_DISCOVER_MANAGER != null) {
        return SERVICE_DISCOVER_MANAGER;
      }
      SERVICE_DISCOVER_MANAGER = new ServiceDiscoverManager();
    }
    return SERVICE_DISCOVER_MANAGER;
  }

  @Override
  public List<DiscoverService> discover(Url url) {
    RegistryType registryType = url.getRegistryType();
    if (!discoverMap.containsKey(registryType)) {
      buildDiscover(registryType);
    }

    ServiceDiscover serviceDiscover = discoverMap.get(registryType);
    if (serviceDiscover == null) {
      throw new ImpCommonException("can not find service discover of type : " + registryType);
    }
    return serviceDiscover.discover(url);
  }

  private void buildDiscover(RegistryType registryType) {
    List<ClassInfo> classInfoList = ClassUtils.buildClass(ClassType.discover);
    if (CollectionUtils.isEmpty(classInfoList)) {
      return;
    }
    Class<?> classPath = null;
    for (ClassInfo classInfo : classInfoList) {
      if (StringUtils.equalsIgnoreCase(classInfo.getName(), registryType.name())) {
        classPath = classInfo.getClassPath();
        break;
      }
    }
    if (classPath == null) {
      return;
    }
    try {
      ServiceDiscover serviceDiscover = (ServiceDiscover) classPath.newInstance();
      discoverMap.putIfAbsent(registryType, serviceDiscover);
    } catch (InstantiationException | IllegalAccessException e) {
      log.error("can not find class : {}", classPath, e);
    }
  }

}
