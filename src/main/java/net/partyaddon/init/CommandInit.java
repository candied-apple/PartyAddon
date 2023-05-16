package net.partyaddon.init;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.group.GroupManager;
import net.partyaddon.network.PartyAddonServerPacket;
import net.partyaddon.util.GroupHelper;

public class CommandInit {

    public static void init() {
        // CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
        // dispatcher.register((CommandManager.literal("party").then(CommandManager.literal("join")).executes((commandContext) -> {
        // return executePartyCommand(commandContext.getSource());
        // })));
        // });

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            dispatcher.register(CommandManager.literal("party").then(CommandManager.literal("join").executes((commandContext) -> {
                return executePartyCommand(commandContext.getSource(), "join");
            })).then(CommandManager.literal("leave").executes((commandContext) -> {
                return executePartyCommand(commandContext.getSource(), "leave");
            })));
        });
        // CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
        // dispatcher.register((CommandManager.literal("environment").requires((serverCommandSource) -> {
        // return serverCommandSource.hasPermissionLevel(2);
        // })).then(CommandManager.literal("affection").then(CommandManager.argument("targets", EntityArgumentType.players())
        // .then(CommandManager.literal("hot").then(CommandManager.argument("affection", BoolArgumentType.bool()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "hot", BoolArgumentType.getBool(commandContext, "affection"),
        // 0);
        // }))).then(CommandManager.literal("cold").then(CommandManager.argument("affection", BoolArgumentType.bool()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "cold", BoolArgumentType.getBool(commandContext, "affection"),
        // 0);
        // }))))).then(CommandManager.literal("resistance").then(CommandManager.argument("targets", EntityArgumentType.players())
        // .then(CommandManager.literal("hot").then(CommandManager.argument("resistance", IntegerArgumentType.integer()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "hot",
        // IntegerArgumentType.getInteger(commandContext, "resistance"), 1);
        // }))).then(CommandManager.literal("cold").then(CommandManager.argument("resistance", IntegerArgumentType.integer()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "cold",
        // IntegerArgumentType.getInteger(commandContext, "resistance"), 1);
        // })))))
        // .then(CommandManager.literal("protection").then(CommandManager.argument("targets", EntityArgumentType.players())
        // .then(CommandManager.literal("hot").then(CommandManager.argument("protection", IntegerArgumentType.integer()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "hot",
        // IntegerArgumentType.getInteger(commandContext, "protection"), 2);
        // }))).then(CommandManager.literal("cold").then(CommandManager.argument("protection", IntegerArgumentType.integer()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "cold",
        // IntegerArgumentType.getInteger(commandContext, "protection"), 2);
        // })))))
        // .then(CommandManager.literal("temperature").then(
        // CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("temperature", IntegerArgumentType.integer()).executes((commandContext) -> {
        // return executeEnvCommand(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), "",
        // IntegerArgumentType.getInteger(commandContext, "temperature"), 3);
        // })))));
        // });

        // CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
        // dispatcher.register((CommandManager.literal("party")).then(CommandManager.literal("join"))
        // .then(CommandManager.literal("resistance").then(CommandManager.argument("targets", EntityArgumentType.players()).executes((commandContext) -> {
        // return executeInfo(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), 1);
        // }))).then(CommandManager.literal("protection").then(CommandManager.argument("targets", EntityArgumentType.players()).executes((commandContext) -> {
        // return executeInfo(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), 2);
        // }))).then(CommandManager.literal("temperature").then(CommandManager.argument("targets", EntityArgumentType.players()).executes((commandContext) -> {
        // return executeInfo(commandContext.getSource(), EntityArgumentType.getPlayers(commandContext, "targets"), 3);
        // }))));
        // });

    }

    private static int executePartyCommand(ServerCommandSource source, String string) {
        if (source.getEntity() != null && source.getEntity() instanceof ServerPlayerEntity serverPlayerEntity) {
            GroupManager groupManager = ((GroupManagerAccess) serverPlayerEntity).getGroupManager();
            if (string.equals("join")) {
                GroupManager.tryJoinGroup(serverPlayerEntity, groupManager.getInvitationPlayerId());
                // if (groupManager.getInvitationPlayerId() != null) {
                // UUID invitationPlayerId = groupManager.getInvitationPlayerId();
                // if (serverPlayerEntity.world.getPlayerByUuid(invitationPlayerId) != null && serverPlayerEntity.world.getPlayerByUuid(invitationPlayerId) instanceof ServerPlayerEntity) {

                // UUID leaderId = ((GroupManagerAccess) serverPlayerEntity.world.getPlayerByUuid(invitationPlayerId)).getGroupManager().getGroupLeaderId() == null ? invitationPlayerId
                // : ((GroupManagerAccess) serverPlayerEntity.world.getPlayerByUuid(invitationPlayerId)).getGroupManager().getGroupLeaderId();

                // // groupManager of leader
                // GroupManager groupLeaderManager = ((GroupManagerAccess) serverPlayerEntity.world.getPlayerByUuid(leaderId)).getGroupManager();
                // if (groupLeaderManager.getGroupPlayerIdList().size() > ConfigInit.CONFIG.groupSize) {
                // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.group_is_full", serverPlayerEntity.world.getPlayerByUuid(leaderId).getName().getString()));
                // } else {
                // if (groupLeaderManager.getGroupPlayerIdList().isEmpty()) {
                // groupLeaderManager.addPlayerToGroup(leaderId); // equals invitationPlayerId
                // groupLeaderManager.setGroupLeaderId(leaderId);

                // groupLeaderManager.addPlayerToGroup(serverPlayerEntity.getUuid());
                // } else {
                // groupLeaderManager.addPlayerToGroup(serverPlayerEntity.getUuid());
                // }
                // // Update leader
                // PartyAddonServerPacket.writeS2CSyncGroupManagerPacket((ServerPlayerEntity) serverPlayerEntity.world.getPlayerByUuid(leaderId), groupLeaderManager);

                // for (int i = 1; i < groupLeaderManager.getGroupPlayerIdList().size(); i++) {
                // UUID playerId = groupLeaderManager.getGroupPlayerIdList().get(i);
                // if (serverPlayerEntity.world.getPlayerByUuid(playerId) != null && serverPlayerEntity.world.getPlayerByUuid(playerId) instanceof ServerPlayerEntity) {

                // ServerPlayerEntity groupServerPlayerEntity = (ServerPlayerEntity) serverPlayerEntity.world.getPlayerByUuid(playerId);

                // List<UUID> groupPlayerList = new ArrayList<UUID>();
                // groupPlayerList.addAll(groupLeaderManager.getGroupPlayerIdList());
                // groupPlayerList.remove((Object) groupServerPlayerEntity.getUuid());
                // groupPlayerList.add(0, groupServerPlayerEntity.getUuid());
                // ((GroupManagerAccess) groupServerPlayerEntity).getGroupManager().updatePlayerGroupIdList(groupPlayerList, leaderId);

                // if (groupServerPlayerEntity.getUuid() != groupServerPlayerEntity.getUuid()) {
                // groupServerPlayerEntity.sendMessage(Text.translatable("text.partyaddon.accepted_invitation", groupServerPlayerEntity.getName().getString()));
                // }
                // // update manager
                // PartyAddonServerPacket.writeS2CSyncGroupManagerPacket(groupServerPlayerEntity, ((GroupManagerAccess) groupServerPlayerEntity).getGroupManager());
                // }
                // }

                // }
                // }
                // // set invitation id server and client to 0
                // groupManager.declineInvitation();
                // PartyAddonServerPacket.writeS2CSyncDeclinePacket(serverPlayerEntity, null);
                // } else {
                // serverPlayerEntity.sendMessage(Text.translatable("text.partyaddon.no_invitation"));
                // }
            } else if (string.equals("leave")) {
                GroupManager.leaveGroup(serverPlayerEntity, false);
            }

            // System.out.println(serverPlayerEntity + " : " + string);
            // if (serverPlayerEntity.getWorld().getRegistryKey() == World.OVERWORLD) {
            // IPlayerChunkClaimAPI chunk = OpenPACServerAPI.get(serverPlayerEntity.getServer()).getServerClaimsManager().get((serverPlayerEntity.getWorld()).getDimensionKey().getValue(),
            // new ChunkPos(serverPlayerEntity.getBlockPos()));
            // if (chunk == null || chunk.getPlayerId().equals(SERVER_UUID)) {
            // if (((ServerPlayerAccess) serverPlayerEntity).getUnstuckTimer() <= 0) {
            // ((ServerPlayerAccess) serverPlayerEntity).setUnstuckTimer(1200);

            // BlockPos blockPos = new BlockPos(serverPlayerEntity.getBlockPos());

            // for (int i = 0; i < 4; i++) {

            // int direction = serverPlayerEntity.getRandom().nextInt(4);
            // if (direction == 0)
            // blockPos = blockPos.north(15);
            // else if (direction == 1)
            // blockPos = blockPos.east(15);
            // else if (direction == 2)
            // blockPos = blockPos.south(15);
            // else if (direction == 3)
            // blockPos = blockPos.west(15);
            // BlockPos newBlockPos = serverPlayerEntity.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockPos);

            // if (!serverPlayerEntity.getWorld().getBlockState(newBlockPos.down()).isOf(Blocks.LAVA)) {
            // serverPlayerEntity.networkHandler.requestTeleport(newBlockPos.getX(), newBlockPos.getY(), newBlockPos.getZ(), serverPlayerEntity.getYaw(),
            // serverPlayerEntity.getRandom().nextFloat() * 360F);
            // break;
            // }
            // if (i == 3) {
            // source.sendFeedback(Text.of("Could not find any valid position"), false);
            // }
            // }
            // } else {
            // source.sendFeedback(Text.of("You have to wait " + (((ServerPlayerAccess) serverPlayerEntity).getUnstuckTimer() / 20) + " seconds"), false);
            // }
            // } else {
            // source.sendFeedback(Text.of("Command can not be used in own claims"), false);
            // }
            // } else {
            // source.sendFeedback(Text.of("Command can only be used in Overworld"), false);
            // }
            // }
            // source.sendFeedback(Text.translatable("commands.changed"), true);

            return 1;
        }
        return 0;
    }
}
