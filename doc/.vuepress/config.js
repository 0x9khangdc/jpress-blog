//reference:
// https://github.com/vuejs/vuepress/blob/master/packages/docs/docs/.vuepress/config.js
// https://vuepress-theme-reco.recoluan.com/views/1.x/
module.exports = {
    title: 'JPress Official documentation',
    description: 'JPress, A professional station building artifact, more than 100,000+ websites have been built in Jpress',
    // base:'/docs/',
    markdown: {
        lineNumbers: true
    },
    theme: 'vuepress-theme-reco',
    themeConfig: {
        //Tencent 404 Public welfare configuration
        noFoundPageByTencent: false,

        mode: 'light', // 默认 auto，auto 跟随系统，dark 暗色模式，light 亮色模式
        modePicker: false, // 默认 true，false 不显示模式调节按钮，true 则显示

        // author
        //author: 'jpress',
        subSidebar: 'auto',//在所有页面中启用自动生成子侧边栏，原 sidebar 仍然兼容

        // if your docs are in a different repo from your main project:
        docsRepo: 'JPressProjects/jpress',
        // if your docs are in a specific branch (defaults to 'master'):
        docsBranch: 'master',
        // if your docs are not at the root of the repo:
        docsDir: 'doc',
        // defaults to false, set to true to enable
        editLinks: true,
        // custom text for edit link. Defaults to "Edit this page"
        editLinkText: 'Edit this page',

        /**
         * support for
         * 'default'
         * 'funky'
         * 'okaidia'
         * 'solarizedlight'
         * 'tomorrow'
         */
        codeTheme: 'tomorrow',

        lastUpdated: 'Update time', // string | boolean

        nav: [
            {text: 'Home', link: '/'},
            {text: 'Manual', link: '/manual/'},
            {text: 'Development', link: '/development/'},
            {text: 'Question', link: 'https://gitee.com/JPressProjects/jpress/issues'},
            {
                text: 'Source', items: [
                    {text: 'Gitee', link: 'https://gitee.com/JPressProjects/jpress'},
                    {text: 'Github', link: 'https://github.com/JPressProjects/jpress'}
                ]
            }
        ],

        sidebar: {
            '/manual/': [{
                    title: 'Get started quickly',
                    collapsable: false,
                    children: [
                        {title: 'JPress Brief introduction', path: '/manual/'},
                        {title: 'Start quickly', path: '/manual/start'}
                    ],
                },
                {
                    title: 'Installation',
                    collapsable: false,
                    children: [
                        {title: 'Java environment configuration', path: '/manual/jdk_config'},
                        {title: 'Maven environment configuration', path: '/manual/maven_config'},
                        {title: 'Idea development environment preparation', path: '/manual/idea_environment_config'},
                        {title: 'JPress source code acquisition', path: '/manual/jpress_download_source'},
                        {title: 'Import jpress source code to editor', path: '/manual/jpress_open_compiler'},
                        {title: 'Compile and run JPress', path: '/manual/jpress_compile_with_run'},
                        {title: 'Install JPress (Tomcat deployment) on Windows)', path: '/manual/windows-tomcat-deploy'},
                        {title: 'Install JPress (UNDERTOW deployment) on Windows', path: '/manual/windows_undertow_deploy'},
                        {title: 'Install JPress (Tomcat deployment) on Linux)', path: '/manual/linux-tomcat-deploy'},
                        {title: 'Install JPress (Undertow deployment) on Linux', path: '/manual/linux_undertow_deploy'},
                        {title: 'Install jpress on the pagoda', path: '/manual/ces_bt_config'},
                        {title: 'Install jpress on docker', path: '/manual/install_docker'},
                        {title: 'A key installation', path: '/manual/install_oneclick'},
                        {title: 'How to migrate', path: '/manual/project_transfer'},
                        {title: 'How to upgrade', path: '/manual/project_upgrade'}
                    ],
                },
                {
                    title: 'Product manual',
                    collapsable: false,
                    children: [
                        {title: 'Article', path: '/manual/article'},
                        {title: 'Product', path: '/manual/product'},
                        {title: 'Recruitment', path: '/manual/job'},
                        {title: 'Page', path: '/manual/page'},
                        {title: 'Attachment', path: '/manual/attachment'},
                        {title: 'Template', path: '/manual/template'},
                        {title: 'Addon', path: '/manual/addon'},
                        {title: 'User', path: '/manual/user'},
                        {title: 'WeChat', path: '/manual/wechat'},
                        {title: 'Setting', path: '/manual/setting'},
                        {title: 'Toolbox', path: '/manual/kits'},
                        {title: 'Common problem', path: '/manual/faq'},
                    ],
                }
            ],


            '/development/': [{
                    title: 'Overview',
                    path: '/development/',
                },
                {
                    title: 'Template development',
                    collapsable: true,
                    children: [
                        {title: '模板简介', path: '/development/template/template_introduce'},
                        {title: '目录结构', path: '/development/template/template_directory'},
                        {title: '模板语法', path: '/development/template/template_grammar'},
                        {title: '全局变量', path: '/development/template/template_global_variable'},
                        {title: '共享方法', path: '/development/template/template_global_method'},
                        {title: '模板指令', path: '/development/template/template_command'},
                        {title: '拖拽设计', path: '/development/template/template_block'},
                        {title: '设置页面', path: '/development/template/template_setting'},

                    ],
                },
                {
                    title: 'Plug-in development',
                    collapsable: true,
                    children: [
                        {title: '插件简介', path: '/development/addon/start'},
                        {title: 'Hello World', path: '/development/addon/helloworld'},
                        {title: '插件安装', path: '/development/addon/install'},
                        {title: '插件升级', path: '/development/addon/upgrade'},
                        {title: '插件资源', path: '/development/addon/resource'},
                        {title: '插件代码生成器', path: '/development/addon/codegen'},
                        {title: '微信插件', path: '/development/addon/wechat'},
                        {title: '常见问题', path: '/development/addon/faq'},
                    ],
                },
                {
                    title: 'Module development',
                    collapsable: true,
                    children: [
                        {title: 'JPress源码获取', path: '/development/jpress_download_source'},
                        {title: 'JAVA环境配置', path: '/development/jdk_config'},
                        {title: 'Maven环境配置', path: '/development/maven_config'},
                        {title: 'IDEA开发环境准备', path: '/development/idea_environment_config'},
                        {title: '导入JPress源码到编辑器', path: '/development/jpress_open_compiler'},
                        {title: '编译并运行JPress', path: '/development/jpress_compile_with_run'},
                        {title: '开始前注意事项', path: '/development/module/note'},
                        {title: '表设计', path: '/development/module/table_design'},
                        {title: '模块代码生成器的使用', path: '/development/module/moduleGenerator'},
                        {title: '模块导入编辑器', path: '/development/module/module_import_compiler'},
                        {title: '后台菜单配置', path: '/development/module/admin_menu_setting'},
                        {title: '面板配置', path: '/development/module/panel_setting'},
                        {title: '工具箱配置', path: '/development/module/toolkit'},
                        {title: 'JPress架构', path: '/development/module/structure'},
                    ],
                },
                {
                    title: 'API interface',
                    collapsable: true,
                    children: [
                        {title: '概述', path: '/development/api/start'},
                        {title: '文章', path: '/development/api/api_article'},
                        {title: '文章分类', path: '/development/api/api_article_category'},
                        {title: '文章评论', path: '/development/api/api_article_comment'},
                        {title: '产品', path: '/development/api/api_product'},
                        {title: '产品分类', path: '/development/api/api_product_category'},
                        {title: '产品评论', path: '/development/api/api_product_comment'},
                        {title: '页面', path: '/development/api/api_page'},
                        {title: '用户', path: '/development/api/api_user'},
                        {title: '微信小程序', path: '/development/api/api_wechat_mp'},
                        {title: '系统设置', path: '/development/api/api_option'},
                    ],
                },
            ]
        },
        sidebarDepth: 1
    },

    head: [
        ['link', {rel: 'icon', href: '/logo.png'}],
        ['script', {}, `
            var _hmt = _hmt || [];
            (function() {
              var hm = document.createElement("script");
              hm.src = "https://hm.baidu.com/hm.js?13a39a1b1e7fb17e8f806d1fb6207796";
              var s = document.getElementsByTagName("script")[0]; 
              s.parentNode.insertBefore(hm, s);
            })();
        `],
    ]
}
