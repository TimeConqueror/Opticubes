package io.socol.opticubes;

public enum OptiFeatures {

    HIDE_TILES(true),
    HIDE_PARTICLES(true),
    HIDE_ENTITIES(false),
    HIDE_SPECIAL_BLOCKS(false);

    private final boolean enabledByDefault;
    private final long mask;

    OptiFeatures(boolean enabledByDefault) {
        this.enabledByDefault =enabledByDefault;
        this.mask = 1 << ordinal();
    }

    public boolean isEnabled(long featuresMask) {
        return (featuresMask & mask) != 0;
    }

    public static final long DEFAULT_MASK;

    static {
        long defaultMask = 0;
        for (OptiFeatures feature : values()) {
            if (feature.enabledByDefault) {
                defaultMask |= feature.mask;
            }
        }
        DEFAULT_MASK = defaultMask;
    }
}
