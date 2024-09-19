package com.kyubae.hermes.configuration

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Data
@Component
@ConfigurationProperties(prefix = "app")
class ApplicationProperties {

    lateinit var dataDirectory: String

}