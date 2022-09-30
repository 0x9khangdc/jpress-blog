package io.jpress.addon.helloworld;

import io.jpress.core.addon.AddonBase;
import io.jpress.core.addon.AddonInfo;

/**
 * This is the Hello World project of the JPRESS plug-in, without specific functions.
 *
 * The purpose of its existence is to help developers understand how to develop a jpress plug-in through Hello World
 *
 */
public class HelloWorldAddon extends AddonBase {

    @Override
    public void onInstall(AddonInfo addonInfo) {

        /**
         * In oninstall, we generally need to create our own data table
         *
         * onInstall the method will only be executed once, and it will not be executed again after the execution is completed, unless the user uninstall the plug -in installation again
         */
        System.out.println("HelloWorldAddon onInstall");

    }

    @Override
    public void onUninstall(AddonInfo addonInfo) {

        /**
         *  In OnunStall, we generally need to delete the tables or other resource files created in Oninstall
         *  This method is triggered by the user when uninstalling the plug -in in the JPRESS background.
         */

        System.out.println("HelloWorldAddon onUninstall");
    }

    @Override
    public void onStart(AddonInfo addonInfo) {

        /**
         *  exist onStart In the method, we can do a lot of things, for example: create a menu in the background or user center
         *
         *  This method is executed every time the project is started.
         *
         *  At the same time, users can also trigger in the background
         */

        System.out.println("HelloWorldAddon onStart");
    }

    @Override
    public void onStop(AddonInfo addonInfo) {

        /**
         *  Corresponding to the onStart, what is processed by OnStart should be released in ONSTOP
         *
         *  At the same time, users can also trigger in the background
         */

        System.out.println("HelloWorldAddon onStop");

    }
}
