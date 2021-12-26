package com.impassive.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author impassivey
 */
public class TestInet {

  public static void main(String[] args) throws SocketException {
    final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface ip = interfaces.nextElement();
      final Enumeration<InetAddress> addresses = ip.getInetAddresses();
      while (addresses.hasMoreElements()){
        final InetAddress inetAddress = addresses.nextElement();
        if (!(inetAddress instanceof Inet4Address) || inetAddress.isLoopbackAddress()){
          continue;
        }
        if (!inetAddress.isSiteLocalAddress()) {
          continue;
        }
        System.out.println(inetAddress.getHostAddress());
      }
    }
  }
}
