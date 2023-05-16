package net.partyaddon.mixin;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.partyaddon.access.GroupLeaderAccess;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.init.ConfigInit;
import net.partyaddon.network.PartyAddonServerPacket;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements GroupLeaderAccess {

    @Shadow
    private int syncedExperience = -99999999;

    private int collectedVanillaXP = 0;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    // @Inject(method = "addExperience", at = @At("HEAD"), cancellable = true)
    // private void addExperienceMixin(int experience, CallbackInfo info) {
    // if (ConfigInit.CONFIG.distributeVanillaXP && !((GroupManagerAccess) this).getGroupManager().getGroupPlayerIdList().isEmpty()) {
    // ((GroupLeaderAccess) this.world.getEntityById(((GroupManagerAccess) this).getGroupManager().getGroupLeaderId())).addLeaderExperience(experience);
    // info.cancel();
    // }
    // }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyFromMixin(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        PartyAddonServerPacket.writeS2CSyncGroupManagerPacket((ServerPlayerEntity) (Object) this, ((GroupManagerAccess) oldPlayer).getGroupManager());
    }

    // @Override
    // public void addLeaderVanillaExperience(int experience) {
    // this.collectedVanillaXP += experience;
    // List<Integer> groupPlayerIdList = ((GroupManagerAccess) this).getGroupManager().getGroupPlayerIdList();
    // if (!groupPlayerIdList.isEmpty()) {
    // if (groupPlayerIdList.size() <= this.collectedVanillaXP) {
    // for (int i = 0; i < groupPlayerIdList.size(); i++) {
    // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.world.getEntityById(groupPlayerIdList.get(i));
    // ((GroupLeaderAccess) serverPlayerEntity).addVanillaExperience(this.collectedVanillaXP / groupPlayerIdList.size());
    // }
    // this.collectedVanillaXP -= groupPlayerIdList.size();
    // }
    // }
    // }

    @Override
    public void addLeaderVanillaExperience(int experience) {
        this.collectedVanillaXP += experience;
        List<UUID> groupPlayerIdList = ((GroupManagerAccess) this).getGroupManager().getGroupPlayerIdList();
        if (!groupPlayerIdList.isEmpty()) {
            if (groupPlayerIdList.size() <= this.collectedVanillaXP) {
                for (int i = 0; i < groupPlayerIdList.size(); i++) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.world.getPlayerByUuid(groupPlayerIdList.get(i));
                    ((GroupLeaderAccess) serverPlayerEntity).addVanillaExperience(this.collectedVanillaXP / groupPlayerIdList.size());
                }
                this.collectedVanillaXP -= groupPlayerIdList.size();
            }
        }
    }

    @Override
    public void addVanillaExperience(int experience) {
        super.addExperience(experience);
        this.syncedExperience = -1;
    }

}
