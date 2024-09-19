package com.kyubae.apollo.command.commands

import com.kyubae.apollo.ApolloApplication.Companion.getGuildAudioPlayer
import com.kyubae.apollo.command.Command
import com.kyubae.apollo.command.SlashCommandInfo
import com.kyubae.apollo.util.GuildMusicManager
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