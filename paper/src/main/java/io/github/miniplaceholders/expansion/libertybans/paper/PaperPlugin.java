package io.github.miniplaceholders.expansion.libertybans.paper;

import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansExpansion;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public final class PaperPlugin extends JavaPlugin {

    @Override
    public void onEnable(){
        this.getSLF4JLogger().info("Starting LibertyBans Expansion for Paper");

        new LibertyBansExpansion() {
            @Override
            protected @Nullable UUID provideUUIDByName(String name) {
                return getServer().getPlayerUniqueId(name);
            }
        }.register();
    }
}
