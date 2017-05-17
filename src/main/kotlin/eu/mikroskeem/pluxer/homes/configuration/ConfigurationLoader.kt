package eu.mikroskeem.pluxer.homes.configuration

import eu.mikroskeem.pluxer.homes.configuration.sections.PluxerHomesSection
import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.loader.HeaderMode
import ninja.leaping.configurate.objectmapping.ObjectMapper
import java.nio.file.Path

/**
 * @author Mark Vainomaa
 */
private val HEADER = """
 Pluxer Homes configuration file
"""

class ConfigurationLoader(configurationPath: Path) {
    private val loader : ConfigurationLoader<CommentedConfigurationNode>
    private val mapper : ObjectMapper<PluxerHomesSection>.BoundInstance
    private lateinit var baseNode : CommentedConfigurationNode

    var configuration : PluxerHomesSection

    init {
        // Build loader
        val builder = HoconConfigurationLoader.builder()
        builder.defaultOptions = getDefaultOptions()
        builder.headerMode = HeaderMode.PRESERVE
        builder.setPath(configurationPath)
        loader = builder.build()

        // Set up mapper
        mapper = ObjectMapper.forClass(PluxerHomesSection::class.java).bindToNew()
        configuration = mapper.instance
    }

    fun load() {
        baseNode = loader.load()
        configuration = mapper.populate(baseNode.getNode("pluxerhomes"))
    }

    fun save() {
        mapper.serialize(baseNode.getNode("pluxerhomes"))
        loader.save(baseNode)
    }

    fun reload() {
        load()
        save()
    }

    private fun getDefaultOptions(): ConfigurationOptions {
        return ConfigurationOptions.defaults()
                .setHeader(HEADER)
                .setShouldCopyDefaults(true)
    }
}
