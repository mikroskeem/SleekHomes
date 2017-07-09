/*
 * This file is part of project PluxerHomes, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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