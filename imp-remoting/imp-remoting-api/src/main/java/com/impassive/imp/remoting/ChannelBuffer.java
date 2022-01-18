package com.impassive.imp.remoting;

public interface ChannelBuffer {

  boolean readBoolean();

  int readInt();

  long readLong();

  int readableBytes();

  void readBytes(byte[] bytes, int begin, int length);

  void writeBytes(byte[] bytes);

  void writeInt(int intValue);

  void writeLong(Long longValue);

  void writeBoolean(boolean boolValue);
}
