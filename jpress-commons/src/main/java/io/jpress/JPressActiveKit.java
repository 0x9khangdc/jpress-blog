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
package io.jpress;

import com.jfinal.plugin.activerecord.Model;
import io.jpress.commons.layer.SortModel;


public class JPressActiveKit {

    /**
     * Used to identify whether the current selection
     */
    public static final String ACTIVE_FLAG = "isActive";

    /**
     * Identify the current object (generally category, menu, etc.)
     *
     * @param model
     */
    public static void makeItActive(Model<?> model) {

        model.put(ACTIVE_FLAG, true);

        if (model instanceof SortModel) {
            SortModel<?> parent = ((SortModel) model).getParent();
            //Theoretically, parent == model This situation cannot exist,
            //At present, just to prevent the code of which rabbit's code is wrong, there will be a dead cycle.
            if (parent != null && parent != model) {
                makeItActive((Model) parent);
            }
        }
    }

}
