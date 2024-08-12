//package com.nippaku_zanmu.trans_addon.mixin;
//
//import com.nippaku_zanmu.trans_addon.TransUtil;
//import meteordevelopment.meteorclient.systems.modules.Category;
//import meteordevelopment.meteorclient.systems.modules.Module;
//import meteordevelopment.meteorclient.utils.Utils;
//import org.spongepowered.asm.mixin.*;
//import org.spongepowered.asm.mixin.gen.Accessor;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(value = Module.class, remap = false)
//public abstract class ModuleMixin {
//    @Shadow
//    @Final
//    @Mutable
//    public String title;
//
//
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    public void onInit(Category category, String name, String description, CallbackInfo ci) {
//
//        title = Utils.nameToTitle(TransUtil.transModuleName(category,name));
//    }
//}
