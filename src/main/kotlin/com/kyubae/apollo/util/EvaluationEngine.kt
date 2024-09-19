package com.kyubae.apollo.util

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import javax.script.SimpleBindings

class EvaluationEngine {

    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")

    private val evalThread: ExecutorService = Executors.newThreadPerTaskExecutor { it: Runnable? ->
        Thread.ofVirtual().name("eval-thread").unstarted(it)
    }

    init {
        this.initEngine()
    }

    companion object {

        val evaluationEngine: EvaluationEngine = EvaluationEngine()

    }

    fun eval(slashEvent: SlashCommandInteractionEvent, code: String?): Any {
        val bindings = SimpleBindings()

        bindings["event"] = slashEvent
        bindings["guild"] = slashEvent.guild
        bindings["jda"] = slashEvent.jda

        val future: Future<Any> = evalThread.submit<Any> {
            try {
                return@submit engine.eval(code, bindings)
            } catch (e: Exception) {
                return@submit e
            }
        }

        try {
            return future.get(5, TimeUnit.SECONDS)
        } catch (ex: Exception) {
            future.cancel(true)
            return ex
        }
    }

    private fun initEngine(): ScriptEngine {
        val packageImports = listOf(
            "java.io",
            "java.lang",
            "java.math",
            "java.time",
            "java.util",
            "java.util.concurrent",
            "java.util.stream",

            "net.dv8tion.jda.api",
            "net.dv8tion.jda.internal.entities",
            "net.dv8tion.jda.api.entities",
            "net.dv8tion.jda.api.entities.channel",
            "net.dv8tion.jda.api.entities.channel.attribute",
            "net.dv8tion.jda.api.entities.channel.middleman",
            "net.dv8tion.jda.api.entities.channel.concrete",
            "net.dv8tion.jda.api.managers",
            "net.dv8tion.jda.internal.managers",
            "net.dv8tion.jda.api.utils"
        )

        val importString = packageImports.stream().map { i: String -> "import $i.*" }.collect(Collectors.joining("\n"))

        try {
            engine.eval(importString)
            return engine
        } catch (e: ScriptException) {
            throw RuntimeException(e)
        }
    }

}