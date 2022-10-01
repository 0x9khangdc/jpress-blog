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
package io.jpress.web.interceptor;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import io.jboot.utils.CookieUtil;
import io.jboot.utils.StrUtil;
import io.jpress.JPressConfig;
import io.jpress.JPressConsts;
import io.jpress.commons.utils.SessionUtils;
import io.jpress.core.menu.MenuManager;
import io.jpress.model.SiteInfo;
import io.jpress.model.User;
import io.jpress.service.RoleService;
import io.jpress.service.SiteInfoService;
import io.jpress.service.UserService;
import io.jpress.web.handler.JPressHandler;

import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 管理后台的拦截器
 * @Package io.jpress.web
 */
public class AdminInterceptor implements Interceptor {


    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;

    @Inject
    private SiteInfoService siteInfoService;

    public static final String ATTR_PAGINATE_SPACING = "_PAGINATE_SPACING";


    @Override
    public void intercept(Invocation inv) {

        if (JPressHandler.getCurrentTarget().equals(JPressConfig.me.getAdminLoginPage())) {
            inv.getController().forwardAction("/admin/login");
            return;
        }

        if (JPressHandler.getCurrentTarget().equals(JPressConfig.me.getAdminLoginAction())) {
            inv.getController().forwardAction("/admin/doLogin");
            return;
        }

        String uid = CookieUtil.get(inv.getController(), JPressConsts.COOKIE_UID);
        if (StrUtil.isBlank(uid)) {
            redirectLoginPage(inv);
            return;
        }

        if (!SessionUtils.isLoginedOk(Long.valueOf(uid))){
            CookieUtil.remove(inv.getController(),JPressConsts.COOKIE_UID);
            redirectLoginPage(inv);
            return;
        }

        User user = userService.findById(uid);
        if (user == null || !user.isStatusOk()) {
            inv.getController().renderError(404);
            return;
        }

        //不允许没有任何权限的用户访问后台
        if (!roleService.hasAnyRole(user.getId())) {
            inv.getController().renderError(404);
            return;
        }


        inv.getController().setAttr("systemMenuGroups",  MenuManager.me().getSystemMenus());
        inv.getController().setAttr("moduleMenuGroups", MenuManager.me().getModuleMenus());
        inv.getController().setAttr(JPressConsts.ATTR_LOGINED_USER, user);


        List<SiteInfo> allSites = siteInfoService.findAll();
        inv.getController().setAttr("SITES",allSites);

        //Set the interval data of the pagination drop -down menu
        inv.getController().setAttr(ATTR_PAGINATE_SPACING,10);

        inv.invoke();
    }


    private void redirectLoginPage(Invocation inv){
        //When the user is not configured to customize the login page, jump directly to the login page
        if (JPressConfig.DEFAULT_LOGIN_PAGE.equals(JPressConfig.me.getAdminLoginPage())) {
            inv.getController().redirect(JPressConfig.DEFAULT_LOGIN_PAGE);
        }
        //If the user configures the custom login page, it will directly render 404, otherwise it will expose the login page of the user configuration
        //In this way, the background login page of the user configuration is meaningless
        else {
            inv.getController().renderError(404);
        }
    }


}
