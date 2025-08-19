package net.modfest.fireblanket.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.modfest.fireblanket.mixinsupport.CommandBE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CommandBlockBlockEntity.class)
public abstract class MixinCommandBlockBlockEntity extends BlockEntity implements CommandBE {
	@Unique
	private UUID fireblanket$owner;
	@Unique
	private UUID fireblanket$lastUpdated;

	public MixinCommandBlockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Inject(method = "writeNbt", at = @At("TAIL"))
	private void fireblanket$writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
		if (fireblanket$owner != null) {
			nbt.put("FB:Owner", Uuids.INT_STREAM_CODEC, fireblanket$owner);
		}

		if (fireblanket$lastUpdated != null) {
			nbt.put("FB:LastUpdated", Uuids.INT_STREAM_CODEC, fireblanket$lastUpdated);
		}
	}

	@Inject(method = "readNbt", at = @At("TAIL"))
	private void fireblanket$readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, registries);
		fireblanket$owner = nbt.getList("FB:Owner").map(tag -> Uuids.INT_STREAM_CODEC.parse(ops, tag).getOrThrow()).orElse(null);
		fireblanket$lastUpdated = nbt.getList("FB:LastUpdated").map(tag -> Uuids.INT_STREAM_CODEC.parse(ops, tag).getOrThrow()).orElse(null);
	}

	@Override
	public void fireblanket$setOwner(UUID uuid) {
		this.fireblanket$owner = uuid;
		markDirty();
	}

	@Override
	public void fireblanket$setLastUpdate(UUID uuid) {
		this.fireblanket$lastUpdated = uuid;
		markDirty();
	}

	@Override
	public UUID fireblanket$getOwner() {
		return fireblanket$owner;
	}

	@Override
	public UUID fireblanket$getLastUpdate() {
		return fireblanket$lastUpdated;
	}
}
