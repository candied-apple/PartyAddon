package net.partyaddon.gui.old;
// package net.partyaddon.gui;

// public class OldTest {
// package net.partyaddon.gui;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Objects;

// import com.mojang.blaze3d.systems.RenderSystem;

// import io.github.cottonmc.cotton.gui.client.LibGui;
// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.fabricmc.fabric.api.client.screen.v1.Screens;
// import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
// import net.levelz.compat.InventorioScreenCompatibility;
// import net.levelz.gui.LevelzGui;
// import net.levelz.gui.LevelzScreen;
// import net.levelz.init.KeyInit;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.font.TextRenderer;
// import net.minecraft.client.gui.DrawableHelper;
// import net.minecraft.client.gui.screen.Screen;
// import net.minecraft.client.gui.screen.ingame.InventoryScreen;
// import net.minecraft.client.gui.screen.ingame.MerchantScreen;
// import net.minecraft.client.gui.screen.world.SelectWorldScreen;
// import net.minecraft.client.gui.widget.ButtonWidget;
// import net.minecraft.client.gui.widget.ClickableWidget;
// import net.minecraft.client.gui.widget.TextFieldWidget;
// import net.minecraft.client.render.GameRenderer;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.screen.ScreenTexts;
// import net.minecraft.text.Text;
// import net.minecraft.util.math.MathHelper;
// import net.partyaddon.PartyAddonMain;
// import net.partyaddon.access.GroupManagerAccess;
// import net.partyaddon.group.GroupManager;
// import net.partyaddon.init.ConfigInit;
// import net.partyaddon.init.RenderInit;

// @Environment(EnvType.CLIENT)
// public class TestScreen extends Screen {

// private int left;
// private int top;

// private PlayerEntity playerEntity;
// private GroupManager groupManager;

// private List<Integer> availablePlayers = new ArrayList<Integer>();
// private WidgetButtonPage[] availablePlayerButtons;
// int indexStartOffset;
// private boolean scrolling;
// private int selectedIndex;
// private TextFieldWidget searchBox;;

// public TestScreen() {
// super(Text.translatable("text.partyaddon.gui.title"));
// }

// @Override
// protected void init() {
// this.left = this.width / 2 - 100;
// this.top = this.height / 2 - 108;
// this.playerEntity = client.player;
// this.groupManager = ((GroupManagerAccess) playerEntity).getGroupManager();

// this.availablePlayers = groupManager.getAvailablePlayerIdList();
// // for (int u = 0; u < 20; u++) {
// // this.availablePlayers.add(u);
// // }
// if (this.availablePlayers.size() >= 13)
// availablePlayerButtons = new WidgetButtonPage[13];
// else
// availablePlayerButtons = new WidgetButtonPage[this.availablePlayers.size()];

// int k = this.top + 15 + ConfigInit.CONFIG.test0x;
// for (int l = 0; l < availablePlayerButtons.length; ++l) {
// this.availablePlayerButtons[l] = this.addDrawableChild(new WidgetButtonPage(this.left + 5, k, l, button -> {
// if (button instanceof WidgetButtonPage) {
// this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
// // this.syncRecipeIndex();
// }
// }));
// k += 13;
// }

// this.searchBox = new TextFieldWidget(this.textRenderer, this.left + ConfigInit.CONFIG.test5x, this.top + ConfigInit.CONFIG.test6x, 93, 15, this.searchBox,
// Text.translatable("searchPlayer.search"));
// this.searchBox.setChangedListener(search -> {
// System.out.println("TEST " + search);
// if (search.equals("BUM"))
// this.availablePlayers.clear();
// // this.levelList.setSearch((String) search);
// });
// this.addSelectableChild(this.searchBox);
// }

// @Override
// public boolean charTyped(char chr, int modifiers) {
// return this.searchBox.charTyped(chr, modifiers);
// }

// // @Override
// // public void tick() {
// // this.searchBox.tick();
// // }

// @Override
// public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
// // translucent background
// renderBackground(matrices);
// // gui background
// RenderSystem.setShader(GameRenderer::getPositionTexShader);
// RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
// RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);

// this.drawTexture(matrices, this.left, this.top, 0, 0, 200, 216);

