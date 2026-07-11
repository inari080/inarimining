package com.inari.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Pathfinding {
    private static final Minecraft mc = Minecraft.getInstance();
    
    private boolean enabled = false;
    private List<BlockPos> waypoints = new ArrayList<>();
    private int currentWaypointIndex = 0;
    private boolean loopPath = true;
    private double moveSpeed = 0.1;
    private double reachDistance = 1.0;
    
    /**
     * 自動ルート巡回を有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * 自動ルート巡回が有効かどうか
     * @return 有効な場合true
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * ウェイポイントを追加
     * @param pos ウェイポイントの座標
     */
    public void addWaypoint(BlockPos pos) {
        waypoints.add(pos);
    }
    
    /**
     * ウェイポイントをクリア
     */
    public void clearWaypoints() {
        waypoints.clear();
        currentWaypointIndex = 0;
    }
    
    /**
     * ウェイポイントのリストを設定
     * @param newWaypoints 新しいウェイポイントリスト
     */
    public void setWaypoints(List<BlockPos> newWaypoints) {
        this.waypoints = new ArrayList<>(newWaypoints);
        this.currentWaypointIndex = 0;
    }
    
    /**
     * パスをループするかどうかを設定
     * @param loop ループする場合true
     */
    public void setLoopPath(boolean loop) {
        this.loopPath = loop;
    }
    
    /**
     * 移動速度を設定
     * @param speed 移動速度
     */
    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed;
    }
    
    /**
     * 到達距離を設定
     * @param distance 到達とみなす距離
     */
    public void setReachDistance(double distance) {
        this.reachDistance = distance;
    }
    
    /**
     * 毎ティック呼び出される更新処理
     */
    public void update() {
        if (!enabled || mc.player == null || waypoints.isEmpty()) {
            return;
        }
        
        BlockPos target = waypoints.get(currentWaypointIndex);
        
        // ターゲットに到達したか確認
        if (hasReachedTarget(target)) {
            // 次のウェイポイントへ
            currentWaypointIndex++;
            
            // 最後のウェイポイントに到達した場合
            if (currentWaypointIndex >= waypoints.size()) {
                if (loopPath) {
                    currentWaypointIndex = 0; // ループ
                } else {
                    enabled = false; // 終了
                    return;
                }
            }
        }
        
        // ターゲットに向かって移動
        moveToTarget(target);
    }
    
    /**
     * ターゲットに到達したかどうかを確認
     * @param target ターゲット座標
     * @return 到達した場合true
     */
    private boolean hasReachedTarget(BlockPos target) {
        if (mc.player == null) return false;
        
        Vec3 playerPos = mc.player.position();
        Vec3 targetPos = Vec3.atCenterOf(target);
        
        return playerPos.distanceTo(targetPos) <= reachDistance;
    }
    
    /**
     * ターゲットに向かって移動
     * @param target ターゲット座標
     */
    private void moveToTarget(BlockPos target) {
        if (mc.player == null) return;
        
        // 視線をターゲットに向ける
        lookAtBlock(target);
        
        // 前進キーを押す
        mc.options.keyUp.setDown(true);
    }
    
    /**
     * 指定されたブロックに視線を向ける
     * @param pos ブロック座標
     */
    private void lookAtBlock(BlockPos pos) {
        if (mc.player == null) return;
        
        Vec3 playerPos = mc.player.getEyePosition();
        Vec3 targetPos = Vec3.atCenterOf(pos);
        
        Vec3 direction = targetPos.subtract(playerPos).normalize();
        
        double yaw = Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90;
        double pitch = Math.toDegrees(-Math.asin(direction.y));
        
        mc.player.setYRot((float) yaw);
        mc.player.setXRot((float) pitch);
    }
    
    /**
     * 現在のウェイポイントインデックスを取得
     * @return 現在のインデックス
     */
    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }
    
    /**
     * ウェイポイントのリストを取得
     * @return ウェイポイントリスト
     */
    public List<BlockPos> getWaypoints() {
        return new ArrayList<>(waypoints);
    }
    
    /**
     * 現在のターゲットを取得
     * @return 現在のターゲット座標
     */
    public BlockPos getCurrentTarget() {
        if (waypoints.isEmpty()) {
            return null;
        }
        return waypoints.get(currentWaypointIndex);
    }
}
