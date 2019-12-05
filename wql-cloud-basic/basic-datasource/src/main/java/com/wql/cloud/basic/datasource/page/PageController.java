package com.wql.cloud.basic.datasource.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通用页面跳转
 * 
 * @author wangqiulin
 *
 */
@Controller
@RequestMapping("/page/")
public class PageController {

	@RequestMapping(value="{page}", method= RequestMethod.GET)
    public String page(@PathVariable("page") String page){
        return page;
    }
	
	@RequestMapping(value="{page}/{page2}", method= RequestMethod.GET)
    public String page(@PathVariable("page") String page, @PathVariable("page2") String page2){
        return page+"/"+page2;
    }

	@RequestMapping(value="{page}/{page2}/{page3}", method= RequestMethod.GET)
    public String page(@PathVariable("page") String page, @PathVariable("page2") String page2, @PathVariable("page3") String page3){
        return page+"/"+page2+"/"+page3;
    }
	
}
