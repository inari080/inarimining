package com.inari.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class AutoMine {
    private static final Minecraft mc = Minecraft.getInstance();
    
    private boolean enabled = false;
    private BlockPos currentTarget = null;
    private int scanRadius = 16;
    private int breakDelay = 100; // ミリ秒
    private long lastBreakTime = 0;
    
    /**
     * 自動採掘を有効/無効にする
     * @param enabled 有効にするかどうか
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            currentTarget = null;
        }
    }
    
    /**
     * 自動採掘が有効かどうか
     * @return 有効な場合true
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * 毎ティック呼び出される更新処理
     */
    public void update() {
        if (!enabled || mc.player == null || mc.level == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBreakTime < breakDelay) {
            return;
        }
        
        // 現在のターゲットがまだ存在するか確認
        if (currentTarget != null) {
            if (!isValidTarget(currentTarget)) {
                currentTarget = null;
            }
        }
        
        // 新しいターゲットを検索
        if (currentTarget == null) {
            currentTarget = BlockScanner.findNearestTargetBlock(scanRadius);
        }
        
        // ターゲットが見つかった場合、採掘を実行
        if (currentTarget != null) {
            mineBlock(currentTarget);
            lastBreakTime = currentTime;
        }
    }
    
    /**
     * 指定されたブロックを採掘
     * @param pos ブロック座標
     */
    private void mineBlock(BlockPos pos) {
        // 視線をターゲットに向ける
        lookAtBlock(pos);
        
        // ブロックを破壊
        mc.options.keyAttack.setDown(true);
        
        // 短時間後にキーを離す（次のティックで処理）
        mc.options.keyAttack.setDown(false);
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
     * ターゲットが有効かどうかを確認
     * @param pos ブロック座標
     * @return 有効な場合true
     */
    private boolean isValidTarget(BlockPos pos) {
        if (mc.level == null) return false;
        
        return BlockScanner.isTargetBlock(mc.level.getBlockState(pos));
    }
    
    /**
     * 検索半径を設定
     * @param radius 半径
     */
    public void setScanRadius(int radius) {
        this.scanRadius = radius;
    }
    
    /**
     * 破壊遅延を設定
     * @param delay 遅延（ミリ秒）
     */
    public void setBreakDelay(int delay) {
        this.breakDelay = delay;
    }
    
    /**
     * 現在のターゲットを取得
     * @return 現在のターゲット座標
     */
    public BlockPos getCurrentTarget() {
        return currentTarget;
    }
}
