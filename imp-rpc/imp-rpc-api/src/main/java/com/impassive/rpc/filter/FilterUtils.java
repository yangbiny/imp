package com.impassive.rpc.filter;

import com.impassive.imp.common.ClassInfo;
import com.impassive.imp.common.ClassType;
import com.impassive.imp.common.ClassUtils;
import com.impassive.imp.exception.common.ImpCommonException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class FilterUtils {

  private static final List<Filter> FILTER_LIST = new ArrayList<>();

  public static List<Filter> buildAllFilter() {
    if (CollectionUtils.isNotEmpty(FILTER_LIST)) {
      return FILTER_LIST;
    }
    Enumeration<URL> resources;
    try {
      resources = FilterUtils.class.getClassLoader().getResources("filter");
    } catch (IOException e) {
      log.error("read filter has error", e);
      throw new ImpCommonException(e);
    }
    List<FilterMeta> filterMetas = new ArrayList<>();
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      String path = url.getPath();
      List<ClassInfo> filterClassInfo = ClassUtils.buildClass(ClassType.filter);
      if (CollectionUtils.isEmpty(filterClassInfo)) {
        continue;
      }
      for (ClassInfo classStr : filterClassInfo) {
        FilterMeta filter = buildFilter(classStr);
        if (filter == null) {
          continue;
        }
        filterMetas.add(filter);
      }
    }
    List<Filter> collect = filterMetas.stream()
        .filter(FilterMeta::isActive)
        .sorted(Comparator.comparing(FilterMeta::getOrder))
        .map(FilterMeta::getFilter)
        .collect(Collectors.toList());
    FILTER_LIST.addAll(collect);
    return FILTER_LIST;
  }

  private static FilterMeta buildFilter(ClassInfo classStr) {
    Class<?> aClass = classStr.getClassPath();
    Class<?>[] interfaces = aClass.getInterfaces();
    Filter filter;
    boolean hasFilterInterface = false;
    for (Class<?> anInterface : interfaces) {
      if (anInterface == Filter.class) {
        hasFilterInterface = true;
        break;
      }
    }
    if (!hasFilterInterface) {
      throw new ImpCommonException("class must implements Filter : " + classStr);
    }
    ImpFilter impFilter = aClass.getAnnotation(ImpFilter.class);
    if (impFilter == null) {
      throw new ImpCommonException("class must have annotation @ImpFilter : " + classStr);
    }
    boolean active = impFilter.active();
    try {
      filter = (Filter) aClass.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      log.error("create instance has error : " + classStr, e);
      throw new ImpCommonException(e);
    }
    return new FilterMeta(filter, active, impFilter.order());
  }


}
