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

import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.validate.EmptyValidate;
import io.jboot.web.validate.Form;
import io.jpress.JPressConsts;
import io.jpress.commons.email.Email;
import io.jpress.commons.email.SimpleEmailSender;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.core.module.ModuleListener;
import io.jpress.core.module.ModuleManager;
import io.jpress.web.base.AdminControllerBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 首页
 * @Package io.jpress.web.admin
 */
@RequestMapping(value = "/admin/setting", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _SettingController extends AdminControllerBase {


    @AdminMenu(text = "conventional", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 0)
    public void index() {
        render("setting/base.html");
    }

    @AdminMenu(text = "communication", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 9)
    public void connection() {
        render("setting/connection.html");
    }

    @EmptyValidate({
            @Form(name = "email", message = "The mailbox address cannot be empty")
    })
    public void testEmail() {
        String emailAddr = getPara("email");
        if (!StrUtil.isEmail(emailAddr)) {
            renderFailJson("The mailbox address you entered is wrong.");
            return;
        }

        Email email = Email.create();

        email.subject("This is a test email to JPress");
        email.content("Congratulations to you, receiving this email, prove that you are available in the email configured in the JPress background.");
        email.to(emailAddr);

        SimpleEmailSender ses = new SimpleEmailSender();
        if (!ses.isEnable()) {
            renderFailJson("You can't send it without opening the mail function.");
            return;
        }

        if (!ses.isConfigOk()) {
            renderFailJson("Unconfigured is correct, SMTP or user name or password is empty.");
            return;
        }

        if (!ses.send(email)) {
            renderFailJson("Not configured correctly, SMTP or user name or password error.");
            return;
        }

        renderOkJson();
    }


    @AdminMenu(text = "interface", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 10)
    public void api() {
        render("setting/api.html");
    }


    @AdminMenu(text = "SEO", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 15)
    public void seo() {
        render("setting/seo.html");
    }


    @AdminMenu(text = "log in Register", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 32)
    public void reg() {
        render("setting/reg.html");
    }


    @AdminMenu(text = "Website acceleration", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 33)
    public void cdn() {
        render("setting/cdn.html");
    }

    @AdminMenu(text = "Garbage filter", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 44)
    public void filter() throws Exception {
        render("setting/filter.html");
    }


    @AdminMenu(text = "Small toolbox", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 222)
    public void tools() {
        List<String> moduleIncludes = new ArrayList<>();
        List<ModuleListener> listeners = ModuleManager.me().getListeners();
        for (ModuleListener listener : listeners) {
            String path = listener.onRenderToolsBox(this);
            if (path == null) {
                continue;
            }

            if (path.startsWith("/")) {
                moduleIncludes.add(path);
            } else {
                moduleIncludes.add("/WEB-INF/views/admin/" + path);
            }
        }

        setAttr("moduleIncludes", moduleIncludes);
        render("setting/tools.html");
    }

    //@AdminMenu(text = "icon", groupId = JPressConsts.SYSTEM_MENU_SYSTEM, order = 223)
    public void icons() {
        render("setting/icons.html");
    }

}
