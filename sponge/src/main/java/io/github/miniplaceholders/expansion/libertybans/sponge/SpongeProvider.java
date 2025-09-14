package io.github.miniplaceholders.expansion.libertybans.sponge;

import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.profile.GameProfile;

import java.util.UUID;

public final class SpongeProvider extends LibertyBansPlaceholders {
    private final Server server;

    public SpongeProvider(final Object o) {
        this.server = (Server) o;
    }

    @Override
    protected UUID provideUUIDByName(String name) {
        return server.player(name)
                .map(ServerPlayer::user)
                .map(User::profile)
                .map(GameProfile::uuid)
                .orElse(null);
    }
}
