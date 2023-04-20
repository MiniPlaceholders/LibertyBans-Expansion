package io.github.miniplaceholders.expansion.libertybans.common;

import io.github.miniplaceholders.api.Expansion;
import io.github.miniplaceholders.api.utils.Components;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.Nullable;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.PunishmentType;
import space.arim.omnibus.Omnibus;
import space.arim.omnibus.OmnibusProvider;

import java.util.UUID;
import java.util.function.Function;

public final class CommonExpansion {
    private final Function<String, @Nullable UUID> uuidProvider;
    private final LibertyBans libertyBans;

    public CommonExpansion(final Function<String, @Nullable UUID> uuidProvider) {
        this.uuidProvider = uuidProvider;
        final Omnibus omnibus = OmnibusProvider.getOmnibus();
        this.libertyBans = omnibus.getRegistry().getProvider(LibertyBans.class).orElseThrow();
    }
    public void register() {
        Expansion.builder("libertybans")
                .audiencePlaceholder("is_muted", (audience, queue, ctx) -> {
                    UUID uuid = audience.get(Identity.UUID).orElse(null);
                    if (uuid == null) {
                        return null;
                    }
                    var punishments = libertyBans.getSelector()
                            .selectionBuilder()
                            .selectActiveOnly()
                            .victim(PlayerVictim.of(uuid))
                            .type(PunishmentType.MUTE)
                            .build()
                            .countNumberOfPunishments()
                            .toCompletableFuture()
                            .join();
                    if (punishments == 0) {
                        return Tag.selfClosingInserting(Components.TRUE_COMPONENT);
                    } else {
                        return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
                    }
                })
                .audiencePlaceholder("is_warned", (audience, queue, ctx) -> {
                    UUID uuid = audience.get(Identity.UUID).orElse(null);
                    if (uuid == null) {
                        return null;
                    }
                    var punishments = libertyBans.getSelector()
                            .selectionBuilder()
                            .selectActiveOnly()
                            .victim(PlayerVictim.of(uuid))
                            .type(PunishmentType.WARN)
                            .build()
                            .countNumberOfPunishments()
                            .toCompletableFuture()
                            .join();
                    if (punishments == 0) {
                        return Tag.selfClosingInserting(Components.TRUE_COMPONENT);
                    } else {
                        return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
                    }
                })
                .globalPlaceholder("is_player_muted", (queue, ctx) -> activePunishmentOf(queue, ctx, PunishmentType.MUTE))
                .globalPlaceholder("is_player_banned", (queue, ctx) -> activePunishmentOf(queue, ctx, PunishmentType.BAN))
                .globalPlaceholder("has_been_kicked", (queue, ctx) -> punishmentAppliedBefore(queue, ctx, PunishmentType.KICK))
                .globalPlaceholder("has_been_muted", (queue, ctx) -> punishmentAppliedBefore(queue, ctx, PunishmentType.MUTE))
                .globalPlaceholder("has_been_banned", (queue, ctx) -> punishmentAppliedBefore(queue, ctx, PunishmentType.BAN))
                .globalPlaceholder("has_been_warned", (queue, ctx) -> punishmentAppliedBefore(queue, ctx, PunishmentType.WARN))
                .build()
                .register();
    }

    private Tag activePunishmentOf(ArgumentQueue queue, Context ctx, PunishmentType punishmentType) {
        final UUID uuid = parseArgumentUUID(queue, ctx);

        var punishments = libertyBans.getSelector()
                .selectionBuilder()
                .victim(PlayerVictim.of(uuid))
                .selectActiveOnly()
                .type(punishmentType)
                .build()
                .countNumberOfPunishments()
                .toCompletableFuture()
                .join();

        if (punishments == 0) {
            return Tag.selfClosingInserting(Components.TRUE_COMPONENT);
        } else {
            return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
        }
    }

    private Tag punishmentAppliedBefore(ArgumentQueue queue, Context ctx, PunishmentType punishmentType) {
        final UUID uuid = parseArgumentUUID(queue, ctx);

        var punishments = libertyBans.getSelector()
                .selectionBuilder()
                .victim(PlayerVictim.of(uuid))
                .type(punishmentType)
                .build()
                .countNumberOfPunishments()
                .toCompletableFuture()
                .join();

        if (punishments == 0) {
            return Tag.selfClosingInserting(Components.TRUE_COMPONENT);
        } else {
            return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
        }
    }

    private UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private UUID parseArgumentUUID(ArgumentQueue queue, Context ctx) {
        final String argumentProvided = queue.popOr("").value();
        UUID uuid = parseUUID(argumentProvided);
        if (uuid == null) {
            uuid = uuidProvider.apply(argumentProvided);
            if (uuid == null) {
                throw ctx.newException("");
            }
        }
        return uuid;
    }
}
