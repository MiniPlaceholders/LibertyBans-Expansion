package io.github.miniplaceholders.expansion.libertybans.common.placeholder.global;

import io.github.miniplaceholders.api.resolver.GlobalTagResolver;
import io.github.miniplaceholders.api.utils.Components;
import io.github.miniplaceholders.expansion.libertybans.common.LibertyBansPlaceholders;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.Victim;
import space.arim.libertybans.api.punish.Punishment;

import java.util.List;
import java.util.function.Predicate;

public record ProvidedVictimSanctionedPlaceholder(
        LibertyBansPlaceholders expansion,
        PunishmentType punishmentType
) implements GlobalTagResolver, VictimProvider {

    @Override
    public Tag tag(@NotNull ArgumentQueue argumentQueue, @NotNull Context context) {
        final Victim victim = victimFrom(expansion, argumentQueue, context);
        final List<Punishment> punishments = expansion.punishmentCache().get(victim);
        if (punishments == null || punishments.isEmpty()) {
            return Tag.selfClosingInserting(Components.FALSE_COMPONENT);
        }
        final boolean isCurrentlyPunished = punishments.stream()
                .filter(punishment -> punishment.getType() == punishmentType)
                .anyMatch(Predicate.not(Punishment::isExpired));

        return Tag.selfClosingInserting(isCurrentlyPunished
                ? Components.TRUE_COMPONENT
                : Components.FALSE_COMPONENT);
    }
}
