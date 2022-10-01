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
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;
import io.jpress.service.UserService;
import io.jpress.web.base.ApiControllerBase;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Api的拦截器
 * @Package io.jpress.web
 */
public class ApiInterceptor implements Interceptor {


    /**
     * The valid time of API, the default is 10 minutes
     */
    private static final long TIMEOUT = 10 * 60 * 1000;


    @Inject
    private UserService userService;


    @Override
    public void intercept(Invocation inv) {

        boolean apiEnable = JPressOptions.getAsBool(JPressConsts.OPTION_API_ENABLE);
        String apiAppId = JPressOptions.get(JPressConsts.OPTION_API_APPID);
        String apiSecret = JPressOptions.get(JPressConsts.OPTION_API_SECRET);

        // API 功能未启用
        if (apiEnable == false) {
            inv.getController().renderJson(Ret.fail().set("message", "The API function has been closed, please ask the administrator to open in the background"));
            return;
        }

        if (StrUtil.isBlank(apiAppId)) {
            inv.getController().renderJson(Ret.fail().set("message", "The APP ID of the background configuration cannot be empty, please enter the interface management of the background first to configure."));
            return;
        }

        if (StrUtil.isBlank(apiSecret)) {
            inv.getController().renderJson(Ret.fail().set("message", "The API key in the background configuration cannot be empty, please enter the interface management of the background first to configure."));
            return;
        }

        ApiControllerBase controller = (ApiControllerBase) inv.getController();
        String queryString = controller.getRequest().getQueryString();
        if (StrUtil.isBlank(queryString)) {
            inv.getController().renderJson(Ret.fail().set("message", "The request parameters are wrong."));
            return;
        }

        Map<String, String> parasMap = queryStringToMap(queryString);
        String appId = parasMap.get("jpressAppId");
        if (StrUtil.isBlank(appId)) {
            inv.getController().renderJson(Ret.fail().set("message", "JPress Appid content is not obtained in the URL, please note whether the URL is correct."));
            return;
        }


        if (!appId.equals(apiAppId)) {
            inv.getController().renderJson(Ret.fail().set("message", "The client configuration appid is inconsistent with the server configuration."));
            return;
        }


        String timeStr = parasMap.get("ct");
        Long time = timeStr == null ? null : Long.valueOf(timeStr);
        if (time == null) {
            controller.renderJson(Ret.fail("message", "Time parameters cannot be empty, please submit CT parameter data."));
            return;
        }

        // 时间验证，可以防止重放攻击
        if (Math.abs(System.currentTimeMillis() - time) > TIMEOUT) {
            controller.renderJson(Ret.fail("message", "The request timeout, please re -request."));
            return;
        }


        String sign = parasMap.get("sign");
        if (StrUtil.isBlank(sign)) {
            controller.renderJson(Ret.fail("message", "Signature data cannot be empty, please submit SIGN data."));
            return;
        }

        String localSign = createLocalSign(controller.getRequest(), apiSecret);
        if (!sign.equals(localSign)) {
            inv.getController().renderJson(Ret.fail().set("message", "Data signature error."));
            return;
        }

        Object userId = controller.getJwtPara(JPressConsts.JWT_USERID, false);
        if (userId != null) {
            controller.setAttr(JPressConsts.ATTR_LOGINED_USER, userService.findById(userId));
        }

        inv.invoke();
    }


    public static String createLocalSign(HttpServletRequest request, String apiSecret) {
        String queryString = request.getQueryString();
        Map<String, String> queryParas = StrUtil.queryStringToMap(queryString);
        String[] keys = queryParas.keySet().toArray(new String[0]);
        ;
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if ("sign".equals(key)) {
                continue;
            }

            String value = queryParas.get(key);
            sb.append(key).append(value);
        }

        return HashKit.md5(sb.append(apiSecret).toString());
    }


    private static Map<String, String> queryStringToMap(String queryString) {
        if (StrUtil.isBlank(queryString)) {
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        String[] params = queryString.split("&");
        for (String paramPair : params) {
            String[] keyAndValue = paramPair.split("=");
            if (keyAndValue.length == 2) {
                map.put(keyAndValue[0], StrUtil.urlDecode(keyAndValue[1]));
            } else if (keyAndValue.length == 1) {
                map.put(keyAndValue[0], "");
            }
        }
        return map;
    }


}
