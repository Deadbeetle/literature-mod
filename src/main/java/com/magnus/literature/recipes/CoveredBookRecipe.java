package com.magnus.literature.recipes;

import com.google.gson.JsonObject;
import com.magnus.literature.LiteratureMod;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AirItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CoveredBookRecipe implements ICraftingRecipe {
	private static final RegistryObject<Item> WRITTEN_BOOK = RegistryObject.of(new ResourceLocation("minecraft:written_book"), ForgeRegistries.ITEMS);
	
	private final ResourceLocation id;

	public CoveredBookRecipe(ResourceLocation idIn) {
		id = idIn;
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(WRITTEN_BOOK.get());
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		ItemStack book = ItemStack.EMPTY;
		ItemStack banner = ItemStack.EMPTY;
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof AirItem) continue;
			if (item instanceof BannerItem && banner.isEmpty())
				banner = stack;
			else if (item instanceof WrittenBookItem && book.isEmpty())
				book = stack;
			else return false;
		}
		return !(book.isEmpty() || banner.isEmpty());
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		ItemStack book = ItemStack.EMPTY;
		ItemStack banner = ItemStack.EMPTY;
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			Item item = stack.getItem();
			if (item instanceof BannerItem && banner.isEmpty())
				banner = stack;
			else if (item instanceof WrittenBookItem && book.isEmpty())
				book = stack;
		}
		
		if (book.isEmpty() || banner.isEmpty()) // Should only happen if the result from matches() gets ignored
			return ItemStack.EMPTY;
		
		CompoundNBT patterns = banner.getChildTag("BlockEntityTag");
		CompoundNBT output_nbt = (patterns == null) ? new CompoundNBT() : patterns.copy();
		output_nbt.putInt("Base", ((BannerItem)banner.getItem()).getColor().getId());
		book.setTagInfo("BlockEntityTag", output_nbt);
		return book;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return LiteratureMod.COVERED_BOOK_RECIPE.get();
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CoveredBookRecipe> {
		@Override
		public CoveredBookRecipe read(ResourceLocation recipeId, JsonObject json) {
			return new CoveredBookRecipe(recipeId);
		}

		@Override
		public CoveredBookRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			return new CoveredBookRecipe(recipeId);
		}

		@Override
		public void write(PacketBuffer buffer, CoveredBookRecipe recipe) {
			// TODO Auto-generated method stub
		}
	}
}
