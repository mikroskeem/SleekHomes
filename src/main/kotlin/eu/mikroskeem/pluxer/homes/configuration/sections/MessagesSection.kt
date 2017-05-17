package eu.mikroskeem.pluxer.homes.configuration.sections

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class MessagesSection {
    @Setting(value = "no-permission", comment = "This message is sent when player has no permission for given command")
    var noPermission = "&cTeil pole piisavalt õiguseid selleks!"

    @Setting(value = "no-such-home", comment = "This message is sent when player has no such home")
    var noSuchHome = "&cEi leitud antud kodu nimega '&l%homename&r&c'"

    @Setting(value = "you-dont-have-any-homes", comment = "This message is sent when player has no homes set")
    var youDontHaveAnyHomes = "&cTeil pole ühtegi kodu sätitud"

    @Setting(value = "you-cant-set-more-homes", comment = "This message is sent when player has reached home set limit")
    var maxHomesReached = "&cTe ei saa rohkem kodusid sättida!"

    @Setting(value = "you-have-been-teleported-to-home",
            comment = "This message is sent when player teleports successfully to home")
    var youHaveBeenTeleportedToHome = "&7Teleportisite koju '&6%homename&7'"

    @Setting(value = "home-deleted", comment = "This message is sent when player's home gets deleted successfully")
    var homeDeleted = "&7Kodu '&6%homename&7' edukalt kustutatud!"

    @Setting(value = "home-set", comment = "This message is sent when player's home gets set successfully")
    var homeSet = "&7Kodu '&6%homename&7' edukalt sätitud!"

    @Setting(value = "list-format", comment = "Home list formatting")
    var listFormat = ListFormatSection()
}