package com.impassive.imp.util.json;

import com.alibaba.fastjson.JSON;
import com.impassive.imp.exception.serialize.SerializeException;
import java.io.Serializable;
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


}
