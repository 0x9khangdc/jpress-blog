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
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import io.jboot.utils.JsonUtil;
import io.jboot.utils.StrUtil;
import io.jboot.utils.TypeDef;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jpress.JPressOptions;
import io.jpress.commons.aliyun.AliyunVideoUtil;
import io.jpress.model.AttachmentVideo;
import io.jpress.service.AttachmentVideoService;

import java.util.Map;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web
 */
@RequestMapping("/commons/video")
public class VideoController extends Controller {

    @Inject
    private AttachmentVideoService videoService;


    /**
     * 获取视频信息
     */
    public void detail() {
        String uuid = getPara("id");

        if (StrUtil.isBlank(uuid)) {
            renderJson(Ret.fail().set("message", "The transmitted video UUID is empty!"));
            return;
        }

        AttachmentVideo video = videoService.findByUuid(uuid);
        if (video == null) {
            renderJson(Ret.fail().set("message", "The video information is empty!"));
            return;
        }


        if (AttachmentVideo.CLOUD_TYPE_ALIYUN.equals(video.getCloudType())) {//阿里云

            //视频云端id
            if (StrUtil.isBlank(video.getVodVid())) {
                renderJson(Ret.fail().set("message", "Alibaba Cloud Video Cloud ID is empty!"));
                return;
            }
            //阿里云视频播放凭证
            String playAuth = AliyunVideoUtil.getPlayAuth(video.getVodVid());
            if (StrUtil.isBlank(playAuth)) {
                renderJson(Ret.fail().set("message", "Alibaba Cloud Video Play Volume is empty!"));
                return;
            }

            renderJson(Ret.ok().set("success", true).set("vid", video.getVodVid()).set("playAuth", playAuth).set("cloudType", video.getCloudType()));


        } else if (AttachmentVideo.CLOUD_TYPE_QCLOUD.equals(video.getCloudType())) {//腾讯云

            String appId = JPressOptions.get("attachment_qcloudvideo_appid");
            if (StrUtil.isBlank(appId)) {
                renderJson(Ret.fail().set("message", "Please configure Tencent Cloud's account ID"));
                return;
            }

            //视频云端id
            if (StrUtil.isBlank(video.getVodVid())) {
                renderJson(Ret.fail().set("message", "Tencent Cloud Video Cloud ID is empty!"));
                return;
            }

            renderJson(Ret.ok().set("success", true).set("vid", video.getVodVid()).set("aid", appId).set("cloudType", video.getCloudType()));

        } else if (AttachmentVideo.CLOUD_TYPE_LOCAL.equals(video.getCloudType())) {//本地视频

            String options = video.getOptions();
            if (StrUtil.isNotBlank(options)) {
                Map<String, String> map = JsonUtil.get(options, "", TypeDef.MAP_STRING);
                if (map == null) {
                    renderJson(Ret.fail().set("message", "This video type is a local video, please upload local videos first!"));
                    return;
                }
                String src = map.get("local_video_url");
                renderJson(Ret.ok().set("success", true).set("src", src).set("cloudType", video.getCloudType()));

            }

        }

    }

}
