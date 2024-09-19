package com.kyubae.hermes.configuration

import com.kyubae.hermes.util.DiscordEventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfiguration {

    @Autowired
    private lateinit var discordProperties: DiscordProperties

    @Autowired
    private lateinit var applicationProperties: ApplicationProperties

    private final val gatewayIntents = listOf(
        GatewayIntent.MESSAGE_CONTENT
    )

    @Bean
    fun jda(): JDA {
        return JDABuilder.createDefault(this.discordProperties.token).enableIntents(this.gatewayIntents).addEventListeners(DiscordEventListener(applicationProperties)).build()
    }

}