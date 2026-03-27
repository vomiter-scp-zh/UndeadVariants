package com.vomiter.undeadvariants.common.entity.group.husk;

import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.api.common.entity.IVillagerThreatEntity;
import com.vomiter.undeadvariants.common.registry.HeadOwner;
import com.vomiter.undeadvariants.common.registry.ModBlocks;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HuskVillager extends ZombieVillager implements IVillagerThreatEntity, IVillagerDataHolder {

    public HuskVillager(EntityType<? extends ZombieVillager> type, Level level) {
        super(type, level);
    }

    public static final EntityDataAccessor<Boolean> UV_CONVERTING =
            SynchedEntityData.defineId(HuskVillager.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean villagerFearEnabled() {
        return getTarget() instanceof Villager;
    }

    @Override
    protected @NotNull ItemStack getSkull() {
        return new ItemStack(ModBlocks.HEADS.get(HeadOwner.HUSK_VILLAGER).get());
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(UV_CONVERTING, false);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        // MVP：先完全沿用 ZombieVillager（別自創平衡）
        return ZombieVillager.createAttributes();
    }

    /**
     * Husk 白天不燃燒的核心：Zombie 會因 sunSensitive 在日照燃燒；
     * Husk 是覆寫成 false。你這裡同樣照做。
     */
    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    /**
     * Husk 攻擊附加飢餓效果（原版邏輯在 Husk#doHurtTarget）
     * 這裡直接複刻，確保行為一致。
     */
    @Override
    public boolean doHurtTarget(@NotNull net.minecraft.world.entity.Entity target) {
        boolean ok = super.doHurtTarget(target);
        if (!ok) return false;

        if (!(target instanceof LivingEntity living)) return true;
        if (this.level().isClientSide) return true;

        Difficulty diff = this.level().getDifficulty();
        if (diff == Difficulty.NORMAL) {
            if (this.random.nextFloat() < 0.5F) {
                living.addEffect(new MobEffectInstance(MobEffects.HUNGER, 7 * 20, 0), this);
            }
        } else if (diff == Difficulty.HARD) {
            living.addEffect(new MobEffectInstance(MobEffects.HUNGER, 7 * 20, 0), this);
        }

        return true;
    }

    @Override
    public VillagerData mobcivics$getVillagerData() {
        return this.getVillagerData();
    }

    public @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource p_32903_) {
        return SoundEvents.HUSK_HURT;
    }

    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    public @NotNull SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }
}
