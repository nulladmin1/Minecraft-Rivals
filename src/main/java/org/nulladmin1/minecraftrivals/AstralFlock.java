package org.nulladmin1.minecraftrivals;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AstralFlock extends Item {
    public AstralFlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user.getItemCooldownManager().isCoolingDown(user.getStackInHand(hand))) {
            return ActionResult.FAIL;
        }

        Vec3d look = user.getRotationVec(1.0f).multiply(18);
        Vec3d target = user.getPos().add(look);

        if (world.isSpaceEmpty(user, user.getBoundingBox().offset(look))) {
            user.requestTeleport(target.x, target.y, target.z);
        }

        user.getItemCooldownManager().set(user.getStackInHand(hand), 100);

        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 1));
        return ActionResult.SUCCESS;
    }
}
