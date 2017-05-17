package eu.mikroskeem.pluxer.homes

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

/**
 * @author Mark Vainomaa
 */
class HomesCommand(private val main: Main) : BaseCommand("pluxerhomes") {
    @CommandAlias("home")
    @CommandCompletion("@homes")
    @CommandPermission("pluxerhomes.home")
    fun home(player: Player, @Optional theHomeName: String?) {
        if(!player.hasWorldPermission()) {
            player.sendColoredMessage(main.configuration.messages.noPermission)
            return
        }

        val homeName = theHomeName ?: main.configuration.defaultHomeName
        val homes = main.database.listHomes(player, player.location.world)
        val selectedHome = homes.find { it.first == homeName }
        if(selectedHome == null) {
            if(homeName == main.configuration.defaultHomeName) {
                if(homes.isEmpty()) {
                    player.sendColoredMessage(main.configuration.messages.youDontHaveAnyHomes)
                } else {
                    listHomes(player)
                }
            } else {
                player.sendColoredMessage(main.configuration.messages.noSuchHome.replace("%homename", homeName))
            }
        } else {
            player.sendColoredMessage(
                    main.configuration.messages.youHaveBeenTeleportedToHome.replace("%homename", selectedHome.first)
            )
            player.teleport(selectedHome.second, PlayerTeleportEvent.TeleportCause.PLUGIN)
        }
    }

    @CommandAlias("sethome")
    @CommandCompletion("@homes")
    @CommandPermission("pluxerhomes.sethome")
    fun setHome(player: Player, @Optional theHomeName: String?) {
        if(!player.hasWorldPermission()) {
            player.sendColoredMessage(main.configuration.messages.noPermission)
            return
        }

        val homeName = theHomeName ?: main.configuration.defaultHomeName
        val homes = main.database.listHomes(player, player.location.world)
        if(homes.size >= player.checkGroups()) {
            player.sendColoredMessage(main.configuration.messages.maxHomesReached)
        } else {
            main.database.setHome(player, player.location, homeName)
            player.sendColoredMessage(main.configuration.messages.homeSet.replace("%homename", homeName))
        }
    }

    @CommandAlias("delhome")
    @CommandCompletion("@homes")
    @CommandPermission("pluxerhomes.delhome")
    fun deleteHome(player: Player, @Optional theHomeName: String?) {
        if(!player.hasWorldPermission()) {
            player.sendColoredMessage(main.configuration.messages.noPermission)
            return
        }

        val homeName = theHomeName ?: main.configuration.defaultHomeName
        if(main.database.listHomes(player, player.location.world).find { it.first == homeName } != null) {
            main.database.deleteHome(player, player.location.world, homeName)
            player.sendColoredMessage(main.configuration.messages.homeDeleted.replace("%homename", homeName))
        } else {
            player.sendColoredMessage(main.configuration.messages.noSuchHome.replace("%homename", homeName))
        }
    }

    @CommandAlias("listhomes|homes")
    @CommandPermission("pluxerhomes.listhomes")
    fun listHomes(player: Player) {
        if(!player.hasWorldPermission()) {
            player.sendColoredMessage(main.configuration.messages.noPermission)
            return
        }

        val homes = main.database.listHomes(player, player.location.world)
        if(homes.isNotEmpty()) {
            val message = homes
                    .map { main.configuration.messages.listFormat.homeNameFormat.replace("%homename", it.first) }
                    .joinToString(
                            prefix = main.configuration.messages.listFormat.prefix,
                            separator = main.configuration.messages.listFormat.delimiter
                    )
            player.sendColoredMessage(message)
        } else {
            player.sendColoredMessage(main.configuration.messages.youDontHaveAnyHomes)
        }
    }

    // Helper extensions
    private fun Player.checkGroups() : Int {
        if(player.hasPermission("pluxerhomes.unlimited")) return 128 // Not really unlimited, but sane-ish one eh?
        var largest = 0
        main.configuration.groups.forEach { groupName, maxCount ->
            if(player.hasPermission("pluxerhomes.group.$groupName")) {
                if(largest < maxCount)
                    largest = maxCount
            }
        }
        return largest
    }

    private fun Player.hasWorldPermission(): Boolean = player
            .hasPermission("pluxerhomes.worldgroup.${main.getGroupOrDefault(player.location.world.name)}")

    private fun Player.sendColoredMessage(text: String) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""+text))
    }
}