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
package io.jpress;

import com.jfinal.kit.PathKit;
import io.jboot.Jboot;
import io.jboot.app.config.annotation.ConfigModel;
import io.jboot.utils.StrUtil;

/**
 * @author Michael Yang Yang Fuhai （fuhai999@gmail.com）
 * @version V1.0
 * @Title: JPress Environmental configuration
 * @Package io.jpress
 */
@ConfigModel(prefix = "jpress")
public class JPressConfig {

    public static final String DEFAULT_LOGIN_PAGE = "/admin/login";

    private String indexAction = "/page";
    private String defaultTemplate = "vn.moment.pluto"; //default template for panel
    private String attachmentRoot; // attachment Directory, without configuration, in the webapp directory
    private String adminLoginPage = DEFAULT_LOGIN_PAGE;         //Login page
    private String adminLoginAction = "/admin/doLogin";     //Login method
    private boolean adminLoginCaptchValidateEnable = true; //Whether the background login is used to verify the verification code


    private String addonRoot; //The plug-in installation directory is convenient for Clear to re-install after the plug-in installation


    public String getIndexAction() {
        return indexAction;
    }

    public void setIndexAction(String indexAction) {
        this.indexAction = indexAction;
    }

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }


    public String getAttachmentRoot() {
        return attachmentRoot;
    }

    public void setAttachmentRoot(String attachmentRoot) {
        this.attachmentRoot = attachmentRoot;
    }

    public String getAdminLoginPage() {
        return adminLoginPage;
    }

    public void setAdminLoginPage(String adminLoginPage) {
        this.adminLoginPage = adminLoginPage;
    }

    public String getAdminLoginAction() {
        return adminLoginAction;
    }

    public void setAdminLoginAction(String adminLoginAction) {
        this.adminLoginAction = adminLoginAction;
    }

    public boolean isAdminLoginCaptchValidateEnable() {
        return adminLoginCaptchValidateEnable;
    }

    public void setAdminLoginCaptchValidateEnable(boolean adminLoginCaptchValidateEnable) {
        this.adminLoginCaptchValidateEnable = adminLoginCaptchValidateEnable;
    }

    public String getAttachmentRootOrWebRoot() {
        return StrUtil.isNotBlank(attachmentRoot)
                ? attachmentRoot
                : PathKit.getWebRootPath();
    }

    public String getAddonRoot() {
        return StrUtil.isNotBlank(addonRoot) ? addonRoot : PathKit.getWebRootPath();
    }

    public void setAddonRoot(String addonRoot) {
        this.addonRoot = addonRoot;
    }

    public static final JPressConfig me = Jboot.config(JPressConfig.class);
}
