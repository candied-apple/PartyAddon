package net.partyaddon.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.libz.registry.TabRegistry;
import net.minecraft.util.Identifier;
import net.partyaddon.gui.PartyScreen;
import net.partyaddon.gui.widget.PartyTab;

@Environment(EnvType.CLIENT)
public class RenderInit {

    public static final Identifier PARTY_ADDON_GUI_ICONS = new Identifier("partyaddon:textures/gui/icons.png");
    public static final Identifier PARTY_ADDON_BACKGROUND = new Identifier("partyaddon:textures/gui/background.png");
    public static final Identifier PARTY_ADDON_TAB_ICON = new Identifier("partyaddon:textures/gui/tab_icon.png");

    public static void init() {
        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
        });
        TabRegistry.registerInventoryTab(new PartyTab(PartyScreen.title, PARTY_ADDON_TAB_ICON, 3, PartyScreen.class));
    }

}
