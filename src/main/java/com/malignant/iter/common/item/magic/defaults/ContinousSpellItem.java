package com.malignant.iter.common.item.magic.defaults;

import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.variables.IterPlayerDataUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

public abstract class ContinousSpellItem extends SpellItem{

    private final int rate;

    public ContinousSpellItem(Properties properties, SpellDomain domain, SpellMethod method, SpellAspect aspect, int tier, float etherCost, int rate) {
        super(properties, domain, method, aspect, tier, rate, (int) etherCost, 5);
        this.rate = rate;
    }

    public int getRate(){
        return this.rate;
    }


    public float getManaCost(Player player, ItemStack spellStack) {

        AttributeInstance EtherEfficiencyAttribute = player.getAttribute(ModAttributes.ETHER_EFFICIENCY);
        float etherCostModifier = EtherEfficiencyAttribute != null ? (float) EtherEfficiencyAttribute.getValue() : 0f;
        float quality = getQuality(spellStack);
        float costBase = this.getEtherCostBase() * (1 - quality * 0.02f);
        float etherCostNew = costBase * (2 - etherCostModifier);

        if (etherCostNew < 0) {etherCostNew=0;}

        return etherCostNew;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, TooltipContext context, List<Component> list, TooltipFlag flag) {

        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        if (registryName != null) {

            String baseKey = BuiltInRegistries.ITEM.getKey(this).getNamespace() + "." + BuiltInRegistries.ITEM.getKey(this).getPath();

            String domainKey = "iterpg.spell.domain." + this.getDomain().toString().toLowerCase();
            String methodKey = "iterpg.spell.method." + this.getMethod().toString().toLowerCase();
            String aspectKey = "iterpg.spell.aspect." + this.getAspect().toString().toLowerCase();
            Component SpellInfo = Component.translatable("iterpg.spell.info",
                    Component.empty().append(returnSymbolDomain(this.getDomain())).append(Component.translatable(domainKey)),
                    Component.empty().append(returnSymbolMethod(this.getMethod())).append(Component.translatable(methodKey)),
                    Component.empty().append(returnSymbolAspect(this.getAspect())).append(Component.translatable(aspectKey)));

            int quality = getQuality(itemstack);
            Component qualityText = Component.translatable("iterpg.spell.quality")
                    .append(Component.literal(": " + quality));

            list.add(Component.translatable("iterpg.spell.tier", Component.translatable("iterpg.spell.tier." + this.getTier())));

            if (context instanceof TooltipContext levelContext && levelContext.level() != null && Objects.requireNonNull(levelContext.level()).isClientSide()) {
                addClientTooltipDetails(itemstack, levelContext.level(), list, SpellInfo, qualityText, baseKey);
            } else {
                list.add(qualityText);
                list.add(Component.literal(""));
                addBaseStats(list);
                list.add(Component.literal(""));
                list.add(Component.translatable(baseKey + ".desc"));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addClientTooltipDetails(ItemStack itemstack, Level world, List<Component> list, Component SpellInfo, Component qualityText, String baseKey) {
        boolean shiftheld = isShiftHeld();

        if (shiftheld) {
            list.add(SpellInfo);
        } else {
            Component SpellPictures = Component.empty().append(returnSymbolDomain(this.getDomain()))
                    .append(returnSymbolMethod(this.getMethod()))
                    .append(returnSymbolAspect(this.getAspect()));
            list.add(SpellPictures);
        }
        list.add(qualityText);
        list.add(Component.literal(""));

        if (shiftheld) {
            Player clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null) {
                addDynamicStats(list, clientPlayer, itemstack);
            } else {
                addBaseStats(list);
            }
        } else {
            list.add(Component.translatable("iterpg.spell.shift"));
        }

        list.add(Component.literal(""));
        list.add(Component.translatable(baseKey + ".desc"));
    }

    @OnlyIn(Dist.CLIENT)
    private boolean isShiftHeld() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            return mc.screen.hasShiftDown();
        }
        return mc.options.keyShift.isDown();
    }

    private void addDynamicStats(List<Component> list, Player player, ItemStack spellStack) {
        float dynamicSpellPower = getSpellPower(player, spellStack);
        float dynamicManaCost = getManaCost(player, spellStack);

        String spellPowerString = String.format("%.2f", dynamicSpellPower);
        String manaCostString = String.format("%.1f", dynamicManaCost);

        list.add(Component.translatable("iterpg.spell.spellpower", spellPowerString));
        list.add(Component.translatable("iterpg.spell.mana_cost_continous", manaCostString));
    }

    private void addBaseStats(List<Component> list) {
        float baseSpellPower = 0.2f;
        float baseManaCost = this.getEtherCostBase();

        String spellPowerString = String.format("%.2f", baseSpellPower);
        String manaCostString = String.format("%.1f", baseManaCost);

        list.add(Component.translatable("iterpg.spell.spellpower", spellPowerString));
        list.add(Component.translatable("iterpg.spell.mana_cost_continous", manaCostString));
    }


    public void spellTick(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {
        if (ticks >= 5) {
            IterPlayerDataUtils.addBurnout(player, (this.getManaCost(player, spellStack) / 20));
            castContinousSpell(level, player, wand, spellStack, spellpower, ticks);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}