package com.impassive.imp.serialization;

import com.impassive.imp.Serialization;
import com.impassive.imp.util.json.JsonTools;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;

public class JsonSerialization implements Serialization {

  @Override
  public byte[] serialization(Object obj) {
    if (obj == null) {
      return new byte[]{};
    }
    return JsonTools.writeToJson(obj).getBytes(StandardCharsets.UTF_8);
  }

  @Nullable
  @Override
  public <T> T deserialization(byte[] bytes, Class<T> tClass) {
    if (bytes.length == 0) {
      return null;
    }
    return JsonTools.readFromJson(new String(bytes), tClass);
  }
}
