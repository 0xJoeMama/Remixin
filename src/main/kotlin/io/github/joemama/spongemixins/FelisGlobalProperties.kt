package io.github.joemama.spongemixins

import org.spongepowered.asm.service.IGlobalPropertyService
import org.spongepowered.asm.service.IPropertyKey

class FelisGlobalProperties : IGlobalPropertyService {
    data class PropertyKey(val key: String) : IPropertyKey
    private val props: MutableMap<String, Any?> = mutableMapOf()
    override fun resolveKey(key: String): IPropertyKey = PropertyKey(key)
    // safe since we trust mixins to keep types properly stored
    @Suppress("unchecked_cast")
    override fun <T> getProperty(key: IPropertyKey): T? = this.props[(key as PropertyKey).key] as T
    override fun <T> getProperty(key: IPropertyKey, default: T?): T? = this.getProperty(key) ?: default
    override fun getPropertyString(key: IPropertyKey, default: String): String =
        this.getProperty(key, default).toString()

    override fun setProperty(key: IPropertyKey, value: Any?) {
        this.props[(key as PropertyKey).key] = value
    }
}