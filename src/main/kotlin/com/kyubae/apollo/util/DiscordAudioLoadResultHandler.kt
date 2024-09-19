package com.kyubae.apollo.util

import com.kyubae.apollo.entity.MyUserData
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.managers.AudioManager
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.nio.file.Files
import kotlin.io.path.Path

class DiscordAudioLoadResultHandler(private val event: SlashCommandInteractionEvent, private val musicManager: GuildMusicManager, private val trackUrl: String) : AudioLoadResultHandler {

    override fun trackLoaded(track: AudioTrack) {
        event.hook.sendMessage("Adding to queue " + track.info.title).queue()

        this.play(event.guild!!, musicManager, track)
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
        var firstTrack = playlist.selectedTrack

        if (firstTrack == null) {
            firstTrack = playlist.tracks[0]
        }

        event.hook.sendMessage("Adding to queue " + firstTrack.info.title + " (first track of playlist " + playlist.name + ")").queue()

        this.play(event.guild!!, musicManager, firstTrack)
    }

    override fun noMatches() {
        event.hook.sendMessage("Nothing found by $trackUrl").queue()
    }

    override fun loadFailed(exception: FriendlyException) {
        event.hook.sendMessage("Could not play: " + exception.message).queue()
    }

    private fun play(guild: Guild, musicManager: GuildMusicManager, track: AudioTrack) {
        val userData = MyUserData(event.user.idLong);
        track.userData = userData;

        this.connectToFirstVoiceChannel(guild.audioManager)

        musicManager.scheduler.queue(track)
    }

    private fun connectToFirstVoiceChannel(audioManager: AudioManager) {
        if (!audioManager.isConnected) {
            for (voiceChannel in audioManager.guild.voiceChannels) {
                audioManager.openAudioConnection(voiceChannel)
                break
            }
        }
    }

}