// RenderSystem.setShaderTexture(0, net.levelz.init.RenderInit.GUI_ICONS);
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
// if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// this.renderTooltip(matrices, Text.translatable("container.inventory"), mouseX, mouseY);
// }
// if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// this.renderTooltip(matrices, Text.translatable("screen.levelz.skill_screen"), mouseX, mouseY);
// }
// super.render(matrices, mouseX, mouseY, delta);

// // Top label
// DrawableHelper.drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.top + 7, 0xFFFFFF);// 0x3F3F3F

// // Available player list label
// this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.player_list"), this.left + ConfigInit.CONFIG.test0, this.top + ConfigInit.CONFIG.test1, 0x3F3F3F);

// // if (!this.groupManager.getStarPlayerIdList().isEmpty()) {
// // groupManager.getAvailablePlayerIdList().removeAll(this.groupManager.getStarPlayerIdList());
// // for (int i = 0; i < this.groupManager.getStarPlayerIdList().size(); i++) {
// // if (!this.groupManager.getGroupPlayerIdList().contains(this.groupManager.getStarPlayerIdList().get(i)))
// // groupManager.getAvailablePlayerIdList().add(0, this.groupManager.getStarPlayerIdList().get(i));
// // }
// // }

// // int i = (this.width - this.backgroundWidth) / 2;
// // int j = (this.height - this.backgroundHeight) / 2;
// // int k = j + 16 + 1;
// // int l = i + 5 + 5;
// int k = this.top + ConfigInit.CONFIG.test3x + 1;
// this.renderScrollbar(matrices, this.left, this.top, this.availablePlayers);
// int m = 0;
// if (!this.availablePlayers.isEmpty()) {
// for (int i = 0; i < this.availablePlayers.size(); i++) {
// if (this.canScroll(this.availablePlayers.size()) && (m < this.indexStartOffset || m >= 13 + this.indexStartOffset)) {
// ++m;
// continue;
// }
// this.textRenderer.draw(matrices, this.playerEntity.getName().getString() + " : " + i, this.left + 10, k, 0xFFFFFF);
// // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
// // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
// // this.itemRenderer.zOffset = 0.0f;
// k += ConfigInit.CONFIG.test4x;
// ++m;

// }
// }
// this.searchBox.render(matrices, mouseX, mouseY, delta);
// // for (TradeOffer tradeOffer2 : tradeOfferList) {
// // if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
// // ++m;
// // continue;
// // }
// // ItemStack itemStack = tradeOffer2.getOriginalFirstBuyItem();
// // ItemStack itemStack2 = tradeOffer2.getAdjustedFirstBuyItem();
// // ItemStack itemStack3 = tradeOffer2.getSecondBuyItem();
// // ItemStack itemStack4 = tradeOffer2.getSellItem();
// // this.itemRenderer.zOffset = 100.0f;
// // int n = k + 2;
// // this.renderFirstBuyItem(matrices, itemStack2, itemStack, l, n);
// // if (!itemStack3.isEmpty()) {
// // this.itemRenderer.renderInGui(itemStack3, i + 5 + 35, n);
// // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack3, i + 5 + 35, n);
// // }
// // this.renderArrow(matrices, tradeOffer2, i, n);
// // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
// // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
// // this.itemRenderer.zOffset = 0.0f;
// // k += 20;
// // ++m;
// // }

// for (WidgetButtonPage widgetButtonPage : this.availablePlayerButtons) {
// if (widgetButtonPage.isHovered()) {
// widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
// }
// widgetButtonPage.visible = widgetButtonPage.index < this.availablePlayers.size();
// }

// }

// @Override
// public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
// if (KeyInit.screenKey.matchesKey(keyCode, scanCode) || Objects.requireNonNull(client).options.inventoryKey.matchesKey(keyCode, scanCode)) {
// this.close();
// return true;
// } else
// return super.keyPressed(keyCode, scanCode, modifiers);
// }

// @Override
// public boolean shouldPause() {
// return false;
// }

