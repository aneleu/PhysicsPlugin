package me.aneleu.physicsplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PhysicsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        DisplayObject object = new DisplayObject(p.getLocation(), "minecraft:stone");
        PhysicsPlugin.objects.add(object);
        return true;
    }
}
