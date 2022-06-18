package com.puradox.practicalbarrels.config;

import com.puradox.practicalbarrels.PracticalBarrelsClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    static Configuration config;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            config = PracticalBarrelsClient.getConfig();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.practical-barrels.config"))
                    .setSavingRunnable(PracticalBarrelsClient::saveConfig);

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory main = builder.getOrCreateCategory(Text.translatable("category.practical-barrels.main"));

            main.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.practical-barrels.accept_damageable_items"), config.acceptDamageableItems)
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("option.practical-barrels.accept_damageable_items.tooltip"))
                    .setSaveConsumer(newValue -> config.acceptDamageableItems = newValue)
                    .build());
            main.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.practical-barrels.accept_unstackable_items"), config.acceptUnstackableItems)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.practical-barrels.accept_unstackable_items.tooltip"))
                    .setSaveConsumer(newValue -> config.acceptUnstackableItems = newValue)
                    .build());
            main.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.practical-barrels.empty_barrel_depositing"), config.emptyBarrelDepositing)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.practical-barrels.empty_barrel_depositing.tooltip"))
                    .setSaveConsumer(newValue -> config.emptyBarrelDepositing = newValue)
                    .build());
            main.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.practical-barrels.auto_deposit"), config.autoDeposit)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.practical-barrels.auto_deposit.tooltip"))
                    .setSaveConsumer(newValue -> config.autoDeposit = newValue)
                    .build());

            return builder.build();
        };
    }
}
