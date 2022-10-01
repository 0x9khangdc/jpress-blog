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
package io.jpress.web.handler;


import com.google.common.collect.Sets;
import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;
import io.jboot.utils.StrUtil;
import io.jpress.JPressConfig;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: Pseudo -static processor
 * @Package io.jpress.web.handler
 */

public class JPressHandler extends Handler {

    private static final ThreadLocal<String> targetContext = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<>();

    public static String getCurrentTarget() {
        return targetContext.get();
    }

    public static void setCurrentTarget(String target) {
        targetContext.set(target);
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestContext.get();
    }

    private static final String ADDON_TARGET_PREFIX = "/addons";
    private static final String TEMPLATES_TARGET_PREFIX = "/templates";
    private static final String ATTACHMENT_TARGET_PREFIX = "/attachment";

    private static final Set<String> v3CssPaths = Sets.newHashSet("/static/commons/article.css"
            , "/static/commons/product.css"
            , "/static/commons/page.css");

    private static final Set<String> v3JsPaths = Sets.newHashSet("/static/commons/article.js"
            , "/static/commons/product.js"
            , "/static/commons/page.js");


    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        try {
            setCurrentTarget(target);
            requestContext.set(request);
            request.setAttribute("VERSION", JPressConsts.VERSION);
            request.setAttribute("CPATH", request.getContextPath());

            // SPATH The default value is "" "empty string
            request.setAttribute("SPATH","");
            doHandle(target, request, response, isHandled);
        } finally {
            targetContext.remove();
            requestContext.remove();
        }
    }


    public void doHandle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        //Do not allow any files in the .html, .sql files and web-inf directory in the plug-in directory
        if (target.startsWith(ADDON_TARGET_PREFIX)) {
            if (target.endsWith(".html")
                    || target.endsWith(".sql")
                    || target.contains("WEB-INF")) {
                HandlerKit.renderError404(request, response, isHandled);
                return;
            } else if (target.contains(".")) {
                AttachmentHandlerKit.handle(JPressConfig.me.getAddonRoot(), target, request, response, isHandled);
                return;
            }
        }

        //Do not allow the .html file in the interview template directory
        if (target.startsWith(TEMPLATES_TARGET_PREFIX)) {
            if (target.endsWith(".html")) {
                HandlerKit.renderError404(request, response, isHandled);
                return;
            }
        }

        //V4 version has been merged with V3 version css to jpressfront.css
        //Need to redirect, otherwise it will not be compatible with the template of V3
        if (target.endsWith(".css") && v3CssPaths.contains(target)) {
            HandlerKit.redirect301(request.getContextPath() + "/static/front/jpressfront.css", request, response, isHandled);
            return;
        }

        //V4 version has been merged with V3 version JS to jpressfront.js
        //Need to redirect, otherwise it will not be compatible with the template of V3
        if (target.endsWith(".js") && v3JsPaths.contains(target)) {
            HandlerKit.redirect301(request.getContextPath() + "/static/front/jpressfront.js", request, response, isHandled);
            return;
        }

        //Attachment directory
        if (target.startsWith(ATTACHMENT_TARGET_PREFIX)) {
            AttachmentHandlerKit.handle(JPressConfig.me.getAttachmentRoot(), target, request, response, isHandled);
            return;
        }

        if (target.contains(".")){
            //If it is access .html, remove the suffix directly
            if (target.endsWith(".html")) {
                target = target.substring(0, target.length() - 5);
                setCurrentTarget(target);
            }else {
                return;
            }
        }


        String suffix = JPressOptions.getAppUrlSuffix();

        //If the pseudo-static is not enabled, let Undertow handle static resource CSS JS, etc.
        if (StrUtil.isBlank(suffix) && target.indexOf('.') != -1) {
            return;
        }

        //Enable pseudo-static
        if (StrUtil.isNotBlank(suffix) && target.endsWith(suffix)) {
            target = target.substring(0, target.length() - suffix.length());
        }

        next.handle(target, request, response, isHandled);
    }


}
