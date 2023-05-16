// package net.partyaddon.gui;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Objects;

// import com.mojang.blaze3d.systems.RenderSystem;

// import org.lwjgl.glfw.GLFW;

// import io.github.cottonmc.cotton.gui.client.LibGui;
// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.levelz.compat.InventorioScreenCompatibility;
// import net.levelz.gui.LevelzGui;
// import net.levelz.gui.LevelzScreen;
// import net.levelz.init.KeyInit;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.font.TextRenderer;
// import net.minecraft.client.gui.DrawableHelper;
// import net.minecraft.client.gui.screen.Screen;
// import net.minecraft.client.gui.screen.ingame.InventoryScreen;
// import net.minecraft.client.gui.widget.ButtonWidget;
// import net.minecraft.client.gui.widget.ClickableWidget;
// import net.minecraft.client.gui.widget.TextFieldWidget;
// import net.minecraft.client.render.GameRenderer;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.screen.ScreenTexts;
// import net.minecraft.text.Text;
// import net.minecraft.util.Formatting;
// import net.minecraft.util.math.MathHelper;
// import net.partyaddon.PartyAddonMain;
// import net.partyaddon.access.GroupManagerAccess;
// import net.partyaddon.group.GroupManager;
// import net.partyaddon.init.ConfigInit;
// import net.partyaddon.init.RenderInit;
// import net.partyaddon.network.PartyAddonClientPacket;

// @Environment(EnvType.CLIENT)
// public class PartyScreen extends Screen {

//     private int left;
//     private int top;

//     private PlayerEntity playerEntity;
//     private GroupManager groupManager;

//     private List<Integer> availablePlayers;
//     private WidgetButtonPage[] availablePlayerButtons;
//     int indexStartOffset;
//     private boolean scrolling;
//     private int selectedIndex;
//     private TextFieldWidget searchBox;
//     private WidgetButtonPage acceptDeclineButton;
//     private WidgetButtonPage[] groupPlayerButtons;

//     public PartyScreen() {
//         super(Text.translatable("text.partyaddon.gui.title"));
//     }

//     @Override
//     protected void init() {
//         this.left = this.width / 2 - 100;
//         this.top = this.height / 2 - 108;
//         this.playerEntity = client.player;
//         this.groupManager = ((GroupManagerAccess) playerEntity).getGroupManager();
//         this.availablePlayers = new ArrayList<Integer>();
//         this.availablePlayers.addAll(groupManager.getAvailablePlayerIdList());
//         // this.availablePlayers = groupManager.getAvailablePlayerIdList();
//         // for (int u = 0; u < 22; u++) {
//         // this.availablePlayers.add(u);
//         // }
//         if (this.availablePlayers.size() >= 13)
//             availablePlayerButtons = new WidgetButtonPage[13];
//         else
//             availablePlayerButtons = new WidgetButtonPage[this.availablePlayers.size()];

//         this.acceptDeclineButton = this.addDrawableChild(new WidgetButtonPage(this.left + 109, this.top + 26, -1, button -> {
//             if (groupManager.getInvitationPlayerId() != 0) {
//                 PartyAddonClientPacket.writeC2SAcceptInvitationPacket(groupManager.getInvitationPlayerId());
//                 this.acceptDeclineButton.active = false;
//                 this.acceptDeclineButton.visible = false;
//             }
//         }));
//         this.acceptDeclineButton.visible = groupManager.getInvitationPlayerId() != 0;
//         this.acceptDeclineButton.active = groupManager.getInvitationPlayerId() != 0;
//         if (this.acceptDeclineButton.visible && this.acceptDeclineButton.active) {
//             this.acceptDeclineButton.setStar(this.groupManager.getStarPlayerIdList().contains(groupManager.getInvitationPlayerId()));
//         }

//         this.groupPlayerButtons = new WidgetButtonPage[ConfigInit.CONFIG.groupSize];
//         int o = this.top + 120;

//         for (int i = 0; i < this.groupPlayerButtons.length; i++) {
//             if (i < 7) {// && i < groupManager.getGroupPlayerIdList().size()
//                 this.groupPlayerButtons[i] = this.addDrawableChild(new WidgetButtonPage(this.left + 109, o, -i - 2, button -> {
//                     if (((WidgetButtonPage) button).getIndex() == -2) {
//                         PartyAddonClientPacket.writeC2SLeaveGroupPacket();
//                     } else if (groupManager.isGroupLeader()) {
//                         PartyAddonClientPacket.writeC2SKickPlayerPacket(groupManager.getGroupPlayerIdList().get(Math.abs(((WidgetButtonPage) button).getIndex()) - 2));
//                     }
//                 }));
//                 if (i < groupManager.getGroupPlayerIdList().size()) {
//                     if (!groupManager.getStarPlayerIdList().isEmpty() && groupManager.getStarPlayerIdList().contains(groupManager.getGroupPlayerIdList().get(i)))
//                         this.groupPlayerButtons[i].setStar(true);
//                     if (groupManager.getGroupLeaderId() == groupManager.getGroupPlayerIdList().get(i))
//                         this.groupPlayerButtons[i].setLeader(true);
//                 } else {
//                     // if (i >= groupManager.getGroupPlayerIdList().size()) {
//                     this.groupPlayerButtons[i].active = false;
//                     this.groupPlayerButtons[i].visible = false;
//                 }
//             } else {
//                 this.groupPlayerButtons[i].active = false;
//                 this.groupPlayerButtons[i].visible = false;
//             }
//             o += 13;
//         }
//         // for (int i = 2; i < this.groupPlayerButtons.length + 2; i++) {
//         // if (i < 9) {// && i < groupManager.getGroupPlayerIdList().size()
//         // this.groupPlayerButtons[i] = this.addDrawableChild(new WidgetButtonPage(this.left + 109, o, -i, button -> {
//         // if (groupManager.isGroupLeader()) {
//         // PartyAddonClientPacket.writeC2SKickPlayerPacket(groupManager.getGroupPlayerIdList().get(Math.abs(((WidgetButtonPage) button).getIndex())) - 2, false);
//         // }
//         // }));
//         // if (groupManager.getGroupPlayerIdList().size() > i - 2) {
//         // if (!groupManager.getStarPlayerIdList().isEmpty() && groupManager.getStarPlayerIdList().contains(groupManager.getGroupPlayerIdList().get(i - 2)))
//         // this.groupPlayerButtons[i].setStar(true);
//         // if (groupManager.getGroupLeaderId() == groupManager.getGroupPlayerIdList().get(i - 2))
//         // this.groupPlayerButtons[i].setLeader(true);
//         // } else {
//         // // if (i >= groupManager.getGroupPlayerIdList().size()) {
//         // this.groupPlayerButtons[i].active = false;
//         // this.groupPlayerButtons[i].visible = false;
//         // }
//         // } else {
//         // this.groupPlayerButtons[i].active = false;
//         // this.groupPlayerButtons[i].visible = false;
//         // }
//         // o += 13;
//         // }

