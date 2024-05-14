package io.github.joemama.spongemixins

import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.spongepowered.asm.logging.LoggerAdapterAbstract
import org.spongepowered.asm.mixin.Mixins

class MixinLogger : LoggerAdapterAbstract("mixin") {
    private val logger = LoggerFactory.getLogger(Mixins::class.java)

    private fun matchLevel(level: org.spongepowered.asm.logging.Level): Level = when (level) {
        org.spongepowered.asm.logging.Level.DEBUG -> Level.DEBUG
        org.spongepowered.asm.logging.Level.WARN -> Level.WARN
        org.spongepowered.asm.logging.Level.INFO -> Level.INFO
        org.spongepowered.asm.logging.Level.TRACE -> Level.TRACE
        org.spongepowered.asm.logging.Level.ERROR -> Level.ERROR
        org.spongepowered.asm.logging.Level.FATAL -> Level.ERROR
        else -> throw IllegalArgumentException("Invalid logging level")
    }

    override fun getType(): String = "slf4j Logger"

    override fun catching(p0: org.spongepowered.asm.logging.Level, p1: Throwable) {
        this.throwing(p1)
    }

    override fun log(p0: org.spongepowered.asm.logging.Level, p1: String, vararg p2: Any) {
        this.logger.atLevel(this.matchLevel(p0)).log(p1, *p2)
    }

    override fun log(p0: org.spongepowered.asm.logging.Level, p1: String, p2: Throwable) {
        this.logger.atLevel(this.matchLevel(p0)).log(p1)
        this.logger.atLevel(this.matchLevel(p0)).log(p2.toString())
    }

    override fun <T : Throwable> throwing(t: T): T {
        this.warn("Throwing {}: {}", t::class.java.getName(), t.message, t)
        return t
    }
}