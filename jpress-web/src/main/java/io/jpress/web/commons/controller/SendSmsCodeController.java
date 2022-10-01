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
package io.jpress.web.commons.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressOptions;
import io.jpress.commons.sms.SmsKit;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web
 */
@RequestMapping("/commons/getsmscode")
public class SendSmsCodeController extends Controller {


    public void index() {
        if (!validateCaptcha("captcha")) {
            renderJson(Ret.fail().set("message", "Verification code error, please re-enter"));
            return;
        }

        String phone = getPara("phone");
        if (StrUtil.isBlank(phone)) {
            renderJson(Ret.fail().set("message", "The mobile phone number cannot be empty ..."));
            return;
        }

        if (!StrUtil.isMobileNumber(phone)) {
            renderJson(Ret.fail().set("message", "The mobile phone number you entered is incorrect"));
            return;
        }


        String code = SmsKit.generateCode();
        String template = JPressOptions.get("reg_sms_validate_template");
        String sign = JPressOptions.get("reg_sms_validate_sign");

        boolean sendOk = SmsKit.sendCode(phone, code, template, sign);

        if (sendOk) {
            renderJson(Ret.ok().set("message", "Successful SMS sending, please check your mobile phone"));
        } else {
            renderJson(Ret.fail().set("message", "SMS failed, please contact the administrator"));
        }

    }


}
