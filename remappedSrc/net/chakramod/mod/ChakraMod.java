package net.chakramod.mod;

import net.chakramod.mod.block.ModBlocks;
import net.chakramod.mod.block.custom.stoneWorkBench.StoneWorkBenchEntity;
import net.chakramod.mod.gen.ModWorldGen;
import net.chakramod.mod.gen.biome.ModBiomes;
import net.chakramod.mod.gen.features.ModConfiguredFeatures;
import net.chakramod.mod.screen.StoneWorkBenchScreenHandler;
import net.chakramod.mod.entity.MineralSnailEntity;
import net.chakramod.mod.item.ModItemGroup;
import net.chakramod.mod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class ChakraMod implements ModInitializer {
	public static final String MOD_ID = "chakramod";
	// a public identifier for multiple parts of our bigger chest
	public static final Identifier CHAKRAMOD = new Identifier(MOD_ID, "stone_work_bench");

//----------PARTICLES------------

	public static final DefaultParticleType GLOWSTONE_CRYSTAL_PARTICLE = FabricParticleTypes.simple();

//----------GEN--------------



//-------------SPAWN EGG-------------

	public static final Item MINERAL_SNAIL_SPAWN_EGG = new SpawnEggItem(ChakraMod.MINERAL_SNAIL, 16770508, 7758668, new Item.Settings().group(ModItemGroup.MALACHIT));

//--------------ENTITY------------------------

	public static final EntityType<MineralSnailEntity> MINERAL_SNAIL =
			Registry.register(
					Registry.ENTITY_TYPE,
					new Identifier("chakramod", "mineral_snail"),
					FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MineralSnailEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.6F)).build()
			);

	/*public static final EntityType<ChakraVillagerEntity> CHAKRA_VILLAGER =
			Registry.register(
					Registry.ENTITY_TYPE,
					new Identifier("chakramod", "chakra_villager"),
					FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChakraVillagerEntity::new).dimensions(EntityDimensions.fixed(1.0F, 2.0F)).build()
			);*/

//------------ORE GENERATION--------------

	private static ConfiguredFeature<?, ?> ORE_HEART_ORE_OVERWORLD = Feature.ORE
			.configure(new OreFeatureConfig(
					OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
					ModBlocks.HEART_ORE.getDefaultState(),
					2)) // Vein size
			.range(new RangeDecoratorConfig(
					// You can also use one of the other height providers if you don't want a uniform distribution
					UniformHeightProvider.create(YOffset.aboveBottom(0), YOffset.fixed(20)))) // Inclusive min and max height
			.spreadHorizontally()
			.repeat(50); // Number of veins per chunk

//-------------BIOME--------------------



//-----------------VAR------------------

	public static final Logger LOGGER = LogManager.getLogger("chakraMod");

//--------------------BLOCK REG------------------------

	public static final ModBlocks STONE_WORK_BENCH = new ModBlocks(FabricBlockSettings.of(Material.STONE).strength(4).requiresTool());

//---------------SCREEN-------------

	public static ScreenHandlerType<StoneWorkBenchScreenHandler> STONE_WORK_BENCH_SCREEN_HANDLER =
			ScreenHandlerRegistry.registerSimple(new Identifier(ChakraMod.MOD_ID, "stone_work_bench"),
					StoneWorkBenchScreenHandler::new);

//--------------BLOCK ENTITY----------------

	public static BlockEntityType<StoneWorkBenchEntity> STONE_WORK_BENCH_ENTITY;

//---------------INIT---------------

	@Override
	public void onInitialize() {
//-----------GEN INIT-------------

		ModConfiguredFeatures.registerConfiguredFeatures();
		ModBiomes.initBiomes();
		ModBiomes.registerBiomes();
		ModWorldGen.generateModWorldGen();

//------------PARTICLE INIT--------------

		Registry.register(Registry.PARTICLE_TYPE, new Identifier("chakramod", "glowstone_crystal_particle"), GLOWSTONE_CRYSTAL_PARTICLE);

//------------ENTITY SPAWN EGG INIT------------

		Registry.register(Registry.ITEM, new Identifier("chakramod", "mineral_snail_spawn_egg"), MINERAL_SNAIL_SPAWN_EGG);

//---------------ENTITY INIT-----------

		FabricDefaultAttributeRegistry.register(MINERAL_SNAIL, MineralSnailEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 5).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F));
		//FabricDefaultAttributeRegistry.register(CHAKRA_VILLAGER, ChakraVillagerEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 10).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0F));

//--------------GECKO_LIB INIT-----------

		GeckoLib.initialize();

//-------------REG INIT-----------

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

//---------BLOCK REG INIT-------

		STONE_WORK_BENCH_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "chakramod:stone_work_bench_entity",
				FabricBlockEntityTypeBuilder.create(StoneWorkBenchEntity::new, STONE_WORK_BENCH).build(null));

//--------BLOCK GEN INIT---------

		RegistryKey<ConfiguredFeature<?, ?>> oreHeartOreOverWorld = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
				new Identifier("tutorial", "ore_wool_overworld"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreHeartOreOverWorld.getValue(), ORE_HEART_ORE_OVERWORLD);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreHeartOreOverWorld);

//--------LOGGER INIT------

		LOGGER.info("Hello Furry world!");
	}
}
