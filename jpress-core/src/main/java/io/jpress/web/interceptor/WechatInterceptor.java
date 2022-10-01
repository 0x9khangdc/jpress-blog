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


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import io.jboot.utils.CookieUtil;
import io.jboot.utils.RequestUtil;
import io.jboot.utils.StrUtil;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: WeChat obtain user-related interceptors
 */

public class WechatInterceptor implements Interceptor{


    @Override
    public void intercept(Invocation inv) {

        boolean enable = JPressOptions.getAsBool(JPressConsts.OPTION_WECHAT_WEB_AUTHORIZE_ENABLE);

        if (!enable) {
            inv.invoke();
            return;
        }


        String uid = CookieUtil.get(inv.getController(), JPressConsts.COOKIE_UID);
        if (StrUtil.isNotBlank(uid)) {
            inv.invoke();
            return;
        }

        if (!RequestUtil.isWechatBrowser(inv.getController().getRequest())) {
            inv.invoke();
            return;
        }

        String gotoUrl = getGotoUrl(inv.getController());
        inv.getController().redirect("/wechat/authorization?goto=" + gotoUrl);
    }

    /**
     * Get the current URL
     *
     * @param controller
     * @return
     */
    public static String getGotoUrl(Controller controller) {

        HttpServletRequest req = controller.getRequest();

        // Get the path that the user is going to go
        String queryString = req.getQueryString();

        // The request URL before being intercepted
        String url = req.getScheme() + "://" + req.getServerName() + req.getRequestURI();
        if (StrUtil.isNotBlank(queryString)) {
            url = url.concat("?").concat(queryString);
        }

        return StrUtil.urlEncode(url);
    }
}
