package com.impassive.imp.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

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
    while (interfaces.hasMoreElements()) {
      NetworkInterface ip = interfaces.nextElement();
      final Enumeration<InetAddress> addresses = ip.getInetAddresses();
      while (addresses.hasMoreElements()) {
        final InetAddress inetAddress = addresses.nextElement();
        if (!(inetAddress instanceof Inet4Address) || inetAddress.isLoopbackAddress()) {
          continue;
        }
        if (!inetAddress.isSiteLocalAddress()) {
          continue;
        }
        return inetAddress.getHostAddress();
      }
    }
    throw new IllegalStateException("while get LocalAddress Error");
  }
}
