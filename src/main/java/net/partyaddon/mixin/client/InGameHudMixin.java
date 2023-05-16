package net.partyaddon.mixin.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.text.Text;
import net.partyaddon.access.GroupManagerAccess;
import net.partyaddon.init.ConfigInit;
import net.partyaddon.init.RenderInit;
import net.partyaddon.util.NameHelper;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Mutable
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    // @Inject(method = "renderHotbar", at = @At("TAIL"))
    // private void renderHotbarMixin(float tickDelta, MatrixStack matrices, CallbackInfo info) {
    // if (this.client.player != null && ConfigInit.CONFIG.showGroupHud && !((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList().isEmpty()) {
    // RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, ConfigInit.CONFIG.hudOpacity);
    // RenderSystem.setShader(GameRenderer::getPositionTexShader);
    // RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
    // RenderSystem.enableBlend();
    // RenderSystem.defaultBlendFunc();

    // List<Integer> groupPlayerIdList = ((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList();

    // int width = 55;
    // int height = groupPlayerIdList.size() * 13 - 5;
    // int otherXPos = ConfigInit.CONFIG.hudPosX;
    // int otherYPos = ConfigInit.CONFIG.hudPosY;

    // // int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight

    // // left border
    // DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + otherYPos, 5, 5, 0, 64, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, 5, height, 0, 70, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, 5, 5, 0, 76, 5, 5, 256, 256);
    // // middle
    // DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + otherYPos, width, 5, 6, 64, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, width, height, 6, 70, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, width, 5, 6, 76, 5, 5, 256, 256);
    // // right border
    // DrawableHelper.drawTexture(matrices, width + 5 + otherXPos, this.scaledHeight / 2 + otherYPos, 5, 5, 12, 64, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, width + 5 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, 5, height, 12, 70, 5, 5, 256, 256);
    // DrawableHelper.drawTexture(matrices, width + 5 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, 5, 5, 12, 76, 5, 5, 256, 256);

    // // this.client.textRenderer.draw(matrices, Text.translatable("text.partyaddon.hud.group"), 5, this.scaledHeight / 2 + 5, 0xE6C0C0C0);
    // // int o = 13;
    // int o = 0;
    // for (int i = 0; i < groupPlayerIdList.size(); i++) {
    // // this.client.textRenderer.getWidth(this.client.player.getName().getString());
    // // this.client.textRenderer.draw(matrices,
    // // NameHelper.getPlayerName(client.world, client.textRenderer, ((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList().get(i)), 5,
    // // this.scaledHeight / 2 + 5 + o, 0xE6C0C0C0);
    // // String playerName = world.getEntityById(playerId).getName().getString();
    // // if (textRenderer.getWidth(playerName) > 55)
    // // playerName = playerName.substring(0, 9) + "..";

    // this.client.textRenderer.draw(matrices, NameHelper.getPlayerName(client.world, client.textRenderer, groupPlayerIdList.get(i)), 5, this.scaledHeight / 2 + 5 + o, 0xE6C0C0C0);
    // o += 13;
    // }
    // RenderSystem.disableBlend();
    // // System.out.println(scaledWidth);
    // }
    // }

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void renderHotbarMixin(float tickDelta, MatrixStack matrices, CallbackInfo info) {
        if (this.client.player != null && ConfigInit.CONFIG.showGroupHud && !((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList().isEmpty()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, ConfigInit.CONFIG.hudOpacity);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RenderInit.PARTY_ADDON_GUI_ICONS);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            List<UUID> groupPlayerIdList = ((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList();
            List<Text> groupPlayerNames = new ArrayList<Text>();

            int maxWidth = ConfigInit.CONFIG.hudMaxWidth;
            int newMaxWidth = 0;
            for (int i = 0; i < groupPlayerIdList.size(); i++) {
                Text playerName = NameHelper.getPlayerName(client, groupPlayerIdList.get(i), maxWidth);
                if (newMaxWidth < this.client.textRenderer.getWidth(playerName)) {
                    newMaxWidth = this.client.textRenderer.getWidth(playerName);
                }
                groupPlayerNames.add(playerName);
            }
            if (newMaxWidth < maxWidth) {
                maxWidth = newMaxWidth;
            }
            int height = groupPlayerIdList.size() * 13 - 5;
            int otherXPos = ConfigInit.CONFIG.hudPosX;
            int otherYPos = ConfigInit.CONFIG.hudPosY;

            // int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight

            // left border
            DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + otherYPos, 5, 5, 0, 64, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, 5, height, 0, 70, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, 0 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, 5, 5, 0, 76, 5, 5, 256, 256);
            // middle
            DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + otherYPos, maxWidth, 5, 6, 64, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, maxWidth, height, 6, 70, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, 5 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, maxWidth, 5, 6, 76, 5, 5, 256, 256);
            // right border
            DrawableHelper.drawTexture(matrices, maxWidth + 5 + otherXPos, this.scaledHeight / 2 + otherYPos, 5, 5, 12, 64, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, maxWidth + 5 + otherXPos, this.scaledHeight / 2 + 5 + otherYPos, 5, height, 12, 70, 5, 5, 256, 256);
            DrawableHelper.drawTexture(matrices, maxWidth + 5 + otherXPos, this.scaledHeight / 2 + height + 5 + otherYPos, 5, 5, 12, 76, 5, 5, 256, 256);

            // this.client.textRenderer.draw(matrices, Text.translatable("text.partyaddon.hud.group"), 5, this.scaledHeight / 2 + 5, 0xE6C0C0C0);
            // int o = 13;
            int o = 0;
            for (int i = 0; i < groupPlayerNames.size(); i++) {
                // this.client.textRenderer.getWidth(this.client.player.getName().getString());
                // this.client.textRenderer.draw(matrices,
                // NameHelper.getPlayerName(client.world, client.textRenderer, ((GroupManagerAccess) this.client.player).getGroupManager().getGroupPlayerIdList().get(i)), 5,
                // this.scaledHeight / 2 + 5 + o, 0xE6C0C0C0);
                // String playerName = world.getEntityById(playerId).getName().getString();
                // if (textRenderer.getWidth(playerName) > 55)
                // playerName = playerName.substring(0, 9) + "..";

                this.client.textRenderer.draw(matrices, groupPlayerNames.get(i), 5 + otherXPos, this.scaledHeight / 2 + 5 + o + otherYPos, 0xE6C0C0C0);
                o += 13;
            }
            RenderSystem.disableBlend();
            // System.out.println(scaledWidth);
        }
    }

}
