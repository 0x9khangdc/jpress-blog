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
package io.jpress.core.support.smartfield.renders;

import io.jboot.utils.DateUtil;
import io.jpress.core.support.smartfield.SmartField;
import io.jpress.core.support.smartfield.SmartFieldRender;

import java.util.Date;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Datetime 时间选择器的渲染器
 */
public class DatetimeRender implements SmartFieldRender {

//    protected static String template = "" +
//            "<div class=\"form-group row\">\n" +
//            "    <label class=\"col-sm-2 col-form-label\">{label}</label>\n" +
//            "    <div class=\"col-sm-6\">\n" +
//            "        <input type=\"text\" class=\"form-control datetime\"\n" +
//            "               id=\"{id}\"\n" +
//            "               name=\"{name}\"\n" +
//            "               placeholder=\"{placeholder}\" value=\"{value}\" {attrs}/>\n" +
//            "        <p class=\"text-muted\">{helpText}</p>\n" +
//            "    </div>\n" +
//            "</div>";

    protected static String template = "" +
            "<div class=\"form-group\">\n" +
            "    <label class=\"col-sm-12\">{label}</label>\n" +
            "    <div class=\"col-sm-12\">\n" +
            "        <input type=\"text\" class=\"form-control datetime\"\n" +
            "               id=\"{id}\"\n" +
            "               name=\"{name}\"\n" +
            "               placeholder=\"{placeholder}\" value=\"{value}\" {attrs}/>\n" +
            "        <p class=\"text-muted\">{helpText}</p>\n" +
            "    </div>\n" +
            "</div>";

    @Override
    public String onRender(SmartField field, Object value) {
        return RenderKit.render(template, field, DateUtil.toDateTimeString((Date) value));
    }


}
