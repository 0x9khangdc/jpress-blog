package io.jpress.module.job.controller.api;

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
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.commons.Rets;
import io.jpress.module.job.model.Job;
import io.jpress.module.job.service.JobService;
import io.jpress.web.base.ApiControllerBase;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version V5.0
 * @Title: 岗位相关的 API
 */
@RequestMapping("/api/job")
@Api("Recruitment related API documents")
public class JobApiController extends ApiControllerBase {

    @Inject
    private JobService jobService;


    @ApiOper(value = "Job details", paraNotes = "ID must be passed into a value")
    @ApiResp(field = "jobDetail", dataType = Job.class, notes = "Job details")
    public Ret detail(@ApiPara("Post ID") Long id) {

        if (id == null) {
            return Ret.fail().set("message", "ID must have a value");
        }

        Job job = jobService.findByIdWithInfo(id);

        if (job == null) {
            return Ret.fail().set("message", "The post does not exist");
        }

        return Ret.ok().set("jobDetail", job);
    }


    @ApiOper("Job paging reading")
    @ApiResp(field = "page", notes = "Job paging data", dataType = Page.class, genericTypes = Job.class)
    public Ret paginate(@ApiPara("sort by") String orderBy,
                        @ApiPara("Job requires academic qualifications") Integer education,
                        @ApiPara("Job classification ID") Long categoryId,
                        @ApiPara("Job department ID") Long deptId,
                        @ApiPara("Position area ID") Long addressId,
                        @ApiPara("Job working life") Integer workYear,
                        @ApiPara("Job type") Integer workType,
                        @ApiPara("Job recruitment type") Integer recruitmentType,
                        @ApiPara("Pagling page number") @DefaultValue("1") int pageNumber,
                        @ApiPara("The number of data per page") @DefaultValue("10") int pageSize) {

        Columns columns = new Columns();
        columns.eq("education", education);
        columns.eq("category_id", categoryId);
        columns.eq("dept_id", deptId);
        columns.eq("address_id", addressId);
        columns.eq("work_year", workYear);
        columns.eq("recruitment_type", recruitmentType);
        columns.eq("work_type", workType);

        Page<Job> page = jobService.paginateByColumnsWithInfo(pageNumber, pageSize, columns, orderBy);

        return Ret.ok().set("page", page);

    }


    @ApiOper("Find the list of posts according to custom conditions")
    @ApiResp(field = "list", notes = "Position list", dataType = List.class, genericTypes = Job.class)
    public Ret listByColumns(@ApiPara("sort by") String orderBy,
                             @ApiPara("Job requires academic qualifications") Integer education,
                             @ApiPara("Job classification ID") Long categoryId,
                             @ApiPara("Job department ID") Long deptId,
                             @ApiPara("Position area ID") Long addressId,
                             @ApiPara("Job working life") Integer workYear,
                             @ApiPara("Job type") Integer workType,
                             @ApiPara("Job recruitment type") Integer recruitmentType,
                             @ApiPara("Number of query") Integer count) {

        Columns columns = new Columns();
        columns.eq("category_id", categoryId);
        columns.eq("address_id", addressId);
        columns.eq("dept_id", deptId);
        columns.eq("education", education);
        columns.eq("work_year", workYear);
        columns.eq("work_type", workType);
        columns.eq("recruitment_type", recruitmentType);

        List<Job> jobList = jobService.findListByColumnsWithInfo(columns, orderBy, count);
        return Ret.ok().set("list", jobList);
    }


    @ApiOper("Delete post")
    public Ret doDelete(@ApiPara("Post ID") @NotNull Long id) {
        jobService.deleteById(id);
        return Rets.OK;
    }

    @ApiOper(value = "Create a new post", contentType = ContentType.JSON)
    @ApiResp(field = "id", notes = "Post ID", dataType = Long.class, mock = "123")
    public Ret doCreate(@ApiPara("Job JSON information") @JsonBody Job job) {
        Object id = jobService.save(job);
        return Ret.ok().set("id", id);
    }

    @ApiOper(value = "Update post", contentType = ContentType.JSON)
    public Ret doUpdate(@ApiPara("Job JSON information") @JsonBody Job job) {
        jobService.update(job);
        return Rets.OK;
    }


}