//         int k = this.top + 42;
//         for (int l = 0; l < availablePlayerButtons.length; ++l) {
//             this.availablePlayerButtons[l] = this.addDrawableChild(new WidgetButtonPage(this.left + 5, k, l, button -> {
//                 if (button instanceof WidgetButtonPage) {
//                     this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
//                     if (this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)) != null) {

//                         // if (this.rightClicked) {
//                         // System.out.println("PRESSED");
//                         // ((WidgetButtonPage) button).setStar(!((WidgetButtonPage) button).isStar());
//                         // // PartyAddonClientPacket.writeC2SChangeStarListPacket(this.availablePlayers.get(this.selectedIndex));
//                         // } else {
//                         PartyAddonClientPacket.writeC2SInvitePlayerToGroupPacket(this.availablePlayers.get(this.selectedIndex));
//                         button.active = false;
//                         // }
//                     }

//                     // ((WidgetButtonPage) button).setStar(true);
//                     // this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
//                     // if (this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)) != null) {
//                     // // PartyAddonClientPacket.writeC2SInvitePlayerToGroupPacket(this.availablePlayers.get(this.selectedIndex));
//                     // // button.active = false;
//                     // }
//                     // button.active = false;

//                     // PartyAddonClientPacket.writeC2SChangeStarListPacket(this.playerId);

//                     // this.syncRecipeIndex();
//                     // System.out.println(this.selectedIndex + " : " + this.availablePlayers.get(this.selectedIndex));
//                     // if (this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)) != null)
//                     // System.out.println("ID: " + this.availablePlayers.get(this.selectedIndex) + " : " + this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)));
//                 }
//             }));
//             if (!groupManager.getStarPlayerIdList().isEmpty()
//                     && groupManager.getStarPlayerIdList().contains(this.availablePlayers.get(this.availablePlayerButtons[l].getIndex() + this.indexStartOffset)))
//                 this.availablePlayerButtons[l].setStar(true);

//             k += 13;
//         }

//         this.searchBox = new TextFieldWidget(this.textRenderer, this.left + 5, this.top + 25, 93, 15, this.searchBox, Text.translatable("searchPlayer.search"));
//         this.searchBox.setChangedListener(searchString -> {

//             if (searchString.equals("")) {

//                 groupManager.getAvailablePlayerIdList().forEach(id -> {
//                     if (!this.availablePlayers.contains(id))
//                         this.availablePlayers.add(id);
//                 });
//                 // for (int u = 0; u < 22; u++) {
//                 // this.availablePlayers.add(u);
//                 // }
//             } else {
//                 // this.availablePlayers.clear();
//                 // groupManager.getAvailablePlayerIdList().forEach(id -> {

//                 // });
//                 // for (int u = 0; u < 22; u++) {
//                 // if (this.playerEntity.world.getEntityById(u) == null)
//                 // continue;
//                 // if (this.playerEntity.world.getEntityById(u).getName().getString().contains(searchString))
//                 // this.availablePlayers.add(u);
//                 // }
//                 for (int i = 0; i < groupManager.getAvailablePlayerIdList().size(); i++) {
//                     int id = groupManager.getAvailablePlayerIdList().get(i);
//                     if (this.playerEntity.world.getEntityById(id) == null) {
//                         this.availablePlayers.remove((Object) id);
//                         continue;
//                     }

//                     if (this.playerEntity.world.getEntityById(id).getName().getString().contains(searchString)) {
//                         if (!this.availablePlayers.contains(id))
//                             this.availablePlayers.add(id);
//                     } else {
//                         this.availablePlayers.remove((Object) id);
//                     }
//                 }
//             }
//         });
//         this.addSelectableChild(this.searchBox);

//     }

//     @Override
//     public boolean charTyped(char chr, int modifiers) {
//         return this.searchBox.charTyped(chr, modifiers);
//     }

//     @Override
//     public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//         // translucent background
//         renderBackground(matrices);
//         // gui background
//         RenderSystem.setShader(GameRenderer::getPositionTexShader);
//         RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//         RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);

//         this.drawTexture(matrices, this.left, this.top, 0, 0, 200, 216);

//         RenderSystem.setShaderTexture(0, net.levelz.init.RenderInit.GUI_ICONS);
//         if (LibGui.isDarkMode()) {
//             // bag icon
//             this.drawTexture(matrices, this.left, this.top - 21, 120, 110, 24, 25);
//             // skill icon
//             this.drawTexture(matrices, this.left + 25, this.top - 21, 168, 110, 24, 21);
//         } else {
//             // bag icon
//             this.drawTexture(matrices, this.left, this.top - 21, 24, 110, 24, 25);
//             // skill icon
//             this.drawTexture(matrices, this.left + 25, this.top - 21, 48, 110, 24, 21);
//         }

//         RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
//         int xPos = 50;
//         if (PartyAddonMain.isJobsAddonLoaded)
//             xPos = 75;
//         if (LibGui.isDarkMode()) {
//             this.drawTexture(matrices, this.left + xPos, this.top - 23, 72, 0, 24, 27);
//         } else {
//             this.drawTexture(matrices, this.left + xPos, this.top - 23, 24, 0, 24, 27);
//         }
//         if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
//             this.renderTooltip(matrices, Text.translatable("container.inventory"), mouseX, mouseY);
//         }
//         if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY)) {
//             this.renderTooltip(matrices, Text.translatable("screen.levelz.skill_screen"), mouseX, mouseY);
//         }
//         super.render(matrices, mouseX, mouseY, delta);

//         // Top label
//         DrawableHelper.drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.top + 7, 0xFFFFFF);// 0x3F3F3F

//         // Available player list label
//         this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.player_list"), this.left + 5, this.top + 15, 0x3F3F3F);

//         int k = this.top + 45;
//         this.renderScrollbar(matrices, this.left, this.top, this.availablePlayers);
//         int m = 0;
//         if (!this.availablePlayers.isEmpty()) {
//             for (int i = 0; i < this.availablePlayers.size(); i++) {
//                 if (this.canScroll(this.availablePlayers.size()) && (m < this.indexStartOffset || m >= availablePlayerButtons.length + this.indexStartOffset)) {
//                     ++m;
//                     continue;
//                 }
//                 if (this.playerEntity.world.getEntityById(this.availablePlayers.get(i)) == null) {
//                     this.availablePlayers.remove(i);
//                     continue;
//                 }

//                 String playerName = this.playerEntity.world.getEntityById(this.availablePlayers.get(i)).getName().getString();
//                 // String playerName = this.playerEntity.getName().getString();

