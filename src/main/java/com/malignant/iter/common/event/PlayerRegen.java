package com.malignant.iter.common.event;


import com.malignant.iter.IterMod;
import com.malignant.iter.common.registry.ModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = IterMod.MOD_ID)
public class PlayerRegen {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            if (player.isDeadOrDying() || !player.isAlive()) {
                return;
            }

            AttributeInstance regenerationAttr = player.getAttribute(ModAttributes.REGENERATION);
            float regenAmount = (float) regenerationAttr.getValue();
            if (regenAmount == 0) return;

            if (player.level().getGameTime() % 10 == 0) {
                regenAmount /= 2f;
                player.heal(regenAmount);
            }
        }
    }
}
