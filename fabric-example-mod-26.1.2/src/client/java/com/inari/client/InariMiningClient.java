package com.inari.client;

import com.inari.client.config.InariMiningConfigData;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InariMiningクライアント側のエントリーポイント。
 * InariMiningConfigを初期化してからInariMiningClientで
 * 追加初期化（キーバインディング登録等）を実行する。
 */
public class InariMiningClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("inarimining");

	@Override
	public void onInitializeClient() {
		// 設定データを初期化（InariMiningConfigDataが自動的にThemedGuiModで管理される）
		LOGGER.info("InariMiningClient initialized!");
		LOGGER.info("ThemedGui integration enabled - Press O to open settings");
	}
}
