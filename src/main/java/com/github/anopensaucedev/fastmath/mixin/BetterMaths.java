package com.github.anopensaucedev.fastmath.mixin;

import com.github.anopensaucedev.fastmath.Util.FastRandom;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(MathHelper.class)
public class BetterMaths {

    @Overwrite
    public static float nextGaussian(Random random, float mean, float deviation) {
        return mean + (float) FastRandom.FastRandomGaussian() * deviation;
    }

    @Overwrite
    public static int nextBetween(Random random, int min, int max) {
        return calculateRandomValue(min, max, () -> (int) FastRandom.FastRandomInt(max - min + 1) + min);
    }

    @Overwrite
    public static float nextBetween(Random random, float min, float max) {
        return calculateRandomValue(min, max, () -> FastRandom.FastRandomFloat() * (max - min) + min);
    }

    @Overwrite
    public static UUID randomUuid(Random random) {
        long l = FastRandom.FastRandomLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        long m = FastRandom.FastRandomLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(l, m);
    }

    private static <T extends Number> T calculateRandomValue(T min, T max, RandomValueSupplier<T> supplier) {
        if (min.doubleValue() >= max.doubleValue()) {
            throw new IllegalArgumentException("Min must be less than max");
        }
        return supplier.get();
    }

    @FunctionalInterface
    private interface RandomValueSupplier<T> {
        T get();
    }
}
