package org.nulladmin1.minecraftrivals;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("minecraftrivals", name));

        // Create the Item instance
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    // Frozen Spitball
    public static final ConsumableComponent FROZEN_SPITBALL_CONSUMABLE_COMPONENT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 10 * 20, 1), 1.0f))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5 * 20, 3), 1.0f))
            .build();

    public static final FoodComponent FROZEN_SPITBALL_FOOD_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .nutrition(3)
            .saturationModifier(0.5f)
            .build();

    public static final Item FROZEN_SPITBALL = register(
            "frozen_spitball",
            Item::new,
            new Item.Settings().food(FROZEN_SPITBALL_FOOD_COMPONENT, FROZEN_SPITBALL_CONSUMABLE_COMPONENT)
    );

    // Magneto's Greatsword
    public static final Item GREATSWORD = register(
            "greatsword",
            Item::new,
            new Item.Settings()
    );

    // ItemGroup
    public static final RegistryKey<ItemGroup> MINECRAFT_RIVALS_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of("minecraftrivals", "minecraft_rivals_items"));
    public static final ItemGroup MINECRAFT_RIVALS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FROZEN_SPITBALL))
            .displayName(Text.translatable("itemGroup.minecraft_rivals_items"))
            .build();

    public static void registerItemGroup() {
        // Register Item Group
        Registry.register(Registries.ITEM_GROUP, MINECRAFT_RIVALS_ITEM_GROUP_KEY, MINECRAFT_RIVALS_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(MINECRAFT_RIVALS_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.FROZEN_SPITBALL);
            itemGroup.add(ModItems.GREATSWORD);
        });
    }

    public static void initialize() {
        registerItemGroup();
    }
}
