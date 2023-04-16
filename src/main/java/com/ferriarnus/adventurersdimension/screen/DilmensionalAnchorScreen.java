package com.ferriarnus.adventurersdimension.screen;

import com.ferriarnus.adventurersdimension.AdventurersDimension;
import com.ferriarnus.adventurersdimension.menu.DimensionalAnchorMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DilmensionalAnchorScreen extends AbstractContainerScreen<DimensionalAnchorMenu> {

    private static final ResourceLocation HOPPER_LOCATION = new ResourceLocation("textures/gui/container/hopper.png");

    public DilmensionalAnchorScreen(DimensionalAnchorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        Button button = new Button.Builder(Component.translatable("test"), (b) -> getMenu().confirmLevel()).pos(16, 16).size(16, 32).build();
        addRenderableWidget(button);
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, HOPPER_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        this.font.draw(pPoseStack, Component.translatable("screen.adventurersdimension.dimensionalanchor.loaded"),40,20,4210752);
        if (getMenu().getAnchor().getCopied() != null) {
            this.font.draw(pPoseStack, getMenu().getAnchor().getCopied().toString(),40,30,4210752);

            if (getMenu().getAnchor().getLevelResourceKey()!= null) {
                this.font.draw(pPoseStack, Component.translatable("screen.adventurersdimension.dimensionalanchor.ready"),40,40,1325400064);
            } else {
                this.font.draw(pPoseStack, Component.translatable("screen.adventurersdimension.dimensionalanchor.waiting"),40,40,0xFFA500);
            }
        } else {
            this.font.draw(pPoseStack, Component.translatable("screen.adventurersdimension.dimensionalanchor.none"),40,30,16736352);
        }
        if (Minecraft.getInstance().level.dimension().location().getNamespace().equals(AdventurersDimension.MODID)) {
            this.font.draw(pPoseStack, Component.translatable("screen.adventurersdimension.dimensionalanchor.wrongdimension"),40,40,16736352);
        }
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }
}
