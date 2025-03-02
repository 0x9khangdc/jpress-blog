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
package io.jpress.module.article.directive;

import com.jfinal.aop.Inject;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import io.jboot.web.controller.JbootControllerContext;
import io.jboot.web.directive.annotation.JFinalDirective;
import io.jboot.web.directive.base.JbootDirectiveBase;
import io.jboot.web.directive.base.PaginateDirectiveBase;
import io.jpress.commons.utils.UrlUtils;
import io.jpress.module.article.model.Article;
import io.jpress.module.article.model.ArticleCategory;
import io.jpress.module.article.service.ArticleService;
import io.jpress.web.base.TemplateControllerBase;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 */
@JFinalDirective("articlePage")
public class ArticlePageDirective extends JbootDirectiveBase {

    @Inject
    private ArticleService service;

    @Override
    public void onRender(Env env, Scope scope, Writer writer) {

        TemplateControllerBase controller = (TemplateControllerBase) JbootControllerContext.get();

        int page = controller.getPageNumber();
        int pageSize = getParaToInt("pageSize", scope, 10);
        String orderBy = getPara("orderBy", scope, "id desc");

        // 可以指定当前的分类ID
        Long categoryId = getParaToLong("categoryId", scope, 0L);
        ArticleCategory category = controller.getAttr("category");

        if (categoryId == 0 && category != null) {
            categoryId = category.getId();
        }

        Page<Article> articlePage = categoryId == 0
                ? service.paginateInNormal(page, pageSize, orderBy)
                : service.paginateByCategoryIdInNormal(page, pageSize, categoryId, orderBy);

        scope.setGlobal("articlePage", articlePage);
        renderBody(env, scope, writer);
    }

    @Override
    public boolean hasEnd() {
        return true;
    }


    @JFinalDirective("articlePaginate")
    public static class TemplatePaginateDirective extends PaginateDirectiveBase {

        @Override
        protected String getUrl(int pageNumber, Env env, Scope scope, Writer writer) {

            ArticleCategory category = JbootControllerContext.get().getAttr("category");
            if (category != null) {
                return category.getUrlWithPageNumber(pageNumber);
            } else {
                boolean firstGotoIndex = getPara("firstGotoIndex", scope, false);
                if (pageNumber == 1 && firstGotoIndex) {
                    return JFinal.me().getContextPath() + "/";
                }
                if (pageNumber > 1) {
                    return UrlUtils.getUrl("/articles", "/", pageNumber);
                } else {
                    return UrlUtils.getUrl("/articles");
                }
            }
        }

        @Override
        protected Page<?> getPage(Env env, Scope scope, Writer writer) {
            return (Page<?>) scope.get("articlePage");
        }

    }
}
