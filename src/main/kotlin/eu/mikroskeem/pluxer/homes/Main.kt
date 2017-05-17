package eu.mikroskeem.pluxer.homes

import co.aikar.commands.ACF
import co.aikar.commands.CommandManager
import eu.mikroskeem.pluxer.homes.configuration.ConfigurationLoader
import eu.mikroskeem.pluxer.homes.configuration.sections.PluxerHomesSection
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author Mark Vainomaa
 */
class Main : JavaPlugin() {
    lateinit var database : DatabaseWrapper
    lateinit var configurationLoader: ConfigurationLoader
    lateinit var configuration: PluxerHomesSection
    private lateinit var commandManager : CommandManager

    override fun onEnable() {
        // Load configuration
        val configPath = Paths.get(dataFolder.absolutePath, "config.cfg")
        Files.createDirectories(configPath.parent)
        configurationLoader = ConfigurationLoader(configPath)
        reloadConfig()
        configuration = configurationLoader.configuration

        // Load commands system and database
        commandManager = ACF.createManager(this)
        commandManager.registerCommand(HomesCommand(this))
        database = DatabaseWrapper(this)
        commandManager.commandCompletions.registerCompletion("homes") { sender, _, _, _ ->
            if(sender is Player) {
                return@registerCompletion database.listHomes(sender, sender.world).map { it.first }
            }
            Collections.emptyList()
        }
    }

    override fun onDisable() {
        database.shutdown()
    }

    override fun reloadConfig() {
        configurationLoader.reload()
    }

    // Gets world group name, if preset
    fun getGroupOrDefault(worldName: String): String {
        for((first, second) in configuration.worlds.groups.map { Pair(it.key, it.value) }) {
            if(second.contains(worldName)) {
                return first
            }
        }
        return "default"
    }
}