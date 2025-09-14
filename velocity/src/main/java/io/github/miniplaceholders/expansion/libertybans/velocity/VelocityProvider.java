package io.github.miniplaceholders.expansion.libertybans.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class VelocityProvider extends LibertyBansPlaceholders {
    private final ProxyServer proxyServer;

    public VelocityProvider(final Object proxy) {
        this.proxyServer = (ProxyServer) proxy;
    }

    @Override
    protected @Nullable UUID provideUUIDByName(String name) {
        return proxyServer.getPlayer(name)
                .map(Player::getUniqueId)
                .orElse(null);
    }
}
