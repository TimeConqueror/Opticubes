package io.socol.opticubes;

public enum OptiFeature {

    HIDE_TILES("hide_tiles", true),
    HIDE_PARTICLES("hide_particles", true),
    HIDE_ENTITIES("hide_entities", false),
    HIDE_DROPPED_ITEMS("hide_dropped_items", false),
    HIDE_SPECIAL_BLOCKS("hide_special_blocks", false);

    private final String name;
    private final String langKey;
    private final boolean enabledByDefault;
    private final long mask;

    OptiFeature(String name, boolean enabledByDefault) {
        this.name = name;
        this.langKey = OptiCubes.MODID + ".feature." + name;
        this.enabledByDefault = enabledByDefault;
        this.mask = 1 << ordinal();
    }

    public String getName() {
        return name;
    }

    public String getLangKey() {
        return langKey;
    }

    public boolean isEnabled(long featuresMask) {
        return (featuresMask & mask) != 0;
    }

    public static final long DEFAULT_MASK;

    static {
        long defaultMask = 0;
        for (OptiFeature feature : values()) {
            if (feature.enabledByDefault) {
                defaultMask |= feature.mask;
            }
        }
        DEFAULT_MASK = defaultMask;
    }

    public long toggleInMask(long featuresMask) {
        return featuresMask ^ mask;
    }
}
