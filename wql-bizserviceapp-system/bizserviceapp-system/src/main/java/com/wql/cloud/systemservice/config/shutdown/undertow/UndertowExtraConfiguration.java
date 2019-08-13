package com.wql.cloud.systemservice.config.shutdown.undertow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.undertow.UndertowOptions;

@Component
public class UndertowExtraConfiguration {

	@Autowired
    private GracefulShutdownWrapper gracefulShutdownWrapper;

    @Bean
    public UndertowEmbeddedServletContainerFactory servletWebServerFactory() {
    	UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownWrapper));
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
        return factory;
    }

}