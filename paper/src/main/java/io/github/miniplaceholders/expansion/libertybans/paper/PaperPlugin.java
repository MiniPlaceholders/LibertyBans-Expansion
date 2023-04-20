package io.github.miniplaceholders.expansion.libertybans.paper;

import io.github.miniplaceholders.expansion.libertybans.common.CommonExpansion;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperPlugin extends JavaPlugin {

    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting LibertyBans Expansion for Paper");

        new CommonExpansion((string) -> getServer().getPlayerUniqueId(string)).register();
    }
}
