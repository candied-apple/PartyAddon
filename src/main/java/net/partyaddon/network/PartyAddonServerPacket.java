package net.partyaddon.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.group.GroupManager;
import net.partyaddon.init.ConfigInit;
import net.partyaddon.util.GroupHelper;

public class PartyAddonServerPacket {

    // screen
    public static final Identifier OPEN_PARTY_SCREEN_CS_PACKET = new Identifier("partyaddon", "open_party_screen_cs");
    public static final Identifier OPEN_PARTY_SCREEN_SC_PACKET = new Identifier("partyaddon", "open_party_screen_sc");
    // star
    public static final Identifier CHANGE_STAR_PLAYER_LIST_CS_PACKET = new Identifier("partyaddon", "change_star_player_list_cs");
    public static final Identifier SYNC_STAR_PLAYER_LIST_SC_PACKET = new Identifier("partyaddon", "sync_star_player_list_sc");
    // group
    public static final Identifier INVITE_PLAYER_TO_GROUP_CS_PACKET = new Identifier("partyaddon", "invite_player_to_group_cs");
    public static final Identifier SYNC_INVITATION_SC_PACKET = new Identifier("partyaddon", "sync_invitation_sc");

    public static final Identifier DECLINE_INVITATION_CS_PACKET = new Identifier("partyaddon", "decline_invitation_cs");
    public static final Identifier SYNC_DECLINE_INVITATION_SC_PACKET = new Identifier("partyaddon", "sync_decline_invitation_sc");

    public static final Identifier ACCEPT_INVITATION_CS_PACKET = new Identifier("partyaddon", "accept_invitation_cs");
    public static final Identifier SYNC_ACCEPT_INVITATION_CS_PACKET = new Identifier("partyaddon", "sync_accept_invitation_cs");

    // public static final Identifier LEAVE_GROUP_SC_PACKET = new Identifier("partyaddon", "leave_group_sc");
    public static final Identifier LEAVE_GROUP_CS_PACKET = new Identifier("partyaddon", "leave_group_cs");

    public static final Identifier KICK_PLAYER_CS_PACKET = new Identifier("partyaddon", "kick_player_cs");

    // manager
    public static final Identifier SYNC_GROUP_MANAGER_SC_PACKET = new Identifier("partyaddon", "sync_group_manager_sc");

