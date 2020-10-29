package com.paynerg.host.util;

public class Constants {
    public static final String SOCKET_URL = "http://24.211.130.88:8888";
    public static final String DEV_SOCKET_URL = "http://10.0.2.2:8888";

    public static class HostEvent{
        private HostEvent() {}

        public static final String EVENT_SEND_MENU = "send menu";
        public static final String EVENT_ORDER_READY = "order ready";
        public static final String EVENT_GUEST_HAS_ORDER = "guest has order";
    }
}
