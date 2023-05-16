// package net.partyaddon.gui.old;

// import com.google.common.collect.Multimap;
// import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
// import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
// import io.github.cottonmc.cotton.gui.widget.WLabel;
// import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
// import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
// import io.github.cottonmc.cotton.gui.widget.WSprite;
// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.fabricmc.fabric.api.util.TriState;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.entity.EquipmentSlot;
// import net.minecraft.entity.attribute.EntityAttribute;
// import net.minecraft.entity.attribute.EntityAttributeModifier;
// import net.minecraft.entity.attribute.EntityAttributes;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.item.*;
// import net.minecraft.text.Text;
// import net.minecraft.util.registry.Registry;
// import net.partyaddon.access.GroupManagerAccess;
// import net.partyaddon.group.GroupManager;
// import net.partyaddon.gui.old.widget.AcceptDeclineButton;
// import net.partyaddon.gui.old.widget.KickButton;
// import net.partyaddon.gui.old.widget.PartyButton;
// import net.partyaddon.gui.old.widget.PartyLabel;
// import net.partyaddon.gui.old.widget.StarButton;
// import net.partyaddon.init.ConfigInit;
// import net.partyaddon.init.RenderInit;

// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;

// @Environment(EnvType.CLIENT)
// public class PartyGui extends LightweightGuiDescription {

//     private final PlayerEntity playerEntity;
//     private final GroupManager groupManager;

//     // , List<Integer> playerIdList, List<Integer> starPlayerIdList, List<Integer> groupIdList, int groupLeaderId
//     public PartyGui(MinecraftClient client) {
//         playerEntity = client.player;
//         this.groupManager = ((GroupManagerAccess) client.player).getGroupManager();

//         WPlainPanel root = new WPlainPanel();
//         setRootPanel(root);
//         root.setSize(200, 215);
//         // Top label
//         WLabel topLlabel = new WLabel(Text.translatable("text.partyaddon.gui.title"));
//         root.add(topLlabel, 100 - client.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.title")) / 2, 7);

//         // Available player list label
//         WLabel playerListLlabel = new WLabel(Text.translatable("text.partyaddon.gui.player_list"));
//         root.add(playerListLlabel, ConfigInit.CONFIG.test0, ConfigInit.CONFIG.test1);

//         WSprite backGroundPlayerList = new WSprite(RenderInit.PARTY_ADDON_GUI_ICONS, 0.0f, 0.256f, 0.1171f, 0.3671f);
//         root.add(backGroundPlayerList, ConfigInit.CONFIG.test15, ConfigInit.CONFIG.test16);

//         WPlainPanel plainPanel = new WPlainPanel();
//         WScrollPanel scrollPanel = new WScrollPanel(plainPanel);
//         scrollPanel.setScrollingHorizontally(TriState.FALSE);

//         // friends list + group list + group leader who can kick
//         // friends on top, maybe search bar

//         if (!this.groupManager.getStarPlayerIdList().isEmpty()) {
//             groupManager.getAvailablePlayerIdList().removeAll(this.groupManager.getStarPlayerIdList());
//             for (int i = 0; i < this.groupManager.getStarPlayerIdList().size(); i++) {
//                 if (!this.groupManager.getGroupPlayerIdList().contains(this.groupManager.getStarPlayerIdList().get(i)))
//                     groupManager.getAvailablePlayerIdList().add(0, this.groupManager.getStarPlayerIdList().get(i));
//             }
//         }
//         // search bar?
//         int gridYSpace = 3;
//         for (int i = 0; i < 20; i++) {

//             // for (int i = 0; i < groupManager.getAvailablePlayerIdList().size(); i++) {
//             int playerId = groupManager.getAvailablePlayerIdList().get(0);
//             // int playerId = groupManager.getAvailablePlayerIdList().get(i);
//             // if (client.world.getEntityById(playerId) == null || !(client.world.getEntityById(playerId) instanceof PlayerEntity))
//             // continue;
//             // PlayerEntity playerEntity = (PlayerEntity) client.world.getEntityById(playerId);
//             PlayerEntity playerEntity = client.world.getPlayers().get(0);

