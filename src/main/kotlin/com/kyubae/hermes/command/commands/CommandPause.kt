package com.kyubae.hermes.command.commands

import com.kyubae.hermes.HermesApplication.Companion.getGuildAudioPlayer
import com.kyubae.hermes.command.Command
import com.kyubae.hermes.command.SlashCommandInfo
import com.kyubae.hermes.util.GuildMusicManager
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