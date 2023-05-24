package com.ruoyi.web.controller.project;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by 21克的爱情
 *
 * @author yau
 * @date 2023-03-18 20:18
 * @description: 项目相关操作
 */
public class ProjectController extends BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public AjaxResult list(){
        return AjaxResult.success();
    }
}
