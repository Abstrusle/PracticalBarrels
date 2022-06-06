package com.puradox.practicalbarrels;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class PracticalBarrelsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> { //Occurs after any Screen is initialized. Barrel inventory isn't prepared yet.
            if (screen.getTitle().getString().toLowerCase().contains("barrel") &&
                    screen instanceof GenericContainerScreen) { //Is it a chest-like inventory containing the name 'Barrel'?
                ClientPlayerEntity player = client.player;
                assert player != null;
                ItemStack inputItem = player.getMainHandStack();
                ScreenEvents.afterTick(screen).register((AfterTick) -> { //After inventory is available.
                    GenericContainerScreen invScreen = (GenericContainerScreen) screen;
                    ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
                    Screens.getButtons(screen).add(new ButtonWidget(screen.width / 2 - 100, (screen.height / 2), 10, 10, Text.of("^"),
                            (PressAction) -> {
                                for (int i = 0; i <= invScreen.getScreenHandler().getInventory().size() - 9; i++) {
                                    doItemsCheck(invScreen.getScreenHandler().getInventory().getStack(i), player, invScreen, interactionManager);
                                }
                            }));
                    if (!player.getMainHandStack().isEmpty()) { //Empty hands will always open barrels normally.
                        if (invScreen.getScreenHandler().getInventory().isEmpty() && !inputItem.isEmpty()) { //Right-clicking an empty barrel.
                            doItemsCheck(inputItem, player, invScreen, interactionManager);
                        } else if (!inputItem.isEmpty() && invScreen.getScreenHandler().getInventory().count(inputItem.getItem()) > 0) { //Right-clicking with a related item.
                            doItemsCheck(inputItem, player, invScreen, interactionManager);
                            screen.close();
                        }
                    }
                });
            }
        });
    }
    private void doItemsCheck(ItemStack inputItem, ClientPlayerEntity player, GenericContainerScreen invScreen, ClientPlayerInteractionManager interactionManager) {
        for(Slot slot:player.playerScreenHandler.slots) {
            if(player.playerScreenHandler.getSlot(slot.getIndex()).getStack().getItem().equals(inputItem.getItem())) {
                assert interactionManager != null;
                interactionManager.clickSlot(
                        invScreen.getScreenHandler().syncId,
                        slot.getIndex()+invScreen.getScreenHandler().getInventory().size()-9,
                        0,
                        SlotActionType.QUICK_MOVE, player);
            }
        }
        for(int i=41; i<45; i++) { //For some reason, the above loop doesn't acknowledge slots 41-44.
            if(player.playerScreenHandler.getSlot(i).getStack().getItem().equals(inputItem.getItem())) {
                assert interactionManager != null;
                interactionManager.clickSlot(
                        invScreen.getScreenHandler().syncId,
                        i+invScreen.getScreenHandler().getInventory().size()-9,
                        0,
                        SlotActionType.QUICK_MOVE, player);
            }
        }
    }

}