//             String playerName = playerEntity.getName().getString();
//             // String playerName = playerEntity.getName().getString() + ":" + String.valueOf(i);
//             // int playerId = playerIdList.get(0);

//             // System.out.println("Width: " + client.textRenderer.getWidth(playerName));
//             int width = client.textRenderer.getWidth(playerName);
//             boolean playerNameTooLong = width > 60;
//             if (playerNameTooLong) {
//                 playerName = playerName.substring(0, 10) + "..";
//             }
//             boolean isInGroup = !this.groupManager.getGroupPlayerIdList().isEmpty() && this.groupManager.getGroupPlayerIdList().contains(playerId);
//             PartyLabel wLabel = new PartyLabel(Text.of(playerName), 60);
//             if (isInGroup) {
//                 wLabel.setColor(0x007F0E, 0x007F0E);
//             }
//             if (playerNameTooLong) {
//                 wLabel.addTooltipText(playerEntity.getName().getString());
//             }
//             plainPanel.add(wLabel, 0, gridYSpace);
//             PartyButton partyButton = new PartyButton(playerEntity.getId(), 11, isInGroup);
//             if (isInGroup) {
//                 partyButton.setEnabled(false);
//             }
//             plainPanel.add(partyButton, ConfigInit.CONFIG.test3, gridYSpace - 3);

//             StarButton starButton = new StarButton(playerEntity.getId(), false);
//             if (this.groupManager.getStarPlayerIdList().contains(playerId)) {
//                 starButton.setActivated(true);
//             }
//             plainPanel.add(starButton, ConfigInit.CONFIG.test4, gridYSpace);

//             gridYSpace += ConfigInit.CONFIG.test2;
//         }

//         if (gridYSpace < 200) {
//             scrollPanel.setScrollingVertically(TriState.FALSE);
//         }
//         plainPanel.setSize(50, gridYSpace);

//         root.add(scrollPanel, 10, 30, 90, 175);

//         // Player invite label
//         WLabel playerInvitationLlabel = new WLabel(Text.translatable("text.partyaddon.gui.invitation"));
//         root.add(playerInvitationLlabel, ConfigInit.CONFIG.test5, ConfigInit.CONFIG.test6);

//         PartyLabel playerInviteLlabel;
//         int invitationPlayerId = ((GroupManagerAccess) client.player).getGroupManager().getInvitationPlayerId();
//         if (invitationPlayerId != 0 && client.world.getEntityById(invitationPlayerId) != null && client.world.getEntityById(invitationPlayerId) instanceof PlayerEntity) {

//             String invitationPlayerName = client.world.getEntityById(invitationPlayerId).getName().getString();
//             if (client.textRenderer.getWidth(invitationPlayerName) > 64)
//                 invitationPlayerName = invitationPlayerName.substring(0, 11) + "..";
//             // String string = Text.translatable("text.partyaddon.gui.invitation_by_player").getString();
//             // Check if too long
//             playerInviteLlabel = new PartyLabel(Text.of(invitationPlayerName), 64);
//             playerInviteLlabel.addTooltipText(client.world.getEntityById(invitationPlayerId).getName().getString());
//             AcceptDeclineButton acceptButton = new AcceptDeclineButton(true, invitationPlayerId, 11);
//             root.add(acceptButton, ConfigInit.CONFIG.test9, ConfigInit.CONFIG.test8 - 3);
//             AcceptDeclineButton declineButton = new AcceptDeclineButton(false, invitationPlayerId, 11);
//             root.add(declineButton, ConfigInit.CONFIG.test10, ConfigInit.CONFIG.test8 - 3);

//             acceptButton.setOnClick(() -> {
//                 acceptButton.setEnabled(false);
//                 declineButton.setEnabled(false);
//             });
//             declineButton.setOnClick(() -> {
//                 acceptButton.setEnabled(false);
//                 declineButton.setEnabled(false);
//             });
//             // AcceptDeclineButton acceptButton = new AcceptDeclineButton(true, invitationPlayerId, 11);
//             // plainPanel.add(acceptButton, ConfigInit.CONFIG.test9, ConfigInit.CONFIG.test8);
//             // AcceptDeclineButton declineButton = new AcceptDeclineButton(false, invitationPlayerId, 11);
//             // plainPanel.add(declineButton, ConfigInit.CONFIG.test10, ConfigInit.CONFIG.test8);

