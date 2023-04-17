package tomsumner.chestbrowser.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CustomInventory implements Inventory {
   public BufferedImage gunPowderImage() {
        try {
            File rawImage = new File("assets/chestbrowser/gunpowder.png");
            return ImageIO.read(rawImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private final ItemStack[] inventory;

    public CustomInventory() {
        this.inventory = new ItemStack[54];
    }

    public BufferedImage getVisualRepresentation(Inventory inventory) {
        // Get the dimensions of the image based on the size of the inventory
        int numRows = (inventory.size() + 8) / 9;
        int numCols = Math.min(9, inventory.size());
        int slotSize = 16;
        int width = numCols * slotSize;
        int height = numRows * slotSize;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

// Draw the inventory slot images onto the BufferedImage
        Graphics2D g2d = image.createGraphics();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                ItemStack itemStack = this.getStack(row * 9 + col);
                if (!itemStack.isEmpty()) {
                    // Get the image for the item in the ItemStack and draw it in the appropriate slot
                    BufferedImage itemImage = this.gunPowderImage(); // Not Working Yet - InventoryUtils.getItemImage(itemStack.getItem());
                    g2d.drawImage(itemImage, col * slotSize, row * slotSize, null);
                }
            }
        }

        g2d.dispose();

        return image;

    }

    @Override
    public int size() {
        return inventory.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        if (inventory[slot].isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (inventory[slot].getCount() > count) {
            ItemStack itemStack = inventory[slot].split(count);
            markDirty();
            return itemStack;
        }

        ItemStack itemStack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        markDirty();
        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        Arrays.fill(inventory, ItemStack.EMPTY);
    }

}
