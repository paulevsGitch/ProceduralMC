package paulevs.proceduralmc.mixin.client;

import java.util.Collection;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.impl.client.texture.FabricSprite;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import paulevs.proceduralmc.InnerRegistry;
import paulevs.proceduralmc.texturing.BufferTexture;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {
	@Inject(method = "loadSprites", at = @At("HEAD"), cancellable = true)
	private void loadSpritesStart(ResourceManager resourceManager, Set<Identifier> ids, CallbackInfoReturnable<Collection<Sprite.Info>> info) {
		ids.removeAll(InnerRegistry.getTextureIDs());
	}
	
	@Inject(method = "loadSprites", at = @At("RETURN"), cancellable = true)
	private void loadSpritesEnd(ResourceManager resourceManager, Set<Identifier> ids, CallbackInfoReturnable<Collection<Sprite.Info>> info) {
		Collection<Sprite.Info> result = info.getReturnValue();
		InnerRegistry.iterateTextures((id, img) -> {
			Sprite.Info spriteInfo = new Sprite.Info(id, img.getWidth(), img.getHeight(), AnimationResourceMetadata.EMPTY);
			result.add(spriteInfo);
		});
		info.setReturnValue(result);
	}
	
	@Inject(method = "loadSprite", at = @At("HEAD"), cancellable = true)
	private void loadSprite(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> callbackInfo) {
		BufferTexture texture = InnerRegistry.getTexture(info.getId());
		if (texture != null) {
			try {
				SpriteAtlasTexture atlas = (SpriteAtlasTexture) (Object) this;
				Sprite sprite = new FabricSprite(atlas, info, maxLevel, atlasWidth, atlasHeight, x, y, texture.makeImage());
				callbackInfo.setReturnValue(sprite);
				callbackInfo.cancel();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
