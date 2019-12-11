package la.niub.abcapi.invest.platform.service;

import la.niub.abcapi.invest.platform.model.bo.sso.UserTokenBO;

/**
 * sso操作
 */
public interface ISsoService {

    /**
     * 获取一对当前所在环境sso的userId和token
     * @return
     */
    UserTokenBO getUserToken();

    /**
     * 获取指定的环境sso的userId和token
     * @param env
     * @return
     */
    UserTokenBO getUserToken(String env);
}
