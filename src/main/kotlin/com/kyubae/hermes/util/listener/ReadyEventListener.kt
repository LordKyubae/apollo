package com.kyubae.hermes.util.listener

import com.kyubae.hermes.HermesApplication.Companion.commands
import com.kyubae.hermes.command.Command
import com.kyubae.hermes.command.SlashCommandInfo
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.reflections.Reflections
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class ReadyEventListener : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        val reflections = Reflections("com.kyubae.hermes.command.commands")
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