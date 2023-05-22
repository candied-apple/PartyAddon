package net.partyaddon.mixin.compat;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.levelz.entity.LevelExperienceOrbEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.partyaddon.access.GroupLeaderAccess;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.init.ConfigInit;

@Mixin(LevelExperienceOrbEntity.class)
public abstract class LevelExperienceOrbEntityMixin extends Entity {

    public LevelExperienceOrbEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"), cancellable = true)
    // private void onPlayerCollisionMixin(PlayerEntity player, CallbackInfo info) {
    // if (ConfigInit.CONFIG.distributeLevelZXP && !((GroupManagerAccess) player).getGroupManager().getGroupPlayerIdList().isEmpty()) {
    // getClumpedMap().forEach((value, amount) -> {
    // ((GroupLeaderAccess) player.world.getEntityById(((GroupManagerAccess) player).getGroupManager().getGroupLeaderId())).addLeaderLevelZExperience(value * amount);
    // });
    // this.discard();
    // info.cancel();
    // }
    // }

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"), cancellable = true)
    private void onPlayerCollisionMixin(PlayerEntity player, CallbackInfo info) {
        if (ConfigInit.CONFIG.distributeLevelZXP && !((GroupManagerAccess) player).getGroupManager().getGroupPlayerIdList().isEmpty()
                && ((GroupManagerAccess) player).getGroupManager().getGroupLeaderId() != null
                && player.world.getPlayerByUuid(((GroupManagerAccess) player).getGroupManager().getGroupLeaderId()) != null) {
            getClumpedMap().forEach((value, amount) -> {
                ((GroupLeaderAccess) player.world.getPlayerByUuid(((GroupManagerAccess) player).getGroupManager().getGroupLeaderId())).addLeaderLevelZExperience(value * amount);
            });
            this.discard();
            info.cancel();
        }
    }

    @Shadow
    private Map<Integer, Integer> getClumpedMap() {
        return null;
    }
}
