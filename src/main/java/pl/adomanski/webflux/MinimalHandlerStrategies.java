package pl.adomanski.webflux;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Collections;
import java.util.List;

@Component
class MinimalHandlerStrategies implements HandlerStrategies, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public List<HttpMessageReader<?>> messageReaders() {
        return context.getBean(ServerCodecConfigurer.class).getReaders();
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return context.getBean(ServerCodecConfigurer.class).getWriters();
    }

    @Override
    public List<ViewResolver> viewResolvers() {
        return Collections.emptyList();
    }

    @Override
    public List<WebFilter> webFilters() {
        return Collections.emptyList();
    }

    @Override
    public List<WebExceptionHandler> exceptionHandlers() {
        return Collections.singletonList(context.getBean(WebExceptionHandler.class));
    }

    @Override
    public LocaleContextResolver localeContextResolver() {
        return context.getBean(LocaleContextResolver.class);
    }
}
