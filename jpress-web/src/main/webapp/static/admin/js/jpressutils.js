/**
 * Perform Swal overall configuration
 * @type {{}}
 */
var mySwal = typeof (Swal) != "undefined" ? Swal.mixin({
    title: 'Are you sure of this operation?',
    text: 'Can\'t recover after confirmation, please handle it carefully!',
    showClass: {
        popup: 'animated fadeInDown faster'
    },
    hideClass: {
        popup: 'animated fadeOutUp faster'
    },
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-danger'
    },
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    cancelButtonText: ' Cancel ',
    showLoaderOnConfirm: true,
    confirmButtonText: ' Determine the operation! ',
}) : {};


function getContextPath() {
    if (typeof jpress == 'undefined') {
        return ""
    } else {
        return jpress.cpath;
    }
}


function getTableSelectedRowData() {
    var retData = null;
    $('[name="tableItem"]').each(function () {
        if ($(this).prop('checked')) {
            retData = {};
            $(this).closest('tr').children().each(function () {
                for (var attr in this.dataset) {
                    retData[attr] = this.dataset[attr];
                }
            })

            var trDatas = $(this).closest('tr').data();
            for (var attr in trDatas) {
                retData[attr] = trDatas[attr];
            }
        }
    });
    return retData;
}

function getTableSelectedIds() {
    var selectedIds = "";
    $('[name="tableItem"]').each(function () {
        if ($(this).prop('checked')) {
            selectedIds += $(this).val() + ",";
        }
    });
    return selectedIds.substring(0, selectedIds.length - 1);
}

/**
 * 获取 url 的参数内容
 * @returns {string}
 */
function getPara(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            return pair[1];
        }
    }
    return "";
}

/**
 * 查看当前页面是不是手机页面
 * @returns {boolean}
 */
function isMobileBrowser() {
    if (window.navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)) {
        return true; // 移动端
    } else {
        return false; // PC端
    }
}

/**
 * 进行 get 请求
 * @param url
 * @param okFunction
 * @param failFunction
 */
function ajaxGet(url, okFunction, failFunction) {
    if (url == null || "" == url) {
        alert("url Can not be empty ");
        return
    }

    okFunction = okFunction || function (result) {
        location.reload();
    };

    failFunction = failFunction || function (result) {
        toastr.error(result.message, 'operation failed');
    };

    $.ajax({
        type: 'GET',
        url: url,
        async: true,
        success: function (result) {
            if (result.state == 'ok') {
                okFunction(result);
            } else {
                failFunction(result);
            }
        },
        error: function (e) {
            toastr.error("System error ...", 'operation failed');
        }
    });

}

/**
 * Ajax request
 * @param url
 * @param data
 * @param okFunction
 * @param failFunction
 */
function ajaxPost(url, data, okFunction, failFunction) {
    if (url == null || "" == url) {
        alert("url Can not be empty ");
        return
    }

    okFunction = okFunction || function (result) {
        location.reload();
    };

    failFunction = failFunction || function (result) {
        toastr.error(result.message, 'operation failed');
    };

    $.ajax({
        url: url,
        type: 'POST',
        data: typeof data === "string" ? data : JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            if (result.state == 'ok') {
                okFunction(result);
            } else {
                failFunction(result);
            }
        },
        error: function (arg1) {
            showErrorMessage("System error ...");
        }
    });
}

/**
 * Submit AJAX for a Form
 * @param form
 * @param okFunction
 * @param failFunction
 */
function ajaxSubmit(form, okFunction, failFunction) {

    if (typeof (CKEDITOR) != "undefined") {
        for (instance in CKEDITOR.instances) {
            CKEDITOR.instances[instance].updateElement();
        }
    }

    okFunction = okFunction || function (result) {
        location.reload();
    };

    failFunction = failFunction || function (result) {
        toastr.error(result.message, 'operation failed');
    };

    $(form).ajaxSubmit({
        type: "post",
        success: function (result) {
            if (result.state == "ok") {
                okFunction(result);
            } else {
                failFunction(result);
            }
        },
        error: function () {
            toastr.error('System error, please try again later.', 'operation failed');
        }
    });
}

/**
 * 弹出消息
 * @param msg
 * @param url
 */
function showMessage(msg, url) {
    if (typeof toastr != "undefined") {
        toastr.options.onHidden = function () {
            reloadOrRedirect(url);
        };
        toastr.success(msg);
    } else {
        alert(msg);
        reloadOrRedirect(url);
    }
}

/**
 * 弹出错误消息
 * @param msg
 * @param url
 */
function showErrorMessage(msg, url) {
    if (typeof toastr != "undefined") {
        toastr.options.onHidden = function () {
            reloadOrRedirect(url);
        };
        toastr.error(msg, 'operation failed');
    } else {
        alert(msg);
        reloadOrRedirect(url);
    }
}


function reloadOrRedirect(url) {
    if (url) {
        if ("reload" == url) {
            location.reload();
        } else {
            location.href = url;
        }
    }
}

/**
 * 获取表格的 选中的 id
 * @returns {string}
 */
