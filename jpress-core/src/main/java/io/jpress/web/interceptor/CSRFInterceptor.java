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
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.render.TextRender;
import io.jboot.utils.CookieUtil;
import io.jboot.utils.RequestUtil;
import io.jboot.utils.StrUtil;
import io.jboot.web.JbootWebConfig;
import io.jpress.JPressConsts;
import io.jpress.commons.utils.SessionUtils;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: For prevention csrf Insertor interceptor
 * <p>
 * Remarks: This interceptor only intercepts the way to start with Do.E.g: 127.0.0.1/user/doDel?id=xxx
 */
public class CSRFInterceptor implements Interceptor {

    public static final String CSRF_ATTR_KEY = "CSRF_TOKEN";
    public static final String CSRF_KEY = "csrf_token";

    private static final String CSRF_METHOD_PREFIX = "do";
    private static final String encryptKey = JbootWebConfig.getInstance().getCookieEncryptKey();


    @Override
    public void intercept(Invocation inv) {

        String uid = CookieUtil.get(inv.getController(), JPressConsts.COOKIE_UID);
        if (StrUtil.isBlank(uid) || !SessionUtils.isLoginedOk(uid)) {
            // No need to control the user's not logging in
            inv.invoke();
            return;
        }


        //It's not from the beginning, let it pass
        //There is a consensus in JPRESS: as long as it is an increase, delete, and modified operation, the method will be named by Do to start
        String methodName = inv.getMethodName();
        if (!methodName.startsWith(CSRF_METHOD_PREFIX)) {
            renderNormal(inv);
            return;
        }

        //It is a lowercase letter or number, Chinese and other non -uppercase letters. At this time, it may be a word such
        if (!Character.isUpperCase(methodName.charAt(2))) {
            renderNormal(inv);
            return;
        }

        //Read from cookies, because third -party websites cannot modify and obtain cookies
        //So it is safe to obtain the stored token from cookie
        String cookieToken = inv.getController().getCookie(CSRF_KEY);
        if (StrUtil.isBlank(cookieToken)) {
            renderBad(inv);
            return;
        }

        //csrf_token in the url parameter
        String paraToken = inv.getController().getPara(CSRF_KEY);
        if (StrUtil.isBlank(paraToken)) {
            renderBad(inv);
            return;
        }

        if (!cookieToken.equals(paraToken)) {
            renderBad(inv);
            return;
        }

        renderNormal(inv);
    }


    private void renderNormal(Invocation inv) {
        // If it is not AJAX request, you need to reset the local token
        // AJAX request, you need to ensure that the previous Token can continue to use
        if (!RequestUtil.isAjaxRequest(inv.getController().getRequest())) {
            String scrfToken = createSCRFToken(inv);
            inv.getController().setCookie(CSRF_KEY, scrfToken, -1);
            inv.getController().setAttr(CSRF_ATTR_KEY, scrfToken);
        }

        inv.invoke();
    }

    public static String createSCRFToken(Invocation inv) {
        String userId = CookieUtil.get(inv.getController(), JPressConsts.COOKIE_UID);

        //user not login
        if (StrUtil.isBlank(userId)) {
            return null;
        }

        return HashKit.md5(SessionUtils.getSessionId(userId) + encryptKey);
    }


    private static final Ret FAIL_RET = Ret.fail().set("message", "Token is invalid, for safety reasons, please refresh it and try it out.");

    private void renderBad(Invocation inv) {
        if (RequestUtil.isAjaxRequest(inv.getController().getRequest())) {
            inv.getController().renderJson(FAIL_RET);
        } else {
            inv.getController().renderError(403, new TextRender("bad or missing csrf token!"));
        }
    }

}
