package eu.mikroskeem.pluxer.homes.configuration.sections

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class ListFormatSection {
    @Setting(value = "prefix", comment = "List prefix")
    var prefix = "&7Kodud: "

    @Setting(value = "delimiter", comment = "List delimiter")
    var delimiter = "&7, "

    @Setting(value = "home-name-format", comment = "Home name format")
    var homeNameFormat = "&6%homename"
}