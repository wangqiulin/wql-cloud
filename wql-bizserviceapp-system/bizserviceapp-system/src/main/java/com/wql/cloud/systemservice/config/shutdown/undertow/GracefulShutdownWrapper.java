package com.wql.cloud.systemservice.config.shutdown.undertow;


import org.springframework.stereotype.Component;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;

@Component
public class GracefulShutdownWrapper implements HandlerWrapper{

    private GracefulShutdownHandler gracefulShutdownHandler;

    @Override
    public HttpHandler wrap(HttpHandler handler) {
        if(gracefulShutdownHandler == null) {
            this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
        }
        return gracefulShutdownHandler;
    }

    public GracefulShutdownHandler getGracefulShutdownHandler() {
        return gracefulShutdownHandler;
    }

}
