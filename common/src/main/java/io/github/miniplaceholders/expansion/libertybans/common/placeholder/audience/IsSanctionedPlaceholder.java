package io.github.miniplaceholders.expansion.libertybans.common.placeholder.audience;

import io.github.miniplaceholders.api.resolver.AudienceTagResolver;
import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.arim.libertybans.api.PlayerVictim;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.punish.Punishment;

import java.util.List;
import java.util.UUID;

public record IsSanctionedPlaceholder(
        LibertyBansPlaceholders placeholders,
        PunishmentType punishmentType
) implements AudienceTagResolver<@NotNull Audience> {
    @Override
    public @Nullable Tag tag(
            final @NotNull Audience audience,
            final @NotNull ArgumentQueue queue,
            final @NotNull Context ctx
    ) {
        final UUID uuid = audience.get(Identity.UUID).orElse(null);
        if (uuid == null) {
            return null;
        }
        final PlayerVictim victim = PlayerVictim.of(uuid);
        final List<Punishment> punishments = this.placeholders.punishmentCache().get(victim);
        if (punishments == null || punishments.isEmpty()) {
            return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
        }
        final boolean isPunished = punishments.stream()
                .anyMatch(punishment -> punishment.getType() == punishmentType);
        if (isPunished) {
            return Tag.selfClosingInserting(Components.TRUE_COMPONENT);
        } else {
            return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
        }
    }
}
