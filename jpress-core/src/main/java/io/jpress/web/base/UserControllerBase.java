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
import io.jpress.web.interceptor.UserInterceptor;
import io.jpress.web.interceptor.UserMustLoginedInterceptor;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web
 */
@Before({UserInterceptor.class, UserMustLoginedInterceptor.class})
public abstract class UserControllerBase extends ControllerBase {


}