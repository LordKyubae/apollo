package com.kyubae.hermes.util.listener

import com.kyubae.hermes.HermesApplication.Companion.commands
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SlashCommandInteractionEventListener : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        commands[event.name]?.execute(event) ?: event.reply("Unknown command: ${event.name}").queue()
    }

}