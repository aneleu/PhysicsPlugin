package me.aneleu.physicsplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PhysicsPlugin extends JavaPlugin {

    public static final int calculationPerTick = 10;
    public static final double tickPerCalculation = 1.0 / calculationPerTick;

    public static PhysicsPlugin plugin;

    public static List<DisplayObject> objects = new ArrayList<>();

    @Override
    public void onEnable() {

        plugin = this;

        getLogger().info("PhysicsPlugin enabled!");
        getCommand("object").setExecutor(new PhysicsCommand());

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (DisplayObject object : objects) {
                for(int i = 0; i < calculationPerTick; i++) {
                    object.move();
                }
            }
        }, 0, 1);

    }
}