    // compat
    public static final Identifier MAP_COMPAT_CS_PACKET = new Identifier("partyaddon", "map_compat_packet_cs");
    public static final Identifier MAP_COMPAT_SC_PACKET = new Identifier("partyaddon", "map_compat_packet_sc");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(OPEN_PARTY_SCREEN_CS_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                writeS2CSyncGroupManagerPacket(player, ((GroupManagerAccess) player).getGroupManager());
                writeS2COpenPartyScreenPacket(player);
            });
        });
        // ServerPlayNetworking.registerGlobalReceiver(CHANGE_STAR_PLAYER_LIST_CS_PACKET, (server, player, handler, buffer, sender) -> {
        // int playerId = buffer.readInt();
        // server.execute(() -> {
        // GroupManager groupManager = ((GroupManagerAccess) player).getGroupManager();
        // if (groupManager.getStarPlayerIdList().contains(playerId)) {
        // groupManager.removePlayerStar(playerId);
        // } else {
        // groupManager.addPlayerStar(playerId);
        // }
        // // Sync star player list
        // writeS2CSyncStarPlayerListPacket(player);
        // });
        // });
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_STAR_PLAYER_LIST_CS_PACKET, (server, player, handler, buffer, sender) -> {
            UUID playerId = buffer.readUuid();
            server.execute(() -> {
                GroupManager groupManager = ((GroupManagerAccess) player).getGroupManager();
                if (groupManager.getStarPlayerIdList().contains(playerId)) {
                    groupManager.removePlayerStar(playerId);
                } else {
                    groupManager.addPlayerStar(playerId);
                }
                // Sync star player list
                writeS2CSyncStarPlayerListPacket(player);
            });
        });
        // ServerPlayNetworking.registerGlobalReceiver(INVITE_PLAYER_TO_GROUP_CS_PACKET, (server, player, handler, buffer, sender) -> {
        // int invitedPlayerId = buffer.readInt();
        // server.execute(() -> {
        // if (player.world.getEntityById(invitedPlayerId) != null && player.world.getEntityById(invitedPlayerId) instanceof ServerPlayerEntity) {
        // player.world.getEntityById(invitedPlayerId).sendMessage(Text.translatable("text.partyaddon.invitation", player.getName().getString()));
        // ((GroupManagerAccess) player.world.getEntityById(invitedPlayerId)).getGroupManager().invitePlayerToGroup(player.getId());
        // // Sync invitation
        // writeS2CSyncInvitationPacket((ServerPlayerEntity) player.world.getEntityById(invitedPlayerId), player.getId());
        // }
        // });
        // });
        ServerPlayNetworking.registerGlobalReceiver(INVITE_PLAYER_TO_GROUP_CS_PACKET, (server, player, handler, buffer, sender) -> {
            UUID invitedPlayerId = buffer.readUuid();
            server.execute(() -> {
                if (player.world.getPlayerByUuid(invitedPlayerId) != null && player.world.getPlayerByUuid(invitedPlayerId) instanceof ServerPlayerEntity) {
                    player.world.getPlayerByUuid(invitedPlayerId).sendMessage(Text.translatable("text.partyaddon.invitation", player.getName().getString()));
                    ((GroupManagerAccess) player.world.getPlayerByUuid(invitedPlayerId)).getGroupManager().invitePlayerToGroup(player.getUuid());
                    // Sync invitation
                    writeS2CSyncInvitationPacket((ServerPlayerEntity) player.world.getPlayerByUuid(invitedPlayerId), player.getUuid());
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(DECLINE_INVITATION_CS_PACKET, (server, player, handler, buffer, sender) -> {
            UUID invitationPlayerId = buffer.readUuid();
            server.execute(() -> {
                writeS2CSyncDeclinePacket(player, invitationPlayerId);
                ((GroupManagerAccess) player).getGroupManager().declineInvitation();

                if (player.world.getPlayerByUuid(invitationPlayerId) != null && player.world.getPlayerByUuid(invitationPlayerId) instanceof ServerPlayerEntity) {
                    player.world.getPlayerByUuid(invitationPlayerId).sendMessage(Text.translatable("text.partyaddon.declined_invitation", player.getName().getString()));
                }
            });
        });
        // ServerPlayNetworking.registerGlobalReceiver(ACCEPT_INVITATION_CS_PACKET, (server, player, handler, buffer, sender) -> {
        // int invitationPlayerId = buffer.readInt();
        // server.execute(() -> {
        // if (player.world.getEntityById(invitationPlayerId) != null && player.world.getEntityById(invitationPlayerId) instanceof ServerPlayerEntity) {

        // int leaderId = ((GroupManagerAccess) player.world.getEntityById(invitationPlayerId)).getGroupManager().getGroupLeaderId() == 0 ? invitationPlayerId
        // : ((GroupManagerAccess) player.world.getEntityById(invitationPlayerId)).getGroupManager().getGroupLeaderId();

        // // groupManager of leader
        // GroupManager groupLeaderManager = ((GroupManagerAccess) player.world.getEntityById(leaderId)).getGroupManager();
        // if (groupLeaderManager.getGroupPlayerIdList().size() > ConfigInit.CONFIG.groupSize) {
        // player.sendMessage(Text.translatable("text.partyaddon.group_is_full", player.world.getEntityById(leaderId).getName().getString()));
        // } else {
        // if (groupLeaderManager.getGroupPlayerIdList().isEmpty()) {
        // groupLeaderManager.addPlayerToGroup(leaderId); // equals invitationPlayerId
        // groupLeaderManager.setGroupLeaderId(leaderId);

        // groupLeaderManager.addPlayerToGroup(player.getId());
        // } else {
        // groupLeaderManager.addPlayerToGroup(player.getId());
        // }
        // // Update leader
        // writeS2CSyncGroupManagerPacket((ServerPlayerEntity) player.world.getEntityById(leaderId), groupLeaderManager);

        // for (int i = 1; i < groupLeaderManager.getGroupPlayerIdList().size(); i++) {
        // int playerId = groupLeaderManager.getGroupPlayerIdList().get(i);
        // if (player.world.getEntityById(playerId) != null && player.world.getEntityById(playerId) instanceof ServerPlayerEntity) {

        // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.world.getEntityById(playerId);

        // List<Integer> groupPlayerList = new ArrayList<Integer>();
        // groupPlayerList.addAll(groupLeaderManager.getGroupPlayerIdList());
        // groupPlayerList.remove((Object) serverPlayerEntity.getId());
        // groupPlayerList.add(0, serverPlayerEntity.getId());
        // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().updatePlayerGroupIdList(groupPlayerList, leaderId);

        // if (serverPlayerEntity.getId() != player.getId()) {
        // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", player.getName().getString()));
        // }
        // // update manager
        // writeS2CSyncGroupManagerPacket(serverPlayerEntity, ((GroupManagerAccess) serverPlayerEntity).getGroupManager());
        // }
        // }

        // // groupLeaderManager.addPlayerToGroup(invitationPlayerId);

        // // groupManager.updateGroup(newGroupPlayerIdList, groupLeaderId);

        // // System.out.println("TEST " + player + " : " + groupManager.getGroupPlayerIdList() + " : Size: " + groupManager.getGroupPlayerIdList().size());

        // // groupManager.getGroupPlayerIdList().forEach(id -> {

        // // System.out.println("Player: " + id + " : " + player.world.getEntityById(id));

        // // if (player.world.getEntityById(id) != null && player.world.getEntityById(id) instanceof ServerPlayerEntity) {

        // // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.world.getEntityById(id);

        // // System.out.println("TESTXXX " + serverPlayerEntity + " : " + groupManager.getGroupPlayerIdList());

        // // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().updateGroup(groupManager.getGroupPlayerIdList(), leaderId);

        // // // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().addPlayerToGroup(player.getId());
        // // if (serverPlayerEntity.getId() != player.getId()) {
        // // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", player.getName().getString()));
        // // }

        // // System.out.println("B " + serverPlayerEntity.getName().getString() + " : " + ((GroupManagerAccess) serverPlayerEntity).getGroupManager().getGroupPlayerIdList());

        // // // update manager
        // // writeS2CUpdateGroupManagerPacket(serverPlayerEntity);

        // // System.out.println("A " + serverPlayerEntity.getName().getString() + " : " + ((GroupManagerAccess) serverPlayerEntity).getGroupManager().getGroupPlayerIdList());
        // // }
        // // System.out.println("I: " + id);

        // // });
        // // for (int i = 1; i <= groupManager.getGroupPlayerIdList().size(); i++) {

        // // // System.out.println("Player: " + k + " : " + player.world.getEntityById(groupManager.getGroupPlayerIdList().get(k)));

        // // if (player.world.getEntityById(groupManager.getGroupPlayerIdList().get(i)) != null
        // // && player.world.getEntityById(groupManager.getGroupPlayerIdList().get(i)) instanceof ServerPlayerEntity) {

        // // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.world.getEntityById(groupManager.getGroupPlayerIdList().get(i));

        // // // System.out.println("TESTXXX " + serverPlayerEntity + " : " + groupManager.getGroupPlayerIdList());

        // // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().updateGroup(groupManager.getGroupPlayerIdList(), leaderId);

        // // // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().addPlayerToGroup(player.getId());
        // // if (serverPlayerEntity.getId() != player.getId()) {
        // // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", player.getName().getString()));
        // // }

        // // // System.out.println("B " + serverPlayerEntity.getName().getString() + " : " + ((GroupManagerAccess) serverPlayerEntity).getGroupManager().getGroupPlayerIdList());

        // // // // update manager
        // // writeS2CUpdateGroupManagerPacket(serverPlayerEntity);

        // // // System.out.println("A " + serverPlayerEntity.getName().getString() + " : " + ((GroupManagerAccess) serverPlayerEntity).getGroupManager().getGroupPlayerIdList());
        // // }
        // // // System.out.println("I: " + k);
        // // }
        // // ((GroupManagerAccess) player).getGroupManager().joinGroup(groupManager.getGroupPlayerIdList(), groupManager.getGroupLeaderId());
        // }
        // }
        // // set invitation id server and client to 0
        // ((GroupManagerAccess) player).getGroupManager().declineInvitation();
        // writeS2CSyncDeclinePacket(player, 0);
        // });
        // });
        ServerPlayNetworking.registerGlobalReceiver(ACCEPT_INVITATION_CS_PACKET, (server, player, handler, buffer, sender) -> {
            UUID invitationPlayerId = buffer.readUuid();
            server.execute(() -> {
                GroupManager.tryJoinGroup(player, invitationPlayerId);
                // if (player.world.getPlayerByUuid(invitationPlayerId) != null && player.world.getPlayerByUuid(invitationPlayerId) instanceof ServerPlayerEntity) {

                // UUID leaderId = ((GroupManagerAccess) player.world.getPlayerByUuid(invitationPlayerId)).getGroupManager().getGroupLeaderId() == null ? invitationPlayerId
                // : ((GroupManagerAccess) player.world.getPlayerByUuid(invitationPlayerId)).getGroupManager().getGroupLeaderId();

                // // groupManager of leader
                // GroupManager groupLeaderManager = ((GroupManagerAccess) player.world.getPlayerByUuid(leaderId)).getGroupManager();
                // if (groupLeaderManager.getGroupPlayerIdList().size() > ConfigInit.CONFIG.groupSize) {
                // player.sendMessage(Text.translatable("text.partyaddon.group_is_full", player.world.getPlayerByUuid(leaderId).getName().getString()));
                // } else {
                // if (groupLeaderManager.getGroupPlayerIdList().isEmpty()) {
                // groupLeaderManager.addPlayerToGroup(leaderId); // equals invitationPlayerId
                // groupLeaderManager.setGroupLeaderId(leaderId);

                // groupLeaderManager.addPlayerToGroup(player.getUuid());
                // } else {
                // groupLeaderManager.addPlayerToGroup(player.getUuid());
                // }
                // // Update leader
                // writeS2CSyncGroupManagerPacket((ServerPlayerEntity) player.world.getPlayerByUuid(leaderId), groupLeaderManager);

                // for (int i = 1; i < groupLeaderManager.getGroupPlayerIdList().size(); i++) {
                // UUID playerId = groupLeaderManager.getGroupPlayerIdList().get(i);
                // if (player.world.getPlayerByUuid(playerId) != null && player.world.getPlayerByUuid(playerId) instanceof ServerPlayerEntity) {

                // ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player.world.getPlayerByUuid(playerId);

                // List<UUID> groupPlayerList = new ArrayList<UUID>();
                // groupPlayerList.addAll(groupLeaderManager.getGroupPlayerIdList());
                // groupPlayerList.remove((Object) serverPlayerEntity.getUuid());
                // groupPlayerList.add(0, serverPlayerEntity.getUuid());
                // ((GroupManagerAccess) serverPlayerEntity).getGroupManager().updatePlayerGroupIdList(groupPlayerList, leaderId);

                // if (serverPlayerEntity.getUuid() != player.getUuid()) {
                // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", player.getName().getString()));
                // }
                // // update manager
                // writeS2CSyncGroupManagerPacket(serverPlayerEntity, ((GroupManagerAccess) serverPlayerEntity).getGroupManager());
                // }
                // }

                // }
                // }
                // // set invitation id server and client to 0
                // ((GroupManagerAccess) player).getGroupManager().declineInvitation();
                // writeS2CSyncDeclinePacket(player, null);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(LEAVE_GROUP_CS_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                GroupManager.leaveGroup(player, false);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(KICK_PLAYER_CS_PACKET, (server, player, handler, buffer, sender) -> {
            UUID groupLeaderUUID = buffer.readUuid();
            UUID kickPlayerId = buffer.readUuid();
            server.execute(() -> {
                if (player.world.getPlayerByUuid(kickPlayerId) != null && player.world.getPlayerByUuid(kickPlayerId) instanceof ServerPlayerEntity
                        && player.world.getPlayerByUuid(groupLeaderUUID) != null && player.world.getPlayerByUuid(groupLeaderUUID) instanceof ServerPlayerEntity
                        && ((GroupManagerAccess) player.world.getPlayerByUuid(groupLeaderUUID)).getGroupManager().isGroupLeader()) {
                    GroupManager.leaveGroup((ServerPlayerEntity) player.world.getPlayerByUuid(kickPlayerId), true);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(MAP_COMPAT_CS_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(() -> {
                writeS2CMapCompatPacket(player);
            });
        });
    }

    // public static void writeS2CSyncGroupManagerPacket(ServerPlayerEntity serverPlayerEntity, GroupManager groupManager) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // IntArrayList intList = new IntArrayList();

    // List<Integer> availablePlayerIdList = new ArrayList<Integer>();
    // for (int i = 0; i < serverPlayerEntity.getServer().getPlayerManager().getPlayerList().size(); i++) {
    // availablePlayerIdList.add(serverPlayerEntity.getServer().getPlayerManager().getPlayerList().get(i).getId());
    // }
    // availablePlayerIdList.remove((Object) serverPlayerEntity.getId());

    // for (int i = 0; i < groupManager.getGroupPlayerIdList().size(); i++) {
    // availablePlayerIdList.remove((Object) groupManager.getGroupPlayerIdList().get(i));
    // }

    // // Map<Integer, String> playerIdNameMap = new HashMap<Integer, String>();

    // // buf.writeMap(playerIdNameMap, PacketByteBuf::writeInt, PacketByteBuf::writeString);

    // intList.addAll(availablePlayerIdList);
    // buf.writeIntList(intList);
    // intList.clear();
    // intList.addAll(groupManager.getStarPlayerIdList());
    // buf.writeIntList(intList);
    // intList.clear();
    // intList.addAll(groupManager.getGroupPlayerIdList());
    // buf.writeIntList(intList);
    // intList.clear();

    // buf.writeInt(groupManager.getGroupLeaderId());

    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_GROUP_MANAGER_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);
    // }

    // public static void writeS2CSyncGroupManagerPacket(ServerPlayerEntity serverPlayerEntity, GroupManager groupManager) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // IntArrayList intList = new IntArrayList();

    // List<Integer> availablePlayerIdList = new ArrayList<Integer>();
    // for (int i = 0; i < serverPlayerEntity.getServer().getPlayerManager().getPlayerList().size(); i++) {
    // availablePlayerIdList.add(serverPlayerEntity.getServer().getPlayerManager().getPlayerList().get(i).getId());
    // }
    // availablePlayerIdList.remove((Object) serverPlayerEntity.getId());

    // for (int i = 0; i < groupManager.getGroupPlayerIdList().size(); i++) {
    // availablePlayerIdList.remove((Object) groupManager.getGroupPlayerIdList().get(i));
    // }

    // // Map<Integer, String> playerIdNameMap = new HashMap<Integer, String>();

    // // buf.writeMap(playerIdNameMap, PacketByteBuf::writeInt, PacketByteBuf::writeString);

    // intList.addAll(availablePlayerIdList);
    // buf.writeIntList(intList);
    // intList.clear();
    // intList.addAll(groupManager.getStarPlayerIdList());
    // buf.writeIntList(intList);
    // intList.clear();
    // intList.addAll(groupManager.getGroupPlayerIdList());
    // buf.writeIntList(intList);
    // intList.clear();

    // buf.writeInt(groupManager.getGroupLeaderId());

    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_GROUP_MANAGER_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);
    // }

    public static void writeS2CSyncGroupManagerPacket(ServerPlayerEntity serverPlayerEntity, GroupManager groupManager) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        List<UUID> availablePlayerIdList = new ArrayList<UUID>();
        for (int i = 0; i < serverPlayerEntity.getServer().getPlayerManager().getPlayerList().size(); i++) {
            availablePlayerIdList.add(serverPlayerEntity.getServer().getPlayerManager().getPlayerList().get(i).getUuid());
        }

        // System.out.println("SYNC S2C Available: "+availablePlayerIdList);

        availablePlayerIdList.remove((Object) serverPlayerEntity.getUuid());

        for (int i = 0; i < groupManager.getGroupPlayerIdList().size(); i++) {
            availablePlayerIdList.remove((Object) groupManager.getGroupPlayerIdList().get(i));
        }
        buf.writeInt(availablePlayerIdList.size());
        for (int i = 0; i < availablePlayerIdList.size(); i++) {
            buf.writeUuid(availablePlayerIdList.get(i));
        }
        int starPlayerCount = groupManager.getStarPlayerIdList().size();
        buf.writeInt(starPlayerCount);
        for (int i = 0; i < starPlayerCount; i++) {
            buf.writeUuid(groupManager.getStarPlayerIdList().get(i));
        }

        int groupCount = groupManager.getGroupPlayerIdList().size();
        buf.writeInt(groupCount);
        for (int i = 0; i < groupCount; i++) {
            buf.writeUuid(groupManager.getGroupPlayerIdList().get(i));
        }
        buf.writeBoolean(groupManager.getGroupLeaderId() != null);
        if (groupManager.getGroupLeaderId() != null) {
            buf.writeUuid(groupManager.getGroupLeaderId());
        }
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_GROUP_MANAGER_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    public static void writeS2CSyncStarPlayerListPacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        int count = ((GroupManagerAccess) serverPlayerEntity).getGroupManager().getStarPlayerIdList().size();
        buf.writeInt(count);
        for (int i = 0; i < count; i++) {
            buf.writeUuid(((GroupManagerAccess) serverPlayerEntity).getGroupManager().getStarPlayerIdList().get(i));
        }
        // buf.writeLongArray(array)
        // IntArrayList intList = new IntArrayList();
        // intList.addAll(((GroupManagerAccess) serverPlayerEntity).getGroupManager().getStarPlayerIdList());
        // buf.writeIntList(intList);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_STAR_PLAYER_LIST_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    // public static void writeS2CSyncInvitationPacket(ServerPlayerEntity serverPlayerEntity, int invitationPlayerId) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // buf.writeInt(invitationPlayerId);
    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_INVITATION_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);
    // }

    public static void writeS2CSyncInvitationPacket(ServerPlayerEntity serverPlayerEntity, UUID invitationPlayerId) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeUuid(invitationPlayerId);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_INVITATION_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    // public static void writeS2CSyncDeclinePacket(ServerPlayerEntity serverPlayerEntity, int playerId) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // // buf.writeInt(playerId);
    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_DECLINE_INVITATION_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);

    // // if (playerId != 0 && serverPlayerEntity.world.getEntityById(playerId) != null && serverPlayerEntity.world.getEntityById(playerId) instanceof ServerPlayerEntity) {
    // // serverPlayerEntity.world.getEntityById(playerId).sendMessage(Text.translatable("text.partyaddon.declined_invitation", serverPlayerEntity.getName().getString()));
    // // }
    // }

    public static void writeS2CSyncDeclinePacket(ServerPlayerEntity serverPlayerEntity, UUID playerId) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        // buf.writeInt(playerId);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(SYNC_DECLINE_INVITATION_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);

        // if (playerId != 0 && serverPlayerEntity.world.getEntityById(playerId) != null && serverPlayerEntity.world.getEntityById(playerId) instanceof ServerPlayerEntity) {
        // serverPlayerEntity.world.getEntityById(playerId).sendMessage(Text.translatable("text.partyaddon.declined_invitation", serverPlayerEntity.getName().getString()));
        // }
    }

    // public static void writeS2CDeclineInvitationPacket(ServerPlayerEntity serverPlayerEntity, int playerId) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // buf.writeInt(playerId);
    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(DECLINE_INVITATION_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);

    // if (serverPlayerEntity.world.getEntityById(playerId) != null && serverPlayerEntity.world.getEntityById(playerId) instanceof ServerPlayerEntity) {
    // serverPlayerEntity.world.getEntityById(playerId).sendMessage(Text.translatable("text.partyaddon.declined_invitation", serverPlayerEntity.getName().getString()));
    // }
    // }

    public static void writeS2COpenPartyScreenPacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(OPEN_PARTY_SCREEN_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    // public static void writeS2CLeaveGroupPacket(ServerPlayerEntity serverPlayerEntity, int playerId, int newGroupLeaderId) {
    // PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    // buf.writeInt(playerId);
    // buf.writeInt(newGroupLeaderId);
    // System.out.println("WRITE");
    // CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(LEAVE_GROUP_SC_PACKET, buf);
    // serverPlayerEntity.networkHandler.sendPacket(packet);

    // // update manager
    // writeS2CUpdateGroupManagerPacket(serverPlayerEntity);
    // }

    public static void writeS2CMapCompatPacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        GroupManager groupManager = ((GroupManagerAccess) serverPlayerEntity).getGroupManager();

        int uuidCount = 0;
        List<UUID> groupPlayerUUIDs = new ArrayList<UUID>();
        List<BlockPos> groupPlayerBlockPoses = new ArrayList<BlockPos>();
        List<Float> groupPlayerYaws = new ArrayList<Float>();

        for (int i = 0; i < groupManager.getGroupPlayerIdList().size(); i++) {
            if (serverPlayerEntity.getWorld().getEntity(groupManager.getGroupPlayerIdList().get(i)) == null || serverPlayerEntity.getUuid().equals(groupManager.getGroupPlayerIdList().get(i))) {
                continue;
            }
            groupPlayerUUIDs.add(groupManager.getGroupPlayerIdList().get(i));
            groupPlayerBlockPoses.add(serverPlayerEntity.getWorld().getEntity(groupManager.getGroupPlayerIdList().get(i)).getBlockPos());
            groupPlayerYaws.add(serverPlayerEntity.getWorld().getEntity(groupManager.getGroupPlayerIdList().get(i)).getYaw());

            uuidCount = i + 1;
        }

        buf.writeInt(uuidCount);
        for (int i = 0; i < uuidCount; i++) {
            buf.writeUuid(groupPlayerUUIDs.get(i));
            buf.writeBlockPos(groupPlayerBlockPoses.get(i));
            buf.writeFloat(groupPlayerYaws.get(i));
        }
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(MAP_COMPAT_SC_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

}
