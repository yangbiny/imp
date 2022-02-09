package com.impassive.imp.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author impassivey
 */
@Slf4j
public class NetUtils {

  public static String getAddress() {
    final Enumeration<NetworkInterface> interfaces;
    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      log.error("get network has error:", e);
      throw new RuntimeException("get network error", e);
    }
    try {

      while (interfaces.hasMoreElements()) {
        NetworkInterface ip = interfaces.nextElement();
        if (ip.isLoopback() || ip.isVirtual() || !ip.isUp() || ip.isPointToPoint()) {
          continue;
        }
        final Enumeration<InetAddress> addresses = ip.getInetAddresses();
        while (addresses.hasMoreElements()) {
          final InetAddress inetAddress = addresses.nextElement();
          if (!(inetAddress instanceof Inet4Address)) {
            continue;
          }
          return inetAddress.getHostAddress();
        }
      }
      throw new IllegalStateException("while get LocalAddress Error");
    } catch (Exception e) {
      throw new IllegalStateException("while get LocalAddress Error");
    }
  }

  public static String socketAddressToStr(SocketAddress socketAddress) {
    if (!(socketAddress instanceof InetSocketAddress)) {
      return null;
    }
    String hostAddress = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
    int port = ((InetSocketAddress) socketAddress).getPort();
    return String.format("%s:%s", hostAddress, port);
  }
}
