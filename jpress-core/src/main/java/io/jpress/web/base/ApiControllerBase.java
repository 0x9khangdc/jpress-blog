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
package io.jpress.web.base;

import com.jfinal.aop.Before;
import com.jfinal.core.NotAction;
import com.jfinal.kit.Ret;
import io.jboot.web.render.JbootJsonRender;
import io.jpress.JPressConsts;
import io.jpress.web.interceptor.ApiInterceptor;
import io.jpress.web.interceptor.UserInterceptor;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web
 */
@Before({ApiInterceptor.class, UserInterceptor.class})
public abstract class ApiControllerBase extends ControllerBase {

    public static final int JWT_ERROR_CODE = 401;

    public static final int JWT_LOGICAL_INVALID_CODE = 101;


    protected void renderFailJson(String message) {
        renderJson(Ret.fail("message", message));
    }

    protected void renderFailJson(int code, String message) {
        renderJson(Ret.fail("code", code).set("message", message));
    }

    protected void renderOkJson(String attr, Object value) {
        renderJson(Ret.ok(attr, value));
    }
    protected void renderOkDataJson(Object value){
        renderJson(Ret.ok("data", value));
    }


    /**
     * Get the current WeChat user OpenId
     * @return
     */
    @NotAction
    public String getCurrentWechatOpenId() {
        return getJwtPara(JPressConsts.JWT_OPENID);
    }

    /**
     * Get the current user unionId
     * @return
     */
    @NotAction
    public String getCurrentWechatUnionId() {
        return getJwtPara(JPressConsts.JWT_UNIONID,false);
    }

    /**
     * Get the current user unionId
     * @return
     */
    @NotAction
    public String getCurrentWechatUnionId(boolean validateNullValue) {
        return getJwtPara(JPressConsts.JWT_UNIONID,validateNullValue);
    }

    @Override
    @NotAction
    public <T> T getJwtPara(String name) {
        return getJwtPara(name, true);
    }


    @NotAction
    public <T> T getJwtPara(String name, boolean validateNullValue) {
        T object = super.getJwtPara(name);
        if (object == null && validateNullValue) {
            renderRelogin();
        }
        return object;
    }

    /**
     * Need to register the front end
     */
    @NotAction
    public void renderRelogin() {
        renderError(200, new JbootJsonRender(Ret.ok().set("code", JWT_ERROR_CODE)));
    }


    /**
     * For some cases, the user has been deleted, but the front -end JWT has not expired
     */
    @NotAction
    public void renderJwtLogicalInvalid() {
        renderError(200, new JbootJsonRender(Ret.ok().set("code", JWT_LOGICAL_INVALID_CODE)));
    }


}
