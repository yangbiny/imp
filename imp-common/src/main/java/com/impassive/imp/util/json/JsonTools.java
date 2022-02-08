package com.impassive.imp.util.json;

import com.alibaba.fastjson.JSON;
import com.impassive.imp.exception.serialize.SerializeException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonTools {


  public static String writeToJson(Object object) {
    if (object == null) {
      return "";
    }
    if (!(object instanceof Serializable)) {
      log.error("object must implements Serializable : {} ", object.getClass());
      throw new SerializeException("object must extent Serializable :" + object.getClass());
    }
    return JSON.toJSONString(object);
  }


  public static <T> T readFromJson(String value, Class<T> classType) {
    return JSON.parseObject(value, classType);
  }

  public static <T> T readFromJson(String paramStr, Type type) {
    return JSON.parseObject(paramStr, type);
  }

  public static <T> List<T> readFromJsonList(String paramStr, Class<T> typeReference) {
    return JSON.parseArray(paramStr, typeReference);
  }
}

