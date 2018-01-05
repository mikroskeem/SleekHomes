/*
 * This file is part of project SleekHomes, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.sleekhomes

import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.PaperCommandManager
import eu.mikroskeem.sleekhomes.configuration.ConfigurationLoader
import eu.mikroskeem.sleekhomes.configuration.sections.SleekHomesSection
import org.bstats.bukkit.Metrics
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Collections

/**
 * @author Mark Vainomaa
 */
class Main: JavaPlugin() {
    lateinit var database : DatabaseWrapper
    lateinit var configurationLoader: ConfigurationLoader
    lateinit var configuration: SleekHomesSection
    private lateinit var commandManager: BukkitCommandManager

    override fun onEnable() {
        // Load configuration
        val configPath = Paths.get(dataFolder.absolutePath, "config.cfg")
        Files.createDirectories(configPath.parent)
        configurationLoader = ConfigurationLoader(configPath)
        reloadConfig()
        configuration = configurationLoader.configuration

        // Load commands system and database
        commandManager = PaperCommandManager(this)
        commandManager.registerCommand(HomesCommand(this))
        database = DatabaseWrapper(this)
        commandManager.commandCompletions.registerCompletion("homes") { ctx ->
            if(ctx.player is Player) {
                return@registerCompletion database.listHomes(ctx.player, ctx.player.world).map { it.first }
            }
            Collections.emptyList()
        }

        server.scheduler.runTaskAsynchronously(this) { Metrics(this) }
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