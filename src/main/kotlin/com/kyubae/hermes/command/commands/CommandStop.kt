package com.kyubae.hermes.command.commands

import com.kyubae.hermes.HermesApplication.Companion.getGuildAudioPlayer
import com.kyubae.hermes.command.Command
import com.kyubae.hermes.command.SlashCommandInfo
import com.kyubae.hermes.util.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

@SlashCommandInfo(name = "stop", description = "Stops the current track")
class CommandStop : Command {

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(false).queue()

        val musicManager: GuildMusicManager = getGuildAudioPlayer(event.guild!!)
        musicManager.scheduler.stop()

        event.hook.sendMessage("Skipped to next track.").queue()
    }

}