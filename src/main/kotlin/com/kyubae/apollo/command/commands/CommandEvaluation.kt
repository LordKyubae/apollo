package com.kyubae.apollo.command.commands

import com.kyubae.apollo.command.Command
import com.kyubae.apollo.command.SlashCommandInfo
import com.kyubae.apollo.util.EvaluationEngine.Companion.evaluationEngine
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

@SlashCommandInfo(name = "eval", description = "???", options = [SlashCommandInfo.Option(type = OptionType.STRING, name = "script", description = "Script to run")])
class CommandEvaluation : Command {

    override fun execute(event: SlashCommandInteractionEvent) {
        if (event.member!!.idLong != 106337005884936192L) {
            event.reply("Sorry Hal, you do not have permission to do that.")
            return
        }

        event.deferReply(false).queue()

        event.hook.sendMessage(evaluationEngine.eval(event, event.getOption("script")!!.asString).toString()).queue()
    }

}