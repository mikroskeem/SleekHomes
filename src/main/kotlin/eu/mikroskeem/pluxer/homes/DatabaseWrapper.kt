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

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.nio.file.Paths
import java.util.*

/**
 * @author Mark Vainomaa
 */
class DatabaseWrapper(private val main: Main) {
    private val hikari : HikariDataSource

    init {
        // Try to load h2 driver
        Class.forName("org.h2.Driver")

        val config = HikariConfig()
        val props = Properties()
        props.setProperty("date_string_format", "dd-MM-yyyy HH:mm:ss")
        config.jdbcUrl = "jdbc:h2:${Paths.get(main.dataFolder.absolutePath, "database")}"
        config.dataSourceProperties = props
        hikari = HikariDataSource(config)

        /* Initialize database */
        hikari.connection.use { it ->
            it.prepareStatement(main.getResource("h2database.sql").reader().use { it.readText() }).use {
                it.executeUpdate()
            }
        }
    }

    /** Get player's home */
    fun getHome(player: Player, world: World, homeName: String): Location? {
        val groupedName = main.getGroupOrDefault(world.name)
        hikari.connection.use {
            it.prepareStatement(SQLStatements.SELECT_BY_HOME_WORLDGROUP_AND_HOME_NAME.statement).use {
                it.setString(1, player.uniqueId.toString())
                it.setString(2, groupedName)
                it.setString(3, homeName)
                val result = it.executeQuery()
                while(result.next()) {
                    /* Construct location object */
                    val worldName = result.getString(1)

                    /* Bail out if can't find given world */
                    val theWorld = main.server.worlds.find { it.name == worldName } ?: return null

                    val x = result.getDouble(2)
                    val y = result.getDouble(3)
                    val z = result.getDouble(4)
                    val yaw = result.getFloat(5)
                    val pitch = result.getFloat(6)
                    return Location(theWorld, x, y, z, yaw, pitch)
                }
            }
        }
        return null
    }

    /** Set player's home */
    fun setHome(player: Player, location: Location, homeName: String) {
        @Suppress("NAME_SHADOWING") val location = location.clone()
        hikari.connection.use { conn ->
            if(getHome(player, location.world, homeName) != null) {
                conn.prepareStatement(SQLStatements.UPDATE.statement).use {
                    it.setString(1, location.world.name)
                    it.setDouble(2, location.x)
                    it.setDouble(3, location.y)
                    it.setDouble(4, location.z)
                    it.setFloat(5, location.yaw)
                    it.setFloat(6, location.pitch)
                    it.setString(7, player.uniqueId.toString())
                    it.setString(8, main.getGroupOrDefault(location.world.name))
                    it.setString(9, homeName)
                    it.execute()
                }
            } else {
                conn.prepareStatement(SQLStatements.INSERT.statement).use {
                    it.setString(1, player.uniqueId.toString())
                    it.setString(2, main.getGroupOrDefault(location.world.name))
                    it.setString(3, location.world.name)
                    it.setString(4, homeName)
                    it.setDouble(5, location.x)
                    it.setDouble(6, location.y)
                    it.setDouble(7, location.z)
                    it.setFloat(8, location.yaw)
                    it.setFloat(9, location.pitch)
                    it.execute()
                }
            }
        }
    }

    /** Delete home */
    fun deleteHome(player: Player, world: World, homeName: String) {
        hikari.connection.use {
            it.prepareStatement(SQLStatements.DELETE.statement).use {
                it.setString(1, player.uniqueId.toString())
                it.setString(2, main.getGroupOrDefault(world.name))
                it.setString(3, homeName)
                it.execute()
            }
        }
    }

    /** List all player homes */
    fun listHomes(player: Player, world: World) : List<Pair<String, Location>> {
        val groupedName = main.getGroupOrDefault(world.name)
        val homes = arrayListOf<Pair<String, Location>>()
        hikari.connection.use {
            it.prepareStatement(SQLStatements.SELECT_BY_UUID_AND_WORLDGROUP.statement).use {
                it.setString(1, player.uniqueId.toString())
                it.setString(2, groupedName)
                val result = it.executeQuery()
                while(result.next()) {
                    val homeName = result.getString(1)
                    val worldName = result.getString(2)

                    /* Bail out if can't find given world */
                    val theWorld = main.server.worlds.find { it.name == worldName } ?: continue

                    /* Construct location object */
                    val x = result.getDouble(3)
                    val y = result.getDouble(4)
                    val z = result.getDouble(5)
                    val yaw = result.getFloat(6)
                    val pitch = result.getFloat(7)
                    homes.add(Pair(homeName, Location(theWorld, x, y, z, yaw, pitch)))
                }
            }
        }
        return homes
    }

    /** Shuts down database pool */
    fun shutdown() {
        hikari.close()
    }

    /** Predefined SQL statements */
    // language=SQL
    enum class SQLStatements(val statement: String) {
        SELECT_BY_UUID_AND_WORLDGROUP("SELECT HOMENAME, WORLDNAME, X, Y, Z, YAW, PITCH FROM HOMES WHERE UUID=? AND WORLDGROUP=?"),
        SELECT_BY_HOME_WORLDGROUP_AND_HOME_NAME("SELECT WORLDNAME, X, Y, Z, YAW, PITCH FROM HOMES WHERE UUID=? AND WORLDGROUP=? AND HOMENAME=? LIMIT 1"),
        UPDATE("UPDATE HOMES SET WORLDNAME=?, X=?, Y=?, Z=?, YAW=?, PITCH=? WHERE UUID=? AND WORLDGROUP=? AND HOMENAME=?"),
        INSERT("INSERT INTO HOMES (UUID, WORLDGROUP, WORLDNAME, HOMENAME, X, Y, Z, YAW, PITCH) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"),
        DELETE("DELETE FROM HOMES WHERE UUID=? AND WORLDGROUP=? AND HOMENAME=?");
    }
}