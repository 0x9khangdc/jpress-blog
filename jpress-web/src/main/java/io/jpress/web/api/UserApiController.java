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
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import io.jboot.apidoc.annotation.Api;
import io.jboot.apidoc.annotation.ApiOper;
import io.jboot.apidoc.annotation.ApiPara;
import io.jboot.apidoc.annotation.ApiResp;
import io.jboot.web.controller.annotation.PostRequest;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.JPressConsts;
import io.jpress.commons.utils.SessionUtils;
import io.jpress.model.User;
import io.jpress.service.UserService;
import io.jpress.web.base.ApiControllerBase;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 用户相关的API
 */
@RequestMapping("/api/user")
@Api("User -related API")
public class UserApiController extends ApiControllerBase {

    @Inject
    private UserService userService;


    @PostRequest
    @ApiOper("User login")
    @ApiResp(field = "Jwt", notes = "JWT's token information", mock = "ey1NiJ9.eyJpYX0ifQ.Y3p4akomy4")
    public Ret login(@ApiPara(value = "login account", notes = "Can be mailboxes") @NotNull String loginAccount
            , @ApiPara("login password") @NotNull String password) {
        User loginUser = userService.findByUsernameOrEmail(loginAccount);
        if (loginUser == null) {
            return Ret.fail("message", "No user information");
        }

        Ret ret = userService.doValidateUserPwd(loginUser, password);
        if (!ret.isOk()) {
            return ret;
        }

        SessionUtils.isLoginedOk(ret.get("user_id"));
        setJwtAttr(JPressConsts.JWT_USERID, ret.get("user_id"));

        String jwt = createJwtToken();
        return Ret.ok().set("Jwt", jwt);
    }


    @ApiOper("User details")
    @ApiResp(field = "user", dataType = User.class, notes = "User Info")
    public Ret detail(@ApiPara("User ID") @NotNull Long id) {
        User user = userService.findById(id);
        return Ret.ok().set("user", user.copy().keepSafe());
    }


    @ApiOper("Update user information")
    public Ret update(@ApiPara("User JSON Information") @JsonBody @NotNull User user) {
        user.keepUpdateSafe();
        userService.update(user);
        return Ret.ok();
    }

    @PostRequest
    @ApiOper("Update user password")
    public Ret updatePassword(@ApiPara("用户ID") @NotNull Long userId
            , @ApiPara("New password") @NotEmpty String newPassword
            , @ApiPara(value = "Old password", notes = "If the login user is a super administrator, you can enter the password without entering the password") String oldPassowrd) {

        User user = userService.findById(userId);
        if (user == null) {
            return Ret.fail("message", "this user does not exist");
        }

        String salt = user.getSalt();
        String hashedPass = HashKit.sha256(salt + newPassword);

        user.setPassword(hashedPass);
        userService.update(user);

        //移除用户登录 session
        SessionUtils.forget(userId);

        return Ret.ok();
    }


    @ApiOper("Create a new user")
    @ApiResp(field = "userId", notes = "User ID, the user returns this data after successful creation", dataType = Long.class)
    public Ret create(@ApiPara("User JSON Information") @JsonBody @NotNull User user) {
        userService.save(user);
        return Ret.ok().set("userId", user.getId());
    }
}
