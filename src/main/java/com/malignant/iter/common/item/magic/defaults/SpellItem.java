package com.malignant.iter.common.item.magic.defaults;

import com.malignant.iter.common.misc.Pictograms;
import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

public abstract class SpellItem extends Item{

    private final int castTime;
    private final int cooldown;
    private final int etherCost;
    private final SpellDomain domain;
    private final SpellMethod method;
    private final SpellAspect aspect;
    private final int tier;

    public SpellItem(Properties properties, SpellDomain domain, SpellMethod method, SpellAspect aspect, int tier, int castTime, int etherCost, int cooldown) {
        super(properties.stacksTo(1));
        this.castTime = castTime;
        this.etherCost = etherCost;
        this.cooldown = cooldown;
        this.domain = domain;
        this.method = method;
        this.aspect = aspect;
        this.tier = tier;
    }

    public enum SpellDomain{
        ARCANE, PRIMAL, OCCULT;
    }

    public enum SpellMethod{
        FORCE, FORM, BODY, CONVEYANCE;
    }

    public enum SpellAspect{
        EARTH, WATER, AIR, FIRE, FROST, LIGHTNING, ETHER, LIFE, ENTROPY;
    }

    public float getCastTimeBase(){
        return this.castTime;
    }
    public float getCooldownBase(){
        return this.cooldown;
    }
    public float getEtherCostBase(){
        return this.etherCost;
    }
    public SpellDomain getDomain(){
        return this.domain;
    }
    public SpellMethod getMethod(){
        return this.method;
    }
    public SpellAspect getAspect(){
        return this.aspect;
    }
    public int getTier(){
        return this.tier;
    }


    public int getQuality(ItemStack stack){
        return stack.getOrDefault(ModDataComponents.SPELL_QUALITY.get(), 0);
    }

    public ItemStack setQuality(ItemStack stack, int newQuality){
        stack.set(ModDataComponents.SPELL_QUALITY, newQuality);
        return stack;
    }


    public String getSpellDisplayName(){
        Component fullname = Component.translatable(this.getDescriptionId());
        Component prefix = Component.translatable("iter.spell.prefix");
        String prefix_trim = prefix.getString();
        String nameOnly = fullname.getString();
        if (nameOnly.startsWith(prefix_trim)){
            nameOnly = nameOnly.substring(prefix_trim.length()).trim();
            return nameOnly;
        }
        return nameOnly;
    }

    public float getCastTime(Player player, ItemStack spellStack) {
        AttributeInstance CastingSpeedAttribute = player.getAttribute(ModAttributes.CASTING_SPEED);
        float castTimeModifier = CastingSpeedAttribute != null ? (float) CastingSpeedAttribute.getValue() : 1f;
        float quality = getQuality(spellStack);
        float castTimeBase = castTime * (1 - quality * 0.025f);
        float castTimeNew = castTimeBase / (((castTimeModifier-1)/2f)+1);

        if (castTimeNew <= 0){castTimeNew = 1;}

        return castTimeNew;
    }

    public float getCooldown(Player player, ItemStack spellStack) {
        AttributeInstance CastingSpeedAttribute = player.getAttribute(ModAttributes.CASTING_SPEED);
        float cooldownModifier = CastingSpeedAttribute != null ? (float) CastingSpeedAttribute.getValue() : 1f;
        float quality = getQuality(spellStack);
        float cooldownBase = cooldown * (1 - quality * 0.025f);
        float cooldownNew = cooldownBase / cooldownModifier;

        if (cooldownNew <= 0){cooldownNew = 1;}

        return cooldownNew;
    }

    public float getManaCost(Player player, ItemStack spellStack) {

        AttributeInstance EtherEfficiencyAttribute = player.getAttribute(ModAttributes.ETHER_EFFICIENCY);
        float etherCostModifier = EtherEfficiencyAttribute != null ? (float) EtherEfficiencyAttribute.getValue() : 0f;
        float quality = getQuality(spellStack);
        float costBase = etherCost * (1 - quality * 0.02f);
        float etherCostNew = costBase * (2 - etherCostModifier);

        if (etherCostNew < 0) {etherCostNew=0;}

        return etherCostNew;
    }

