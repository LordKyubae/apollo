package com.kyubae.apollo

import com.kyubae.apollo.command.Command
import com.kyubae.apollo.util.GuildMusicManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.entities.Guild
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApolloApplication {

    companion object {

        val commands = mutableMapOf<String, Command>()

        val audioPlayerManager = DefaultAudioPlayerManager()

        private val guildMusicManagers: HashMap<Long, GuildMusicManager> = HashMap()

        init {
            AudioSourceManagers.registerLocalSource(this.audioPlayerManager)
        }

        @Synchronized
        fun getGuildAudioPlayer(guild: Guild): GuildMusicManager {
            val guildId: Long = guild.id.toLong()
            var musicManager: GuildMusicManager? = this.guildMusicManagers[guildId]
            if (musicManager == null) {
                musicManager = GuildMusicManager(this.audioPlayerManager)
                this.guildMusicManagers[guildId] = musicManager
            }
            guild.audioManager.sendingHandler = musicManager.getSendHandler()
            return musicManager
        }

    }

}

fun main(args: Array<String>) {
    runApplication<ApolloApplication>(*args)
}