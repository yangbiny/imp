package com.impassive.imp.common.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {

  private static final Map<Class, ExtensionLoader> EXTENSION_LOADER_MAP = new ConcurrentHashMap<>();

  private final Class<T> type;

  private ExtensionLoader(Class<T> type) {
    this.type = type;
  }

  public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> tClass) {
    ExtensionLoader extensionLoader = EXTENSION_LOADER_MAP.get(tClass);
    if (extensionLoader != null) {
      return extensionLoader;
    }
    extensionLoader = new ExtensionLoader<>(tClass);
    EXTENSION_LOADER_MAP.putIfAbsent(tClass, extensionLoader);
    return extensionLoader;
  }

  public T getValue() {
    try {
      return type.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
