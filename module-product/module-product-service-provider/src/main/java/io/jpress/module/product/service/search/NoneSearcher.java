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
package io.jpress.module.product.service.search;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.module.product.model.Product;

public class NoneSearcher implements ProductSearcher {

    @Override
    public void addProduct(Product product) {

    }

    @Override
    public void deleteProduct(Object id) {

    }

    @Override
    public void updateProduct(Product product) {

    }

    @Override
    public Page<Product> search(String keyword, int pageNum, int pageSize) {
        return null;
    }
}
