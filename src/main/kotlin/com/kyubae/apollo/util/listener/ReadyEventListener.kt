package com.kyubae.apollo.util.listener

import com.kyubae.apollo.ApolloApplication.Companion.commands
import com.kyubae.apollo.command.Command
import com.kyubae.apollo.command.SlashCommandInfo
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.reflections.Reflections
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class ReadyEventListener : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        println("https://discord.com/oauth2/authorize?client_id=1258744672990269452")

        val reflections = Reflections("com.kyubae.apollo.command.commands")
        val commandClasses = reflections.getTypesAnnotatedWith(SlashCommandInfo::class.java)

        for (clazz in commandClasses) {
            val commandInstance = clazz.kotlin.primaryConstructor?.call() as Command
            val info = clazz.kotlin.findAnnotation<SlashCommandInfo>()
            commands[info!!.name] = commandInstance
        }

        val commandDataList = commands.values.mapNotNull { command ->
            val clazz = command::class
            val info = clazz.findAnnotation<SlashCommandInfo>() ?: return@mapNotNull null

            val commandData = Commands.slash(info.name, info.description)
            info.options.forEach { option ->
                commandData.addOption(option.type, option.name, option.description, option.required)
            }

            commandData
        }

        event.jda.updateCommands().addCommands(commandDataList).queue()
    }

}