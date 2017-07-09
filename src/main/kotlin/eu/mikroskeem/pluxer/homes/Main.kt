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