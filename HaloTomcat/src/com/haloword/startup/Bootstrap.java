package com.haloword.startup;

import com.haloword.connector.http.HttpConnector;

public final class Bootstrap {
  public static void main(String[] args) {
    HttpConnector connector = new HttpConnector();
    connector.start();
  }
}