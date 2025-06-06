package org.nulladmin1.minecraftrivals;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {
    public static final String MOD_ID = "minecraftrivals";

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));

        // Create the Item instance
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, name));
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        if (shouldRegisterItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
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
            new Item.Settings().food(FROZEN_SPITBALL_FOOD_COMPONENT, FROZEN_SPITBALL_CONSUMABLE_COMPONENT).rarity(Rarity.COMMON)
    );

    // Magik's Soulsword
    public static final ToolMaterial ILLYANAS_LIFE_FORCE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            3000,
            8.0f,
            4.0f,
            18,
            ItemTags.IRON_TOOL_MATERIALS
    );

    public static final Item SOULSWORD = register(
            "soulsword",
            settings -> new SwordItem(ModItems.ILLYANAS_LIFE_FORCE, 16, -3.5f, settings),
            new Item.Settings().rarity(Rarity.EPIC).maxCount(1)
    );

    // Venom's Symbiote
    public static final Potion SYMBIOTE = Registry.register(
            Registries.POTION,
            Identifier.of(MOD_ID, "symbiote"),
            new Potion("symbiote",
                    new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 30, 2),
                    new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 30, 2),
                    new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 20 * 60, 2)
            )
    );

    // AstralFlock
    public static final Item ASTRAL_FLOCK = register(
            "astral_flock",
            AstralFlock::new,
            new Item.Settings().rarity(Rarity.EPIC).maxCount(1)
    );

    // Thornlash Wall and Child
    public static final Block THORNLASH_WALL = register(
            "thornlash_wall",
            ThornlashWall::new,
            AbstractBlock.Settings.create(),
            true
    );

    public static final Block THORNLASH_WALL_CHILD = register(
      "thornlash_wall_child",
            Block::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD).hardness(10.0f).resistance(1200.0f),
            false
    );

    // ItemGroup
    public static final RegistryKey<ItemGroup> MINECRAFT_RIVALS_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "minecraft_rivals_items"));
    public static final ItemGroup MINECRAFT_RIVALS_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FROZEN_SPITBALL))
            .displayName(Text.translatable("itemGroup.minecraft_rivals_items"))
            .build();

    public static void registerItemGroup() {
        // Register Item Group
        Registry.register(Registries.ITEM_GROUP, MINECRAFT_RIVALS_ITEM_GROUP_KEY, MINECRAFT_RIVALS_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(MINECRAFT_RIVALS_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.FROZEN_SPITBALL);
            itemGroup.add(ModItems.SOULSWORD);
            itemGroup.add(ModItems.ASTRAL_FLOCK);
            itemGroup.add(ModItems.THORNLASH_WALL.asItem());
        });
    }

    public static void initialize() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.registerPotionRecipe(
                Potions.INFESTED,
                Items.BLACK_DYE,
                Registries.POTION.getEntry(SYMBIOTE)
        ));

        registerItemGroup();
    }
}
