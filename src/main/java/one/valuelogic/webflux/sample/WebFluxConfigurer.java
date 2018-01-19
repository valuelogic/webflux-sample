package one.valuelogic.webflux.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;

@Configuration
@Import(JsonConfig.class)
public class WebFluxConfigurer extends WebFluxConfigurationSupport {

    private static final MimeType[] MIME_TYPES = new MimeType[] {
        MimeType.valueOf("application/json"),
        MimeType.valueOf("application/problem+json")
    };

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        ServerCodecConfigurer.ServerDefaultCodecs defaultCodecs = configurer.defaultCodecs();
        defaultCodecs.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MIME_TYPES));
        defaultCodecs.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MIME_TYPES));
    }

    @Bean
    public MinimalHandlerStrategies handlerStrategies() {
        return new MinimalHandlerStrategies();
    }
}
