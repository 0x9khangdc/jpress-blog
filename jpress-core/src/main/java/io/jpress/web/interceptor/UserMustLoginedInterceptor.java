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
import com.jfinal.kit.Ret;
import io.jboot.utils.RequestUtil;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: The user must log in, the interceptor must be placed in UserInterceptor Then execute
 */
public class UserMustLoginedInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {

        if (UserInterceptor.getThreadLocalUser() == null) {
            if (RequestUtil.isAjaxRequest(inv.getController().getRequest())) {
                inv.getController().renderJson(Ret.fail("message", "User not logging in"));
            } else {
                inv.getController().redirect("/user/login");
            }
            return;
        }

        inv.invoke();

    }

}