//                 if (this.textRenderer.getWidth(playerName) > 60)
//                     playerName = playerName.substring(0, 10) + "..";
//                 this.textRenderer.draw(matrices, playerName, this.left + 10, k, 0xFFFFFF);
//                 k += 13;
//                 ++m;
//             }
//         }
//         // for (TradeOffer tradeOffer2 : tradeOfferList) {
//         // if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
//         // ++m;
//         // continue;
//         // }
//         // ItemStack itemStack = tradeOffer2.getOriginalFirstBuyItem();
//         // ItemStack itemStack2 = tradeOffer2.getAdjustedFirstBuyItem();
//         // ItemStack itemStack3 = tradeOffer2.getSecondBuyItem();
//         // ItemStack itemStack4 = tradeOffer2.getSellItem();
//         // this.itemRenderer.zOffset = 100.0f;
//         // int n = k + 2;
//         // this.renderFirstBuyItem(matrices, itemStack2, itemStack, l, n);
//         // if (!itemStack3.isEmpty()) {
//         // this.itemRenderer.renderInGui(itemStack3, i + 5 + 35, n);
//         // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack3, i + 5 + 35, n);
//         // }
//         // this.renderArrow(matrices, tradeOffer2, i, n);
//         // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
//         // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
//         // this.itemRenderer.zOffset = 0.0f;
//         // k += 20;
//         // ++m;
//         // }

//         for (WidgetButtonPage widgetButtonPage : this.availablePlayerButtons) {
//             if (widgetButtonPage.isHovered()) {
//                 widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
//             }
//             widgetButtonPage.visible = widgetButtonPage.index < this.availablePlayers.size();
//             if (widgetButtonPage.visible) {
//                 widgetButtonPage.setStar(groupManager.getStarPlayerIdList().contains(this.availablePlayers.get(widgetButtonPage.getIndex() + this.indexStartOffset)));
//             }
//         }
//         if (this.acceptDeclineButton.active && this.acceptDeclineButton.visible && this.acceptDeclineButton.isHovered()) {
//             this.acceptDeclineButton.renderTooltip(matrices, mouseX, mouseY);
//         }

//         // Invitation label
//         this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.invitation"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.invitation")),
//                 this.top + 16, 0x3F3F3F);

//         this.acceptDeclineButton.visible = groupManager.getInvitationPlayerId() != 0;
//         this.acceptDeclineButton.active = groupManager.getInvitationPlayerId() != 0;
//         if (this.acceptDeclineButton.visible && this.acceptDeclineButton.active) {
//             this.acceptDeclineButton.setStar(this.groupManager.getStarPlayerIdList().contains(groupManager.getInvitationPlayerId()));

//             if (this.playerEntity.world.getEntityById(groupManager.getInvitationPlayerId()) != null) {
//                 String playerName = this.playerEntity.world.getEntityById(groupManager.getInvitationPlayerId()).getName().getString();
//                 if (this.textRenderer.getWidth(playerName) > 60)
//                     playerName = playerName.substring(0, 10) + "..";
//                 this.textRenderer.draw(matrices, playerName, this.left + 114, this.top + 29, 0xFFFFFF);
//             }
//         }
//         // Info label
//         this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.info"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.info")), this.top + 43,
//                 0x3F3F3F);

//         if (!this.groupManager.getGroupPlayerIdList().isEmpty()) {
//             this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.info_player_count", this.groupManager.getGroupPlayerIdList().size()), this.left + 109, this.top + 55, 0x3F3F3F);

//             this.textRenderer.draw(matrices,
//                     Text.translatable("text.partyaddon.gui.info_experience", Math.round(ConfigInit.CONFIG.groupXpBonus * 100 * this.groupManager.getGroupPlayerIdList().size())), this.left + 109,
//                     this.top + 67, 0x3F3F3F);
//             if (this.groupManager.getGroupPlayerIdList().size() == ConfigInit.CONFIG.groupSize) {
//                 this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.full_group_bonus").formatted(Formatting.DARK_GREEN), this.left + 109, this.top + 83, 0x3F3F3F);
//                 this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.full_group_bonus_amount", Math.round(ConfigInit.CONFIG.fullGroupBonus * 100)).formatted(Formatting.DARK_GREEN),
//                         this.left + 109, this.top + 95, 0x3F3F3F);
//             }
//         } else {
//             this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.empty_info"), this.left + 109, this.top + 55, 0x3F3F3F);
//         }

//         // Group label
//         this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.group"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.group")), this.top + 110,
//                 0x3F3F3F);
//         // if (!this.groupManager.getGroupPlayerIdList().isEmpty()) {
//         int g = 0;
//         for (int i = 0; i < ConfigInit.CONFIG.groupSize; i++) {
//             // if (i < ConfigInit.CONFIG.groupSize) {
//             // if (this.groupPlayerButtons[i].visible) {
//             if (i < this.groupManager.getGroupPlayerIdList().size() && this.playerEntity.world.getEntityById(this.groupManager.getGroupPlayerIdList().get(i)) != null) {
//                 int playerId = this.groupManager.getGroupPlayerIdList().get(i);

//                 this.groupPlayerButtons[i].active = true;
//                 this.groupPlayerButtons[i].visible = true;

//                 String playerName = this.playerEntity.world.getEntityById(playerId).getName().getString();
//                 if (this.textRenderer.getWidth(playerName) > 60)
//                     playerName = playerName.substring(0, 9) + "..";
//                 this.textRenderer.draw(matrices, playerName, this.left + 114, this.top + 123 + g, 0xFFFFFF);

//                 if (i != 0) {

//                     // System.out.println(this.groupManager.getStarPlayerIdList().contains(playerId) + " : " + playerId + " : " + this.groupManager.getStarPlayerIdList());

//                     this.groupPlayerButtons[i].setStar(this.groupManager.getStarPlayerIdList().contains(playerId));
//                 }
//                 // if (this.groupManager.getStarPlayerIdList().contains(playerId)) {
//                 // this.groupPlayerButtons[i].setStar(true);
//                 // } else {
//                 // this.groupPlayerButtons[i].setStar(false);
//                 // }
//                 if (i != 0 && this.groupManager.isGroupLeader()) {
//                     RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
//                     RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//                     this.drawTexture(matrices, this.left + 176, this.top + 123 + g, 21, 32, 7, 7);
//                 }
//                 if (this.groupPlayerButtons[i].isLeader()) {
//                     if (this.groupManager.getGroupLeaderId() != playerId) {
//                         this.groupPlayerButtons[i].setLeader(false);
//                     }
//                 } else {
//                     if (this.groupManager.getGroupLeaderId() == playerId) {
//                         this.groupPlayerButtons[i].setLeader(true);
//                     }
//                 }
//                 g += 13;

//             } else {
//                 this.groupPlayerButtons[i].active = false;
//                 this.groupPlayerButtons[i].visible = false;
//             }
//             // }
//             // }
//             // }
//         }
//         this.searchBox.render(matrices, mouseX, mouseY, delta);
//     }

//     @Override
//     public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//         if (this.searchBox.keyPressed(keyCode, scanCode, modifiers) || (this.searchBox.isActive() && keyCode != GLFW.GLFW_KEY_ESCAPE))
//             return true;
//         if (KeyInit.screenKey.matchesKey(keyCode, scanCode) || Objects.requireNonNull(client).options.inventoryKey.matchesKey(keyCode, scanCode)) {
//             this.close();
//             return true;
//         } else
//             return super.keyPressed(keyCode, scanCode, modifiers);
//     }

//     @Override
//     public boolean shouldPause() {
//         return false;
//     }

