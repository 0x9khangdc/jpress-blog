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
package io.jpress;

/**
 * @author Michael Yang （fuhai999@gmail.com）
 * @version V1.0
 * @Title: JPress 常量
 * @Package io.jpress
 */
public class JPressConsts {

    public static final String VERSION = "v5.0.3";

    //v2.x not define
    //v3.x  VERSION_CODE < 40
    //v4.x  VERSION_CODE >= 40
    //v5.x  VERSION_CODE >= 80
    public static final String VERSION_CODE = "83";

    /**
     * Background system menu ID
     */
    public static final String SYSTEM_MENU_USER = "user";
    public static final String SYSTEM_MENU_ATTACHMENT = "attachment";
    public static final String SYSTEM_MENU_TEMPLATE = "template";
    public static final String SYSTEM_MENU_ADDON = "addon";
    public static final String SYSTEM_MENU_SYSTEM = "system";
    public static final String SYSTEM_MENU_WECHAT_PUBULIC_ACCOUNT = "wechat_pubulic_account";
    public static final String SYSTEM_MENU_WECHAT_MINI_PROGRAM = "wechat_mini_program";

    /**
     * Menu ID of the user center
     */
    public static final String UCENTER_MENU_COMMENT ="comment";
    public static final String UCENTER_MENU_PERSONAL_INFO ="personal";


    /**
     * The following is related to the configuration
     */
    public static final String OPTION_WEB_TITLE = "web_title"; //Website title
    public static final String OPTION_WEB_SUBTITLE = "web_subtitle"; // Site sub -title
    public static final String OPTION_WEB_NAME = "web_name"; // Website name
    public static final String OPTION_WEB_DOMAIN = "web_domain"; // Website domain name
    public static final String OPTION_WEB_COPYRIGHT = "web_copyright"; // Website copyright information
    public static final String OPTION_WEB_IPC_NO = "web_ipc_no"; // Website filing number
    public static final String OPTION_SEO_TITLE = "seo_title"; // SEO title
    public static final String OPTION_SEO_KEYWORDS = "seo_keywords"; //  SEO Keyword
    public static final String OPTION_SEO_DESCRIPTION = "seo_description"; // SEO describe


    public static final String OPTION_WEB_FAKE_STATIC_ENABLE = "web_fake_static_enable"; //Whether to enable pseudo-static
    public static final String OPTION_WEB_FAKE_STATIC_SUFFIX = "web_fake_static_suffix"; //Website pseudo-static suffix
    public static final String OPTION_WEB_TEMPLATE_PREVIEW_ENABLE = "web_template_preview_enable"; //Whether to enable the template preview function
    public static final String OPTION_WEB_FLAT_URL_ENABLE = "web_flat_url_enable"; //Whether to enable flattea URL

    public static final String OPTION_CDN_ENABLE = "cdn_enable"; //Whether to enable CDN
    public static final String OPTION_CDN_DOMAIN = "cdn_domain"; //CDN domain name

    public static final String OPTION_API_ENABLE = "api_enable"; //Whether to enable API
    public static final String OPTION_API_SECRET = "api_secret"; //API key
    public static final String OPTION_API_APPID = "api_app_id"; //API application ID

    public static final String OPTION_WECHAT_WEB_AUTHORIZE_ENABLE = "wechat_web_authorize_enable"; //Whether to enable the WeChat webpage authorization function


    public static final String OPTION_WECHAT_APPID = "wechat_appid"; //WeChat app Id
    public static final String OPTION_WECHAT_APPSECRET = "wechat_appsecret"; //WeChat APP Secret
    public static final String OPTION_WECHAT_TOKEN = "wechat_token"; //WeChat token

    public static final String OPTION_WECHAT_MINIPROGRAM_APPID = "wechat_miniprogram_appid"; //WeChat mini-program token
    public static final String OPTION_WECHAT_MINIPROGRAM_APPSECRET = "wechat_miniprogram_appsecret"; //WeChat mini-program token
    public static final String OPTION_WECHAT_MINIPROGRAM_TOKEN = "wechat_miniprogram_token"; //WeChat mini-program token


    public static final String OPTION_CONNECTION_EMAIL_ENABLE = "connection_email_enable"; // Whether to enable the email sending function
    public static final String OPTION_CONNECTION_EMAIL_SMTP = "connection_email_smtp"; // Email server SMTP
    public static final String OPTION_CONNECTION_EMAIL_ACCOUNT = "connection_email_account"; //email address
    public static final String OPTION_CONNECTION_EMAIL_PASSWORD = "connection_email_password"; //email Password
    public static final String OPTION_CONNECTION_EMAIL_SSL_ENABLE = "connection_email_ssl_enable"; //Whether to enable SSL


    public static final String OPTION_CONNECTION_SMS_ENABLE = "connection_sms_enable"; //Whether to enable SMS
    public static final String OPTION_CONNECTION_SMS_TYPE = "connection_sms_type"; //SMS service provider
    public static final String OPTION_CONNECTION_SMS_APPID = "connection_sms_appid"; // Service provider's appid (or appKey)
    public static final String OPTION_CONNECTION_SMS_APPSECRET = "connection_sms_appsecret"; //APP key


    public static final String SMS_TYPE_ALIYUN = "aliyun"; //SMS service provider: Alibaba Cloud
    public static final String SMS_TYPE_QCLOUD = "qcloud"; //SMS service provider: Tencent Cloud

    /**
     * Use cookies name constant
     */
    public static final String COOKIE_UID = "_jpuid";
    public static final String COOKIE_ANONYM = "_jpanonym";
    public static final String COOKIE_EDIT_MODE = "_jpeditmode";
    public static final String COOKIE_SITE_LANG_CLOSE = "_jpsitelc";
    public static final String COOKIE_ADMIN_SITE_ID = "_jpasiteid";

    /**
     * Request used Attribute constant
     */
    public static final String ATTR_LOGINED_USER = "USER";


    public static final String ATTR_WEB_TITLE = "WEB_TITLE"; //Website title
    public static final String ATTR_WEB_SUBTITLE = "WEB_SUBTITLE"; // Site sub -title
    public static final String ATTR_WEB_NAME = "WEB_NAME"; // Website name
    public static final String ATTR_WEB_DOMAIN = "WEB_DOMAIN"; // Website domain name
    public static final String ATTR_WEB_COPYRIGHT = "WEB_COPYRIGHT"; // Website copyright information
    public static final String ATTR_WEB_IPC_NO = "WEB_IPC_NO"; // Website filing number
    public static final String ATTR_SEO_TITLE = "SEO_TITLE"; // SEO title
    public static final String ATTR_SEO_KEYWORDS = "SEO_KEYWORDS"; //  SEO Keyword
    public static final String ATTR_SEO_DESCRIPTION = "SEO_DESCRIPTION"; // SEO describe

    public static final String ATTR_MENUS = "MENUS"; // Pages

    public static final String ATTR_SITE_ID = "SITE_ID"; // Current site ID

    public static final String EDIT_MODE_HTML = "html"; //html Edit mode
    public static final String EDIT_MODE_MARKDOWN = "markdown"; //markdown Edit mode


    public static final String JWT_USERID = "userId";
    public static final String JWT_OPENID = "openId";
    public static final String JWT_UNIONID = "unionId";


    public static final String DEFAULT_ADMIN_VIEW = "/WEB-INF/views/admin/";
}
