package com.kyubae.hermes.util

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.managers.AudioManager
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.nio.file.Files
import kotlin.io.path.Path

class DiscordAudioLoadResultHandler(private val channel: TextChannel, private val musicManager: GuildMusicManager, private val trackUrl: String) : AudioLoadResultHandler {

    override fun trackLoaded(track: AudioTrack) {
        track.userData = JSONObject(Files.readString(Path("$trackUrl.info.json")))

        channel.sendMessage("Adding to queue " + (track.userData as JSONObject).getString("title")).queue()
        this.play(channel.guild, musicManager, track)
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
        var firstTrack = playlist.selectedTrack
        if (firstTrack == null) {
            firstTrack = playlist.tracks[0]
        }
        firstTrack.userData = JSONObject(Files.readString(Path("$trackUrl.info.json")))
        channel.sendMessage("Adding to queue " + (firstTrack!!.userData as JSONObject).getString("title") + " (first track of playlist " + playlist.name + ")").queue()
        this.play(channel.guild, musicManager, firstTrack)
    }

    override fun noMatches() {
        channel.sendMessage("Nothing found by $trackUrl").queue()
    }

    override fun loadFailed(exception: FriendlyException) {
        channel.sendMessage("Could not play: " + exception.message).queue()
    }

    private fun play(guild: Guild, musicManager: GuildMusicManager, track: AudioTrack) {
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