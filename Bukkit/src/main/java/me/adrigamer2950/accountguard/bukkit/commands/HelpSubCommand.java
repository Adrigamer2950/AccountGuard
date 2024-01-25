package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.adriapi.api.colors.Colors;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpSubCommand extends SubCommand {

    public HelpSubCommand(Command parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        List<String> strs = List.of(
                "&a&m----------------------------------------------",
                "&7> &e/"+ s +" help &aShows this help message",
                "&7> &e/"+ s +" add <player> <ip> &aAdds an ip to a player's whitelist",
                "&7> &e/"+ s +" remove <player> <ip> &aRemoves an ip from a player's whitelist",
                "&7> &e/"+ s +" list <player> &aShows current IPs listed for a player",
                "&7> &e/"+ s +" reload &aReloads configurations and messages",
                "&a&m----------------------------------------------"
        );

        for(String str : strs)
            commandSender.sendMessage(Colors.translateColors(str));

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
