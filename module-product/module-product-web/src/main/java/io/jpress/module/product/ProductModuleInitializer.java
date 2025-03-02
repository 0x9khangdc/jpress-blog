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
package io.jpress.module.product;

import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.jfinal.template.Engine;
import io.jboot.db.model.Columns;
import io.jboot.utils.DateUtil;
import io.jpress.commons.url.FlatUrlHandler;
import io.jpress.core.menu.MenuGroup;
import io.jpress.core.module.ModuleBase;
import io.jpress.module.product.model.Product;
import io.jpress.module.product.service.ProductService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Product module initialization
 * @Package io.jpress
 */
public class ProductModuleInitializer extends ModuleBase {

    @Override
    public void onConfigAdminMenu(List<MenuGroup> adminMenus) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId("product");
        menuGroup.setText("Product");
        menuGroup.setIcon("<i class=\"fas fa-mitten\"></i>");
        menuGroup.setOrder(99);
        adminMenus.add(menuGroup);
    }

    @Override
    public String onRenderDashboardBox(Controller controller) {

        Integer date = controller.getParaToInt("date");

        Columns columns = new Columns();
        columns.eq("status",Product.STATUS_NORMAL);

        //If it is today
        if(date !=null && date == 0){

            columns.between("created", DateUtil.getStartOfDay(new Date()), DateUtil.getEndOfDay(new Date()));
        }

        //At most, check for 28 days
        else if (date != null && date > 0 && date < 29) {

            //Create a calendar object
            Calendar calendar = Calendar.getInstance();

            //Set the current time
            calendar.setTime(new Date());

            //Set the current time plus a few days
            calendar.add(Calendar.DATE, -date);

            columns.between("created", DateUtil.getStartOfDay(calendar.getTime()), DateUtil.getStartOfDay(new Date()));

        }

        List<Product> productList = Aop.get(ProductService.class).findListByColumns(columns, "created desc", 5);
        controller.setAttr("productList", productList);

        return "product/_dashboard_box.html";
    }

    @Override
    public void onEngineConfig(Engine engine) {
        engine.addSharedFunction("/WEB-INF/views/commons/product/defaultProductCommentPage.html");
        engine.addSharedFunction("/WEB-INF/views/commons/product/defaultProductHeader.html");
    }


    @Override
    public void onStart() {
        FlatUrlHandler.addProcesser(new ProductFlatUrlProcesser());
    }
}
