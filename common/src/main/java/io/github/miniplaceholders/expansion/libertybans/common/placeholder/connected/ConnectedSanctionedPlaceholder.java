package io.github.miniplaceholders.expansion.libertybans.common.placeholder.connected;

import io.github.miniplaceholders.api.placeholder.AudiencePlaceholder;
import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansExpansion;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.PunishmentType;

import java.util.UUID;

public final class ConnectedSanctionedPlaceholder implements AudiencePlaceholder {
    private final LibertyBansExpansion expansion;
    private final PunishmentType punishmentType;

    public ConnectedSanctionedPlaceholder(LibertyBansExpansion expansion, PunishmentType punishmentType) {
        this.expansion = expansion;
        this.punishmentType = punishmentType;
    }
    @Override
    public @Nullable Tag tag(@NotNull Audience audience, @NotNull ArgumentQueue queue, @NotNull Context ctx) {
        UUID uuid = audience.get(Identity.UUID).orElse(null);
        if (uuid == null) {
            return null;
        }
        var punishments = expansion.getLibertyBans()
                .getSelector()
                .selectionBuilder()
                .selectActiveOnly()
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
}
