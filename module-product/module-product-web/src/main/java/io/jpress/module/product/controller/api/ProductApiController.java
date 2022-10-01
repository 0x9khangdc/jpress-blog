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
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.DefaultValue;
import io.jboot.apidoc.ContentType;
import io.jboot.apidoc.annotation.Api;
import io.jboot.apidoc.annotation.ApiOper;
import io.jboot.apidoc.annotation.ApiPara;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.commons.Rets;
import io.jpress.module.product.model.Product;
import io.jpress.module.product.service.ProductService;
import io.jpress.web.base.ApiControllerBase;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author michael yang (fuhai999@gmail.com)
 * @Date: 2019/11/30
 */
@RequestMapping("/api/product")
@Api("产品相关 API")
public class ProductApiController extends ApiControllerBase {

    @Inject
    private ProductService productService;

    @ApiOper(value = "Get product details", paraNotes = "ID and slug must be passed into a value")
    public Ret detail(@ApiPara("Product ID") Long id, @ApiPara("Product fixed connection") String slug) {
        Product product = id != null ? productService.findById(id)
                : (StrUtil.isNotBlank(slug) ? productService.findFirstBySlug(slug) : null);

        if (product == null || !product.isNormal()) {
            return Rets.FAIL;
        }

        productService.doIncProductViewCount(product.getId());
        return Ret.ok().set("detail", product);
    }


    @ApiPara("Get the product list according to the product FLAG")
    public Ret listByFlag(@ApiPara("Product Identification flag") String flag
            , @ApiPara(value = "Do you need pictures", notes = "True must have pictures, false must have no picture") Boolean hasThumbnail
            , @ApiPara("sort by") String orderBy
            , @ApiPara("Query quantity") @DefaultValue("10") int count) {

        Columns columns = Columns.create("flag", flag);
        if (hasThumbnail != null) {
            if (hasThumbnail) {
                columns.isNotNull("thumbnail");
            } else {
                columns.isNull("thumbnail");
            }
        }

        List<Product> products = productService.findListByColumns(columns, orderBy, count);
        return Ret.ok().set("list", products);
    }


    @ApiPara("Query about the product of a certain product")
    public Ret listByRelevant(@ApiPara("Product ID") @NotNull @Min(1) Long id
            , @ApiPara("Number of products to be query") @DefaultValue("3") int count) {
        List<Product> relevantProducts = productService.findRelevantListByProductId(id, Product.STATUS_NORMAL, count);
        return Ret.ok().set("list", relevantProducts);
    }


    @ApiPara("product search")
    public Ret search(@ApiPara("Keyword") String keyword
            , @ApiPara("Paging page number") @DefaultValue("1") int pageNumber
            , @ApiPara("Number per page") @DefaultValue("10") int pageSize) {
        Page<Product> dataPage = StrUtil.isNotBlank(keyword)
                ? productService.search(keyword, pageNumber, pageSize)
                : null;
        return Ret.ok().set("page", dataPage);
    }


    @ApiOper("Delete product")
    public Ret doDelete(@ApiPara("Product ID") @NotNull Long id) {
        productService.deleteById(id);
        return Rets.OK;
    }


    @ApiOper(value = "New product", contentType = ContentType.JSON)
    public Ret doCreate(@ApiPara("Product JSON information") @JsonBody Product product) {
        Object id = productService.save(product);
        return Ret.ok().set("id", id);
    }


    @ApiOper(value = "Update product information", contentType = ContentType.JSON)
    public Ret doUpdate(@ApiPara("Product JSON information") @JsonBody Product product) {
        productService.update(product);
        return Rets.OK;
    }


}
