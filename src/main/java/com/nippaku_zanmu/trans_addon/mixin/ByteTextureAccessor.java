package com.nippaku_zanmu.trans_addon.mixin;

import meteordevelopment.meteorclient.utils.render.ByteTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.ByteBuffer;

@Mixin(value = ByteTexture.class,remap = false)
public interface ByteTextureAccessor {
    //@Invoker("upload")
    //void upload(int width, int height, ByteBuffer buffer, ByteTexture.Format format, ByteTexture.Filter filterMin, ByteTexture.Filter filterMag);
}
