/**
 * Copyright (c) 2016-2020, Michael Yang Fuhai (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.web.admin;

import com.jfinal.kit.Ret;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;
import io.jboot.utils.FileUtil;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressConsts;
import io.jpress.commons.utils.MarkdownUtils;
import io.jpress.core.addon.AddonInfo;
import io.jpress.core.addon.AddonManager;
import io.jpress.core.addon.AddonUtil;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.web.base.AdminControllerBase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 首页
 * @Package io.jpress.web.admin
 */
@RequestMapping(value = "/admin/addon", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _AddonController extends AdminControllerBase {

    private static final Log LOG = Log.getLog(_AddonController.class);


    @AdminMenu(text = "All addon", groupId = JPressConsts.SYSTEM_MENU_ADDON, order = 0)
    public void list() {

        List<AddonInfo> addons = AddonManager.me().getAllAddonInfos();
        setAttr("addons", addons);
        render("addon/list.html");
    }


    @AdminMenu(text = "Install", groupId = JPressConsts.SYSTEM_MENU_ADDON, order = 5)
    public void install() {
        render("addon/install.html");
    }

    /**
     * 进行插件安装
     */
    public void doUploadAndInstall() {
        if (!isMultipartRequest()) {
            renderError(404);
            return;
        }

        UploadFile ufile = getFile();
        if (ufile == null) {
            renderJson(Ret.fail().set("success", false));
            return;
        }

        if (!StringUtils.equalsAnyIgnoreCase(FileUtil.getSuffix(ufile.getFileName()), ".zip", ".jar")) {
            renderFailAndDeleteFile("Only support .zip or .jar Plug -in file",ufile);
            return;
        }

        AddonInfo addon = AddonUtil.readSimpleAddonInfo(ufile.getFile());
        if (addon == null || StrUtil.isBlank(addon.getId())) {
            renderFailAndDeleteFile("Unable to read the addon configuration information.",ufile);
            return;
        }

        File newAddonFile = addon.buildJarFile();

        //When the plug -in file exists, there are two possibilities
        // 1. The plug-in does exist, and it cannot be installed again at this time
        // 2. The plug-in may not be uninstalled. At this time, you need to try to remove the plug -in that has been uninstalled before
        if (newAddonFile.exists()) {

            //Explain that the plugin has been installed
            if (AddonManager.me().getAddonInfo(addon.getId()) != null) {
                renderFailAndDeleteFile("The addon already exists.",ufile);
                return;
            }
            //The plug-in has been uninstalled before
            else {

                //Try to remove the JAR package again, if it is still unable to delete, it cannot be installed
                if (!AddonUtil.forceDelete(newAddonFile)) {
                    renderFailAndDeleteFile("The addon already exists.",ufile);
                    return;
                }
            }
        }

        if (!newAddonFile.getParentFile().exists()) {
            newAddonFile.getParentFile().mkdirs();
        }

        try {
            FileUtils.moveFile(ufile.getFile(), newAddonFile);
            if (!AddonManager.me().install(newAddonFile)) {
                renderFailAndDeleteFile("The plug-in installation failed, please contact the administrator.",ufile);
                return;
            }
            if (!AddonManager.me().start(addon.getId())) {
                renderFailAndDeleteFile("The plug-in installation failed, please contact the administrator.",ufile);
                return;
            }
        } catch (Throwable e) {
            LOG.error("addon install error : ", e);
            renderFailAndDeleteFile("The plug-in installation failed, please contact the administrator.",ufile);
            deleteFileQuietly(newAddonFile);
            return;
        }

        renderJson(Ret.ok().set("success", true));
    }


    public void upgrade() {

        String id = getPara("id");

        if (StrUtil.isBlank(id)) {
            renderError(404);
            return;
        }

        AddonInfo addonInfo = AddonManager.me().getAddonInfo(id);
        if (addonInfo == null) {
            renderError(404);
            return;
        }

        setAttr("addon", addonInfo);
        render("addon/upgrade.html");
    }

    /**
     * 进行插件安装
     */
    public void doUploadAndUpgrade() {
        if (!isMultipartRequest()) {
            renderError(404);
            return;
        }

        UploadFile ufile = getFile();
        if (ufile == null) {
            renderFailAndDeleteFile(null);
            return;
        }

        String oldAddonId = getPara("id");
        AddonInfo oldAddon = AddonManager.me().getAddonInfo(oldAddonId);
        if (oldAddon == null) {
            renderFailAndDeleteFile("Unable to read the old plug-in information may have been unloaded.", ufile);
            return;
        }

        if (!StringUtils.equalsAnyIgnoreCase(FileUtil.getSuffix(ufile.getFileName()), ".zip", ".jar")) {
            renderFailAndDeleteFile("Only support .zip or .jar plug-in file", ufile);
            return;
        }

        try {
            Ret ret = AddonManager.me().upgrade(ufile.getFile(), oldAddonId);
            render(ret);
            return;
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
            renderFailAndDeleteFile("The plug-in upgrade failed, please contact the administrator", ufile);
            return;
        } finally {
            deleteFileQuietly(ufile.getFile());
        }
    }

    private void deleteFileQuietly(File file) {
        org.apache.commons.io.FileUtils.deleteQuietly(file);
    }

    private void renderFailAndDeleteFile(String msg, UploadFile... uploadFiles) {
        renderJson(Ret.fail()
                .set("success", false)
                .setIfNotBlank("message", msg));

        for (UploadFile ufile : uploadFiles) {
            deleteFileQuietly(ufile.getFile());
        }
    }


    public void doDel() {
        doUninstall();
    }

    public void doInstall() {
        String id = getPara("id");
        if (StrUtil.isBlank(id)) {
            renderJson(Ret.fail().set("message", "ID data cannot be empty"));
            return;
        }
        if (AddonManager.me().install(id)) {
            renderOkJson();
        } else {
            renderJson(Ret.fail().set("message", "The plug-in installation fails, please contact the plug -in developer."));
        }
    }

    public void doUninstall() {
        String id = getPara("id");
        if (StrUtil.isBlank(id)) {
            renderJson(Ret.fail().set("message", "ID data cannot be empty"));
            return;
        }
        if (AddonManager.me().uninstall(id)) {
            renderOkJson();
        } else {
            renderFailJson();
        }
    }

    public void doStart() {
        String id = getPara("id");
        if (StrUtil.isBlank(id)) {
            renderJson(Ret.fail().set("message", "ID data cannot be empty"));
            return;
        }

        if (AddonManager.me().start(id)) {
            renderOkJson();
        } else {
            renderJson(Ret.fail().set("message", "The plug-in was abnormal when starting and failed."));
        }

    }

    public void doStop() {
        String id = getPara("id");
        if (StrUtil.isBlank(id)) {
            renderJson(Ret.fail().set("message", "ID data cannot be empty"));
            return;
        }
        AddonManager.me().stop(id);
        renderOkJson();
    }


    public void readme(){
        String id = getPara("id");
        AddonInfo addonInfo = AddonManager.me().getAddonInfo(id);
        if (addonInfo.getReadmeText() != null){
            setAttr("content",MarkdownUtils.toHtml(addonInfo.getReadmeText()));
        }
        render("addon/readme.html");
    }

    public void changelog(){
        String id = getPara("id");
        AddonInfo addonInfo = AddonManager.me().getAddonInfo(id);
        if (addonInfo.getChangeLogText() != null){
            setAttr("content",MarkdownUtils.toHtml(addonInfo.getChangeLogText()));
        }
        render("addon/changelog.html");
    }


}