    public float getSpellPower(Player player, ItemStack spellStack){
        AttributeInstance SpellPowerAttribute = player.getAttribute(ModAttributes.SPELL_POWER);
        float spellpower = SpellPowerAttribute != null ? (float) SpellPowerAttribute.getValue() : 1f;
        float quality = getQuality(spellStack);
        float spellpowerBase = spellpower * (1 + quality * 0.1f);
        spellpower = spellpowerBase * 0.2f;

        if (spellpower <= 0.05) {spellpower = 0.05f;}
        return spellpower;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);

        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        if (registryName != null) {

            String baseKey = BuiltInRegistries.ITEM.getKey(this).getNamespace() + "." + BuiltInRegistries.ITEM.getKey(this).getPath();

            String domainKey = "iterpg.spell.domain." + this.getDomain().toString().toLowerCase();
            String methodKey = "iterpg.spell.method." + this.getMethod().toString().toLowerCase();
            String aspectKey = "iterpg.spell.aspect." + this.getAspect().toString().toLowerCase();
            Component SpellInfo = Component.translatable("iterpg.spell.info",
                    Component.empty().append(returnSymbolDomain(this.domain)).append(Component.translatable(domainKey)),
                    Component.empty().append(returnSymbolMethod(this.method)).append(Component.translatable(methodKey)),
                    Component.empty().append(returnSymbolAspect(this.aspect)).append(Component.translatable(aspectKey)));

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
            Component SpellPictures = Component.empty().append(returnSymbolDomain(this.domain))
                    .append(returnSymbolMethod(this.method))
                    .append(returnSymbolAspect(this.aspect));
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
        float dynamicCastTime = getCastTime(player, spellStack) / 20f;
        float dynamicCooldown = getCooldown(player, spellStack) / 20f;
        float dynamicManaCost = getManaCost(player, spellStack);

        String spellPowerString = String.format("%.2f", dynamicSpellPower);
        String castTimeString = String.format("%.1f", dynamicCastTime);
        String cooldownString = String.format("%.1f", dynamicCooldown);
        String manaCostString = String.format("%.1f", dynamicManaCost);

        list.add(Component.translatable("iterpg.spell.spellpower", spellPowerString));
        if (dynamicCastTime > 0.05f) {
            list.add(Component.translatable("iterpg.spell.cast_time", castTimeString));
        }
        list.add(Component.translatable("iterpg.spell.mana_cost", manaCostString));
        list.add(Component.translatable("iterpg.spell.cooldown", cooldownString));
    }

    private void addBaseStats(List<Component> list) {
        float baseSpellPower = 0.2f;
        float baseCastTime = this.castTime / 20f;
        float baseCooldown = this.cooldown / 20f;
        float baseManaCost = this.etherCost;

        String spellPowerString = String.format("%.2f", baseSpellPower);
        String castTimeString = String.format("%.1f", baseCastTime);
        String cooldownString = String.format("%.1f", baseCooldown);
        String manaCostString = String.format("%.1f", baseManaCost);

        list.add(Component.translatable("iterpg.spell.spellpower", spellPowerString));
        if (baseCastTime > 0.05f) {
            list.add(Component.translatable("iterpg.spell.cast_time", castTimeString));
        }
        list.add(Component.translatable("iterpg.spell.mana_cost", manaCostString));
        list.add(Component.translatable("iterpg.spell.cooldown", cooldownString));
    }

    public MutableComponent returnSymbol(String type){
        char icon = switch (type){
            case "arcane" -> Pictograms.ID_ARCANE;
            case "primal" -> Pictograms.ID_PRIMAL;
            case "occult" -> Pictograms.ID_OCCULT;

            case "force" -> Pictograms.IM_FORCE;
            case "form" -> Pictograms.IM_FORM;
            case "body" -> Pictograms.IM_BODY;
            case "conveyance" -> Pictograms.IM_CONVEYANCE;

            case "earth" -> Pictograms.IA_EARTH;
            case "water" -> Pictograms.IA_WATER;
            case "air" -> Pictograms.IA_AIR;
            case "fire" -> Pictograms.IA_FIRE;
            case "frost" -> Pictograms.IA_FROST;
            case "lightning" -> Pictograms.IA_LIGHTNING;
            case "ether" -> Pictograms.IA_ETHER;
            case "life" -> Pictograms.IA_LIFE;
            case "entropy" -> Pictograms.IA_ENTROPY;


            default -> Pictograms.IA_FIRE;
        };
        return Pictograms.getIcon(icon);
    }

    public MutableComponent returnSymbolDomain(SpellDomain type){
        char icon = switch (type){
            case ARCANE -> Pictograms.ID_ARCANE;
            case PRIMAL -> Pictograms.ID_PRIMAL;
            case OCCULT -> Pictograms.ID_OCCULT;

            default -> Pictograms.ID_ARCANE;
        };
        return Pictograms.getIcon(icon);
    }

    public MutableComponent returnSymbolMethod(SpellMethod type){
        char icon = switch (type){
            case FORCE -> Pictograms.IM_FORCE;
            case FORM -> Pictograms.IM_FORM;
            case BODY -> Pictograms.IM_BODY;
            case CONVEYANCE -> Pictograms.IM_CONVEYANCE;

            default -> Pictograms.IM_FORCE;
        };
        return Pictograms.getIcon(icon);
    }

    public MutableComponent returnSymbolAspect(SpellAspect type){
        char icon = switch (type){
            case EARTH -> Pictograms.IA_EARTH;
            case WATER -> Pictograms.IA_WATER;
            case AIR -> Pictograms.IA_AIR;
            case FIRE -> Pictograms.IA_FIRE;
            case FROST -> Pictograms.IA_FROST;
            case LIGHTNING -> Pictograms.IA_LIGHTNING;
            case ETHER -> Pictograms.IA_ETHER;
            case LIFE -> Pictograms.IA_LIFE;
            case ENTROPY -> Pictograms.IA_ENTROPY;

            default -> Pictograms.IA_FIRE;
        };
        return Pictograms.getIcon(icon);
    }

    public abstract void castSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower);
    public abstract void castContinousSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks);

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}