// // @Override
// // public void close() {
// // // if (this.client != null && this.client.player != null && !this.switchScreen)
// // // NumismaticClaimClientPacket.writeC2SCloseScreenPacket(client, ((VillagerAccess) this.client.player).getCurrentOfferer().getId());

// // super.close();
// // }

// @Override
// public boolean mouseClicked(double mouseX, double mouseY, int button) {
// // if (this.isPointWithinBounds(168, 0, 20, 20, (double) mouseX, (double) mouseY)) {
// // this.switchScreen = true;
// // this.client.setScreen(new MerchantScreen(this.handler, this.inventory, this.title));
// // }
// // return super.mouseClicked(mouseX, mouseY, button);

// if (this.client != null) {
// if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// assert this.client.player != null;
// if (net.levelz.init.RenderInit.isInventorioLoaded)
// InventorioScreenCompatibility.setInventorioScreen(client);
// else
// this.client.setScreen(new InventoryScreen(this.client.player));
// } else if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY))
// this.client.setScreen(new LevelzScreen(new LevelzGui(client)));
// }

// this.scrolling = false;
// int i = (this.width - 200) / 2;
// int j = (this.height - 216) / 2;
// if (this.canScroll(this.availablePlayers.size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
// this.scrolling = true;
// }
// return super.mouseClicked(mouseX, mouseY, button);
// }

// private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
// int i = (this.width - 200) / 2;
// int j = (this.height - 216) / 2;
// return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
// }

// private boolean canScroll(int listSize) {
// return listSize > 13;
// }

// @Override
// public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
// int i = this.availablePlayers.size();
// if (this.canScroll(i)) {
// int j = i - 13;
// this.indexStartOffset = MathHelper.clamp((int) ((double) this.indexStartOffset - amount), 0, j);
// }
// return true;
// }

// @Override
// public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
// int i = this.availablePlayers.size();
// if (this.scrolling) {
// int j = this.top + 18;
// int k = j + 139;
// int l = i - 13;
// float f = ((float) mouseY - (float) j - 13.5f) / ((float) (k - j) - 27.0f);
// f = f * (float) l + 0.5f;
// this.indexStartOffset = MathHelper.clamp((int) f, 0, l);
// return true;
// }
// return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
// }

// private void renderScrollbar(MatrixStack matrices, int x, int y, List<Integer> availablePlayers) {
// RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
// int i = availablePlayers.size() + 1 - 13;
// if (i > 1) {
// int j = 167 - (27 + (i - 1) * 167 / i);
// int k = 1 + j / i + 167 / i;
// int m = Math.min(142, this.indexStartOffset * k);
// if (this.indexStartOffset == i - 1) {
// m = 142;
// }
// this.drawTexture(matrices, x + 94 + ConfigInit.CONFIG.test1x, y + 18 + m + ConfigInit.CONFIG.test2x, 200, 0, 6, 27);
// } else {
// this.drawTexture(matrices, x + 94 + ConfigInit.CONFIG.test1x, y + 18, 206, 0, 6, 27);
// }
// }

// private class WidgetButtonPage extends ButtonWidget {
// final int index;

// public WidgetButtonPage(int x, int y, int index, ButtonWidget.PressAction onPress) {
// super(x, y, 87, 13, ScreenTexts.EMPTY, onPress);
// this.index = index;
// this.visible = false;
// }

// public int getIndex() {
// return this.index;
// }

// @Override
// public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
// MinecraftClient minecraftClient = MinecraftClient.getInstance();
// TextRenderer textRenderer = minecraftClient.textRenderer;
// RenderSystem.setShader(GameRenderer::getPositionTexShader);
// RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
// RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
// int i = this.getYImage(this.isHovered());
// RenderSystem.enableBlend();
// RenderSystem.defaultBlendFunc();
// RenderSystem.enableDepthTest();
// // this.drawTexture(matrices, this.x, this.y, 0, 216 + i * 13, this.width / 2, this.height);
// // this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
// this.drawTexture(matrices, this.x, this.y, 0, 216 + i * 13, this.width, this.height);

// // this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

// this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
// int j = this.active ? 0xFFFFFF : 0xA0A0A0;
// ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);

// if (this.isHovered()) {
// this.renderTooltip(matrices, mouseX, mouseY);
// }
// }

