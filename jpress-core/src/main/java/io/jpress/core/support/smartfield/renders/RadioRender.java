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

import io.jboot.utils.StrUtil;
import io.jpress.core.support.smartfield.SmartField;
import io.jpress.core.support.smartfield.SmartFieldRender;

import java.util.Objects;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Input 输入框的渲染器
 */
public class RadioRender implements SmartFieldRender {

    protected static String template1 = "" +
            "<div class=\"form-group\">\n" +
            "    <label class=\"col-sm-12\">{label}</label>";


    protected static String template_item = "" +
            "   <div class=\"{offset} col-sm-12\">\n" +
            "        <div class=\"radio\">\n" +
            "            <label>\n" +
            "                <input type=\"radio\" name=\"{name}\" value=\"{value}\" {checked}> {text}\n" +
            "            </label>\n" +
            "        </div>\n" +
            "    </div>";


    protected static String template2 = "</div>";

    @Override
    public String onRender(SmartField field, Object value) {
        if (StrUtil.isBlank(field.getValue())) {
            return null;
        }

        String[] values = field.getValue().split(",");
        String[] texts = StrUtil.isBlank(field.getValueText()) ? values : field.getValueText().split(",");


        int index = 0;
        StringBuilder items = new StringBuilder();
        for (String v : values) {
            String item = template_item.replace("{offset}", index == 0 ? "" : "offset-sm-2")
                    .replace("{text}", getText(texts, index++, v))
                    .replace("{checked}", getCheckedText(v, value))
                    .replace("{name}", field.getName())
                    .replace("{value}",v);
            items.append(item);
        }

        return RenderKit.replace(template1, "{label}", field.getLabel()) +
                items + template2;
    }

    private String getCheckedText(String v, Object value) {
        return Objects.equals(v, value) ? "checked" : "";
    }

    private String getText(String[] texts, int i, String v) {
        if (texts != null && texts.length > i) {
            return texts[i];
        }
        return v;
    }
}
