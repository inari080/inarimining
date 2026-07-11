package com.inari.mining;

import com.inari.InariMining;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class AntiStaff {
    private static final Minecraft mc = Minecraft.getInstance();
    
    private boolean enabled = false;
    private boolean autoLogout = true;
    private boolean stopMovement = true;
    private int detectionRadius = 64;
    private List<String> whitelistedPlayers = new ArrayList<>();
    private List<String> staffNames = new ArrayList<>();
    
    /**
     * 管理者・対人回避を有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * 管理者・対人回避が有効かどうか
     * @return 有効な場合true
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 自動ログアウトを有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setAutoLogout(boolean enabled) {
        this.autoLogout = enabled;
    }
    
    /**
     * 移動停止を有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setStopMovement(boolean enabled) {
        this.stopMovement = enabled;
    }
    
    /**
     * 検出半径を設定
     * @param radius 半径
     */
    public void setDetectionRadius(int radius) {
        this.detectionRadius = radius;
    }
    
    /**
     * ホワイトリストにプレイヤーを追加
     * @param playerName プレイヤー名
     */
    public void addWhitelistedPlayer(String playerName) {
        whitelistedPlayers.add(playerName);
    }
    
    /**
     * ホワイトリストからプレイヤーを削除
     * @param playerName プレイヤー名
     */
    public void removeWhitelistedPlayer(String playerName) {
        whitelistedPlayers.remove(playerName);
    }
    
    /**
     * スタッフ名を追加
     * @param staffName スタッフ名
     */
    public void addStaffName(String staffName) {
        staffNames.add(staffName);
    }
    
    /**
     * 毎ティック呼び出される更新処理
     */
    public void update() {
        if (!enabled || mc.player == null || mc.level == null) {
            return;
        }
        
        // 近くのプレイヤーを検出
        List<Player> nearbyPlayers = detectNearbyPlayers();
        
        // スタッフや他のプレイヤーが近くにいるか確認
        for (Player nearbyPlayer : nearbyPlayers) {
            if (shouldReactToPlayer(nearbyPlayer)) {
                handlePlayerDetection(nearbyPlayer);
                return;
            }
        }
    }
    
    /**
     * 近くのプレイヤーを検出
     * @return 近くのプレイヤーリスト
     */
    private List<Player> detectNearbyPlayers() {
        List<Player> nearbyPlayers = new ArrayList<>();
        
        if (mc.level == null || mc.player == null) {
            return nearbyPlayers;
        }
        
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof Player && entity != mc.player) {
                Player player = (Player) entity;
                
                // 距離チェック
                double distance = mc.player.distanceTo(player);
                if (distance <= detectionRadius) {
                    nearbyPlayers.add(player);
                }
            }
        }
        
        return nearbyPlayers;
    }
    
    /**
     * プレイヤーに反応すべきかどうかを判定
     * @param player プレイヤー
     * @return 反応すべき場合true
     */
    private boolean shouldReactToPlayer(Player player) {
        String playerName = player.getName().getString();
        
        // ホワイトリストに含まれる場合は反応しない
        if (whitelistedPlayers.contains(playerName)) {
            return false;
        }
        
        // スタッフ名に含まれる場合は反応する
        if (staffNames.contains(playerName)) {
            return true;
        }
        
        // 他のプレイヤー全員に反応する設定の場合
        return true;
    }
    
    /**
     * プレイヤー検出時の処理
     * @param player 検出されたプレイヤー
     */
    private void handlePlayerDetection(Player player) {
        String playerName = player.getName().getString();
        
        // スタッフの場合はより厳しく対応
        if (staffNames.contains(playerName)) {
            if (autoLogout) {
                // 即座にログアウト
                logout();
            }
            if (stopMovement) {
                // 全ての機能を停止
                stopAllFeatures();
            }
        } else {
            // 一般プレイヤーの場合
            if (stopMovement) {
                // 一時的に機能を停止
                pauseFeatures();
            }
        }
    }
    
    /**
     * ログアウトを実行
     */
    private void logout() {
        if (mc.player != null) {
            // ワールドから切断（簡易実装 - 実際のサーバーでは適切なパケットを送信）
            // ここではプレースホルダー
            InariMining.LOGGER.info("Staff detected - would disconnect");
        }
    }
    
    /**
     * 全ての機能を停止
     */
    private void stopAllFeatures() {
        // 他の機能への参照が必要
        // InariMiningClientを通じて各機能を停止
        // ここでは基本的な枠組みのみ実装
    }
    
    /**
     * 機能を一時停止
     */
    private void pauseFeatures() {
        // 他の機能への参照が必要
        // InariMiningClientを通じて各機能を一時停止
        // ここでは基本的な枠組みのみ実装
    }
    
    /**
     * ホワイトリストを取得
     * @return ホワイトリスト
     */
    public List<String> getWhitelistedPlayers() {
        return new ArrayList<>(whitelistedPlayers);
    }
    
    /**
     * スタッフ名リストを取得
     * @return スタッフ名リスト
     */
    public List<String> getStaffNames() {
        return new ArrayList<>(staffNames);
    }
    
    /**
     * 近くにプレイヤーがいるかどうかを確認
     * @return プレイヤーがいる場合true
     */
    public boolean isPlayerNearby() {
        if (!enabled || mc.player == null || mc.level == null) {
            return false;
        }
        
        List<Player> nearbyPlayers = detectNearbyPlayers();
        return !nearbyPlayers.isEmpty();
    }
    
    /**
     * 近くにスタッフがいるかどうかを確認
     * @return スタッフがいる場合true
     */
    public boolean isStaffNearby() {
        if (!enabled || mc.player == null || mc.level == null) {
            return false;
        }
        
        List<Player> nearbyPlayers = detectNearbyPlayers();
        
        for (Player player : nearbyPlayers) {
            String playerName = player.getName().getString();
            if (staffNames.contains(playerName)) {
                return true;
            }
        }
        
        return false;
    }
}
