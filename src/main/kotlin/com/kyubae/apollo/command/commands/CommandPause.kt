package com.kyubae.apollo.command.commands

import com.kyubae.apollo.ApolloApplication.Companion.getGuildAudioPlayer
import com.kyubae.apollo.command.Command
import com.kyubae.apollo.command.SlashCommandInfo
import com.kyubae.apollo.util.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

@SlashCommandInfo(name = "pause", description = "Pause or unpause the player")
class CommandPause : Command {

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(false).queue()

        val musicManager: GuildMusicManager = getGuildAudioPlayer(event.guild!!)
        val paused: Boolean = musicManager.scheduler.pause()

        event.hook.sendMessage("Player has been " + (if (paused) "paused" else "resumed") + ".").queue()
    }

}