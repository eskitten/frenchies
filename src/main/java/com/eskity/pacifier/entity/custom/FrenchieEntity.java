package com.eskity.pacifier.entity.custom;

import com.eskity.pacifier.entity.ModEntities;
import com.eskity.pacifier.entity.variant.FrenchieVariant;
import com.ibm.icu.impl.CalendarCache;
import com.mojang.serialization.Keyable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FrenchieEntity extends TameableEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    public FrenchieEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.FRENCHIE.create(world);
    }
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.APPLE;
    }
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.targetSelector.add(2, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.goalSelector.add(3, new EscapeDangerGoal(this, 1.5D));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 0.35f, 10.0F, 2.0F, false));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(8, new UniversalAngerGoal(this, true));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0));
    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.frenchie.walk", true));
            return PlayState.CONTINUE;
        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.frenchie.sit", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.frenchie.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController
                (this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    protected SoundEvent getAmbientSound() {
        this.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, 0.8F, 2.0F);
        return SoundEvents.ENTITY_WOLF_AMBIENT;
    }
    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(SoundEvents.ENTITY_WOLF_HURT, 0.8F, 2.0F);
        return SoundEvents.ENTITY_WOLF_HURT;
    }
    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.ENTITY_WOLF_DEATH, 0.8F, 2.0F);
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.10F, 0.80F);
    }
    protected SoundEvent getAngrySound() {
        this.playSound(SoundEvents.ENTITY_WOLF_GROWL, 0.8F, 2.0F);
        return SoundEvents.ENTITY_WOLF_GROWL;
    }
    protected void getWhineSound() {
        this.playSound(SoundEvents.ENTITY_WOLF_WHINE, 0.8F, 2.0F);
    }

    /* TAMABLE ENTITY */
    private static final TrackedData<Boolean> SITTING
            = DataTracker.registerData(FrenchieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();
        Item itemForTame = Items.BONE;
        if (isBreedingItem(itemstack)) {
            return super.interactMob(player, hand);
        }
        if (item == Items.APPLE && this.getHealth() < this.getMaxHealth()) {
            if (this.world.isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!this.world.isClient()) {
                    this.heal(1.0F);
                    itemstack.decrement(1);
                    getMuchySound();
                    return ActionResult.SUCCESS;
                } else {
                    return ActionResult.PASS;
                }
            }
        }

        if (item == itemForTame && !isTamed()) {
            if (this.world.isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                if (!this.world.isClient()) {
                    int chances = (int)Math.floor(Math.random() * 10);
                    switch (chances) {
                        case 0, 2, 4 -> {
                            super.setOwner(player);
                            this.navigation.recalculatePath();
                            this.setTarget(null);
                            this.world.sendEntityStatus(this, (byte)7);
                            setSit(true);
                        }
                        case 1, 3 -> {
                            getWhineSound();
                        }
                        default -> {
                            return ActionResult.PASS;
                        }
                    }
                }

                return ActionResult.SUCCESS;
            }
        }

        if(isTamed() && !this.world.isClient() && hand == Hand.MAIN_HAND) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }
        if (itemstack.getItem() == itemForTame) {
            return ActionResult.PASS;
        }
        return super.interactMob(player, hand);
    }

    private void getMuchySound() {
        this.playSound(SoundEvents.ENTITY_DONKEY_EAT, 0.4F, 4.0F);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }
    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(26.0D);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2D);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(13.0D);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(1D);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.125D);
        }
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
        nbt.putInt("Variant", this.getTypeVariant());
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
    }
    private void setAttackTarget(LivingEntity livingEntity) {
        if (livingEntity != null) {
            this.setTarget(livingEntity);
            this.setAttackTarget(livingEntity);
        }
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(DATA_ID_TYPE_VARIANT,0);
    }

    /* VARIANTS */
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT
            = DataTracker.registerData(FrenchieEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 @Nullable EntityData entityData, @Nullable EntityData entityTag, @Nullable NbtCompound entityNbt) {
        FrenchieVariant variant = Util.getRandom(FrenchieVariant.values(), this.random);
        setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityTag, entityNbt);
    }

    public FrenchieVariant getVariant() {
        return FrenchieVariant.byId(this.getTypeVariant() & 255);
    }
    private int getTypeVariant() {
        return this.dataTracker.get(DATA_ID_TYPE_VARIANT);
    }
    private void setVariant(FrenchieVariant variant) {
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
}
