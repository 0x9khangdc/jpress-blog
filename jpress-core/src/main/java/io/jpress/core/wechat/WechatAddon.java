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
package io.jpress.core.wechat;

import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InMsg;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.core.wechat
 */
public interface WechatAddon {

    /**
     * Used to match whether it is executed by the plugin
     *
     * @param inMsg
     * @param msgController
     * @return
     */
    public boolean onMatchingMessage(InMsg inMsg, MsgController msgController);


    /**
     * Execute the reply logic
     *
     * @param inMsg
     * @param msgController
     */
    public boolean onRenderMessage(InMsg inMsg, MsgController msgController);

}
