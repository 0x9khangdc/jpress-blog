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
package io.jpress.module.article;

import io.jboot.utils.StrUtil;
import io.jpress.core.support.smartfield.SmartField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Article field
 */
public class ArticleFields {

    private List<SmartField> fields = new ArrayList<>();
    private static ArticleFields me = new ArticleFields();

    public static ArticleFields me() {
        return me;
    }

    public void addField(SmartField field) {
        removeField(field.getId()); //防止添加重复的Field
        fields.add(field);
        fields.sort(Comparator.comparingInt(SmartField::getOrderNo));
    }

    public void removeField(String id) {
        if (StrUtil.isBlank(id)) {
            return;
        }
        fields.removeIf(field -> id.equals(field.getId()));
        fields.sort(Comparator.comparingInt(SmartField::getOrderNo));
    }

    public List<SmartField> getFields() {
        return fields;
    }


    public String render(String defaultContent) {
        String content = render();
        return StrUtil.isNotBlank(content) ? content : defaultContent;
    }


    public String render() {
        StringBuilder s = new StringBuilder();
        for (SmartField field : fields) {
            String html = field.render();
            if (html != null) {
                s.append(html);
            }
        }
        return s.toString();
    }

}
