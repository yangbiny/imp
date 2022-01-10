package com.impassive.imp.common.extension;

import com.impassive.imp.common.ClassInfo;
import com.impassive.imp.common.ClassUtils;
import com.impassive.imp.exception.common.ImpCommonException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ExtensionLoader<T> {

  private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();

  private final Class<T> type;

  private final Map<String, T> instanceMap = new ConcurrentHashMap<>();

  private final List<WrapperClassInfo> wrapperCache = new ArrayList<>();

  private final List<ClassInfo> activityCache = new ArrayList<>();

  private final Map<T, T> wrapperInstance = new ConcurrentHashMap<>();

  private ExtensionLoader(Class<T> type) {
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> tClass) {
    ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADER_MAP.get(tClass);
    if (extensionLoader != null) {
      return extensionLoader;
    }
    synchronized (EXTENSION_LOADER_MAP) {
      extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADER_MAP.get(tClass);
      if (extensionLoader != null) {
        return extensionLoader;
      }
      extensionLoader = new ExtensionLoader<>(tClass);
      EXTENSION_LOADER_MAP.putIfAbsent(tClass, extensionLoader);
    }
    return extensionLoader;
  }

  public T getDefaultExtension() {
    if (type == null) {
      throw new NullPointerException("class type can not be null");
    }

    SPI spi = type.getAnnotation(SPI.class);
    if (spi == null) {
      throw new ImpCommonException("class must has spi info " + type);
    }
    String extensionName = StringUtils.isEmpty(spi.name()) ? spi.value() : spi.name();

    if (instanceMap.containsKey(extensionName)) {
      return instanceMap.get(extensionName);
    }

    // 1. 加栽class 名称对应的文件
    buildClass();

    // 2. 创建对象
    T instance = createInstance(extensionName);

    // 3. 如果存在包装类，则注入包装对象
    T finalInstance = injectWrapper(instance);
    instanceMap.putIfAbsent(extensionName, finalInstance);
    return finalInstance;
  }

  private void buildClass() {
    List<ClassInfo> classInfoList = ClassUtils.buildClassInfoList(type.getName());
    if (CollectionUtils.isEmpty(classInfoList)) {
      throw new ImpCommonException("can not find activity class for " + type.getName());
    }
    // 3. 遍历该文件下的所有数据
    buildClass(classInfoList);
  }

  private T injectWrapper(T instance) {
    if (wrapperCache.isEmpty()) {
      return instance;
    }
    for (ClassInfo classInfo : wrapperCache) {
      instance = injectWrapper(instance, classInfo);
    }
    return instance;
  }

  @SuppressWarnings("unchecked")
  private T injectWrapper(T instance, ClassInfo classInfo) {
    Constructor<?> constructor;
    try {
      constructor = classInfo.getClassPath().getConstructor(type);
      instance = (T) constructor.newInstance(instance);
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new ImpCommonException("execute constructor has error : ", e);
    } catch (NoSuchMethodException e) {
      throw new ImpCommonException("can not find no arg constructor of type " + classInfo);
    }
    return instance;
  }

  @SuppressWarnings("unchecked")
  private T createInstance(String extensionName) {
    if (StringUtils.isEmpty(extensionName)) {
      throw new ImpCommonException("extension name can not be empty");
    }

    Class<?> classType = null;
    for (ClassInfo classInfo : activityCache) {
      if (StringUtils.equals(classInfo.getName(), extensionName)) {
        classType = classInfo.getClassPath();
        break;
      }
    }
    if (classType == null) {
      throw new ImpCommonException("can not find activity class of type " + type);
    }
    Object instance;
    try {
      instance = classType.getConstructor().newInstance();
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new ImpCommonException("execute constructor has error : ", e);
    } catch (NoSuchMethodException e) {
      throw new ImpCommonException("can not find no arg constructor of type " + classType);
    }
    return (T) instance;
  }

  private void buildClass(List<ClassInfo> classInfoList) {
    if (CollectionUtils.isEmpty(classInfoList)) {
      return;
    }

    for (ClassInfo classInfo : classInfoList) {
      buildClass(classInfo);
    }
  }

  private void buildClass(ClassInfo classInfo) {

    checkAndAddActivity(classInfo);

    checkAndAddWrapper(classInfo);
  }

  private void checkAndAddActivity(ClassInfo classInfo) {
    Activity activity = classInfo.getClassPath().getAnnotation(Activity.class);
    if (activity == null) {
      return;
    }
    activityCache.add(classInfo);
  }

  private void checkAndAddWrapper(ClassInfo classInfo) {
    boolean isWrapper = true;
    try {
      classInfo.getClassPath().getConstructor(type);
    } catch (NoSuchMethodException e) {
      isWrapper = false;
    }
    if (isWrapper) {
      Extension extension = classInfo.getClassPath().getAnnotation(Extension.class);
      if (extension == null) {
        throw new ImpCommonException("wrapper class mush annotated with Extension");
      }
      wrapperCache.add(new WrapperClassInfo(classInfo, extension));
    }
  }

  public T wrapper(T instance) {
    if (wrapperInstance.containsKey(instance)) {
      return wrapperInstance.get(instance);
    }
    if (wrapperCache.isEmpty()) {
      buildClass();
    }
    T finalInstance = injectWrapper(instance);
    wrapperInstance.putIfAbsent(instance, finalInstance);
    return finalInstance;
  }

  @Getter
  private static class WrapperClassInfo extends ClassInfo {

    private final Integer order;

    private final Boolean active;

    public WrapperClassInfo(ClassInfo classInfo, Extension extension) {
      super(classInfo.getName(), classInfo.getClassPath());
      this.order = extension.order();
      this.active = extension.active();
    }
  }
}
