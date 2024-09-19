package com.kyubae.apollo.command.commands

import com.kyubae.apollo.ApolloApplication.Companion.getGuildAudioPlayer
import com.kyubae.apollo.command.Command
import com.kyubae.apollo.command.SlashCommandInfo
import com.kyubae.apollo.util.GuildMusicManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import okhttp3.internal.format

@SlashCommandInfo(name = "now-playing", description = "Shows what is currently playing")
class CommandNowPlaying : Command {

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(false).queue()

        val musicManager: GuildMusicManager = getGuildAudioPlayer(event.guild!!)
        val currentTrack: AudioTrack? = musicManager.scheduler.current()

        if (currentTrack == null) {
            event.hook.sendMessage("Nothing playing currently!").queue()
            return
        }

        val trackInfo: AudioTrackInfo = currentTrack.info

        event.hook.sendMessage(format("Currently playing: %s\nDuration: %s/%s", trackInfo.title, currentTrack.position, trackInfo.length)).queue()
    }

}