package com.impassive.imp.serialization;


import com.google.protobuf.InvalidProtocolBufferException;
import com.impassive.imp.Serialization;
import com.impassive.imp.common.extension.Activity;
import com.impassive.imp.serialization.Message.MessageValue;
import com.impassive.imp.util.json.JsonTools;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Activity
public class ProtoSerialization implements Serialization {

  @Override
  public byte[] serialization(Object obj) {
    String json = JsonTools.writeToJson(obj);
    MessageValue build = MessageValue.newBuilder().setMsg(json).build();
    return build.toByteArray();
  }

  @Nullable
  @Override
  public <T> T deserialization(byte[] bytes, Class<T> tClass) {
    String msg = buildMsg(bytes);
    return JsonTools.readFromJson(msg, tClass);
  }

  @Nullable
  @Override
  public <T> T deserialization(byte[] bytes, Type type) {
    String msg = buildMsg(bytes);
    return JsonTools.readFromJson(msg, type);
  }

  private String buildMsg(byte[] bytes) {
    MessageValue messageValue;
    try {
      messageValue = MessageValue.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      log.error("proto parse has error :", e);
      throw new RuntimeException(e);
    }
    return messageValue.getMsg();
  }
}
