package com.nippaku_zanmu.trans_addon.mixin;

import com.nippaku_zanmu.trans_addon.font_fix.FontFix;
import meteordevelopment.meteorclient.renderer.*;
import meteordevelopment.meteorclient.renderer.text.CustomTextRenderer;
import meteordevelopment.meteorclient.renderer.text.Font;
import meteordevelopment.meteorclient.renderer.text.FontFace;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.ByteBuffer;

@Mixin(value = CustomTextRenderer.class, remap = false)
public abstract class CustomTextRendererMixin implements TextRenderer {
    //    @Shadow
//    @Final
//    private Font[] fonts;
    @Shadow
    @Final
    private Mesh mesh;
    //    @Shadow
//    @Final
//    public FontFace fontFace;
//    @Shadow
//    private Font font;
    @Shadow

    private boolean building;
    @Shadow
    private boolean scaleOnly;
    @Shadow
    private double fontScale = 1;
    @Shadow
    private double scale = 1;


    @Unique
    FontFix[] fixedFonts = new FontFix[5];
    @Unique
    private FontFix fixedFont;


    @Inject(method = "<init>", at = @At(value = "RETURN")

        , locals = LocalCapture.CAPTURE_FAILHARD)
    public void onInit(FontFace fontFace, CallbackInfo ci, byte[] bytes, ByteBuffer buffer) {
        for (int i = 0; i < fixedFonts.length; i++) {
            fixedFonts[i] = new FontFix(buffer, (int) Math.round(27 * ((i * 0.5) + 1)));
            //fonts[i] = new FontFix(buffer, (int) Math.round(27 * ((i * 0.5) + 1)));
        }
    }


    /**
     * @author Nippaku_Zanmu
     * @reason FontFix类 如果继承原版Font类回导致渲染乱码 我只能这样做
     */
    @Overwrite
    public double getWidth(String text, int length, boolean shadow) {
        if (text.isEmpty()) return 0;

        FontFix font = building ? this.fixedFont : fixedFonts[0];
        return (font.getWidth(text, length) + (shadow ? 1 : 0)) * scale / 1.5;
    }

    /**
     * @author Nippaku_Zanmu
     * @reason FontFix类 如果继承原版Font类回导致渲染乱码 我只能这样做
     */
    @Overwrite
    public double getHeight(boolean shadow) {
        FontFix font = building ? this.fixedFont : fixedFonts[0];
        return (font.getHeight() + 1 + (shadow ? 1 : 0)) * scale / 1.5;
    }

    /**
     * @author Nippaku_Zanmu
     * @reason FontFix类 如果继承原版Font类回导致渲染乱码 我只能这样做
     */
    @Overwrite
    public void begin(double scale, boolean scaleOnly, boolean big) {
        if (building) throw new RuntimeException("CustomTextRenderer.begin() called twice");

        if (!scaleOnly) mesh.begin();

        if (big) {
            this.fixedFont = fixedFonts[fixedFonts.length - 1];
        } else {
            double scaleA = Math.floor(scale * 10) / 10;

            int scaleI;
            if (scaleA >= 3) scaleI = 5;
            else if (scaleA >= 2.5) scaleI = 4;
            else if (scaleA >= 2) scaleI = 3;
            else if (scaleA >= 1.5) scaleI = 2;
            else scaleI = 1;

            fixedFont = fixedFonts[scaleI - 1];
        }

        this.building = true;
        this.scaleOnly = scaleOnly;

        this.fontScale = fixedFont.getHeight() / 27.0;
        this.scale = 1 + (scale - fontScale) / fontScale;
    }


    /**
     * @author Nippaku_Zanmu
     * @reason FontFix类 如果继承原版Font类回导致渲染乱码 我只能这样做
     */
    @Overwrite
    public void end(MatrixStack matrices) {
        if (!building) throw new RuntimeException("CustomTextRenderer.end() called without calling begin()");

        if (!scaleOnly) {
            mesh.end();

            GL.bindTexture(fixedFont.texture.getGlId());
            mesh.render(matrices);
        }

        building = false;
        scale = 1;
    }

    /**
     * @author Nippaku_Zanmu
     * @reason FontFix类 如果继承原版Font类回导致渲染乱码 我只能这样做
     */
    @Overwrite
    public double render(String text, double x, double y, Color color, boolean shadow) {
        boolean wasBuilding = building;
        if (!wasBuilding) begin();

        double width;
        if (shadow) {
            int preShadowA = CustomTextRenderer.SHADOW_COLOR.a;
            CustomTextRenderer.SHADOW_COLOR.a = (int) (color.a / 255.0 * preShadowA);

            width = fixedFont.render(mesh, text, x + fontScale * scale / 1.5, y + fontScale * scale / 1.5, CustomTextRenderer.SHADOW_COLOR, scale / 1.5);
            fixedFont.render(mesh, text, x, y, color, scale / 1.5);

            CustomTextRenderer.SHADOW_COLOR.a = preShadowA;
        } else {
            width = fixedFont.render(mesh, text, x, y, color, scale / 1.5);
        }

        if (!wasBuilding) end();
        return width;
    }
}
