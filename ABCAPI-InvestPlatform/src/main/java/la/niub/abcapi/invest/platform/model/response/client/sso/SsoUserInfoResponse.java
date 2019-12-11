/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package la.niub.abcapi.invest.platform.model.response.client.sso;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 用户信息
 *
 * @author liuzh
 * @since 2016-01-31 21:39
 */
@Data
public class SsoUserInfoResponse {

    private String userId;

    private String userName;

    private String password;

    private String xingming;

    private String mobile;

    private String email;

    private String weixin;

    private String city;

    private Integer status;

    //创建时间
    private Timestamp createdAt;

    //上一次修改时间
    private Timestamp updatedAt;

    private String avatar;

    private String field;

    private String gender;

    private String instName;

    private Integer instId;

    private String department;

    private String position;

    private String nickName;

    private String unionid;

    private String businessCard;

    private String linkedId;

    private String lastname;

    private String channel;

    private Integer terminal;

    private String extend;

    private String code;

}
