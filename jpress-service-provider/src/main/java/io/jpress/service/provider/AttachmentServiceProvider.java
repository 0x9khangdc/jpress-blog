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
package io.jpress.service.provider;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;
import io.jboot.aop.annotation.Bean;
import io.jboot.db.model.Columns;
import io.jboot.utils.StrUtil;
import io.jpress.JPressOptions;
import io.jpress.commons.service.JPressServiceBase;
import io.jpress.commons.utils.ImageUtils;
import io.jpress.model.Attachment;
import io.jpress.service.AttachmentService;

import java.io.File;

@Bean
public class AttachmentServiceProvider extends JPressServiceBase<Attachment> implements AttachmentService {

    private static final Log LOG = Log.getLog(AttachmentServiceProvider.class);

    @Override
    public Page _paginate(int page, int pagesieze, String title) {
        Columns columns = Columns.create();
        if (StrUtil.isNotBlank(title)) {
            columns.like("title", "%" + title + "%");
        }

        return DAO.paginateByColumns(page, pagesieze, columns, "id desc");
    }

    @Override
    public Page<Attachment> _paginateByColumns(int page, int pageSize, Columns columns) {
        return DAO.paginateByColumns(page, pageSize, columns, "id desc");
    }

    @Override
    public boolean deleteByIds(Object... ids) {
        for (Object id : ids) {
            deleteById(id);
        }
        return true;
    }


    @Override
    public Object save(Attachment model) {
        tryToProcessWatermark(model);
        return super.save(model);
    }


    /**
     * 处理水印的问题
     *
     * @param model
     */
    private void tryToProcessWatermark(Attachment model) {
        //不是图片，不用处理水印的问题
        if (ImageUtils.isImageExtName(model.getPath()) == false) {
            return;
        }

        boolean waterMarkEnable = JPressOptions.getAsBool("attachment_watermark_enable");
        if (waterMarkEnable == false) {
            return;
        }

        String waterImage = JPressOptions.get("attachment_watermark_img");
        if (StrUtil.isBlank(waterImage)) {
            LOG.warn("The watermark function has been enabled, but the watermark picture is not configured.");
            return;
        }

        File waterImageFile = new File(PathKit.getWebRootPath(), waterImage);
        if (!waterImageFile.exists()) {
            LOG.warn("The watermark function has been enabled, but the watermark picture does not exist.");
            return;
        }

        //默认值 5 ，水印在右下角。
        int waterMarkPosition = JPressOptions.getAsInt("attachment_watermark_position", 5);

        //透明度
        float alpha = JPressOptions.getAsFloat("attachment_watermark_transparency", 0.2f);

        try {
            ImageUtils.pressImage(waterImageFile.getAbsolutePath(),
                    PathKit.getWebRootPath() + model.getPath(),
                    PathKit.getWebRootPath() + model.getPath(),
                    waterMarkPosition,
                    alpha);
        } catch (Exception ex) {
            LOG.error("Watermark treatment failed:" + model.getPath());
            LOG.error(ex.toString(), ex);
        }
    }
}