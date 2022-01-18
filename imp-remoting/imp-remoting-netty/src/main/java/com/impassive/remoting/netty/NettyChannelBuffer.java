package com.impassive.remoting.netty;

import com.impassive.imp.remoting.ChannelBuffer;
import io.netty.buffer.ByteBuf;

public class NettyChannelBuffer implements ChannelBuffer {

  private final ByteBuf byteBuf;

  public NettyChannelBuffer(ByteBuf byteBuf) {
    this.byteBuf = byteBuf;
  }

  @Override
  public boolean readBoolean() {
    return byteBuf.readBoolean();
  }

  @Override
  public int readInt() {
    return byteBuf.readInt();
  }

  @Override
  public long readLong() {
    return byteBuf.readLong();
  }

  @Override
  public int readableBytes() {
    return byteBuf.readableBytes();
  }

  @Override
  public void readBytes(byte[] bytes, int begin, int length) {
    byteBuf.readBytes(bytes, begin, length);
  }

  @Override
  public void writeBytes(byte[] bytes) {
    byteBuf.writeBytes(bytes);
  }

  @Override
  public void writeInt(int intValue) {
    byteBuf.writeInt(intValue);
  }

  @Override
  public void writeLong(Long longValue) {
    byteBuf.writeLong(longValue);
  }

  @Override
  public void writeBoolean(boolean boolValue) {
    byteBuf.writeBoolean(boolValue);
  }
}
