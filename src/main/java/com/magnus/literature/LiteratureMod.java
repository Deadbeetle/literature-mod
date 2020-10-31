package com.magnus.literature;

import com.magnus.literature.recipes.CoveredBookRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("literature")
public class LiteratureMod {
	public static final String MOD_ID = "literature";
	
	private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
	public static final RegistryObject<CoveredBookRecipe.Serializer> COVERED_BOOK_RECIPE = RECIPE_SERIALIZERS.register("coveredbook", CoveredBookRecipe.Serializer::new);
	
	public LiteratureMod() {
		RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
	}
}
