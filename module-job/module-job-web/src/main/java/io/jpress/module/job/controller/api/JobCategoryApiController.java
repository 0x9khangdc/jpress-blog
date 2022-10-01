package io.jpress.module.job.controller.api;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.aop.annotation.DefaultValue;
import io.jboot.apidoc.ContentType;
import io.jboot.apidoc.annotation.Api;
import io.jboot.apidoc.annotation.ApiOper;
import io.jboot.apidoc.annotation.ApiPara;
import io.jboot.apidoc.annotation.ApiResp;
import io.jboot.db.model.Columns;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.commons.Rets;
import io.jpress.commons.layer.SortKit;
import io.jpress.module.job.model.JobCategory;
import io.jpress.module.job.service.JobCategoryService;
import io.jpress.web.base.ApiControllerBase;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V5.0
 * @Title: 岗位分类相关的 API
 */
@RequestMapping("/api/job/category")
@Api("Job classification related API documentation")
public class JobCategoryApiController extends ApiControllerBase {


    @Inject
    private JobCategoryService jobCategoryService;


    @ApiOper(value = "Division classification details", paraNotes = "ID cannot be empty")
    @ApiResp(field = "detail", dataType = JobCategory.class, notes = "Division classification details")
    public Ret detail(@ApiPara("Job classification ID") Long id){

        if (id == null ) {
            return Ret.fail().set("message", "id Can not be empty");
        }

        JobCategory category = jobCategoryService.findById(id);

        if(category == null){
            return Ret.fail().set("message", "The current classification does not exist");
        }


        return Ret.ok("detail", category);

    }


    @ApiOper("Find the list classification list according to custom conditions")
    @ApiResp(field = "list", notes = "List of job classification", dataType = List.class, genericTypes = JobCategory.class)
    public Ret listByColumns(@ApiPara("Category father ID") Long pid,
                             @ApiPara("Category Create User ID") Long userId,
                             @ApiPara("Sort attribute") @DefaultValue("order_number asc") String orderBy){

        List<JobCategory> categories = jobCategoryService.findListByColumns(Columns.create().eq("user_id",userId),orderBy);
        if (categories == null || categories.isEmpty()) {
            return Ret.ok().set("list", new HashMap<>());
        }

        if (pid != null) {
            categories = categories.stream()
                    .filter(category -> pid.equals(category.getPid()))
                    .collect(Collectors.toList());
        } else {
            SortKit.toTree(categories);
        }

        return Ret.ok().set("list", categories);
    }



    @ApiOper("Delete job classification")
    public Ret doDelete(@ApiPara("Job classification ID") @NotNull Long id) {
        jobCategoryService.deleteById(id);
        return Rets.OK;
    }


    @ApiOper(value = "Create a new job classification",contentType = ContentType.JSON)
    @ApiResp(field = "id", notes = "Job classification ID", dataType = Long.class, mock = "123")
    public Ret doCreate(@ApiPara("Job classification JSON") @JsonBody JobCategory jobCategory) {
        Object id = jobCategoryService.save(jobCategory);
        return Ret.ok().set("id", id);
    }


    @ApiOper(value = "Update post classification",contentType = ContentType.JSON)
    public Ret doUpdate(@ApiPara("Job classification JSON") @JsonBody JobCategory jobCategory) {
        jobCategoryService.update(jobCategory);
        return Rets.OK;
    }


}
