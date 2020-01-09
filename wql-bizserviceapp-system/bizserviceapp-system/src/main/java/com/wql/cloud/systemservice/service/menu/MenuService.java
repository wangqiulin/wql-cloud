package com.wql.cloud.systemservice.service.menu;

import com.wql.cloud.systemservice.pojo.req.menu.UserMenuReq;
import com.wql.cloud.systemservice.pojo.res.menu.UserMenuRes;

public interface MenuService {

	/**
	 * 查询用户菜单
	 * @param req
	 * @return
	 */
	UserMenuRes getUserMenu(UserMenuReq req);
	
}
