package pl.adomanski.webflux

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.zalando.problem.ProblemModule

@Configuration
class JsonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder()
                .modulesToInstall(ProblemModule().withStackTraces(false))
                .build()
    }
}