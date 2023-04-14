package io.github.miniplaceholders.expansion.example.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.proxy.Player;

import org.slf4j.Logger;

import io.github.miniplaceholders.api.Expansion;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

@Plugin(
    name = "Example-Expansion",
    id = "example-expansion",
    version = Constants.VERSION,
    authors = {"4drian3d"},
    dependencies = {
        @Dependency(id = "miniplaceholders")}
)
public final class VelocityPlugin {
    private final Logger logger;

    @Inject
    public VelocityPlugin(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        logger.info("Starting Example Expansion for Velocity");

        Expansion.builder("example")
            .filter(Player.class)
            .audiencePlaceholder("name", (aud, queue, ctx) -> Tag.selfClosingInserting(Component.text(((Player)aud).getUsername())))
            .build()
            .register();
    }
}
