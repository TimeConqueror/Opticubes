package io.socol.opticubes;

import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.ArrayUtils;
import timecore.api.common.config.Config;

import java.util.HashSet;

public class OCConfigs {
    public static final MainConfig MAIN = new MainConfig();

    public static void load() {
        MAIN.load();
    }

    public static boolean skipOptiForTile(Class<? extends TileEntity> tileClass) {
        return MAIN.tilesToSkip.contains(tileClass);
    }

    public static class MainConfig extends Config {
        private final HashSet<Class<?>> tilesToSkip = new HashSet<>();

        public MainConfig() {
            super("main");
        }

        @Override
        public void init() {
            tilesToSkip.clear();

            String[] defaults = {"crazypants.enderio.teleport.anchor.TileTravelAnchor"};
            String[] disabledTiles = config.getStringList("disable_opti_for_tiles", "main", defaults, "TileEntity classes, which will be not affected by this mod. Should contain fully-qualified class names.");
            for (String tile : disabledTiles) {
                try {
                    Class<?> clazz = Class.forName(tile, false, getClass().getClassLoader());
                    if (!TileEntity.class.isAssignableFrom(clazz)) {
                        OptiCubes.LOGGER.error("'{}' was not added to OptiCube's exception list: Not a TileEntity", tile);
                        continue;
                    }

                    tilesToSkip.add(clazz);
                } catch (ClassNotFoundException e) {
                    if (!ArrayUtils.contains(defaults, tile)) {
                        OptiCubes.LOGGER.error("'{}' was not added to OptiCube's exception list: Not Found", tile);
                    }
                }
            }
        }

        @Override
        public String getRelativePath() {
            return OptiCubes.MODID + ".cfg";
        }
    }

}
