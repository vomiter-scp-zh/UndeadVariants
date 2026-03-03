package com.vomiter.undeadvariants.data;

import com.vomiter.undeadvariants.UndeadVariants;
import com.vomiter.undeadvariants.common.registry.HeadOwner;
import com.vomiter.undeadvariants.common.registry.ModBlocks;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import com.vomiter.undeadvariants.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    String locale;
    static String[] locales = {
            "en_us",
            "zh_tw"
    };

    public ModLangProvider(PackOutput output, String locale) {
        super(output, UndeadVariants.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        // ===== Head =====
        addBlock(
                ModBlocks.HEADS.get(HeadOwner.DROWNED_VILLAGER),
                tr("Drowned Villager Head", "沉屍村民頭顱")
        );
        addBlock(
                ModBlocks.WALL_HEADS.get(HeadOwner.DROWNED_VILLAGER),
                tr("Drowned Villager Head", "沉屍村民頭顱")
        );


        addBlock(
                ModBlocks.HEADS.get(HeadOwner.HUSK_VILLAGER),
                tr("Husk Villager Head", "屍殼村民頭顱")
        );
        addBlock(
                ModBlocks.WALL_HEADS.get(HeadOwner.HUSK_VILLAGER),
                tr("Husk Villager Head", "屍殼村民頭顱")
        );



        // ===== Entities =====
        addEntityType(ModEntityTypes.ZOMBIE_WANDERING_TRADER, tr(
                "Zombie Wandering Trader",
                "殭屍流浪商人"
        ));

        addEntityType(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE, tr(
                "Zombified Piglin Brute",
                "殭屍化豬布林蠻兵"
        ));

        addEntityType(ModEntityTypes.HUSK_VILLAGER, tr(
                "Husk Villager",   // en_us
                "屍殼村民"             // zh_tw
        ));


        addEntityType(ModEntityTypes.DROWNED_PIGLIN, tr(
                "Drowned Piglin",   // en_us
                "沉屍豬布林"             // zh_tw
        ));


        addEntityType(ModEntityTypes.DROWNED_VILLAGER, tr(
                "Drowned Villager",   // en_us
                "沉屍村民"             // zh_tw
        ));

        // ===== Spawn Eggs =====
        addItem(ModItems.ZOMBIE_WANDERING_TRADER_SPAWN_EGG, tr(
                "Zombie Wandering Trader Spawn Egg",
                "殭屍流浪商人 生怪蛋"
        ));


        addItem(ModItems.ZOMBIFIED_PIGLIN_BRUTE_SPAWN_EGG, tr(
                "Zombified Piglin Spawn Egg",
                "殭屍化豬布林蠻兵 生怪蛋"
        ));

        addItem(ModItems.HUSK_VILLAGER_SPAWN_EGG, tr(
                "Husk Villager Spawn Egg",
                "屍殼村民 生怪蛋"
        ));


        addItem(ModItems.DROWNED_PIGLIN_SPAWN_EGG, tr(
                "Drowned Piglin Spawn Egg",
                "沉屍豬布林 生怪蛋"
        ));


        addItem(ModItems.DROWNED_VILLAGER_SPAWN_EGG, tr(
                "Drowned Villager Spawn Egg",
                "沉屍村民 生怪蛋"
        ));

        add("itemGroup." + UndeadVariants.MOD_ID, tr("Undead Variants", "異屍變骸"));
    }

    /**
     * 讓同一份 provider 同時支援 en_us / zh_tw（用 locale 判斷）
     */
    private String tr(String en, String zhTw) {
        return switch (getName()) {
            default -> (locale.equals("zh_tw") ? zhTw : en);
        };
    }
}
