package org.nulladmin1.minecraftrivals;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class Soulsword extends SwordItem {
    public static final ToolMaterial ILLYANAS_LIFE_FORCE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            3000,
            8.0f,
            4.0f,
            18,
            ItemTags.IRON_TOOL_MATERIALS
    );
    public Soulsword(Settings settings) {
        super(ILLYANAS_LIFE_FORCE, 16, -3.5f, settings);
    }
}
