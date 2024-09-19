package com.kyubae.hermes.command.commands

import com.kyubae.hermes.HermesApplication.Companion.audioPlayerManager
import com.kyubae.hermes.HermesApplication.Companion.getGuildAudioPlayer
import com.kyubae.hermes.command.Command
import com.kyubae.hermes.command.SlashCommandInfo
import com.kyubae.hermes.util.DiscordAudioLoadResultHandler
import com.kyubae.hermes.util.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import kotlin.io.path.Path


@SlashCommandInfo(name = "play", description = "Play a song", options = [SlashCommandInfo.Option(type = OptionType.STRING, name = "identifier", description = "The identifier of the song you want to play", required = true)])
class CommandPlay : Command {

    companion object {
        const val DATA_DIRECTORY: String = "C:/GitHub/hermes/.data"
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(false).queue()
        val id = event.getOption("identifier")!!.asString
        val url = "https://www.youtube.com/watch?v=$id"
        val file = "$DATA_DIRECTORY/$id"
        downloadFileIfNeeded(file, url)
        val musicManager: GuildMusicManager = getGuildAudioPlayer(event.channel.asTextChannel().guild)
        audioPlayerManager.loadItemOrdered(musicManager, "$file.mp3", DiscordAudioLoadResultHandler(event, musicManager, "$file.mp3"))
    }

    private fun downloadFileIfNeeded(file: String, url: String) {
        if (!Files.exists(Path("$file.mp3"))) {
            val command = listOf("yt-dlp.exe", "-x", "--audio-format", "mp3", "--audio-quality", "0", "-o", "$file.webm", url)
            try {
                val processBuilder = ProcessBuilder(command)
                processBuilder.redirectErrorStream(true)
                val process = processBuilder.start()
                BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        println(line)
                    }
                }
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}