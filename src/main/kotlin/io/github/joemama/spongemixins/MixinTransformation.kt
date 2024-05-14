package io.github.joemama.spongemixins

import felis.transformer.ClassContainer
import felis.transformer.Transformation
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.transformer.IMixinTransformer

class MixinTransformation(private val environment: MixinEnvironment, private val transformer: IMixinTransformer) :
    Transformation {
    override fun transform(container: ClassContainer) {
        val newBytes = this.transformer.transformClass(this.environment, container.name, container.bytes)
        container.newBytes(newBytes)
    }
}