package com.impassive.imp.common;

import com.impassive.imp.exception.common.ImpCommonException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ClassUtils {

  /**
   * 扫描指定类类型的包，并返回相关配置信息
   *
   * @param classType 类的类型
   * @return 类
   */
  public static List<ClassInfo> buildClass(@NotNull ClassType classType) {
    Enumeration<URL> resources;
    try {
      resources = Thread.currentThread().getContextClassLoader()
          .getResources(classType.getPath());
    } catch (IOException e) {
      log.error("build class has exception : {}", classType);
      throw new ImpCommonException("build class has exception : ", e);
    }
    List<ClassInfo> result = new ArrayList<>();
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      List<String> lines = parseFile(url.getPath());
      List<ClassInfo> collect = lines.stream()
          .filter(StringUtils::isNotEmpty)
          .map(item -> StringUtils.split(item, "="))
          .map(item -> {
            try {
              return new ClassInfo(item[0], Class.forName(item[1]));
            } catch (ClassNotFoundException e) {
              log.error("can not find class : {}", Arrays.toString(item));
              throw new ImpCommonException(e);
            }
          })
          .collect(Collectors.toList());
      result.addAll(collect);
    }
    return result;
  }

  private static List<String> parseFile(String path) {
    File file = new File(path);
    File[] files;
    if (file.isDirectory()) {
      files = file.listFiles();
    } else {
      files = new File[]{file};
    }
    if (files == null) {
      return Collections.emptyList();
    }
    try {
      List<String> result = new ArrayList<>();
      for (File file1 : files) {
        result.addAll(FileUtils.readLines(file1));
      }
      return result;
    } catch (IOException e) {
      log.error("parse file has error : {}", path);
      throw new ImpCommonException(e);
    }
  }

}
