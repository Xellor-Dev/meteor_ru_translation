package com.nippaku_zanmu.trans_addon;


import com.mojang.logging.LogUtils;
import com.nippaku_zanmu.trans_addon.modules.Translation;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MeteorTranslation extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("MeteorTranslation");
    public static  BufferedWriter bw ;

    static {
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("D:\\hack\\Misc\\meteor-translation-addon\\test\\en_us.json",false), StandardCharsets.UTF_8));
        } catch (FileNotFoundException ignored) {

        }
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // Modules
        Modules.get().add(new Translation());

    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.nippaku_zanmu.trans_addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Nippaku-Zanmu", "meteor-translation-addon");
    }
}
