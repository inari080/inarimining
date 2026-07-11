package com.inari.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockScanner {
    private static final Minecraft mc = Minecraft.getInstance();
    
    // ターゲットとする鉱石ブロックの種類（サーバーに合わせてカスタマイズ）
    private static final List<String> TARGET_BLOCK_NAMES = new ArrayList<>();
    
    static {
        // デフォルトのターゲットブロック（サーバーに合わせて変更）
        TARGET_BLOCK_NAMES.add("diamond_ore");
        TARGET_BLOCK_NAMES.add("iron_ore");
        TARGET_BLOCK_NAMES.add("gold_ore");
        TARGET_BLOCK_NAMES.add("coal_ore");
        TARGET_BLOCK_NAMES.add("redstone_ore");
        TARGET_BLOCK_NAMES.add("lapis_ore");
        TARGET_BLOCK_NAMES.add("emerald_ore");
        TARGET_BLOCK_NAMES.add("copper_ore");
    }
    
    /**
     * ターゲットブロック名を追加
     * @param blockName ブロック名
     */
    public static void addTargetBlockName(String blockName) {
        TARGET_BLOCK_NAMES.add(blockName);
    }
    
    /**
     * ターゲットブロック名をクリア
     */
    public static void clearTargetBlockNames() {
        TARGET_BLOCK_NAMES.clear();
    }
    
    /**
     * 指定された範囲内のターゲットブロックを検索
     * @param center 中心座標
     * @param radius 検索半径
     * @return ターゲットブロックのリスト
     */
    public static List<BlockPos> scanNearbyBlocks(BlockPos center, int radius) {
        List<BlockPos> foundBlocks = new ArrayList<>();
        Level level = mc.level;
        
        if (level == null) {
            return foundBlocks;
        }
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    
                    if (isTargetBlock(state)) {
                        foundBlocks.add(pos);
                    }
                }
            }
        }
        
        return foundBlocks;
    }
    
    /**
     * プレイヤーの周囲のターゲットブロックを検索
     * @param radius 検索半径
     * @return ターゲットブロックのリスト
     */
    public static List<BlockPos> scanNearbyBlocks(int radius) {
        if (mc.player == null) {
            return new ArrayList<>();
        }
        
        BlockPos playerPos = mc.player.blockPosition();
        return scanNearbyBlocks(playerPos, radius);
    }
    
    /**
     * ブロックがターゲットかどうかを判定
     * @param state チェックするブロック状態
     * @return ターゲットの場合true
     */
    public static boolean isTargetBlock(BlockState state) {
        String blockName = state.getBlock().toString().toLowerCase();
        
        for (String target : TARGET_BLOCK_NAMES) {
            if (blockName.contains(target)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 最も近いターゲットブロックを取得
     * @param radius 検索半径
     * @return 最も近いターゲットブロックの座標、見つからない場合はnull
     */
    public static BlockPos findNearestTargetBlock(int radius) {
        List<BlockPos> blocks = scanNearbyBlocks(radius);
        
        if (blocks.isEmpty() || mc.player == null) {
            return null;
        }
        
        BlockPos playerPos = mc.player.blockPosition();
        BlockPos nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (BlockPos pos : blocks) {
            double distance = playerPos.distSqr(pos);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = pos;
            }
        }
        
        return nearest;
    }
}
