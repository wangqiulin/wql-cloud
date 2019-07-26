package com.wql.cloud.basic.oss.util;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {
	private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }
    
    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(500);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                    // 可选的, 关闭30秒内不活动的连接
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
