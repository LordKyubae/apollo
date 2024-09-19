package com.kyubae.hermes.configuration

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Data
@Component
@ConfigurationProperties(prefix = "discord")
class DiscordProperties {

    lateinit var token: String

}