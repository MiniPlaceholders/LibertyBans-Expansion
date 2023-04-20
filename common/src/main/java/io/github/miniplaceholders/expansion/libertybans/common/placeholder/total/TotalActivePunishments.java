package io.github.miniplaceholders.expansion.libertybans.common.placeholder.total;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import space.arim.libertybans.api.LibertyBans;
import space.arim.libertybans.api.PunishmentType;
import space.arim.libertybans.api.select.SelectionPredicate;

import java.util.function.BiFunction;

public class TotalActivePunishments implements BiFunction<ArgumentQueue, Context, Tag> {
    private final PunishmentType type;
    private final LibertyBans libertyBans;

    public TotalActivePunishments(LibertyBans libertyBans, PunishmentType type) {
        this.type = type;
        this.libertyBans = libertyBans;
    }

    @Override
    public Tag apply(ArgumentQueue argumentQueue, Context context) {
        final Integer activeBans = libertyBans.getSelector()
                .selectionBuilder()
                .victims(SelectionPredicate.matchingAll())
                .type(type)
                .selectActiveOnly()
                .build()
                .countNumberOfPunishments()
                .toCompletableFuture()
                .join();
        return Tag.preProcessParsed(Integer.toString(activeBans));
    }
}
