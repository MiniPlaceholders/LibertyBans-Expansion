package io.github.miniplaceholders.expansion.libertybans.paper;

import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public final class PaperProvider extends LibertyBansPlaceholders {

    @Override
    protected @Nullable UUID provideUUIDByName(final String name) {
        return Bukkit.getServer().getPlayerUniqueId(name);
    }
}