//     @Override
//     public boolean mouseClicked(double mouseX, double mouseY, int button) {
//         if (this.client != null) {
//             if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
//                 assert this.client.player != null;
//                 if (net.levelz.init.RenderInit.isInventorioLoaded)
//                     InventorioScreenCompatibility.setInventorioScreen(client);
//                 else
//                     this.client.setScreen(new InventoryScreen(this.client.player));
//             } else if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY))
//                 this.client.setScreen(new LevelzScreen(new LevelzGui(client)));
//         }

//         this.scrolling = false;
//         int i = (this.width - 200) / 2;
//         int j = (this.height - 216) / 2;
//         if (this.canScroll(this.availablePlayers.size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
//             this.scrolling = true;
//         }
//         return super.mouseClicked(mouseX, mouseY, button);
//     }

//     private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
//         int i = (this.width - 200) / 2;
//         int j = (this.height - 216) / 2;
//         return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
//     }

//     private boolean canScroll(int listSize) {
//         return listSize > availablePlayerButtons.length;
//     }

//     @Override
//     public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
//         int i = this.availablePlayers.size();
//         if (this.canScroll(i)) {
//             int j = i - availablePlayerButtons.length;
//             this.indexStartOffset = MathHelper.clamp((int) ((double) this.indexStartOffset - amount), 0, j);
//         }
//         return true;
//     }

//     @Override
//     public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//         int i = this.availablePlayers.size();
//         if (this.scrolling) {
//             int j = this.top + 18;
//             int k = j + 139;
//             int l = i - availablePlayerButtons.length;
//             float f = ((float) mouseY - (float) j - 13.5f) / ((float) (k - j) - 27.0f);
//             f = f * (float) l + 0.5f;
//             this.indexStartOffset = MathHelper.clamp((int) f, 0, l);
//             return true;
//         }
//         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
//     }

//     private void renderScrollbar(MatrixStack matrices, int x, int y, List<Integer> availablePlayers) {
//         RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
//         int i = availablePlayers.size() + 1 - availablePlayerButtons.length;
//         if (i > 1) {
//             int j = 167 - (27 + (i - 1) * 167 / i);
//             int k = 1 + j / i + 167 / i;
//             int m = Math.min(142, this.indexStartOffset * k);
//             if (this.indexStartOffset == i - 1) {
//                 m = 142;
//             }
//             this.drawTexture(matrices, x + 92, y + 42 + m, 200, 0, 6, 27);
//         } else {
//             this.drawTexture(matrices, x + 92, y + 42, 206, 0, 6, 27);
//         }
//     }

//     class WidgetButtonPage extends ButtonWidget {
//         final int index;
//         private boolean star = false;
//         private boolean leader = false;

//         public WidgetButtonPage(int x, int y, int index, ButtonWidget.PressAction onPress) {
//             super(x, y, 86, 13, ScreenTexts.EMPTY, onPress);
//             this.index = index;
//             this.visible = false;
//         }

//         public boolean isStar() {
//             return this.star;
//         }

//         public void setStar(boolean star) {
//             this.star = star;
//         }

//         public int getIndex() {
//             return this.index;
//         }

//         public void setLeader(boolean leader) {
//             this.leader = leader;
//         }

//         public boolean isLeader() {
//             return this.leader;
//         }

//         @Override
//         public boolean mouseClicked(double mouseX, double mouseY, int button) {
//             if (this.visible && this.active && button == 1 && this.isHovered()) {
//                 if (this.getIndex() == -1) {
//                     if (PartyScreen.this.groupManager.getInvitationPlayerId() != 0) {
//                         PartyAddonClientPacket.writeC2SDeclineInvitationPacket(PartyScreen.this.groupManager.getInvitationPlayerId());
//                     }
//                     this.active = false;
//                 } else if (this.getIndex() != -2) {
//                     int playerId = 0;
//                     if (this.getIndex() < -1) {
//                         int index = Math.abs(this.index) - 2;
//                         if (!PartyScreen.this.groupManager.getGroupPlayerIdList().isEmpty() && PartyScreen.this.groupManager.getGroupPlayerIdList().size() >= index) {
//                             playerId = PartyScreen.this.groupManager.getGroupPlayerIdList().get(index);
//                         }
//                     } else {
//                         int index = this.index + PartyScreen.this.indexStartOffset;
//                         if (!PartyScreen.this.availablePlayers.isEmpty() && PartyScreen.this.availablePlayers.size() >= index) {
//                             playerId = PartyScreen.this.availablePlayers.get(index);
//                         }
//                     }

//                     // if (this.isStar()) {
//                     // PartyScreen.this.groupManager.removePlayerStar(playerId);
//                     // } else {
//                     // PartyScreen.this.groupManager.addPlayerStar(playerId);
//                     // }

//                     // PartyScreen.this.groupManager.removePlayerStar(playerId);
//                     this.setStar(!this.isStar());
//                     PartyAddonClientPacket.writeC2SChangeStarListPacket(playerId);
//                 }

//                 // if (this.getIndex() < -1) {
//                 // // groupManager.getStarPlayerIdList()
//                 // // Remove from list here
//                 // if (this.getIndex() != -2) {
//                 // this.setStar(!this.isStar());
//                 // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.groupManager.getGroupPlayerIdList().get(Math.abs(this.index) - 2));
//                 // }
//                 // }

//                 // this.setStar(!this.isStar());
//                 // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.availablePlayers.get(this.index + PartyScreen.this.indexStartOffset));

//                 // // else if (this.getIndex() == -1) {
//                 // // PartyAddonClientPacket.writeC2SDeclineInvitationPacket(PartyScreen.this.groupManager.getInvitationPlayerId());
//                 // // } else {
//                 // this.setStar(!this.isStar());
//                 // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.availablePlayers.get(this.index + PartyScreen.this.indexStartOffset));
//                 // // }
//                 return true;
//             }

//             return super.mouseClicked(mouseX, mouseY, button);
//         }

//         @Override
//         public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//             MinecraftClient minecraftClient = MinecraftClient.getInstance();
//             TextRenderer textRenderer = minecraftClient.textRenderer;
//             RenderSystem.setShader(GameRenderer::getPositionTexShader);
//             RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
//             RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
//             int i = this.getYImage(this.isHovered());
//             RenderSystem.enableBlend();
//             RenderSystem.defaultBlendFunc();
//             RenderSystem.enableDepthTest();
//             this.drawTexture(matrices, this.x, this.y, star ? 86 : 0, 216 + i * 13, this.width, this.height);
//             if (this.leader) {
//                 RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
//                 RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
//                 RenderSystem.enableBlend();

//                 this.drawTexture(matrices, this.x + 67, this.y + 3, 0, 32, 7, 7);
//             }

//             this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
//             int j = this.active ? 0xFFFFFF : 0xA0A0A0;
//             ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);

//             if (this.isHovered()) {
//                 this.renderTooltip(matrices, mouseX, mouseY);
//             }
//         }

//         @Override
//         public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
//             if (this.hovered && this.active) {

