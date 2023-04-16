package com.ferriarnus.adventurersdimension.menu;

import com.ferriarnus.adventurersdimension.blockentity.DimensionAnchorBlockEntity;
import com.ferriarnus.adventurersdimension.config.AdventureConfig;
import com.ferriarnus.adventurersdimension.network.AdventureNetwork;
import com.ferriarnus.adventurersdimension.network.CreateDimensionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;

public class DimensionalAnchorMenu extends AbstractContainerMenu {
    private final Inventory inventory;
    private DimensionAnchorBlockEntity anchor;

    public DimensionalAnchorMenu(int pContainerId, Inventory inv, FriendlyByteBuf data) {
        this(pContainerId, inv, data.readBlockPos(), inv.player.level);
    }

    public DimensionalAnchorMenu(int pContainerId, Inventory inv, BlockPos pos, Level level) {
        super(MenuRegistry.DIMENSIONAL_ANCHOR.get(), pContainerId);
        this.inventory = inv;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof DimensionAnchorBlockEntity anchor) {
            this.anchor = anchor;
        }

        addSlot(new SlotItemHandler(anchor.getHandler(), 0, 44, 20) {
            @Override
            public boolean mayPickup(Player playerIn) {
                if (!playerIn.hasPermissions(AdventureConfig.PERMISSION.get())) {
                    return false;
                }
                return super.mayPickup(playerIn);
            }
        });

        for(int l = 0; l < 3; ++l) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inv, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inv, i1, 8 + i1 * 18, 109));
        }
    }

    public void confirmLevel(){
        AdventureNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CreateDimensionPacket(anchor.getBlockPos(), anchor.getLevel().dimension()));
    }

    public DimensionAnchorBlockEntity getAnchor() {
        return anchor;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 1 && pPlayer.hasPermissions(AdventureConfig.PERMISSION.get())) {
                if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
