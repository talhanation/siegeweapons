package com.talhanation.siegeweapons.inventory;

import com.talhanation.siegeweapons.entities.SmallHorseCartEntity;
import com.talhanation.siegeweapons.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SmallHorseCartMenu extends AbstractContainerMenu {

    private static final int CART_ROWS = 6;
    private static final int CART_COLS = 9;
    private static final int CART_SLOTS = CART_ROWS * CART_COLS; // 54

    // GUI layout constants (matches minecraft:textures/gui/container/generic_54.png)
    private static final int CART_START_X = 8;
    private static final int CART_START_Y = 18;
    private static final int PLAYER_INV_X = 8;
    private static final int PLAYER_INV_Y = 140;
    private static final int PLAYER_HOTBAR_Y = 198;

    private final Container cartInventory;
    private final SmallHorseCartEntity cartEntity;

    public SmallHorseCartMenu(int id, SmallHorseCartEntity cartEntity, Inventory playerInventory) {
        super(ModMenus.SMALL_HORSE_CART_CONTAINER.get(), id);
        this.cartEntity = cartEntity;
        this.cartInventory = cartEntity.getInventory();

        checkContainerSize(cartInventory, CART_SLOTS);
        cartInventory.startOpen(playerInventory.player);

        // Cart inventory slots (6 rows x 9 cols = 54 slots)
        for (int row = 0; row < CART_ROWS; ++row) {
            for (int col = 0; col < CART_COLS; ++col) {
                this.addSlot(new Slot(cartInventory,
                        col + row * CART_COLS,
                        CART_START_X + col * 18,
                        CART_START_Y + row * 18));
            }
        }

        // Player inventory (rows 1-3)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,
                        PLAYER_INV_X + col * 18,
                        PLAYER_INV_Y + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory,
                    col,
                    PLAYER_INV_X + col * 18,
                    PLAYER_HOTBAR_Y));
        }
    }

    public SmallHorseCartEntity getCartEntity() {
        return cartEntity;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return cartInventory.stillValid(player)
                && cartEntity.isAlive()
                && cartEntity.distanceToSqr(player) < 64.0D; // 8 block range
    }

    /**
     * Shift-click: move items between cart inventory and player inventory.
     */
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            copy = slotStack.copy();

            if (index < CART_SLOTS) {
                // Cart → player inventory
                if (!this.moveItemStackTo(slotStack, CART_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Player inventory → cart
                if (!this.moveItemStackTo(slotStack, 0, CART_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return copy;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        cartInventory.stopOpen(player);
    }
}