// @Override
// public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
// // if (this.hovered && ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().size() > this.index + MerchantScreen.this.indexStartOffset) {
// // if (mouseX < this.x + 20) {
// // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset))
// // .getAdjustedFirstBuyItem();
// // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // } else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
// // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset)).getSecondBuyItem();
// // if (!itemStack.isEmpty()) {
// // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // }
// // } else if (mouseX > this.x + 65) {
// // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset)).getSellItem();
// // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // }
// // }
// }
// }

// }

// // package net.partyaddon.gui;

// // import java.util.ArrayList;
// // import java.util.List;
// // import java.util.Objects;

// // import com.mojang.blaze3d.systems.RenderSystem;

// // import io.github.cottonmc.cotton.gui.client.LibGui;
// // import net.fabricmc.api.EnvType;
// // import net.fabricmc.api.Environment;
// // import net.fabricmc.fabric.api.client.screen.v1.Screens;
// // import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
// // import net.levelz.compat.InventorioScreenCompatibility;
// // import net.levelz.gui.LevelzGui;
// // import net.levelz.gui.LevelzScreen;
// // import net.levelz.init.KeyInit;
// // import net.minecraft.client.MinecraftClient;
// // import net.minecraft.client.font.TextRenderer;
// // import net.minecraft.client.gui.DrawableHelper;
// // import net.minecraft.client.gui.screen.Screen;
// // import net.minecraft.client.gui.screen.ingame.InventoryScreen;
// // import net.minecraft.client.gui.screen.ingame.MerchantScreen;
// // import net.minecraft.client.gui.screen.world.SelectWorldScreen;
// // import net.minecraft.client.gui.widget.ButtonWidget;
// // import net.minecraft.client.gui.widget.ClickableWidget;
// // import net.minecraft.client.gui.widget.TextFieldWidget;
// // import net.minecraft.client.render.GameRenderer;
// // import net.minecraft.client.util.math.MatrixStack;
// // import net.minecraft.entity.player.PlayerEntity;
// // import net.minecraft.screen.ScreenTexts;
// // import net.minecraft.text.Text;
// // import net.minecraft.util.math.MathHelper;
// // import net.partyaddon.PartyAddonMain;
// // import net.partyaddon.access.GroupManagerAccess;
// // import net.partyaddon.group.GroupManager;
// // import net.partyaddon.init.ConfigInit;
// // import net.partyaddon.init.RenderInit;

// // @Environment(EnvType.CLIENT)
// // public class TestScreen extends Screen {

// // private int left;
// // private int top;

// // private PlayerEntity playerEntity;
// // private GroupManager groupManager;

// // private List<Integer> availablePlayers = new ArrayList<Integer>();
// // private WidgetButtonPage[] availablePlayerButtons;
// // int indexStartOffset;
// // private boolean scrolling;
// // private int selectedIndex;
// // private TextFieldWidget searchBox;;

// // public TestScreen() {
// // super(Text.translatable("text.partyaddon.gui.title"));
// // }

// // @Override
// // protected void init() {
// // this.left = this.width / 2 - 100;
// // this.top = this.height / 2 - 108;
// // this.playerEntity = client.player;
// // this.groupManager = ((GroupManagerAccess) playerEntity).getGroupManager();

// // this.availablePlayers = groupManager.getAvailablePlayerIdList();
// // // for (int u = 0; u < 20; u++) {
// // // this.availablePlayers.add(u);
// // // }
// // if (this.availablePlayers.size() >= 13)
// // availablePlayerButtons = new WidgetButtonPage[13];
// // else
// // availablePlayerButtons = new WidgetButtonPage[this.availablePlayers.size()];

// // int k = this.top + 15 + ConfigInit.CONFIG.test0x;
// // for (int l = 0; l < availablePlayerButtons.length; ++l) {
// // this.availablePlayerButtons[l] = this.addDrawableChild(new WidgetButtonPage(this.left + 5, k, l, button -> {
// // if (button instanceof WidgetButtonPage) {
// // this.selectedIndex = ((WidgetButtonPage) button).getIndex() + this.indexStartOffset;
// // // this.syncRecipeIndex();
// // }
// // }));
// // k += 13;
// // }

