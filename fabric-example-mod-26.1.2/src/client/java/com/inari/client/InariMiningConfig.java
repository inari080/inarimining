package com.inari.client;

import com.inari.InariMining;
import com.inari.mining.*;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class InariMiningConfig implements ClientModInitializer {
	// ===== シングルトンインスタンス =====
	public static final InariMiningConfig INSTANCE = new InariMiningConfig();

	// ===== キーバインディング =====
	private static KeyMapping toggleAutoMineKey;
	private static KeyMapping togglePathfindingKey;
	private static KeyMapping toggleEtherwarpKey;
	private static KeyMapping toggleInventoryManagerKey;
	private static KeyMapping toggleAntiStaffKey;
	private static KeyMapping toggleMacroCheckKey;
	private static KeyMapping openConfigKey;

	// ===== 機能インスタンス =====
	private static AutoMine autoMine;
	private static Pathfinding pathfinding;
	private static Etherwarp etherwarp;
	private static InventoryManager inventoryManager;
	private static AntiStaff antiStaff;
	private static MacroCheckAvoidance macroCheckAvoidance;

	// ===== 設定フィールド =====
	// Auto Mining
	public boolean autoMineEnabled = false;
	public int autoMineScanRadius = 16;
	public int autoMineBreakDelay = 100;

	// Pathfinding
	public boolean pathfindingEnabled = false;
	public boolean pathfindingLoop = false;
	public float pathfindingMoveSpeed = 1.0f;
	public float pathfindingReachDistance = 5.0f;

	// Etherwarp
	public boolean etherwarpEnabled = false;
	public int etherwarpMaxDistance = 100;
	public int etherwarpDelay = 50;

	// Inventory Manager
	public boolean inventoryManagerEnabled = false;
	public boolean autoRefuel = false;
	public boolean autoSortToSack = false;
	public boolean autoSell = false;
	public int minFuelLevel = 100;
	public int inventoryThreshold = 50;

	// Anti-Staff
	public boolean antiStaffEnabled = false;
	public boolean autoLogout = false;
	public boolean stopMovement = true;
	public int detectionRadius = 30;

	@Override
	public void onInitializeClient() {
		// 設定をロード（ThemedGuiModが自動的に管理）

		// 各機能の初期化
		autoMine = new AutoMine();
		pathfinding = new Pathfinding();
		etherwarp = new Etherwarp();
		inventoryManager = new InventoryManager();
		antiStaff = new AntiStaff();
		macroCheckAvoidance = new MacroCheckAvoidance();

		// 設定を各機能に適用
		applyConfig();

		// キーバインディングの登録（簡易版 - カテゴリはnullで一時対応）
		toggleAutoMineKey = new KeyMapping(
				"key.inarimining.toggle_automine",
				GLFW.GLFW_KEY_M,
				null
		);

		togglePathfindingKey = new KeyMapping(
				"key.inarimining.toggle_pathfinding",
				GLFW.GLFW_KEY_P,
				null
		);

		toggleEtherwarpKey = new KeyMapping(
				"key.inarimining.toggle_etherwarp",
				GLFW.GLFW_KEY_E,
				null
		);

		toggleInventoryManagerKey = new KeyMapping(
				"key.inarimining.toggle_inventory",
				GLFW.GLFW_KEY_I,
				null
		);

		toggleAntiStaffKey = new KeyMapping(
				"key.inarimining.toggle_antistaff",
				GLFW.GLFW_KEY_S,
				null
		);

		toggleMacroCheckKey = new KeyMapping(
				"key.inarimining.toggle_macrocheck",
				GLFW.GLFW_KEY_C,
				null
		);

		openConfigKey = new KeyMapping(
				"key.inarimining.open_config",
				GLFW.GLFW_KEY_K,
				null
		);

		// クライアントティックイベントの登録はMixinで実装

		InariMining.LOGGER.info("InariMining Client initialized");
	}

	private void handleKeyInputs() {
		// 自動採掘の切り替え
		while (toggleAutoMineKey.consumeClick()) {
			boolean newState = !autoMine.isEnabled();
			autoMine.setEnabled(newState);
			autoMineEnabled = newState;
			save();
			InariMining.LOGGER.info("Auto Mine " + (newState ? "enabled" : "disabled"));
		}

		// パスファインディングの切り替え
		while (togglePathfindingKey.consumeClick()) {
			boolean newState = !pathfinding.isEnabled();
			pathfinding.setEnabled(newState);
			pathfindingEnabled = newState;
			save();
			InariMining.LOGGER.info("Pathfinding " + (newState ? "enabled" : "disabled"));
		}

		// エーテルワープの切り替え
		while (toggleEtherwarpKey.consumeClick()) {
			boolean newState = !etherwarp.isEnabled();
			etherwarp.setEnabled(newState);
			etherwarpEnabled = newState;
			save();
			InariMining.LOGGER.info("Etherwarp " + (newState ? "enabled" : "disabled"));
		}

		// インベントリ管理の切り替え
		while (toggleInventoryManagerKey.consumeClick()) {
			boolean newState = !inventoryManager.isEnabled();
			inventoryManager.setEnabled(newState);
			inventoryManagerEnabled = newState;
			save();
			InariMining.LOGGER.info("Inventory Manager " + (newState ? "enabled" : "disabled"));
		}

		// アンチスタッフの切り替え
		while (toggleAntiStaffKey.consumeClick()) {
			boolean newState = !antiStaff.isEnabled();
			antiStaff.setEnabled(newState);
			antiStaffEnabled = newState;
			save();
			InariMining.LOGGER.info("Anti Staff " + (newState ? "enabled" : "disabled"));
		}

		// マクロチェック回避の切り替え
		while (toggleMacroCheckKey.consumeClick()) {
			boolean newState = !macroCheckAvoidance.isEnabled();
			macroCheckAvoidance.setEnabled(newState);
			save();
			InariMining.LOGGER.info("Macro Check Avoidance " + (newState ? "enabled" : "disabled"));
		}

		// 設定画面を開く
		while (openConfigKey.consumeClick()) {
			save();
			InariMining.LOGGER.info("Config saved");
		}
	}

	private void updateFeatures() {
		// 各機能の更新
		autoMine.update();
		pathfinding.update();
		etherwarp.update();
		inventoryManager.update();
		antiStaff.update();
		macroCheckAvoidance.update();
	}

	private void applyConfig() {
		// 設定を各機能に適用
		autoMine.setEnabled(autoMineEnabled);
		autoMine.setScanRadius(autoMineScanRadius);
		autoMine.setBreakDelay(autoMineBreakDelay);

		pathfinding.setEnabled(pathfindingEnabled);
		pathfinding.setLoopPath(pathfindingLoop);
		pathfinding.setMoveSpeed(pathfindingMoveSpeed);
		pathfinding.setReachDistance(pathfindingReachDistance);

		etherwarp.setEnabled(etherwarpEnabled);
		etherwarp.setMaxWarpDistance(etherwarpMaxDistance);
		etherwarp.setWarpDelay(etherwarpDelay);

		inventoryManager.setEnabled(inventoryManagerEnabled);
		inventoryManager.setAutoRefuel(autoRefuel);
		inventoryManager.setAutoSortToSack(autoSortToSack);
		inventoryManager.setAutoSell(autoSell);
		inventoryManager.setMinFuelLevel(minFuelLevel);
		inventoryManager.setInventoryThreshold(inventoryThreshold);

		antiStaff.setEnabled(antiStaffEnabled);
		antiStaff.setAutoLogout(autoLogout);
		antiStaff.setStopMovement(stopMovement);
		antiStaff.setDetectionRadius(detectionRadius);
	}

	/**
	 * 設定をファイルに保存
	 * TODO: JSONまたはTomlファイルとして保存する実装を追加
	 */
	public void save() {
		// 設定を永続化するコード（後で実装）
		InariMining.LOGGER.debug("Config saved");
	}

	// ===== アクセサー =====
	public static AutoMine getAutoMine() {
		return autoMine;
	}

	public static Pathfinding getPathfinding() {
		return pathfinding;
	}

	public static Etherwarp getEtherwarp() {
		return etherwarp;
	}

	public static InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	public static AntiStaff getAntiStaff() {
		return antiStaff;
	}

	public static MacroCheckAvoidance getMacroCheckAvoidance() {
		return macroCheckAvoidance;
	}

	public static InariMiningConfig getConfig() {
		return INSTANCE;
	}
}
