package eu.mikroskeem.pluxer.homes.configuration.sections

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class PluxerHomesSection {
    @Setting(value = "groups", comment = "Home permission groups")
    var groups: Map<String, Int> = mapOf(
            Pair("default", 1),
            Pair("aadlik", 3),
            Pair("meister",  5),
            Pair("isand",  8),
            Pair("sponsor", 64)
    )

    @Setting(value = "default-home-name", comment = "Default home name")
    var defaultHomeName = "Peamine"

    @Setting(value = "worlds", comment = "Worlds configuration")
    var worlds = WorldsSection()

    @Setting(value = "messages", comment = "Plugin messages")
    var messages = MessagesSection()
}