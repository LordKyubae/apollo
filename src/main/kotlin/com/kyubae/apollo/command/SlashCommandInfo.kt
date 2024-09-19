package com.kyubae.apollo.command

import net.dv8tion.jda.api.interactions.commands.OptionType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SlashCommandInfo(val name: String, val description: String = "", val options: Array<Option> = []) {

    annotation class Option(val type: OptionType, val name: String, val description: String, val required: Boolean = false)

}