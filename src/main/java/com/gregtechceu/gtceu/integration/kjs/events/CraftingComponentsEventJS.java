package com.gregtechceu.gtceu.integration.kjs.events;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.Context;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({ "unused", "unchecked" })
@NoArgsConstructor
public class CraftingComponentsEventJS extends StartupEventJS {

    public void modify(CraftingComponent.Component component, int tier, Object value) {
        component.appendIngredients(Map.of(tier, value));
    }

    public void modify(CraftingComponent.Component component, Map<Object, Object> map) {
        component.appendIngredients(toMap(map));
    }

    public void modifyItem(CraftingComponent.Component component, int tier, ItemStack item) {
        component.appendIngredients(Map.of(tier, item));
    }

    public void modifyItem(CraftingComponent.Component component, Map<Object, ItemStack> map) {
        component.appendIngredients(toMap(map));
    }

    public void modifyTag(CraftingComponent.Component component, int tier, ResourceLocation tag) {
        component.appendIngredients(Map.of(tier, TagKey.create(Registries.ITEM, tag)));
    }

    public void modifyTag(CraftingComponent.Component component, Map<Object, ResourceLocation> map) {
        component.appendIngredients(toTagMap(map));
    }

    public void modifyUnificationEntry(CraftingComponent.Component component, int tier, UnificationEntry item) {
        component.appendIngredients(Map.of(tier, item));
    }

    public void modifyUnificationEntry(CraftingComponent.Component component, Map<Object, UnificationEntry> map) {
        component.appendIngredients(toMap(map));
    }

    public CraftingComponent.Component create(Map<Object, Object> map) {
        return new CraftingComponent.Component(toMap(map));
    }

    public CraftingComponent.Component createItem(Map<Object, ItemStack> map) {
        return new CraftingComponent.Component(toMap(map));
    }

    public CraftingComponent.Component createTag(Map<Object, ResourceLocation> map) {
        return new CraftingComponent.Component(toTagMap(map));
    }

    public CraftingComponent.Component createUnificationEntry(Map<Object, UnificationEntry> map) {
        return new CraftingComponent.Component(toMap(map));
    }

    private static Map<Integer, Object> toMap(Map<Object, ?> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> toTier(e.getKey()), Map.Entry::getValue));
    }

    private static Map<Integer, Object> toTagMap(Map<Object, ResourceLocation> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> toTier(e.getKey()), e -> TagKey.create(Registries.ITEM, e.getValue())));
    }

    private static int toTier(Object o) {
        if (o instanceof String s) {
            Integer tier = GTValues.RVN.get(s);
            if (tier == null) {
                ConsoleJS.getCurrent((Context) null).error("Unknown tier  '" + s + "'!");
                throw new IllegalArgumentException("Unknown tier  '" + s + "'!");
            }
            return tier;
        } else if (o instanceof Number n) {
            int tier = n.intValue();
            if (tier < 0 || tier >= GTValues.TIER_COUNT) {
                ConsoleJS.getCurrent((Context) null).error("Tier out of range  '" + n + "'!");
                throw new IllegalArgumentException("Tier out of range  '" + n + "'!");
            }
            return tier;
        } else {
            ConsoleJS.getCurrent((Context) null).error("Unknown tier  '" + o + "'!");
            throw new IllegalArgumentException("Unknown tier  '" + o + "'!");
        }
    }
}