//                 int playerId = 0;
//                 if (this.getIndex() == -1) {
//                     if (PartyScreen.this.playerEntity.world.getEntityById(PartyScreen.this.groupManager.getInvitationPlayerId()) != null) {
//                         playerId = PartyScreen.this.groupManager.getInvitationPlayerId();
//                     }
//                 } else {
//                     if (this.getIndex() < -1) {
//                         int index = Math.abs(this.index) - 2;
//                         if (!PartyScreen.this.groupManager.getGroupPlayerIdList().isEmpty() && PartyScreen.this.groupManager.getGroupPlayerIdList().size() >= index) {
//                             playerId = PartyScreen.this.groupManager.getGroupPlayerIdList().get(index);
//                         }
//                     } else {
//                         int index = this.index + PartyScreen.this.indexStartOffset;
//                         if (!PartyScreen.this.availablePlayers.isEmpty() && PartyScreen.this.availablePlayers.size() >= index) {
//                             playerId = PartyScreen.this.availablePlayers.get(index);
//                         }
//                     }
//                 }

//                 if (playerId != 0) {
//                     if (PartyScreen.this.playerEntity.world.getEntityById(playerId) != null) {
//                         String playerName = "";
//                         playerName = PartyScreen.this.playerEntity.world.getEntityById(playerId).getName().getString();
//                         if (PartyScreen.this.textRenderer.getWidth(playerName) > 60) {
//                             PartyScreen.this.renderTooltip(matrices, Text.of(playerName), mouseX, mouseY);
//                         }
//                     }
//                 }
//             }
//         }
//     }

// }

package net.partyaddon.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.glfw.GLFW;

import io.github.cottonmc.cotton.gui.client.LibGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.levelz.compat.InventorioScreenCompatibility;
import net.levelz.init.KeyInit;
import net.libz.api.Tab;
import net.libz.util.DrawTabHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.partyaddon.PartyAddonMain;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.group.GroupManager;
import net.partyaddon.init.ConfigInit;
import net.partyaddon.init.RenderInit;
import net.partyaddon.network.PartyAddonClientPacket;
import net.partyaddon.util.NameHelper;

@Environment(EnvType.CLIENT)
public class PartyScreen extends Screen implements Tab {

    public static final Text title = Text.translatable("text.partyaddon.gui.title");

    private int left;
    private int top;

    private PlayerEntity playerEntity;
    private GroupManager groupManager;

    private List<UUID> availablePlayers;
    private WidgetButtonPage[] availablePlayerButtons;
    int indexStartOffset;
    private boolean scrolling;
    private int selectedIndex;
    private TextFieldWidget searchBox;
    private WidgetButtonPage acceptDeclineButton;
    private WidgetButtonPage[] groupPlayerButtons;

    public PartyScreen() {
        super(title);
    }

