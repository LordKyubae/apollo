package com.kyubae.hermes.util

import com.kyubae.hermes.configuration.ApplicationProperties
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordEventListener(private val applicationProperties: ApplicationProperties) : ListenerAdapter() {

    private val guildMusicManagers: HashMap<Long, GuildMusicManager> = HashMap()

    private val audioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    @Synchronized
    private fun getGuildAudioPlayer(guild: Guild): GuildMusicManager {
        val guildId: Long = guild.id.toLong()
        var musicManager: GuildMusicManager? = this.guildMusicManagers[guildId]
        if (musicManager == null) {
            musicManager = GuildMusicManager(this.audioPlayerManager)
            this.guildMusicManagers[guildId] = musicManager
        }
        guild.audioManager.sendingHandler = musicManager.getSendHandler()
        return musicManager
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val command: Array<String> = event.message.contentRaw.split(" ", limit = 2).toTypedArray()

        if ("~play" == command[0] && command.size == 2) {
            this.loadAndPlay(event.channel.asTextChannel(), this.applicationProperties.dataDirectory + "\\" + command[1] + ".mp3")
        } else if ("~skip" == command[0]) {
            this.skipTrack(event.channel.asTextChannel())
        }

        super.onMessageReceived(event)
    }

    private fun loadAndPlay(channel: TextChannel, trackUrl: String) {
        val musicManager: GuildMusicManager = this.getGuildAudioPlayer(channel.guild)
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, DiscordAudioLoadResultHandler(channel, musicManager, trackUrl))
    }

    private fun skipTrack(channel: TextChannel) {
        val musicManager: GuildMusicManager = getGuildAudioPlayer(channel.guild)
        musicManager.scheduler.nextTrack()
        channel.sendMessage("Skipped to next track.").queue()
    }

}