// // this.searchBox = new TextFieldWidget(this.textRenderer, this.left + ConfigInit.CONFIG.test5x, this.top + ConfigInit.CONFIG.test6x, 93, 15, this.searchBox,
// // Text.translatable("searchPlayer.search"));
// // this.searchBox.setChangedListener(search -> {
// // System.out.println("TEST " + search);
// // if (search.equals("BUM"))
// // this.availablePlayers.clear();
// // // this.levelList.setSearch((String) search);
// // });
// // this.addSelectableChild(this.searchBox);
// // }

// // @Override
// // public boolean charTyped(char chr, int modifiers) {
// // return this.searchBox.charTyped(chr, modifiers);
// // }

// // // @Override
// // // public void tick() {
// // // this.searchBox.tick();
// // // }

// // @Override
// // public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
// // // translucent background
// // renderBackground(matrices);
// // // gui background
// // RenderSystem.setShader(GameRenderer::getPositionTexShader);
// // RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
// // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);

// // this.drawTexture(matrices, this.left, this.top, 0, 0, 200, 216);

// // RenderSystem.setShaderTexture(0, net.levelz.init.RenderInit.GUI_ICONS);
// // if (LibGui.isDarkMode()) {
// // // bag icon
// // this.drawTexture(matrices, this.left, this.top - 21, 120, 110, 24, 25);
// // // skill icon
// // this.drawTexture(matrices, this.left + 25, this.top - 21, 168, 110, 24, 21);
// // } else {
// // // bag icon
// // this.drawTexture(matrices, this.left, this.top - 21, 24, 110, 24, 25);
// // // skill icon
// // this.drawTexture(matrices, this.left + 25, this.top - 21, 48, 110, 24, 21);
// // }

// // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
// // int xPos = 50;
// // if (PartyAddonMain.isJobsAddonLoaded)
// // xPos = 75;
// // if (LibGui.isDarkMode()) {
// // this.drawTexture(matrices, this.left + xPos, this.top - 23, 72, 0, 24, 27);
// // } else {
// // this.drawTexture(matrices, this.left + xPos, this.top - 23, 24, 0, 24, 27);
// // }
// // if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// // this.renderTooltip(matrices, Text.translatable("container.inventory"), mouseX, mouseY);
// // }
// // if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// // this.renderTooltip(matrices, Text.translatable("screen.levelz.skill_screen"), mouseX, mouseY);
// // }
// // super.render(matrices, mouseX, mouseY, delta);

// // // Top label
// // DrawableHelper.drawCenteredText(matrices, textRenderer, this.title, this.width / 2, this.top + 7, 0xFFFFFF);// 0x3F3F3F

// // // Available player list label
// // this.textRenderer.draw(matrices, Text.translatable("text.partyaddon.gui.player_list"), this.left + ConfigInit.CONFIG.test0, this.top + ConfigInit.CONFIG.test1, 0x3F3F3F);

// // // if (!this.groupManager.getStarPlayerIdList().isEmpty()) {
// // // groupManager.getAvailablePlayerIdList().removeAll(this.groupManager.getStarPlayerIdList());
// // // for (int i = 0; i < this.groupManager.getStarPlayerIdList().size(); i++) {
// // // if (!this.groupManager.getGroupPlayerIdList().contains(this.groupManager.getStarPlayerIdList().get(i)))
// // // groupManager.getAvailablePlayerIdList().add(0, this.groupManager.getStarPlayerIdList().get(i));
// // // }
// // // }

// // // int i = (this.width - this.backgroundWidth) / 2;
// // // int j = (this.height - this.backgroundHeight) / 2;
// // // int k = j + 16 + 1;
// // // int l = i + 5 + 5;
// // int k = this.top + ConfigInit.CONFIG.test3x + 1;
// // this.renderScrollbar(matrices, this.left, this.top, this.availablePlayers);
// // int m = 0;
// // if (!this.availablePlayers.isEmpty()) {
// // for (int i = 0; i < this.availablePlayers.size(); i++) {
// // if (this.canScroll(this.availablePlayers.size()) && (m < this.indexStartOffset || m >= 13 + this.indexStartOffset)) {
// // ++m;
// // continue;
// // }
// // this.textRenderer.draw(matrices, this.playerEntity.getName().getString() + " : " + i, this.left + 10, k, 0xFFFFFF);
// // // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
// // // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
// // // this.itemRenderer.zOffset = 0.0f;
// // k += ConfigInit.CONFIG.test4x;
// // ++m;

