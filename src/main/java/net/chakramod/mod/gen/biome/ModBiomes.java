package net.chakramod.mod.gen.biome;

import net.chakramod.mod.ChakraMod;
import net.chakramod.mod.block.ModBlocks;
import net.chakramod.mod.gen.features.ModConfiguredFeatures;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModBiomes {
    public static final RegistryKey<Biome> CHAKRA_BIOME_KEY = registerKey("chakra_biome");
    public static final Biome CHAKRA_BIOME = createChakraBiome();

    private static Biome createChakraBiome(){
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        generationSettings.surfaceBuilder(ModSurfaceConfigs.CHAKRA_SURFACE_BUILDER);

        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModConfiguredFeatures.HEART_ORE);

        DefaultBiomeFeatures.addSprings(generationSettings);

        return(new Biome.Builder()).category(Biome.Category.NONE)
                .depth(0.125F).scale(0.1F).temperature(0.8F).downfall(0.4F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(65535).waterFogColor(65535)
                        .fogColor(0).skyColor(0)
                        .build()).spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build()).build();
    }

    public static RegistryKey<Biome> registerKey(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier(ChakraMod.MOD_ID, name));
    }

    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, CHAKRA_BIOME_KEY.getValue(), CHAKRA_BIOME);

        OverworldBiomes.addContinentalBiome(CHAKRA_BIOME_KEY, OverworldClimate.DRY, 1D);
    }

    public static void initBiomes() {
        System.out.println("Registering ModBiomes for " + ChakraMod.MOD_ID);
    }
}
