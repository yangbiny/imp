package com.impassive.rpc.routing.impl;

import com.impassive.imp.common.ClassInfo;
import com.impassive.imp.common.ClassType;
import com.impassive.imp.common.ClassUtils;
import com.impassive.imp.common.ServiceRoutingType;
import com.impassive.imp.common.Url;
import com.impassive.imp.exception.common.ImpCommonException;
import com.impassive.imp.common.DiscoverService;
import com.impassive.rpc.routing.AbstractServiceRouting;
import com.impassive.rpc.routing.ServiceRouting;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ServiceRoutingManager implements ServiceRouting {

  private static final Map<ServiceRoutingType, AbstractServiceRouting> SERVICE_ROUTING_MAP = new HashMap<>();

  private static volatile ServiceRoutingManager SERVICE_ROUTING_MANAGER;

  private ServiceRoutingManager() {
  }

  public static ServiceRoutingManager getInstance() {
    if (SERVICE_ROUTING_MANAGER != null) {
      return SERVICE_ROUTING_MANAGER;
    }
    synchronized (ServiceRoutingManager.class) {
      if (SERVICE_ROUTING_MANAGER != null) {
        return SERVICE_ROUTING_MANAGER;
      }
      SERVICE_ROUTING_MANAGER = new ServiceRoutingManager();
      return SERVICE_ROUTING_MANAGER;
    }
  }

  @Override
  public DiscoverService routing(Url url, List<DiscoverService> discoverServices) {
    ServiceRoutingType serviceRoutingType = url.getServiceRoutingType();
    // 默认是随机路由
    if (serviceRoutingType == null) {
      serviceRoutingType = ServiceRoutingType.random;
    }
    AbstractServiceRouting serviceRouting = SERVICE_ROUTING_MAP.get(serviceRoutingType);
    if (serviceRouting == null) {
      serviceRouting = buildServiceRouting(serviceRoutingType);
    }
    return serviceRouting.routing(url, discoverServices);
  }

  private AbstractServiceRouting buildServiceRouting(ServiceRoutingType serviceRoutingType) {
    List<ClassInfo> classInfos = ClassUtils.buildClass(ClassType.routing);
    if (CollectionUtils.isEmpty(classInfos)) {
      throw new ImpCommonException("can not find service routing");
    }

    Class<? extends ServiceRouting> routingClass = null;
    for (ClassInfo classInfo : classInfos) {
      if (StringUtils.equalsIgnoreCase(classInfo.getName(), serviceRoutingType.name())) {
        routingClass = (Class<? extends ServiceRouting>) classInfo.getClassPath();
        break;
      }
    }
    if (routingClass == null) {
      throw new ImpCommonException("can not find service routing of " + serviceRoutingType);
    }
    try {
      AbstractServiceRouting serviceRouting = (AbstractServiceRouting) routingClass.getConstructor()
          .newInstance();
      SERVICE_ROUTING_MAP.put(serviceRouting.serviceRoutingType(), serviceRouting);
      return serviceRouting;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new ImpCommonException("create service has exception : ", e);
    }
  }
}
