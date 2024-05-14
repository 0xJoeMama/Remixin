package io.github.joemama.spongemixins

import felis.ModLoader
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.launch.platform.container.ContainerHandleURI
import org.spongepowered.asm.launch.platform.container.IContainerHandle
import org.spongepowered.asm.logging.ILogger
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.transformer.IMixinTransformer
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory
import org.spongepowered.asm.service.*
import org.spongepowered.asm.util.ReEntranceLock
import java.io.InputStream
import java.net.URL

class FelisMixinService : IMixinService, IClassProvider, IClassBytecodeProvider, ITransformerProvider, IClassTracker {
    private val logger = MixinLogger()
    lateinit var transformer: IMixinTransformer
    private val lock = ReEntranceLock(1)

    // TODO: Change when we get a legit name
    override fun getName(): String = "Felis"

    // TODO: change once we get legit side handling
    override fun getSideName(): String = ModLoader.side.name
    override fun isValid(): Boolean = true
    override fun getClassProvider(): IClassProvider = this
    override fun getBytecodeProvider(): IClassBytecodeProvider = this
    override fun getTransformerProvider(): ITransformerProvider = this
    override fun getClassTracker(): IClassTracker = this
    override fun getAuditTrail(): IMixinAuditTrail? = null
    override fun getPlatformAgents(): Collection<String> =
        listOf("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault")

    override fun getPrimaryContainer(): IContainerHandle =
        ContainerHandleURI(ModLoader::class.java.protectionDomain.codeSource.location.toURI())
    override fun getResourceAsStream(name: String): InputStream? = ModLoader.classLoader.getResourceAsStream(name)
    override fun prepare() = Unit
    override fun getInitialPhase(): MixinEnvironment.Phase = MixinEnvironment.Phase.PREINIT
    override fun init() = Unit
    override fun beginPhase() = Unit
    override fun checkEnv(o: Any) = Unit
    override fun getReEntranceLock(): ReEntranceLock = this.lock
    override fun getMixinContainers(): Collection<IContainerHandle> = listOf()
    override fun getMinCompatibilityLevel(): MixinEnvironment.CompatibilityLevel =
        MixinEnvironment.CompatibilityLevel.JAVA_8

    override fun getMaxCompatibilityLevel(): MixinEnvironment.CompatibilityLevel =
        MixinEnvironment.CompatibilityLevel.JAVA_17

    override fun getLogger(name: String): ILogger = this.logger

    @Deprecated("Deprecated in Java", ReplaceWith("i have no idea, ask mixins"))
    override fun getClassPath(): Array<out URL> = arrayOf()
    override fun findClass(name: String): Class<*>? = ModLoader.classLoader.findClass(name)
    override fun findClass(name: String, resolve: Boolean): Class<*>? =
        Class.forName(name, resolve, ModLoader.classLoader)
    override fun findAgentClass(name: String, resolve: Boolean): Class<*> =
        Class.forName(name, resolve, ModLoader::class.java.classLoader)
    override fun getClassNode(name: String): ClassNode? = this.getClassNode(name, true)
    override fun getClassNode(name: String, runTransformers: Boolean): ClassNode? =
        ModLoader.classLoader.getClassData(name)?.node
    override fun getTransformers(): Collection<ITransformer> = listOf()
    override fun getDelegatedTransformers(): Collection<ITransformer> = listOf()
    override fun addTransformerExclusion(name: String) = Unit
    override fun registerInvalidClass(name: String) = Unit
    override fun isClassLoaded(name: String): Boolean = false
    override fun getClassRestrictions(name: String): String = ""

    override fun offer(internal: IMixinInternal) {
        if (internal is IMixinTransformerFactory) {
            transformer = internal.createTransformer()
        }
    }
}