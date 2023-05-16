package net.partyaddon.mixin.compat;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.partyaddon.access.GroupLeaderAccess;
import net.partyaddon.access.GroupManagerAccess;

@Mixin(value = ServerPlayerEntity.class, priority = 1010)
public abstract class LevelZServerPlayerEntityMixin extends PlayerEntity implements GroupLeaderAccess {

    private int collectedLevelZXP = 0;

    public LevelZServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    // @Override
    // public void addLeaderLevelZExperience(int experience) {
    // this.collectedLevelZXP += experience;
    // List<Integer> groupPlayerIdList = ((GroupManagerAccess) this).getGroupManager().getGroupPlayerIdList();
    // if (!groupPlayerIdList.isEmpty()) {
    // if (groupPlayerIdList.size() <= this.collectedLevelZXP) {
    // for (int i = 0; i < groupPlayerIdList.size(); i++) {
    // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.world.getEntityById(groupPlayerIdList.get(i));
    // ((GroupLeaderAccess) serverPlayerEntity).addLevelZExperience(this.collectedLevelZXP / groupPlayerIdList.size());
    // }
    // this.collectedLevelZXP -= groupPlayerIdList.size();
    // }
    // }
    // }

    @Override
    public void addLeaderLevelZExperience(int experience) {
        this.collectedLevelZXP += experience;
        List<UUID> groupPlayerIdList = ((GroupManagerAccess) this).getGroupManager().getGroupPlayerIdList();
        if (!groupPlayerIdList.isEmpty()) {
            if (groupPlayerIdList.size() <= this.collectedLevelZXP) {
                for (int i = 0; i < groupPlayerIdList.size(); i++) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.world.getPlayerByUuid(groupPlayerIdList.get(i));
                    ((GroupLeaderAccess) serverPlayerEntity).addLevelZExperience(this.collectedLevelZXP / groupPlayerIdList.size());
                }
                this.collectedLevelZXP -= groupPlayerIdList.size();
            }
        }
    }

    @Override
    public void addLevelZExperience(int experience) {
        addLevelExperience(experience);
    }

    @Shadow
    public void addLevelExperience(int experience) {
    }
}
