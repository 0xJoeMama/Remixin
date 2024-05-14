package io.github.joemama.spongemixins

import felis.LoaderPluginEntrypoint
import felis.ModLoader
import felis.meta.ModMetadataExtended
import net.peanuuutz.tomlkt.asTomlLiteral
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.MixinEnvironment.Phase
import org.spongepowered.asm.mixin.Mixins
import org.spongepowered.asm.service.MixinService

object MixinLoaderPlugin : LoaderPluginEntrypoint {
    private val ModMetadataExtended.mixins: String?
        get() = this["mixin"]?.asTomlLiteral()?.toString()

    override fun onLoaderInit() {
        // initialize mixins
        MixinBootstrap.init()

        // pass in configs
        for (cfg in ModLoader.discoverer.mods.mapNotNull { it.metadata.mixins }) {
            Mixins.addConfiguration(cfg)
        }

        // move to the default phase
        val environment = MixinEnvironment.getEnvironment(Phase.DEFAULT)
        // grab our transformer
        val transformer = (MixinService.getService() as FelisMixinService).transformer
        // don't transform the mixin package
        ModLoader.transformer.ignored.ignorePackage("org.spongepowered")
        // register our transformations
        ModLoader.transformer.registerTransformation(MixinTransformation(environment, transformer))
    }
}

