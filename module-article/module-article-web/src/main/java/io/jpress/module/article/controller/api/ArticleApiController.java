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
package io.jpress.module.article.controller.api;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.DefaultValue;
import io.jboot.apidoc.ContentType;
import io.jboot.apidoc.annotation.Api;
import io.jboot.apidoc.annotation.ApiOper;
import io.jboot.apidoc.annotation.ApiPara;
import io.jboot.apidoc.annotation.ApiResp;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.commons.Rets;
import io.jpress.module.article.model.Article;
import io.jpress.module.article.model.ArticleCategory;
import io.jpress.module.article.service.ArticleCategoryService;
import io.jpress.module.article.service.ArticleService;
import io.jpress.web.base.ApiControllerBase;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 文章相关的 API
 */
@RequestMapping("/api/article")
@Api("Article related API documentation")
public class ArticleApiController extends ApiControllerBase {

    @Inject
    private ArticleService articleService;

    @Inject
    private ArticleCategoryService categoryService;


    @ApiOper(value = "Article details", paraNotes = "ID or slug must be passed into a value")
    @ApiResp(field = "detail", dataType = Article.class, notes = "Article details")
    public Ret detail(@ApiPara("Article ID") Long id, @ApiPara("Article fixed connection") String slug) {

        if (id == null && slug == null) {
            return Ret.fail().set("message", "ID or slug must have a value");
        }

        Article article = id != null ? articleService.findById(id) : articleService.findFirstBySlug(slug);

        if (article == null || !article.isNormal()) {
            return Ret.fail().set("message", "This article does not exist or has been offline");
        }

        articleService.doIncArticleViewCount(article.getId());
        return Ret.ok().set("detail", article);
    }


    /**
     * 通过 分类ID 分页读取文章列表
     */
    @ApiOper("Article pagination reading")
    @ApiResp(field = "page", notes = "Article paging data", dataType = Page.class, genericTypes = Article.class)
    public Ret paginate(@ApiPara("文章分类ID") Long categoryId
            , @ApiPara("sort by") String orderBy
            , @ApiPara("Pagling page number") @DefaultValue("1") int pageNumber
            , @ApiPara("The number of data per page") @DefaultValue("10") int pageSize) {
        Page<Article> page = categoryId == null
                ? articleService.paginateInNormal(pageNumber, pageSize, orderBy)
                : articleService.paginateByCategoryIdInNormal(pageNumber, pageSize, categoryId, orderBy);

        return Ret.ok().set("page", page);
    }


    /**
     * 通过文章分类的 ID 操作文章列表
     *
     * @param categoryId
     * @param count
     * @return
     */
    @ApiOper("Find the list of articles based on the ID of the article")
    @ApiResp(field = "list", notes = "Article list", dataType = List.class, genericTypes = Article.class)
    public Ret listByCategoryId(@ApiPara("Article classification ID") @NotNull Long categoryId
            , @ApiPara("Query quantity") @DefaultValue("10") int count) {
        List<Article> articles = articleService.findListByCategoryId(categoryId, null, "id desc", count);
        return Ret.ok().set("list", articles);
    }


    /**
     * 通过文章分类的固定链接查找所有文章列表
     *
     * @param categorySlug
     * @param count
     * @return
     */
    @ApiOper("Find the article list according to the fixed connection of the article")
    @ApiResp(field = "list", notes = "Article list", dataType = List.class, genericTypes = Article.class)
    public Ret listByCategorySlug(@ApiPara("Classified fixed connection") @NotEmpty String categorySlug
            , @ApiPara("Query quantity") @DefaultValue("10") int count) {
        ArticleCategory category = categoryService.findFirstByTypeAndSlug(ArticleCategory.TYPE_TAG, categorySlug);
        if (category == null) {
            return Rets.FAIL;
        }

        List<Article> articles = articleService.findListByCategoryId(category.getId(), null, "id desc", count);
        return Ret.ok().set("list", articles);
    }


    /**
     * 通过 文章属性 获得文章列表
     */
    @ApiOper("Find the article list according to the FLAG of the article")
    @ApiResp(field = "list", notes = "Article list", dataType = List.class, genericTypes = Article.class)
    public Ret listByFlag(@ApiPara("Articles") @NotEmpty String flag
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

        List<Article> articles = articleService.findListByColumns(columns, orderBy, count);
        return Ret.ok().set("list", articles);
    }


    @ApiOper("Check the relevant article of a certain article")
    @ApiResp(field = "list", notes = "Article list", dataType = List.class, genericTypes = Article.class)
    public Ret listByRelevant(@ApiPara("Article ID") @NotNull Long articleId
            , @ApiPara("Query quantity") @DefaultValue("3") int count) {
        List<Article> relevantArticles = articleService.findRelevantListByArticleId(articleId, Article.STATUS_NORMAL, count);
        return Ret.ok().set("list", relevantArticles);
    }


    @ApiOper("Article search")
    @ApiResp(field = "page", notes = "Article paging data", dataType = Page.class, genericTypes = Article.class)
    public Ret search(@ApiPara("Key words") String keyword
            , @ApiPara("Paging page number") @DefaultValue("1") int pageNumber
            , @ApiPara("Number per page") @DefaultValue("10") int pageSize) {
        Page<Article> dataPage = StrUtil.isNotBlank(keyword)
                ? articleService.search(keyword, pageNumber, pageSize)
                : null;
        return Ret.ok().set("page", dataPage);
    }


    @ApiOper("Delete articles")
    public Ret doDelete(@ApiPara("Article ID") @NotNull Long id) {
        articleService.deleteById(id);
        return Rets.OK;
    }

    @ApiOper(value = "Create a new article", contentType = ContentType.JSON)
    @ApiResp(field = "id", notes = "Article ID", dataType = Long.class, mock = "123")
    public Ret doCreate(@ApiPara("JSON information of the article") @JsonBody Article article) {
        Object id = articleService.save(article);
        return Ret.ok().set("id", id);
    }

    @ApiOper(value = "Update articles", contentType = ContentType.JSON)
    public Ret doUpdate(@ApiPara("JSON information of the article") @JsonBody Article article) {
        articleService.update(article);
        return Rets.OK;
    }


}
