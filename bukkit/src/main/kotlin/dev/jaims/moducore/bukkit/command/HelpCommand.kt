/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.bukkit.command

import dev.jaims.mcutils.bukkit.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.allCommands
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HelpCommand(override val plugin: ModuCore) : BaseCommand
{

    override val usage: String = "/help [command]"
    override val description: String = "Show help menus for all commands or a specific one."
    override val commandName: String = "help"

    private val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            1 ->
            {
                val matches = allCommands.filter { it.commandName.contains(args[0].toLowerCase()) }
                sender.send(fileManager.getString(Lang.HELP_HEADER, sender as? Player).replace("{filter}", args[0]))
                when (matches.size)
                {
                    0 ->
                    {
                        sender.send(
                            fileManager.getString(Lang.HELP_NOT_FOUND, sender as? Player).replace("{name}", args[0])
                        )
                    }
                    else -> matches.forEach {
                        sender.send(
                            listOf(
                                fileManager.getString(Lang.HELP_COMMAND_USAGE, sender as? Player)
                                    .replace("{usage}", it.usage),
                                fileManager.getString(Lang.HELP_COMMAND_DESCRIPTION, sender as? Player)
                                    .replace("{description}", it.description)
                            )
                        )
                    }
                }
            }
            else ->
            {
                sender.send(fileManager.getString(Lang.HELP_HEADER, sender as? Player).replace("{filter}", "None"))
                allCommands.forEach {
                    sender.send(
                        listOf(
                            fileManager.getString(Lang.HELP_COMMAND_USAGE, sender as? Player)
                                .replace("{usage}", it.usage),
                            fileManager.getString(Lang.HELP_COMMAND_DESCRIPTION, sender as? Player)
                                .replace("{description}", it.description)
                        )
                    )
                }
            }
        }
    }

}