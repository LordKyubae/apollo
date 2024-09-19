package com.kyubae.hermes.configuration

import com.kyubae.hermes.util.listener.ReadyEventListener
import com.kyubae.hermes.util.listener.SlashCommandInteractionEventListener
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

    @Bean
    fun jda(): JDA {
        return JDABuilder.createDefault(this.discordProperties.token)
            .enableIntents(
                GatewayIntent.GUILD_VOICE_STATES
            ).addEventListeners(
                ReadyEventListener(),
                SlashCommandInteractionEventListener()
        ).build()
    }

}