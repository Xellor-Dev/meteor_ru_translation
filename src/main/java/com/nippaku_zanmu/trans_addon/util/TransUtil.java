package com.nippaku_zanmu.trans_addon.util;

import com.nippaku_zanmu.trans_addon.MeteorTranslation;
import com.nippaku_zanmu.trans_addon.modules.Translation;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class TransUtil {
    private static String trans(String s) {
        return Text.translatable(s).getString();
    }
    public static Set<String> getAddonsName(){
       return AddonManager.ADDONS.stream().map(addon->addon.name).map(TransUtil::baseFormat).collect(Collectors.toSet());
    }

    public static String transModuleName(Module m) {
        Category c = m.category;
        String moduleName = m.name;
        String key = "meteor." + baseFormat(c.name) + "." + baseFormat(moduleName);
        //转换成规范的key
        //meteor.模块类型.模块名

        dumpEn_USJson(key, moduleName);
        //开发者使用 导出英文语言文件

        String trans = trans(key);
        //调用mc函数翻译

        if (trans.equals(key)) {
            return moduleName;
        }//如果没有翻译 即翻译后的还是原本的key 则返回原模块名称
        return trans;
    }

    public static String transModuleDescription(Module m) {
        Category c = m.category;
        String moduleName = m.name;
        String moduleDes = m.description;
        String key = "meteor." + baseFormat(c.name) + "." + baseFormat(moduleName) + ".description";
        //转换成规范的key
        dumpEn_USJson(key, moduleDes);

        String trans = trans(key);
        //调用mc函数翻译

        if (trans.equals(key)) {
            return moduleDes;
        }//如果没有翻译 即翻译后的还是原本的key 则返回原名称
        return trans;
    }

    public static String transSettingName(Module mod, SettingGroup group, Setting s) {
        String key = "meteor." +
            baseFormat(mod.category.name) + "." +
            baseFormat(mod.name) + "." +
            "setting." +
            group.name + "." +
            s.name;

        dumpEn_USJson(key, s.name);
        //dump

        String trans = trans(key);
        //调用mc函数翻译

        if (trans.equals(key)) {
            return s.name;
        }//如果没有翻译 即翻译后的还是原本的key 则返回原名称
        return trans;
    }

    public static String transSettingDes(Module mod, SettingGroup group, Setting s) {
        String key = "meteor." +
            baseFormat(mod.category.name) + "." +
            baseFormat(mod.name) + "." +
            "setting." +
            group.name + "." +
            s.name + "." +
            "description";

        dumpEn_USJson(key, s.description);
        //dump

        String trans = trans(key);
        //调用mc函数翻译

        if (trans.equals(key)) {
            return s.description;
        }//如果没有翻译 即翻译后的还是原本的key 则返回原名称
        return trans;
    }


    public static String baseFormat(String s) {
        //把某些Addon作者的不规范模块命名还原
        s = s.toLowerCase();
        s = s.replace(" ", "_");
        s = s.replace("-", "_");
        return s;
    }

    public static void dumpEn_USJson(String key, String en_usName) {
        if (!Modules.get().get(Translation.class).bSetDumpFile.get()) return;

        try {
            en_usName = en_usName.replace("\"", "\\\"");
            MeteorTranslation.bw.write("\"" + key + "\"" + ":" + "\"" + en_usName + "\"" + ",");
            MeteorTranslation.bw.newLine();
            MeteorTranslation.bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
