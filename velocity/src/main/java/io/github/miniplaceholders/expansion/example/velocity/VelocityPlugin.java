package io.github.miniplaceholders.expansion.example.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.proxy.Player;

import com.velocitypowered.api.proxy.ProxyServer;
import io.github.miniplaceholders.expansion.libertybans.common.CommonExpansion;
import org.slf4j.Logger;

@Plugin(
    name = "Example-Expansion",
    id = "example-expansion",
    version = Constants.VERSION,
    authors = {"4drian3d"},
    dependencies = {
        @Dependency(id = "miniplaceholders"),
        @Dependency(id = "libertybans")
    }
)
public final class VelocityPlugin {
    private final Logger logger;
    private final ProxyServer proxyServer;

    @Inject
    public VelocityPlugin(final Logger logger, ProxyServer proxyServer) {
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting LibertyBans Expansion for Velocity");

        new CommonExpansion(
                (string) -> proxyServer.getPlayer(string)
                        .map(Player::getUniqueId)
                        .orElse(null)
        ).register();
    }
}
