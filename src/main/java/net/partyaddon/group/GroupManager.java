package net.partyaddon.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.init.ConfigInit;
import net.partyaddon.network.PartyAddonClientPacket;
import net.partyaddon.network.PartyAddonServerPacket;

public class GroupManager {

    private final PlayerEntity playerEntity;

    // private Map<Integer, String> cachedPlayerNames = new HashMap<Integer, String>();

    // private List<Integer> availablePlayerIdList = new ArrayList<>();
    // private List<Integer> starPlayerIdList = new ArrayList<>();
    // private List<Integer> groupPlayerIdList = new ArrayList<>();

    private List<UUID> availablePlayerIdList = new ArrayList<UUID>();
    private List<UUID> starPlayerIdList = new ArrayList<UUID>();
    private List<UUID> groupPlayerIdList = new ArrayList<UUID>();
    @Nullable
    private UUID groupLeaderId = null;
    @Nullable
    private UUID invitationPlayerId = null;
    // private int groupLeaderId;
    // private int invitationPlayerId = 0;
    private int invitationTick = 0;

    public GroupManager(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public void readNbt(NbtCompound nbt) {
        // nbt.getUuid(key)
        // this.starPlayerIdList.clear();
        // int[] starList = nbt.getIntArray("StarPlayerList");
        // for (int i = 0; i < starList.length; i++)
        // this.starPlayerIdList.add(starList[i]);

    }

    public void writeNbt(NbtCompound nbt) {
        // nbt.putIntArray("StarPlayerList", this.starPlayerIdList);
    }

    // public void tick() {
    // if (!this.playerEntity.getWorld().isClient && this.invitationPlayerId != 0) {
    // this.invitationTick++;
    // if (this.invitationTick >= ConfigInit.CONFIG.invitationTime) {
    // PartyAddonClientPacket.writeC2SDeclineInvitationPacket(this.invitationPlayerId);
    // }
    // }
    // }
    public void tick() {
        // Can be server instead of client?
        if (this.playerEntity.getWorld().isClient() && this.invitationPlayerId != null) {
            this.invitationTick++;
            if (this.invitationTick >= ConfigInit.CONFIG.invitationTime) {
                PartyAddonClientPacket.writeC2SDeclineInvitationPacket(this.invitationPlayerId);
            }
        }
    }

    // // available player
    // public List<Integer> getAvailablePlayerIdList() {
    // return this.availablePlayerIdList;
    // }

    // available player
    public List<UUID> getAvailablePlayerIdList() {
        // System.out.println("GET ON GROUP MANAGER " + this.availablePlayerIdList);
        // if (this.playerEntity.getWorld().isClient) {
        // ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

        // System.out.println("GET ON GROUP MANAGER SECOND " + networkHandler.getPlayerUuids());
        // System.out.println("GET ON GROUP MANAGER THIRD " + networkHandler.getPlayerList().isEmpty());
        // Iterator<PlayerListEntry> iterator = networkHandler.getPlayerList().iterator();
        // while (iterator.hasNext()) {
        // System.out.println(iterator.next().getDisplayName().getString());
        // }
        // }
        // ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        // networkHandler.getPlayerList();
        // PlayerListEntry playerListEntry = networkHandler.getPlayerListEntry(networkHandler.getPlayerList().);
        return this.availablePlayerIdList;
    }

    // // available player
    // public void setAvailablePlayerIdList(List<Integer> availablePlayerIdList) {
    // this.availablePlayerIdList = availablePlayerIdList;
    // }

    // available player
    public void setAvailablePlayerIdList(List<UUID> availablePlayerIdList) {
        this.availablePlayerIdList = availablePlayerIdList;
    }

    // // Star stuff
    // public List<Integer> getStarPlayerIdList() {
    // return this.starPlayerIdList;
    // }
    // Star stuff
    public List<UUID> getStarPlayerIdList() {
        return this.starPlayerIdList;
    }

    // public void removePlayerStar(int playerId) {
    // if (this.starPlayerIdList.contains(playerId)) {
    // this.starPlayerIdList.remove((Object) playerId);
    // }
    // }
    public void removePlayerStar(UUID playerId) {
        if (this.starPlayerIdList.contains(playerId)) {
            this.starPlayerIdList.remove((Object) playerId);
        }
    }

    // public void addPlayerStar(int playerId) {
    // if (this.playerEntity.getWorld().getEntityById(playerId) != null && !this.starPlayerIdList.contains(playerId)) {
    // this.starPlayerIdList.add(playerId);
    // }
    // }
    public void addPlayerStar(UUID playerId) {
        if (this.playerEntity.getWorld().getPlayerByUuid(playerId) != null && !this.starPlayerIdList.contains(playerId)) {
            this.starPlayerIdList.add(playerId);
        }
    }

    // public void updateStarPlayerIdList(List<Integer> newStarPlayerIdList) {
    // this.starPlayerIdList = newStarPlayerIdList;
    // }
    public void updateStarPlayerIdList(List<UUID> newStarPlayerIdList) {
        this.starPlayerIdList = newStarPlayerIdList;
    }

    public void clearStarPlayerIdList() {
        this.starPlayerIdList.clear();
    }

    // // Group stuff
    // public List<Integer> getGroupPlayerIdList() {
    // return this.groupPlayerIdList;
    // }
    // Group stuff
    public List<UUID> getGroupPlayerIdList() {
        return this.groupPlayerIdList;
    }

    // public void addPlayerToGroup(int playerId) {
    // if (this.playerEntity.getWorld().getEntityById(playerId) != null && !this.groupPlayerIdList.contains(playerId)) {
    // this.groupPlayerIdList.add(playerId);
    // }
    // }
    public void addPlayerToGroup(UUID playerId) {
        if (this.playerEntity.getWorld().getPlayerByUuid(playerId) != null && !this.groupPlayerIdList.contains(playerId)) {
            this.groupPlayerIdList.add(playerId);
        }
    }

    // public void removePlayerFromGroup(int playerId) {
    // if (this.groupPlayerIdList.contains(playerId)) {
    // this.groupPlayerIdList.remove((Object) playerId);
    // }
    // }
    public void removePlayerFromGroup(UUID playerId) {
        if (this.groupPlayerIdList.contains(playerId)) {
            this.groupPlayerIdList.remove((Object) playerId);
        }
    }

    public void clearPlayerGroupIdList() {
        this.groupPlayerIdList.clear();
    }

    // public void updatePlayerGroupIdList(List<Integer> newGroupPlayerIdList, int groupLeaderId) {
    // this.groupLeaderId = groupLeaderId;
    // this.groupPlayerIdList = newGroupPlayerIdList;
    // }
    public void updatePlayerGroupIdList(List<UUID> newGroupPlayerIdList, UUID groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
        this.groupPlayerIdList = newGroupPlayerIdList;
    }

    // maybe an issue?
    // public void joinGroup(List<Integer> groupPlayerIdList, int groupLeaderId) {
    // leaveGroup(false);
    // this.groupLeaderId = groupLeaderId;
    // this.groupPlayerIdList.clear();
    // this.groupPlayerIdList.addAll(groupPlayerIdList);
    // }

    public void leaveGroup() {
        if (!this.groupPlayerIdList.isEmpty()) {
            // if (writePacket) {
            // for (int i = 0; i < this.groupPlayerIdList.size(); i++) {
            // int playerId = this.groupPlayerIdList.get(i);

            // System.out.println("Leave: " + playerId + " : " + this.playerEntity.getWorld().getEntityById(playerId));

            // if (this.playerEntity.getWorld().getEntityById(playerId) != null && this.playerEntity.getWorld().getEntityById(playerId) instanceof ServerPlayerEntity) {

            // System.out.println("???");
            // PartyAddonServerPacket.writeS2CLeaveGroupPacket((ServerPlayerEntity) this.playerEntity.getWorld().getEntityById(playerId), this.playerEntity.getId(),
            // this.groupPlayerIdList.size() > 2 ? this.getGroupLeaderId() : 0);

            // // PartyAddonServerPacket.writeS2CLeaveGroupPacket((ServerPlayerEntity) this.playerEntity.getWorld().getEntityById(playerId), this.playerEntity.getId(),
            // // isGroupLeader() && this.groupPlayerIdList.size() > 2 ? this.groupPlayerIdList.get(0) : 0);

            // this.playerEntity.getWorld().getEntityById(playerId).sendMessage(Text.translatable("text.partyaddon.left_group", this.playerEntity.getName().getString()));

            // }
            // }
            // }
            // Do this before writeS2CLeaveGroupPacket cause it syncs to client
            this.groupPlayerIdList.clear();
        }
        // setGroupLeaderId(0);
        setGroupLeaderId(null);
    }

    // public void invitePlayerToGroup(int playerId) {
    // this.invitationPlayerId = playerId;
    // }

    public void invitePlayerToGroup(UUID playerId) {
        this.invitationPlayerId = playerId;
    }

    public void declineInvitation() {
        this.invitationTick = 0;
        // if (!this.playerEntity.getWorld().isClient && writePacket) {
        // PartyAddonServerPacket.writeS2CDeclineInvitationPacket((ServerPlayerEntity) this.playerEntity, this.invitationPlayerId);
        // }
        // this.invitationPlayerId = 0;
        this.invitationPlayerId = null;
    }

    // public void declineInvitation(boolean writePacket) {
    // this.invitationTick = 0;
    // if (!this.playerEntity.getWorld().isClient && writePacket) {
    // PartyAddonServerPacket.writeS2CDeclineInvitationPacket((ServerPlayerEntity) this.playerEntity, this.invitationPlayerId);
    // }
    // this.invitationPlayerId = 0;
    // }

    // public int getInvitationPlayerId() {
    // return this.invitationPlayerId;
    // }
    @Nullable
    public UUID getInvitationPlayerId() {
        return this.invitationPlayerId;
    }

    // public void setGroupLeaderId(int groupLeaderId) {
    // this.groupLeaderId = groupLeaderId;
    // }
    public void setGroupLeaderId(UUID groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    // public int getGroupLeaderId() {
    // return this.groupLeaderId;
    // }
    @Nullable
    public UUID getGroupLeaderId() {
        return this.groupLeaderId;
    }

    // public boolean isGroupLeader() {
    // return this.groupLeaderId == this.playerEntity.getId();
    // }
    @Nullable
    public boolean isGroupLeader() {
        return this.groupLeaderId != null && this.groupLeaderId.equals(this.playerEntity.getUuid());
    }

    // public void kickPlayer(int playerId) {
    // if (isGroupLeader() && getGroupPlayerIdList().contains(playerId)) {
    // removePlayerFromGroup(playerId);
    // }
    // }

    public void kickPlayer(UUID playerId) {
        if (isGroupLeader() && getGroupPlayerIdList().contains(playerId)) {
            removePlayerFromGroup(playerId);
        }
    }

    // public static void leaveGroup(ServerPlayerEntity player, boolean kicked) {
    // GroupManager groupManager = ((GroupManagerAccess) player).getGroupManager();
    // for (int i = 1; i < groupManager.getGroupPlayerIdList().size(); i++) {
    // int playerId = groupManager.getGroupPlayerIdList().get(i);
    // if (player.getWorld().getEntityById(playerId) != null && player.getWorld().getEntityById(playerId) instanceof ServerPlayerEntity) {
    // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.getWorld().getEntityById(playerId);

    // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.left_group", player.getName().getString()));

    // GroupManager groupMemberManager = ((GroupManagerAccess) serverPlayerEntity).getGroupManager();
    // groupMemberManager.removePlayerFromGroup(player.getId());
    // if (groupMemberManager.getGroupPlayerIdList().size() <= 1) {
    // groupMemberManager.clearPlayerGroupIdList();
    // groupMemberManager.setGroupLeaderId(0);
    // }
    // PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(serverPlayerEntity, groupMemberManager);
    // }
    // }
    // groupManager.leaveGroup();
    // if (kicked) {
    // player.sendMessage(Text.translatable("text.partyaddon.kicked"));
    // } else {
    // player.sendMessage(Text.translatable("text.partyaddon.leave_group"));
    // }
    // PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(player, groupManager);
    // }

    public static void tryJoinGroup(ServerPlayerEntity player, UUID invitationPlayerUUID) {
        if (invitationPlayerUUID != null && player.getWorld().getPlayerByUuid(invitationPlayerUUID) != null && player.getWorld().getPlayerByUuid(invitationPlayerUUID) instanceof ServerPlayerEntity) {

            UUID leaderId = ((GroupManagerAccess) player.getWorld().getPlayerByUuid(invitationPlayerUUID)).getGroupManager().getGroupLeaderId() == null ? invitationPlayerUUID
                    : ((GroupManagerAccess) player.getWorld().getPlayerByUuid(invitationPlayerUUID)).getGroupManager().getGroupLeaderId();

            // groupManager of leader
            GroupManager groupLeaderManager = ((GroupManagerAccess) player.getWorld().getPlayerByUuid(leaderId)).getGroupManager();
            if (groupLeaderManager.getGroupPlayerIdList().size() > ConfigInit.CONFIG.groupSize) {
                player.sendMessage(Text.translatable("text.partyaddon.group_is_full", player.getWorld().getPlayerByUuid(leaderId).getName().getString()));
            } else {
                if (groupLeaderManager.getGroupPlayerIdList().isEmpty()) {
                    groupLeaderManager.addPlayerToGroup(leaderId); // equals invitationPlayerId
                    groupLeaderManager.setGroupLeaderId(leaderId);

                    groupLeaderManager.addPlayerToGroup(player.getUuid());
                } else {
                    groupLeaderManager.addPlayerToGroup(player.getUuid());
                }
                // Update leader
                PartyAddonServerPacket.writeS2CSyncGroupManagerPacket((ServerPlayerEntity) player.getWorld().getPlayerByUuid(leaderId), groupLeaderManager);

                for (int i = 1; i < groupLeaderManager.getGroupPlayerIdList().size(); i++) {
                    UUID playerId = groupLeaderManager.getGroupPlayerIdList().get(i);
                    if (player.getWorld().getPlayerByUuid(playerId) != null && player.getWorld().getPlayerByUuid(playerId) instanceof ServerPlayerEntity) {

                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.getWorld().getPlayerByUuid(playerId);

                        List<UUID> groupPlayerList = new ArrayList<UUID>();
                        groupPlayerList.addAll(groupLeaderManager.getGroupPlayerIdList());
                        groupPlayerList.remove((Object) serverPlayerEntity.getUuid());
                        groupPlayerList.add(0, serverPlayerEntity.getUuid());
                        ((GroupManagerAccess) serverPlayerEntity).getGroupManager().updatePlayerGroupIdList(groupPlayerList, leaderId);

                        if (serverPlayerEntity.getUuid() != player.getUuid()) {
                            serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", player.getName().getString()));
                        }
                        // update manager
                        PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(serverPlayerEntity, ((GroupManagerAccess) serverPlayerEntity).getGroupManager());
                    }
                }

            }
        } else {
            player.sendMessage(Text.translatable("text.partyaddon.no_invitation"));
        }
        // set invitation id server and client to 0
        ((GroupManagerAccess) player).getGroupManager().declineInvitation();
        PartyAddonServerPacket.writeS2CSyncDeclinePacket(player, null);
    }

    public static void leaveGroup(ServerPlayerEntity player, boolean kicked) {
        GroupManager groupManager = ((GroupManagerAccess) player).getGroupManager();
        if (groupManager.getGroupPlayerIdList().size() > 0) {
            for (int i = 1; i < groupManager.getGroupPlayerIdList().size(); i++) {
                UUID playerId = groupManager.getGroupPlayerIdList().get(i);
                if (player.getWorld().getPlayerByUuid(playerId) != null && player.getWorld().getPlayerByUuid(playerId) instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.getWorld().getPlayerByUuid(playerId);

                    serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.left_group", player.getName().getString()));

                    GroupManager groupMemberManager = ((GroupManagerAccess) serverPlayerEntity).getGroupManager();
                    groupMemberManager.removePlayerFromGroup(player.getUuid());
                    if (groupMemberManager.getGroupPlayerIdList().size() <= 1) {
                        groupMemberManager.clearPlayerGroupIdList();
                        groupMemberManager.setGroupLeaderId(null);
                    }
                    PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(serverPlayerEntity, groupMemberManager);
                }
            }

            groupManager.leaveGroup();
            if (kicked) {
                player.sendMessage(Text.translatable("text.partyaddon.kicked"));
            } else {
                player.sendMessage(Text.translatable("text.partyaddon.leave_group"));
            }
            PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(player, groupManager);
        } else {
            player.sendMessage(Text.translatable("text.partyaddon.no_group"));
        }
    }

}
