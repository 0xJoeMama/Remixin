package io.github.joemama.spongemixins

import org.spongepowered.asm.service.IMixinServiceBootstrap

class FelisMixinServiceBootstrap : IMixinServiceBootstrap {
    override fun getName(): String = "Felis Bootstrap"
    override fun getServiceClassName(): String = FelisMixinService::class.java.name
    override fun bootstrap() = Unit
}