function getTableSelectedIds() {
    var selectedIds = "";
    $('[name="tableItem"]').each(function () {
        if ($(this).prop('checked')) {
            selectedIds += $(this).val() + ",";
        }
    });
    return selectedIds.substring(0, selectedIds.length - 1);
}


function getTableSelectedRowData() {
    var retData = null;
    $('[name="tableItem"]').each(function () {
        if ($(this).prop('checked')) {
            retData = {};
            $(this).closest('tr').children().each(function () {
                for (var attr in this.dataset) {
                    retData[attr] = this.dataset[attr];
                }
            })

            var trDatas = $(this).closest('tr').data();
            for (var attr in trDatas) {
                retData[attr] = trDatas[attr];
            }
        }
    });
    return retData;
}


/**
 * 弹出 Alert 确认框
 * @param title
 * @param btnText
 */
function sweetAlert(title, btnText) {
    Swal.fire({
        title: title,
        confirmButtonText: btnText || '  OK ',
        showClass: {
            popup: 'animated fadeInDown faster'
        },
        hideClass: {
            popup: 'animated fadeOutUp faster'
        }
    });
}

/**
 * 弹出确认对话框
 * @param title
 * @param text
 * @param btnText
 * @param actionUrl
 * @param successTitle
 * @param successText
 */
function sweetConfirm(title, text, btnText, actionUrl, successTitle, successText, actionComponent) {
    mySwal.fire({
        title: title || 'Are you sure of this operation?',
        icon: 'question',
        text: text || 'Can\'t recover after confirmation, please handle it carefully!',
        confirmButtonText: btnText || ' Determine the operation! ',
        preConfirm: () => {

            var token = getCookie("csrf_token");
            if (token) {
                if (actionUrl.indexOf("?") == -1) {
                    actionUrl = actionUrl + "?csrf_token=" + token;
                } else {
                    if (actionUrl.indexOf("csrf_token=") == -1) {
                        actionUrl = actionUrl + "&csrf_token=" + token;
                    }
                }
            }

            return fetch(actionUrl, {
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(response.statusText)
                    }
                    return response.json()
                })
                .catch(error => {
                    Swal.showValidationMessage(
                        `Request failed: ${error}`
                    )
                });
        }
    }).then((result) => {

        var successFun = actionComponent.attr('data-success-function');
        var successGoto = actionComponent.attr('data-success-goto');
        var successMsg = actionComponent.attr('data-success-message');

        var failFun = actionComponent.attr('data-fail-function');
        var failMsg = actionComponent.attr('data-fail-message');

        if (result.value && result.value.state == 'ok') {
            mySwal.fire({
                title: successTitle || 'Successful operation!',
                text: successText || '',
                icon: 'success',
                showCancelButton: false,
                confirmButtonColor: '#3085d6',
                confirmButtonText: '  Sure ',
                showLoaderOnConfirm: false,
                timer: 2000,
                timerProgressBar: true,
            }).then(() => {
                if (successFun) {
                    eval(successFun)(result);
                    return;
                }

                if (successMsg) {
                    showMessage(successMsg, successGoto);
                    return;
                }

                if (successGoto) {
                    location.href = successGoto;
                    return
                }

                if (result.value && result.value.message) {
                    showMessage(result.value.message);
                    return;
                }

                window.location.reload();
            })
        } else if ("cancel" === result.dismiss) {
            //用户点击了取消按钮，什么都不操作
        } else {
            if (failFun) {
                eval(failFun)(result);
                return;
            }

            if (failMsg) {
                showErrorMessage(failMsg);
                return;
            }

            if (result.value.message) {
                showErrorMessage(result.value.message);
            } else {
                showErrorMessage('operation failed.')
            }

        }
    })
}


/**
 * 弹出删除确认框
 * @param title
 * @param text
 * @param btnText
 * @param actionUrl
 * @param successTitle
 * @param successText
 */
function sweetConfirmDel(title, text, btnText, actionUrl, successTitle, successText, actionComponent) {
    this.sweetConfirm(title || 'Are you sure you want to delete it?',
        text || 'Can\'t recover after deleting, please handle it carefully!',
        btnText || ' confirm delete! ',
        actionUrl,
        successTitle || 'successfully deleted!',
        successText || 'You have successfully deleted the data!',
        actionComponent);
}

/**
 * 获取 cookie 信息
 * @param name
 * @returns {string|null}
 */
function getCookie(name) {
    var cookieString = document.cookie;
    var cookies = cookieString.split("; ");
    for (var i = 0; i < cookies.length; i++) {
        var arr = cookies[i].split("=");
        if (arr[0] == name) {
            return arr[1];
        }
    }
    return null;
}

function doActivateEmail(userId) {
    ajaxGet(jpress.cpath + "/commons/emailactivate?userId=" + userId, function (result) {
        alert(result.message);
    })
}

function closeLayerAndRefresh() {
    if (typeof parent != "undefined" && parent.layer) {
        parent.layer.data.needRefresh = true;
        parent.layer.closeAll();
    }
}


function closeLayer() {
    if (typeof parent != "undefined" && parent.layer) {
        parent.layer.closeAll();
    }
}