//         } else {
//             playerInviteLlabel = new PartyLabel(Text.of("-"), 16);
//         }
//         // System.out.println("LENGTH " + client.textRenderer.getWidth(Text.of("This is A Tes")));

//         root.add(playerInviteLlabel, ConfigInit.CONFIG.test7, ConfigInit.CONFIG.test8);

//         // Group
//         WLabel groupLlabel = new WLabel(Text.translatable("text.partyaddon.gui.group"));
//         root.add(groupLlabel, ConfigInit.CONFIG.test7, ConfigInit.CONFIG.test11);

//         if (groupManager.getGroupPlayerIdList().isEmpty()) {
//             WLabel emptyLabel = new WLabel(Text.of("-"));
//             root.add(emptyLabel, ConfigInit.CONFIG.test7, ConfigInit.CONFIG.test12);
//         } else {
//             // hdißsnds+dbsdbs+
//             System.out.println("not Empty!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//             // self kick
//             KickButton leaveButton = new KickButton(this.playerEntity.getId(), true, 11);
//             root.add(leaveButton, ConfigInit.CONFIG.test7 + ConfigInit.CONFIG.test14, ConfigInit.CONFIG.test11 - 3);

//             // group list
//             int groupGridYSpace = ConfigInit.CONFIG.test12;
//             List<Integer> groupList = groupManager.getGroupPlayerIdList();
//             groupList.add(0, this.playerEntity.getId());

//             for (int i = 0; i < groupList.size(); i++) {

//                 int playerId = groupList.get(i);
//                 if (client.world.getEntityById(playerId) == null || !(client.world.getEntityById(playerId) instanceof PlayerEntity))
//                     continue;
//                 PlayerEntity playerEntity = (PlayerEntity) client.world.getEntityById(playerId);

//                 String playerName = playerEntity.getName().getString();
//                 int width = client.textRenderer.getWidth(playerName);
//                 boolean playerNameTooLong = width > 60;
//                 if (playerNameTooLong) {
//                     playerName = playerName.substring(0, 10) + "..";
//                 }
//                 PartyLabel wLabel = new PartyLabel(Text.of(playerName), 60);

//                 if (playerNameTooLong) {
//                     wLabel.addTooltipText(playerEntity.getName().getString());
//                 }
//                 root.add(wLabel, ConfigInit.CONFIG.test7, groupGridYSpace);

//                 System.out.println("LEADER: " + groupManager.getGroupLeaderId() + " : " + playerId);
//                 if (groupManager.getGroupLeaderId() == playerId) {
//                     WSprite wSprite = new WSprite(RenderInit.PARTY_ADDON_GUI_ICONS, 0.0f, 0.125f, 0.0273f, 0.1523f);
//                     root.add(wSprite, ConfigInit.CONFIG.test7 + ConfigInit.CONFIG.test4, groupGridYSpace);
//                 }
//                 if (groupManager.isGroupLeader() && i > 0) {
//                     KickButton kickButton = new KickButton(playerId, false, 11);
//                     root.add(kickButton, ConfigInit.CONFIG.test7 + ConfigInit.CONFIG.test3, groupGridYSpace - 3);
//                 }
//                 // PartyButton partyButton = new PartyButton(playerEntity.getId(), 11, isInGroup);
//                 // plainPanel.add(partyButton, ConfigInit.CONFIG.test3, groupGridYSpace - 3);

//                 // StarButton starButton = new StarButton(playerEntity.getId(), false);
//                 // if (this.groupManager.getStarPlayerIdList().contains(playerId)) {
//                 // starButton.setActivated(true);
//                 // }
//                 // plainPanel.add(starButton, ConfigInit.CONFIG.test4, groupGridYSpace);

//                 // TESTSTETST
//                 groupGridYSpace += ConfigInit.CONFIG.test2;
//             }
//         }

//         root.validate(this);
//     }

// }