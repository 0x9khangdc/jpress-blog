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
package io.jpress.web.commons.controller;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import io.jboot.utils.FileUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressOptions;
import io.jpress.commons.utils.AliyunOssUtils;
import io.jpress.commons.utils.AttachmentUtils;
import io.jpress.model.Attachment;
import io.jpress.service.AttachmentCategoryService;
import io.jpress.service.AttachmentService;
import io.jpress.web.base.UserControllerBase;

import java.io.File;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: 首页
 * @Package io.jpress.web.admin
 */
@RequestMapping("/commons/attachment")
public class AttachmentController extends UserControllerBase {


    @Inject
    private AttachmentService service;

    @Inject
    private AttachmentCategoryService categoryService;

    public void upload() {
        if (!isMultipartRequest()) {
            renderError(404);
            return;
        }

        UploadFile uploadFile = getFile();
        if (uploadFile == null) {
            renderJson(Ret.fail().set("message", "Please select the file to be uploaded"));
            return;
        }


        File file = uploadFile.getFile();
        if (!getLoginedUser().isStatusOk()) {
            AttachmentUtils.delete(file);
            renderJson(Ret.of("error", Ret.of("message", "The current users are not activated and are not allowed to upload any files.")));
            return;
        }

        if (AttachmentUtils.isUnSafe(file)) {
            AttachmentUtils.delete(file);
            renderJson(Ret.fail().set("message", "Do not support such files upload"));
            return;
        }

        String mineType = uploadFile.getContentType();
        String fileType = mineType.split("/")[0];

        int maxImgSize = JPressOptions.getAsInt("attachment_img_maxsize", 10);
        int maxOtherSize = JPressOptions.getAsInt("attachment_other_maxsize", 100);

        Integer maxSize = "image".equals(fileType) ? maxImgSize : maxOtherSize;

        int fileSize = Math.round(file.length() / 1024 * 100) / 100;
        if (maxSize > 0 && fileSize > maxSize * 1024) {
            file.delete();
            renderJson(Ret.fail().set("message", "The size of the upload file cannot be exceeded " + maxSize + " MB"));
            return;
        }

        String path = AttachmentUtils.moveFile(uploadFile);
        AliyunOssUtils.upload(path, AttachmentUtils.file(path));

        //附件分类id
        Long categoryId = getParaToLong("categoryId");

        Attachment attachment = new Attachment();
        attachment.setUserId(getLoginedUser().getId());
        attachment.setCategoryId(categoryId);
        attachment.setTitle(uploadFile.getOriginalFileName());
        attachment.setPath(path.replace("\\", "/"));
        attachment.setSuffix(FileUtil.getSuffix(uploadFile.getFileName()));
        attachment.setMimeType(uploadFile.getContentType());

        Object attachmentId = service.save(attachment);
        //更新分类下的内容数量
        if(attachment.getCategoryId() != null){
            categoryService.doUpdateAttachmentCategoryCount(attachment.getCategoryId().longValue());
        }

        renderJson(Ret.ok().set("success", true)
                .set("src", attachment.getPath())
                .set("title",attachment.getTitle())
                .set("attachmentId",attachmentId)
        );
    }


}
