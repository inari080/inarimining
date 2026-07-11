package com.inari.client.addon;

import com.example.themedgui.client.api.AddonRegistration;
import com.example.themedgui.client.api.ThemedGuiAddon;
import com.inari.client.config.InariMiningConfigData;

/**
 * ThemedGuiModとの連携アドオン。
 * fabric.mod.json の "themedgui:addon" エントリーポイント経由で登録される。
 * 
 * ThemedGuiModのハブスクリーン（Oキー）に「InariMining」として表示される。
 */
public class InariMiningThemedGuiAddon implements ThemedGuiAddon {

    @Override
    public void register(AddonRegistration registration) {
        // InariMiningの設定をThemedGuiModに登録
        registration.registerMod(
                "inarimining",                                    // Mod ID
                "InariMining",                                    // 表示名
                InariMiningConfigData.INSTANCE,                   // 設定インスタンス
                null                                              // ウィジェット登録関数（不要）
        );
    }
}
