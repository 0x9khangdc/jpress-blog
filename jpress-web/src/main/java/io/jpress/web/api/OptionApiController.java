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
package io.jpress.web.api;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.apidoc.ContentType;
import io.jboot.apidoc.annotation.*;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.JPressOptions;
import io.jpress.commons.Rets;
import io.jpress.service.OptionService;
import io.jpress.web.base.ApiControllerBase;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web.api
 * <p>
 * 用法： http://127.0.0.1:8080/api/option?key=key1,key2
 * <p>
 * 返回数据
 * {
 * state : "ok",
 * data : {
 * key1: "data1",
 * key2: "data2"
 * }
 * }
 */
@RequestMapping("/api/option")
@Api("System configuration correlation API ")
public class OptionApiController extends ApiControllerBase {

    @Inject
    private OptionService optionService;

    @ApiOper("Query configuration")
    @ApiResps({
            @ApiResp(field = "value", notes = "Query data returned when a single configuration", mock = "..."),
            @ApiResp(field = "values", notes = "Back data when you query multiple key", dataType = Map.class, mock = "{\"key1\":\"value1\",\"key2\":\"value2\"}"),
    })
    public Ret query(@ApiPara(value = "Query key", notes = "Multiple key is separated with citations comma") @NotEmpty(message = "key must not empty") String key) {
        if (key.contains(",")) {
            Set<String> keys = StrUtil.splitToSet(key, ",");
            Map<String, String> data = new HashMap<>();
            for (String k : keys) {
                if (StrUtil.isNotBlank(k)) {
                    data.put(k, JPressOptions.get(k));
                }
            }
            return Ret.ok().set("values", data);
        } else {
            return Ret.ok().set("value", JPressOptions.get(key));
        }
    }

    @ApiOper(value = "Update configuration", contentType = ContentType.JSON)
    public Ret set(@ApiPara("Data to be updated map") @NotEmpty @JsonBody Map<String, String> keyValues) {
        keyValues.forEach((key, value) -> {
            optionService.saveOrUpdate(key, value);
            JPressOptions.set(key, value);
        });
        return Rets.OK;
    }
}