// // }
// // }
// // this.searchBox.render(matrices, mouseX, mouseY, delta);
// // // for (TradeOffer tradeOffer2 : tradeOfferList) {
// // // if (this.canScroll(tradeOfferList.size()) && (m < this.indexStartOffset || m >= 7 + this.indexStartOffset)) {
// // // ++m;
// // // continue;
// // // }
// // // ItemStack itemStack = tradeOffer2.getOriginalFirstBuyItem();
// // // ItemStack itemStack2 = tradeOffer2.getAdjustedFirstBuyItem();
// // // ItemStack itemStack3 = tradeOffer2.getSecondBuyItem();
// // // ItemStack itemStack4 = tradeOffer2.getSellItem();
// // // this.itemRenderer.zOffset = 100.0f;
// // // int n = k + 2;
// // // this.renderFirstBuyItem(matrices, itemStack2, itemStack, l, n);
// // // if (!itemStack3.isEmpty()) {
// // // this.itemRenderer.renderInGui(itemStack3, i + 5 + 35, n);
// // // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack3, i + 5 + 35, n);
// // // }
// // // this.renderArrow(matrices, tradeOffer2, i, n);
// // // this.itemRenderer.renderInGui(itemStack4, i + 5 + 68, n);
// // // this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack4, i + 5 + 68, n);
// // // this.itemRenderer.zOffset = 0.0f;
// // // k += 20;
// // // ++m;
// // // }

// // for (WidgetButtonPage widgetButtonPage : this.availablePlayerButtons) {
// // if (widgetButtonPage.isHovered()) {
// // widgetButtonPage.renderTooltip(matrices, mouseX, mouseY);
// // }
// // widgetButtonPage.visible = widgetButtonPage.index < this.availablePlayers.size();
// // }

// // }

// // @Override
// // public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
// // if (KeyInit.screenKey.matchesKey(keyCode, scanCode) || Objects.requireNonNull(client).options.inventoryKey.matchesKey(keyCode, scanCode)) {
// // this.close();
// // return true;
// // } else
// // return super.keyPressed(keyCode, scanCode, modifiers);
// // }

// // @Override
// // public boolean shouldPause() {
// // return false;
// // }

// // // @Override
// // // public void close() {
// // // // if (this.client != null && this.client.player != null && !this.switchScreen)
// // // // NumismaticClaimClientPacket.writeC2SCloseScreenPacket(client, ((VillagerAccess) this.client.player).getCurrentOfferer().getId());

// // // super.close();
// // // }

// // @Override
// // public boolean mouseClicked(double mouseX, double mouseY, int button) {
// // // if (this.isPointWithinBounds(168, 0, 20, 20, (double) mouseX, (double) mouseY)) {
// // // this.switchScreen = true;
// // // this.client.setScreen(new MerchantScreen(this.handler, this.inventory, this.title));
// // // }
// // // return super.mouseClicked(mouseX, mouseY, button);

// // if (this.client != null) {
// // if (this.isPointWithinBounds(0, -21, 24, 21, (double) mouseX, (double) mouseY)) {
// // assert this.client.player != null;
// // if (net.levelz.init.RenderInit.isInventorioLoaded)
// // InventorioScreenCompatibility.setInventorioScreen(client);
// // else
// // this.client.setScreen(new InventoryScreen(this.client.player));
// // } else if (this.isPointWithinBounds(25, -21, 24, 21, (double) mouseX, (double) mouseY))
// // this.client.setScreen(new LevelzScreen(new LevelzGui(client)));
// // }

// // this.scrolling = false;
// // int i = (this.width - 200) / 2;
// // int j = (this.height - 216) / 2;
// // if (this.canScroll(this.availablePlayers.size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
// // this.scrolling = true;
// // }
// // return super.mouseClicked(mouseX, mouseY, button);
// // }

