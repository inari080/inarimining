package com.inari.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Etherwarp {
    private static final Minecraft mc = Minecraft.getInstance();
    
    private boolean enabled = false;
    private BlockPos targetPosition = null;
    private int maxWarpDistance = 30;
    private int warpDelay = 500; // ミリ秒
    private long lastWarpTime = 0;
    
    /**
     * エーテルワープを有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * エーテルワープが有効かどうか
     * @return 有効な場合true
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * ターゲット位置を設定
     * @param pos ターゲット座標
     */
    public void setTargetPosition(BlockPos pos) {
        this.targetPosition = pos;
    }
    
    /**
     * 最大ワープ距離を設定
     * @param distance 最大距離
     */
    public void setMaxWarpDistance(int distance) {
        this.maxWarpDistance = distance;
    }
    
    /**
     * ワープ遅延を設定
     * @param delay 遅延（ミリ秒）
     */
    public void setWarpDelay(int delay) {
        this.warpDelay = delay;
    }
    
    /**
     * 毎ティック呼び出される更新処理
     */
    public void update() {
        if (!enabled || mc.player == null || targetPosition == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWarpTime < warpDelay) {
            return;
        }
        
        // 距離チェック
        if (isWithinWarpRange(targetPosition)) {
            // 視線をターゲットに向ける
            lookAtBlock(targetPosition);
            
            // エーテルワープを実行
            performWarp(targetPosition);
            lastWarpTime = currentTime;
        }
    }
    
    /**
     * ターゲットがワープ範囲内かどうかを確認
     * @param target ターゲット座標
     * @return 範囲内の場合true
     */
    private boolean isWithinWarpRange(BlockPos target) {
        if (mc.player == null) return false;
        
        BlockPos playerPos = mc.player.blockPosition();
        double distance = Math.sqrt(
            Math.pow(target.getX() - playerPos.getX(), 2) +
            Math.pow(target.getY() - playerPos.getY(), 2) +
            Math.pow(target.getZ() - playerPos.getZ(), 2)
        );
        
        return distance <= maxWarpDistance;
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
     * エーテルワープを実行
     * @param target ターゲット座標
     */
    private void performWarp(BlockPos target) {
        // 実際のエーテルワープはサーバー側のパケットを送信する必要があります
        // ここでは基本的な枠組みのみ実装
        
        if (mc.player == null) return;
        
        // 視線方向に基づいてワープ位置を計算
        Vec3 lookDir = mc.player.getViewVector(1.0f);
        Vec3 warpPos = mc.player.getEyePosition().add(lookDir.scale(maxWarpDistance));
        
        // サーバーに移動パケットを送信（実際の実装では適切なパケットを使用）
        // これはプレースホルダーです
        mc.player.setPos(warpPos.x, warpPos.y, warpPos.z);
    }
    
    /**
     * 指定された方向にエーテルワープを実行
     * @param distance ワープ距離
     */
    public void warpInDirection(double distance) {
        if (!enabled || mc.player == null) return;
        
        Vec3 lookDir = mc.player.getViewVector(1.0f);
        Vec3 warpPos = mc.player.getEyePosition().add(lookDir.scale(distance));
        
        mc.player.setPos(warpPos.x, warpPos.y, warpPos.z);
    }
    
    /**
     * 現在のターゲットを取得
     * @return 現在のターゲット座標
     */
    public BlockPos getTargetPosition() {
        return targetPosition;
    }
    
    /**
     * エーテルワープが可能かどうかを確認
     * @return 可能な場合true
     */
    public boolean canWarp() {
        if (!enabled || mc.player == null || targetPosition == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        return currentTime - lastWarpTime >= warpDelay && isWithinWarpRange(targetPosition);
    }
}