    @Override
    protected void init() {
        this.left = this.width / 2 - 100;
        this.top = this.height / 2 - 108;
        this.playerEntity = client.player;
        this.groupManager = ((GroupManagerAccess) playerEntity).getGroupManager();
        this.availablePlayers = new ArrayList<UUID>();
        this.availablePlayers.addAll(groupManager.getAvailablePlayerIdList());

        // // TEST TEST TEST TEST
        // this.availablePlayers.add(playerEntity.getUuid());
        // this.availablePlayers.add(playerEntity.getUuid());
        // this.availablePlayers.add(playerEntity.getUuid());
        // this.availablePlayers.add(playerEntity.getUuid());

        // this.client.getNetworkHandler().getpla
        // ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        // System.out.println("INIT: " + this.availablePlayers);
        // this.availablePlayers = groupManager.getAvailablePlayerIdList();
        // for (int u = 0; u < 22; u++) {
        // this.availablePlayers.add(u);
        // }
        if (this.availablePlayers.size() >= 13) {
            this.availablePlayerButtons = new WidgetButtonPage[13];
        } else {
            this.availablePlayerButtons = new WidgetButtonPage[this.availablePlayers.size()];
        }
        this.acceptDeclineButton = this.addDrawableChild(new WidgetButtonPage(this.left + 109, this.top + 26, -1, button -> {
            if (this.groupManager.getInvitationPlayerId() != null) {
                PartyAddonClientPacket.writeC2SAcceptInvitationPacket(this.groupManager.getInvitationPlayerId());
                this.acceptDeclineButton.active = false;
                this.acceptDeclineButton.visible = false;
            }
        }));
        boolean acceptDeclineButton = this.groupManager.getInvitationPlayerId() != null && !this.groupManager.getGroupPlayerIdList().contains(this.groupManager.getInvitationPlayerId());
        this.acceptDeclineButton.visible = acceptDeclineButton;
        this.acceptDeclineButton.active = acceptDeclineButton;
        if (acceptDeclineButton) {
            this.acceptDeclineButton.setStar(this.groupManager.getStarPlayerIdList().contains(this.groupManager.getInvitationPlayerId()));
        }

        this.groupPlayerButtons = new WidgetButtonPage[ConfigInit.CONFIG.groupSize];
        int o = this.top + 120;

        for (int i = 0; i < this.groupPlayerButtons.length; i++) {
            // if (i < 7) {// && i < groupManager.getGroupPlayerIdList().size()
            this.groupPlayerButtons[i] = this.addDrawableChild(new WidgetButtonPage(this.left + 109, o, -i - 2, button -> {
                if (((WidgetButtonPage) button).getIndex() == -2) {
                    PartyAddonClientPacket.writeC2SLeaveGroupPacket();
                } else if (this.groupManager.isGroupLeader() && this.playerEntity != null) {
                    PartyAddonClientPacket.writeC2SKickPlayerPacket(this.playerEntity.getUuid(), this.groupManager.getGroupPlayerIdList().get(Math.abs(((WidgetButtonPage) button).getIndex()) - 2));
                }
            }));
            if (i < this.groupManager.getGroupPlayerIdList().size()) {
                if (!this.groupManager.getStarPlayerIdList().isEmpty() && this.groupManager.getStarPlayerIdList().contains(this.groupManager.getGroupPlayerIdList().get(i))) {
                    this.groupPlayerButtons[i].setStar(true);
                }
                if (this.groupManager.getGroupLeaderId().equals(this.groupManager.getGroupPlayerIdList().get(i))) {
                    this.groupPlayerButtons[i].setLeader(true);
                }
            } else {
                // if (i >= groupManager.getGroupPlayerIdList().size()) {
                this.groupPlayerButtons[i].active = false;
                this.groupPlayerButtons[i].visible = false;
            }
            // } else {
            if (i >= 7) {
                this.groupPlayerButtons[i].active = false;
                this.groupPlayerButtons[i].visible = false;
            }
            // }
            o += 13;
        }
        // for (int i = 2; i < this.groupPlayerButtons.length + 2; i++) {
        // if (i < 9) {// && i < groupManager.getGroupPlayerIdList().size()
        // this.groupPlayerButtons[i] = this.addDrawableChild(new WidgetButtonPage(this.left + 109, o, -i, button -> {
        // if (groupManager.isGroupLeader()) {
        // PartyAddonClientPacket.writeC2SKickPlayerPacket(groupManager.getGroupPlayerIdList().get(Math.abs(((WidgetButtonPage) button).getIndex())) - 2, false);
        // }
        // }));
        // if (groupManager.getGroupPlayerIdList().size() > i - 2) {
        // if (!groupManager.getStarPlayerIdList().isEmpty() && groupManager.getStarPlayerIdList().contains(groupManager.getGroupPlayerIdList().get(i - 2)))
        // this.groupPlayerButtons[i].setStar(true);
        // if (groupManager.getGroupLeaderId() == groupManager.getGroupPlayerIdList().get(i - 2))
        // this.groupPlayerButtons[i].setLeader(true);
        // } else {
        // // if (i >= groupManager.getGroupPlayerIdList().size()) {
        // this.groupPlayerButtons[i].active = false;
        // this.groupPlayerButtons[i].visible = false;
        // }
        // } else {
        // this.groupPlayerButtons[i].active = false;
        // this.groupPlayerButtons[i].visible = false;
        // }
        // o += 13;
        // }

        int k = this.top + 42;
        for (int l = 0; l < this.availablePlayerButtons.length; ++l) {
            this.availablePlayerButtons[l] = this.addDrawableChild(new WidgetButtonPage(this.left + 5, k, l, button -> {
                this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
                if (NameHelper.isPlayerUUIDNotNull(client, this.availablePlayers.get(this.selectedIndex))) {

                    // if (this.rightClicked) {
                    // System.out.println("PRESSED");
                    // ((WidgetButtonPage) button).setStar(!((WidgetButtonPage) button).isStar());
                    // // PartyAddonClientPacket.writeC2SChangeStarListPacket(this.availablePlayers.get(this.selectedIndex));
                    // } else {
                    PartyAddonClientPacket.writeC2SInvitePlayerToGroupPacket(this.availablePlayers.get(this.selectedIndex));
                    button.active = false;
                    // }
                }

                // ((WidgetButtonPage) button).setStar(true);
                // this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
                // if (this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)) != null) {
                // // PartyAddonClientPacket.writeC2SInvitePlayerToGroupPacket(this.availablePlayers.get(this.selectedIndex));
                // // button.active = false;
                // }
                // button.active = false;

                // PartyAddonClientPacket.writeC2SChangeStarListPacket(this.playerId);

                // this.syncRecipeIndex();
                // System.out.println(this.selectedIndex + " : " + this.availablePlayers.get(this.selectedIndex));
                // if (this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)) != null)
                // System.out.println("ID: " + this.availablePlayers.get(this.selectedIndex) + " : " + this.playerEntity.world.getEntityById(this.availablePlayers.get(this.selectedIndex)));

            }));
            if (!groupManager.getStarPlayerIdList().isEmpty()
                    && groupManager.getStarPlayerIdList().contains(this.availablePlayers.get(this.availablePlayerButtons[l].getIndex() + this.indexStartOffset))) {
                this.availablePlayerButtons[l].setStar(true);
            }

            k += 13;
        }

        this.searchBox = new TextFieldWidget(this.textRenderer, this.left + 5, this.top + 25, 93, 15, this.searchBox, Text.translatable("searchPlayer.search"));
        this.searchBox.setChangedListener(searchString -> {

            if (searchString.equals("")) {

                groupManager.getAvailablePlayerIdList().forEach(id -> {
                    if (!this.availablePlayers.contains(id)) {
                        this.availablePlayers.add(id);
                    }
                });
                // for (int u = 0; u < 22; u++) {
                // this.availablePlayers.add(u);
                // }
            } else {
                // this.availablePlayers.clear();
                // groupManager.getAvailablePlayerIdList().forEach(id -> {

                // });
                // for (int u = 0; u < 22; u++) {
                // if (this.playerEntity.world.getEntityById(u) == null)
                // continue;
                // if (this.playerEntity.world.getEntityById(u).getName().getString().contains(searchString))
                // this.availablePlayers.add(u);
                // }
                for (int i = 0; i < groupManager.getAvailablePlayerIdList().size(); i++) {
                    UUID id = groupManager.getAvailablePlayerIdList().get(i);
                    if (!NameHelper.isPlayerUUIDNotNull(client, id)) {
                        this.availablePlayers.remove((Object) id);
                        continue;
                    }
                    if (NameHelper.getPlayerName(client, id, 0).getString().contains(searchString)) {
                        if (!this.availablePlayers.contains(id))
                            this.availablePlayers.add(id);
                    } else {
                        this.availablePlayers.remove((Object) id);
                    }
                }
            }
        });
        this.addSelectableChild(this.searchBox);

    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.searchBox.charTyped(chr, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // translucent background
        renderBackground(matrices);
        // gui background
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);

        this.drawTexture(matrices, this.left, this.top, 0, 0, 200, 216);

        RenderSystem.setShaderTexture(0, net.levelz.init.RenderInit.GUI_ICONS);
        // if (LibGui.isDarkMode()) {
        // // bag icon
        // this.drawTexture(matrices, this.left, this.top - 21, 120, 110, 24, 25);
        // // skill icon
        // this.drawTexture(matrices, this.left + 25, this.top - 21, 168, 110, 24, 21);
        // } else {
        // // bag icon
        // this.drawTexture(matrices, this.left, this.top - 21, 24, 110, 24, 25);
        // // skill icon
        // this.drawTexture(matrices, this.left + 25, this.top - 21, 48, 110, 24, 21);
        // }

        // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
        // int xPos = 50;
        // if (PartyAddonMain.isJobsAddonLoaded)
        // xPos = 75;
        // if (LibGui.isDarkMode()) {
        // this.drawTexture(matrices, this.left + xPos, this.top - 23, 72, 0, 24, 27);
        // } else {
        // this.drawTexture(matrices, this.left + xPos, this.top - 23, 24, 0, 24, 27);
        // }
        // if (this.isPointWithinBounds(0, -21, 23, 21, (double) mouseX, (double) mouseY)) {
        // this.renderTooltip(matrices, Text.translatable("container.inventory"), mouseX, mouseY);
        // }
        // if (this.isPointWithinBounds(25, -21, 23, 21, (double) mouseX, (double) mouseY)) {
        // this.renderTooltip(matrices, Text.translatable("screen.levelz.skill_screen"), mouseX, mouseY);
        // }
        super.render(matrices, mouseX, mouseY, delta);

        // Top label
        DrawableHelper.drawCenteredText(matrices, textRenderer, PartyScreen.title, this.width / 2, this.top + 7, 0xFFFFFF);// 0x3F3F3F

        // Available player list label
        this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.player_list"), this.left + 5, this.top + 15, 0x3F3F3F);

        int k = this.top + 45;
        this.renderScrollbar(matrices, this.left, this.top, this.availablePlayers);
        int m = 0;
        if (!this.availablePlayers.isEmpty()) {
            for (int i = 0; i < this.availablePlayers.size(); i++) {
                if (this.canScroll(this.availablePlayers.size()) && (m < this.indexStartOffset || m >= availablePlayerButtons.length + this.indexStartOffset)) {
                    ++m;
                    continue;
                }
                if (!NameHelper.isPlayerUUIDNotNull(client, this.availablePlayers.get(i))) {
                    this.availablePlayers.remove(i);
                    continue;
                }
                // if(this.availablePlayers.get(i).equals(this.groupManager.)){
                // this.availablePlayers.remove(i);
                // }

                String playerName = NameHelper.getPlayerName(client, this.availablePlayers.get(i), 60).getString();
                this.textRenderer.draw(matrices, playerName, this.left + 10, k, 0xFFFFFF);
                k += 13;
                ++m;
            }
        }
        // for (TradeOffer tradeOffer2 : tradeOfferList) {
        // if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
        // ++m;
        // continue;
        // }
        // ItemStack itemStack = tradeOffer2.getOriginalFirstBuyItem();
        // ItemStack itemStack2 = tradeOffer2.getAdjustedFirstBuyItem();
        // ItemStack itemStack3 = tradeOffer2.getSecondBuyItem();
        // ItemStack itemStack4 = tradeOffer2.getSellItem();
        // this.itemRenderer.zOffset = 100.0f;
        // int n = k + 2;
        // this.renderFirstBuyItem(matrices, itemStack2, itemStack, l, n);
        // if (!itemStack3.isEmpty()) {
        // this.itemRenderer.renderInGui(itemStack3, i + 5 + 35, n);
        // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack3, i + 5 + 35, n);
        // }
        // this.renderArrow(matrices, tradeOffer2, i, n);
        // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
        // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
        // this.itemRenderer.zOffset = 0.0f;
        // k += 20;
        // ++m;
        // }

        for (WidgetButtonPage widgetButtonPage : this.availablePlayerButtons) {
            if (widgetButtonPage.isHovered()) {
                widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
            }
            widgetButtonPage.visible = widgetButtonPage.index < this.availablePlayers.size();
            if (widgetButtonPage.visible) {
                widgetButtonPage.setStar(groupManager.getStarPlayerIdList().contains(this.availablePlayers.get(widgetButtonPage.getIndex() + this.indexStartOffset)));
            }
        }

        if (this.acceptDeclineButton.active && this.acceptDeclineButton.visible && this.acceptDeclineButton.isHovered()) {
            this.acceptDeclineButton.renderTooltip(matrices, mouseX, mouseY);
        }

        // Invitation label
        this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.invitation"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.invitation")),
                this.top + 16, 0x3F3F3F);

        boolean acceptDeclineButton = this.groupManager.getInvitationPlayerId() != null && !this.groupManager.getGroupPlayerIdList().contains(this.groupManager.getInvitationPlayerId());
        this.acceptDeclineButton.visible = acceptDeclineButton;
        this.acceptDeclineButton.active = acceptDeclineButton;
        if (acceptDeclineButton) {
            this.acceptDeclineButton.setStar(this.groupManager.getStarPlayerIdList().contains(groupManager.getInvitationPlayerId()));

            if (NameHelper.isPlayerUUIDNotNull(client, groupManager.getInvitationPlayerId())) {
                String playerName = NameHelper.getPlayerName(client, groupManager.getInvitationPlayerId(), 60).getString();
                this.textRenderer.draw(matrices, playerName, this.left + 114, this.top + 29, 0xFFFFFF);
            }
        }
        // Info label
        this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.info"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.info")), this.top + 43,
                0x3F3F3F);

        if (!this.groupManager.getGroupPlayerIdList().isEmpty()) {
            this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.info_player_count", this.groupManager.getGroupPlayerIdList().size()), this.left + 109, this.top + 55, 0x3F3F3F);
            if (ConfigInit.CONFIG.groupXpBonus > 0.0001f) {
                this.textRenderer.draw(matrices,
                        Text.translatable("text.partyaddon.gui.info_experience", Math.round(ConfigInit.CONFIG.groupXpBonus * 100 * this.groupManager.getGroupPlayerIdList().size())), this.left + 109,
                        this.top + 67, 0x3F3F3F);
            }
            if (this.groupManager.getGroupPlayerIdList().size() == ConfigInit.CONFIG.groupSize && ConfigInit.CONFIG.fullGroupBonus >= 0.0001f) {
                this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.full_group_bonus").formatted(Formatting.DARK_GREEN), this.left + 109,
                        this.top + 83 - (ConfigInit.CONFIG.groupXpBonus > 0.0001f ? 0 : 16), 0x3F3F3F);
                this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.full_group_bonus_amount", Math.round(ConfigInit.CONFIG.fullGroupBonus * 100)).formatted(Formatting.DARK_GREEN),
                        this.left + 109, this.top + 95, 0x3F3F3F);
            }
        } else {
            this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.empty_info"), this.left + 109, this.top + 55, 0x3F3F3F);
        }

        // Group label
        this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.group"), this.left + 195 - this.textRenderer.getWidth(Text.translatable("text.partyaddon.gui.group")), this.top + 110,
                0x3F3F3F);
        // if (!this.groupManager.getGroupPlayerIdList().isEmpty()) {
        int g = 0;
        for (int i = 0; i < ConfigInit.CONFIG.groupSize; i++) {
            // if (i < ConfigInit.CONFIG.groupSize) {
            // if (this.groupPlayerButtons[i].visible) {
            if (i < this.groupManager.getGroupPlayerIdList().size() && NameHelper.isPlayerUUIDNotNull(client, this.groupManager.getGroupPlayerIdList().get(i))) {
                UUID playerId = this.groupManager.getGroupPlayerIdList().get(i);

                this.groupPlayerButtons[i].active = true;
                this.groupPlayerButtons[i].visible = true;

                String playerName = NameHelper.getPlayerName(client, playerId, 60).getString();
                this.textRenderer.draw(matrices, playerName, this.left + 114, this.top + 123 + g, 0xFFFFFF);

                if (i != 0) {

                    // System.out.println(this.groupManager.getStarPlayerIdList().contains(playerId) + " : " + playerId + " : " + this.groupManager.getStarPlayerIdList());

                    this.groupPlayerButtons[i].setStar(this.groupManager.getStarPlayerIdList().contains(playerId));
                }
                // if (this.groupManager.getStarPlayerIdList().contains(playerId)) {
                // this.groupPlayerButtons[i].setStar(true);
                // } else {
                // this.groupPlayerButtons[i].setStar(false);
                // }
                if (i != 0 && this.groupManager.isGroupLeader()) {
                    RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawTexture(matrices, this.left + 176, this.top + 123 + g, 21, 32, 7, 7);
                }
                if (this.groupPlayerButtons[i].isLeader()) {
                    if (!this.groupManager.getGroupLeaderId().equals(playerId)) {
                        this.groupPlayerButtons[i].setLeader(false);
                    }
                } else {
                    if (this.groupManager.getGroupLeaderId().equals(playerId)) {
                        this.groupPlayerButtons[i].setLeader(true);
                    }
                }
                g += 13;

            } else {
                this.groupPlayerButtons[i].active = false;
                this.groupPlayerButtons[i].visible = false;
            }
            // }
            // }
            // }
        }
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        DrawTabHelper.drawTab(client, matrices, this, this.left, this.top, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers) || (this.searchBox.isActive() && keyCode != GLFW.GLFW_KEY_ESCAPE))
            return true;
        if (KeyInit.screenKey.matchesKey(keyCode, scanCode) || Objects.requireNonNull(client).options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        } else
            return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.client != null) {
            DrawTabHelper.onTabButtonClick(client, this, this.left, this.top, mouseX, mouseY, this.getFocused() != null);
        }

        this.scrolling = false;
        int i = (this.width - 200) / 2;
        int j = (this.height - 216) / 2;
        if (this.canScroll(this.availablePlayers.size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
            this.scrolling = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        int i = (this.width - 200) / 2;
        int j = (this.height - 216) / 2;
        return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
    }

    private boolean canScroll(int listSize) {
        return listSize > availablePlayerButtons.length;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = this.availablePlayers.size();
        if (this.canScroll(i)) {
            int j = i - availablePlayerButtons.length;
            this.indexStartOffset = MathHelper.clamp((int) ((double) this.indexStartOffset - amount), 0, j);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int i = this.availablePlayers.size();
        if (this.scrolling) {
            int j = this.top + 18;
            int k = j + 139;
            int l = i - availablePlayerButtons.length;
            float f = ((float) mouseY - (float) j - 13.5f) / ((float) (k - j) - 27.0f);
            f = f * (float) l + 0.5f;
            this.indexStartOffset = MathHelper.clamp((int) f, 0, l);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void renderScrollbar(MatrixStack matrices, int x, int y, List<UUID> availablePlayers) {
        RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
        int i = availablePlayers.size() + 1 - availablePlayerButtons.length;
        if (i > 1) {
            int j = 167 - (27 + (i - 1) * 167 / i);
            int k = 1 + j / i + 167 / i;
            int m = Math.min(142, this.indexStartOffset * k);
            if (this.indexStartOffset == i - 1) {
                m = 142;
            }
            this.drawTexture(matrices, x + 92, y + 42 + m, 200, 0, 6, 27);
        } else {
            this.drawTexture(matrices, x + 92, y + 42, 206, 0, 6, 27);
        }
    }

    class WidgetButtonPage extends ButtonWidget {
        private final int index;
        private boolean star = false;
        private boolean leader = false;

        public WidgetButtonPage(int x, int y, int index, ButtonWidget.PressAction onPress) {
            super(x, y, 86, 13, ScreenTexts.EMPTY, onPress);
            this.index = index;
            this.visible = false;
        }

        public boolean isStar() {
            return this.star;
        }

        public void setStar(boolean star) {
            this.star = star;
        }

        public int getIndex() {
            return this.index;
        }

        public void setLeader(boolean leader) {
            this.leader = leader;
        }

        public boolean isLeader() {
            return this.leader;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.visible && this.active && button == 1 && this.isHovered()) {
                if (this.getIndex() == -1) {
                    if (PartyScreen.this.groupManager.getInvitationPlayerId() != null) {
                        PartyAddonClientPacket.writeC2SDeclineInvitationPacket(PartyScreen.this.groupManager.getInvitationPlayerId());
                    }
                    this.active = false;
                } else if (this.getIndex() != -2) {
                    UUID playerId = null;

                    if (this.getIndex() < -1) {
                        int index = Math.abs(this.getIndex()) - 2;
                        if (!PartyScreen.this.groupManager.getGroupPlayerIdList().isEmpty() && PartyScreen.this.groupManager.getGroupPlayerIdList().size() >= index) {
                            playerId = PartyScreen.this.groupManager.getGroupPlayerIdList().get(index);
                        }
                    } else {
                        int index = this.getIndex() + PartyScreen.this.indexStartOffset;
                        if (!PartyScreen.this.availablePlayers.isEmpty() && PartyScreen.this.availablePlayers.size() >= index) {
                            playerId = PartyScreen.this.availablePlayers.get(index);
                        }
                    }
                    // System.out.println(playerId);
                    // if (this.isStar()) {
                    // PartyScreen.this.groupManager.removePlayerStar(playerId);
                    // } else {
                    // PartyScreen.this.groupManager.addPlayerStar(playerId);
                    // }

                    // PartyScreen.this.groupManager.removePlayerStar(playerId);
                    this.setStar(!this.isStar());
                    PartyAddonClientPacket.writeC2SChangeStarListPacket(playerId);
                }

                // if (this.getIndex() < -1) {
                // // groupManager.getStarPlayerIdList()
                // // Remove from list here
                // if (this.getIndex() != -2) {
                // this.setStar(!this.isStar());
                // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.groupManager.getGroupPlayerIdList().get(Math.abs(this.index) - 2));
                // }
                // }

                // this.setStar(!this.isStar());
                // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.availablePlayers.get(this.index + PartyScreen.this.indexStartOffset));

                // // else if (this.getIndex() == -1) {
                // // PartyAddonClientPacket.writeC2SDeclineInvitationPacket(PartyScreen.this.groupManager.getInvitationPlayerId());
                // // } else {
                // this.setStar(!this.isStar());
                // PartyAddonClientPacket.writeC2SChangeStarListPacket(PartyScreen.this.availablePlayers.get(this.index + PartyScreen.this.indexStartOffset));
                // // }
                return true;
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            TextRenderer textRenderer = client.textRenderer;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            int i = this.getYImage(this.isHovered());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.drawTexture(matrices, this.x, this.y, star ? 86 : 0, 216 + i * 13, this.width, this.height);
            if (this.leader) {
                RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
                RenderSystem.enableBlend();

                this.drawTexture(matrices, this.x + 67, this.y + 3, 0, 32, 7, 7);
            }

            this.renderBackground(matrices, client, mouseX, mouseY);
            int j = this.active ? 0xFFFFFF : 0xA0A0A0;
            ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);

            if (this.isHovered()) {
                this.renderTooltip(matrices, mouseX, mouseY);
            }
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            if (this.hovered && this.active) {

                UUID playerId = null;
                if (this.getIndex() == -1) {
                    if (NameHelper.isPlayerUUIDNotNull(client, PartyScreen.this.groupManager.getInvitationPlayerId())) {
                        playerId = PartyScreen.this.groupManager.getInvitationPlayerId();
                    }
                } else {
                    if (this.getIndex() < -1) {
                        int index = Math.abs(this.index) - 2;
                        if (!PartyScreen.this.groupManager.getGroupPlayerIdList().isEmpty() && PartyScreen.this.groupManager.getGroupPlayerIdList().size() >= index) {
                            playerId = PartyScreen.this.groupManager.getGroupPlayerIdList().get(index);
                        }
                    } else {
                        int index = this.index + PartyScreen.this.indexStartOffset;
                        if (!PartyScreen.this.availablePlayers.isEmpty() && PartyScreen.this.availablePlayers.size() >= index) {
                            playerId = PartyScreen.this.availablePlayers.get(index);
                        }
                    }
                }

                if (playerId != null) {
                    if (NameHelper.isPlayerUUIDNotNull(client, playerId)) {
                        String playerName = NameHelper.getPlayerName(client, playerId, 0).getString();
                        if (PartyScreen.this.textRenderer.getWidth(playerName) > 60) {
                            PartyScreen.this.renderTooltip(matrices, Text.of(playerName), mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }

}
