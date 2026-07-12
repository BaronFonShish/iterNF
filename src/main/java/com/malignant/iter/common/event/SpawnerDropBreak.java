package com.malignant.iter.common.event;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.registry.ModEnchantments;
import com.malignant.iter.common.registry.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = IterMod.MOD_ID)
public class SpawnerDropBreak {
    @SubscribeEvent
    public static void onBlockHarvestDrops(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == Blocks.SPAWNER) {
            if (event.getPlayer() == null) return;
            ItemStack mainHandItem = event.getPlayer().getMainHandItem();
            if (event.getPlayer().isCreative()) return;
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.getHolder(event.getPlayer().registryAccess(), Enchantments.SILK_TOUCH), mainHandItem) != 0) return;

            Level level = (Level) event.getLevel();

            int amount = level.random.nextInt(2) + 1;

            ItemStack fragmentStack = new ItemStack(ModItems.SPAWNER_FRAGMENT.get(), amount);

            ItemEntity itemEntity = new ItemEntity(
                    level,
                    event.getPos().getX() + 0.5,
                    event.getPos().getY() + 0.5,
                    event.getPos().getZ() + 0.5,
                    fragmentStack
            );
            level.addFreshEntity(itemEntity);
        }
        }
    }