// // private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
// // int i = (this.width - 200) / 2;
// // int j = (this.height - 216) / 2;
// // return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
// // }

// // private boolean canScroll(int listSize) {
// // return listSize > 13;
// // }

// // @Override
// // public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
// // int i = this.availablePlayers.size();
// // if (this.canScroll(i)) {
// // int j = i - 13;
// // this.indexStartOffset = MathHelper.clamp((int) ((double) this.indexStartOffset - amount), 0, j);
// // }
// // return true;
// // }

// // @Override
// // public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
// // int i = this.availablePlayers.size();
// // if (this.scrolling) {
// // int j = this.top + 18;
// // int k = j + 139;
// // int l = i - 13;
// // float f = ((float) mouseY - (float) j - 13.5f) / ((float) (k - j) - 27.0f);
// // f = f * (float) l + 0.5f;
// // this.indexStartOffset = MathHelper.clamp((int) f, 0, l);
// // return true;
// // }
// // return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
// // }

// // private void renderScrollbar(MatrixStack matrices, int x, int y, List<Integer> availablePlayers) {
// // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
// // int i = availablePlayers.size() + 1 - 13;
// // if (i > 1) {
// // int j = 167 - (27 + (i - 1) * 167 / i);
// // int k = 1 + j / i + 167 / i;
// // int m = Math.min(142, this.indexStartOffset * k);
// // if (this.indexStartOffset == i - 1) {
// // m = 142;
// // }
// // this.drawTexture(matrices, x + 94 + ConfigInit.CONFIG.test1x, y + 18 + m + ConfigInit.CONFIG.test2x, 200, 0, 6, 27);
// // } else {
// // this.drawTexture(matrices, x + 94 + ConfigInit.CONFIG.test1x, y + 18, 206, 0, 6, 27);
// // }
// // }

// // private class WidgetButtonPage extends ButtonWidget {
// // final int index;

// // public WidgetButtonPage(int x, int y, int index, ButtonWidget.PressAction onPress) {
// // super(x, y, 87, 13, ScreenTexts.EMPTY, onPress);
// // this.index = index;
// // this.visible = false;
// // }

// // public int getIndex() {
// // return this.index;
// // }

// // @Override
// // public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
// // MinecraftClient minecraftClient = MinecraftClient.getInstance();
// // TextRenderer textRenderer = minecraftClient.textRenderer;
// // RenderSystem.setShader(GameRenderer::getPositionTexShader);
// // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_BACKGROUND);
// // RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
// // int i = this.getYImage(this.isHovered());
// // RenderSystem.enableBlend();
// // RenderSystem.defaultBlendFunc();
// // RenderSystem.enableDepthTest();
// // // this.drawTexture(matrices, this.x, this.y, 0, 216 + i * 13, this.width / 2, this.height);
// // // this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
// // this.drawTexture(matrices, this.x, this.y, 0, 216 + i * 13, this.width, this.height);

// // // this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

// // this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
// // int j = this.active ? 0xFFFFFF : 0xA0A0A0;
// // ClickableWidget.drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0f) << 24);

// // if (this.isHovered()) {
// // this.renderTooltip(matrices, mouseX, mouseY);
// // }
// // }

// // @Override
// // public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
// // // if (this.hovered && ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().size() > this.index + MerchantScreen.this.indexStartOffset) {
// // // if (mouseX < this.x + 20) {
// // // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset))
// // // .getAdjustedFirstBuyItem();
// // // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // // } else if (mouseX < this.x + 50 && mouseX > this.x + 30) {
// // // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset)).getSecondBuyItem();
// // // if (!itemStack.isEmpty()) {
// // // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // // }
// // // } else if (mouseX > this.x + 65) {
// // // ItemStack itemStack = ((TradeOffer) ((MerchantScreenHandler) MerchantScreen.this.handler).getRecipes().get(this.index + MerchantScreen.this.indexStartOffset)).getSellItem();
// // // MerchantScreen.this.renderTooltip(matrices, itemStack, mouseX, mouseY);
// // // }
// // // }
// // }
// // }

// // }

// }
