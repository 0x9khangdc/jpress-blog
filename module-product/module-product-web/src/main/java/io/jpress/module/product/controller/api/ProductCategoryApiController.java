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
package io.jpress.module.product.controller.api;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.apidoc.ContentType;
import io.jboot.apidoc.annotation.Api;
import io.jboot.apidoc.annotation.ApiOper;
import io.jboot.apidoc.annotation.ApiPara;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.commons.Rets;
import io.jpress.module.product.model.ProductCategory;
import io.jpress.module.product.service.ProductCategoryService;
import io.jpress.web.base.ApiControllerBase;

import javax.validation.constraints.NotNull;

@RequestMapping("/api/product/category")
@Api("Product classification API")
public class ProductCategoryApiController extends ApiControllerBase {

    @Inject
    private ProductCategoryService productCategoryService;


    @ApiOper(value = "Get product classification details", paraNotes = "ID and slug must be passed into a value")
    public Ret detail(@ApiPara("Product ID") Long id, @ApiPara("Product fixed connection") String slug) {
        return Ret.ok();//.set("detail", product);
    }

    @ApiOper("Delete product classification")
    public Ret doDelete(@ApiPara("Product classification ID") @NotNull Long id) {
        productCategoryService.deleteById(id);
        return Rets.OK;
    }

    @ApiOper(value = "Create a new product classification", contentType = ContentType.JSON)
    public Ret doCreate(@ApiPara("Product classification JSON information") @JsonBody ProductCategory productCategory) {
        Object id = productCategoryService.save(productCategory);
        return Ret.ok().set("id", id);
    }

    @ApiOper(value = "Update product classification", contentType = ContentType.JSON)
    public Ret doUpdate(@ApiPara("Product classification JSON information") @JsonBody ProductCategory productCategory) {
        productCategoryService.update(productCategory);
        return Rets.OK;
    }


}
