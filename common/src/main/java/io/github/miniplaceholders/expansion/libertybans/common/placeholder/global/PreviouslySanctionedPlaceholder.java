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

public record PreviouslySanctionedPlaceholder(
        LibertyBansPlaceholders expansion,
        PunishmentType punishmentType
) implements GlobalTagResolver, VictimProvider {

    @Override
    public Tag tag(final @NotNull ArgumentQueue argumentQueue, final @NotNull Context context) {
        final Victim victim = victimFrom(expansion, argumentQueue, context);
        final List<Punishment> punishments = expansion.punishmentCache().get(victim);
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
