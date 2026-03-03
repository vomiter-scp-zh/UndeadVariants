package com.vomiter.undeadvariants.common.entity.group;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ZombifiedPiglinBrute extends ZombifiedPiglin {
    public ZombifiedPiglinBrute(EntityType<? extends ZombifiedPiglin> p_34427_, Level p_34428_) {
        super(p_34427_, p_34428_);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return
                Zombie.createAttributes().
                        add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
                        .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                        .add(Attributes.ATTACK_DAMAGE, 7.0D)
                        .add(Attributes.MAX_HEALTH, 45);
    }

    @Override
    protected void randomizeReinforcementsChance() {
        var value = this.random.nextDouble()
                * net.minecraftforge.common.ForgeConfig.SERVER.zombieBaseSummonChance.get()
                * 3;
        value = Math.max(value, 1);
        Objects.requireNonNull(this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE))
                .setBaseValue(value);
    }

    public void randomizeReinforcementsChanceAfterConversion(){
        this.randomizeReinforcementsChance();
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource p_219171_, DifficultyInstance p_219172_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

}
