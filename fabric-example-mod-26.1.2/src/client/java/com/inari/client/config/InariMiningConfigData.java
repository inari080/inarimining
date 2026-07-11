package com.inari.client.config;

import com.example.themedgui.client.config.Setting;

/**
 * ThemedGuiModのハブスクリーンで設定を管理するクラス。
 * InariMining機能はこのクラスのフィールドを直接読み書きします。
 */
public class InariMiningConfigData {
    public static final InariMiningConfigData INSTANCE = new InariMiningConfigData();

    // ===== Auto Mining =====
    @Setting(category = "Auto Mining", label = "Enabled", tooltip = "自動マイニングを有効化")
    public boolean autoMineEnabled = false;

    @Setting(category = "Auto Mining", label = "Scan Radius", tooltip = "スキャン範囲（ブロック）", min = 5, max = 64)
    public int autoMineScanRadius = 16;

    @Setting(category = "Auto Mining", label = "Break Delay", tooltip = "ブロック破壊の遅延（ミリ秒）", min = 10, max = 500)
    public int autoMineBreakDelay = 100;

    // ===== Pathfinding =====
    @Setting(category = "Pathfinding", label = "Enabled", tooltip = "パスファインディングを有効化")
    public boolean pathfindingEnabled = false;

    @Setting(category = "Pathfinding", label = "Loop Path", tooltip = "経路をループ実行")
    public boolean pathfindingLoop = false;

    @Setting(category = "Pathfinding", label = "Move Speed", tooltip = "移動速度", min = 0.1f, max = 2.0f)
    public float pathfindingMoveSpeed = 1.0f;

    @Setting(category = "Pathfinding", label = "Reach Distance", tooltip = "到達判定距離", min = 1.0f, max = 10.0f)
    public float pathfindingReachDistance = 5.0f;

    // ===== Etherwarp =====
    @Setting(category = "Etherwarp", label = "Enabled", tooltip = "エーテルワープを有効化")
    public boolean etherwarpEnabled = false;

    @Setting(category = "Etherwarp", label = "Max Distance", tooltip = "最大ワープ距離（ブロック）", min = 10, max = 256)
    public int etherwarpMaxDistance = 100;

    @Setting(category = "Etherwarp", label = "Delay", tooltip = "ワープの遅延（ミリ秒）", min = 10, max = 500)
    public int etherwarpDelay = 50;

    // ===== Inventory Manager =====
    @Setting(category = "Inventory Manager", label = "Enabled", tooltip = "インベントリ管理を有効化")
    public boolean inventoryManagerEnabled = false;

    @Setting(category = "Inventory Manager", label = "Auto Refuel", tooltip = "自動燃料補給")
    public boolean autoRefuel = false;

    @Setting(category = "Inventory Manager", label = "Auto Sort to Sack", tooltip = "自動でサックにソート")
    public boolean autoSortToSack = false;

    @Setting(category = "Inventory Manager", label = "Auto Sell", tooltip = "自動売却")
    public boolean autoSell = false;

    @Setting(category = "Inventory Manager", label = "Min Fuel Level", tooltip = "最小燃料レベル", min = 0, max = 100)
    public int minFuelLevel = 100;

    @Setting(category = "Inventory Manager", label = "Inventory Threshold", tooltip = "インベントリ満杯の閾値", min = 1, max = 64)
    public int inventoryThreshold = 50;

    // ===== Anti-Staff =====
    @Setting(category = "Anti-Staff", label = "Enabled", tooltip = "スタッフ検知を有効化")
    public boolean antiStaffEnabled = false;

    @Setting(category = "Anti-Staff", label = "Auto Logout", tooltip = "自動ログアウト")
    public boolean autoLogout = false;

    @Setting(category = "Anti-Staff", label = "Stop Movement", tooltip = "スタッフ検知時に動きを停止")
    public boolean stopMovement = true;

    @Setting(category = "Anti-Staff", label = "Detection Radius", tooltip = "検知範囲（ブロック）", min = 5, max = 128)
    public int detectionRadius = 30;

    // ===== Macro Check Avoidance =====
    @Setting(category = "Macro Check", label = "Enabled", tooltip = "マクロチェック回避を有効化")
    public boolean macroCheckAvoidanceEnabled = false;
}
