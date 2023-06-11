package com.ruoyi.web.controller.clue;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.SysClue;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysClueService;
import com.ruoyi.system.service.ISysUserOnlineService;
import me.ihxq.projects.pna.PhoneNumberInfo;
import me.ihxq.projects.pna.PhoneNumberLookup;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 21克的爱情
 *
 * @author yau
 * @date 2023-06-10 18:59
 * @description:
 */
@RestController
@RequestMapping("/sys/clue")
public class SysClueController extends BaseController {
    @Resource
    private ISysClueService sysClueService;


    @GetMapping("/test")
    public AjaxResult test(){
        SysClue sysClue = new SysClue();
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('sys:clue:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysClue sysClue){
        startPage();
        List<SysClue> list = sysClueService.selectSysClueList(sysClue);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('sys:clue:add')")
    @PostMapping("/info")
    public AjaxResult insert(@RequestBody SysClue sysClue){
        sysClue.setCreateBy(getUsername());
        return toAjax(sysClueService.insertSysClue(sysClue));
    }

    @PreAuthorize("@ss.hasPermi('sys:clue:edit')")
    @PutMapping("/info")
    public AjaxResult info(@RequestBody SysClue sysClue){
        sysClue.setUpdateBy(getUsername());
        return toAjax(sysClueService.updateClue(sysClue));
    }

    @PreAuthorize("@ss.hasPermi('sys:clue:query')")
    @GetMapping("/info/{id}")
    public AjaxResult info(@PathVariable Long id){
        SysClue sysClue = new SysClue();
        sysClue.setId(id);
        SysClue sysClues = sysClueService.selectSysClueByInfo(sysClue);
        return AjaxResult.success(sysClues);
    }

    /**
     * 删除线索
     */
    @PreAuthorize("@ss.hasPermi('sys:clue:remove')")
    @Log(title = "线索管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clueId}")
    public AjaxResult remove(@PathVariable Long[] clueId)
    {
        return toAjax(sysClueService.deleteSysClueByIds(clueId));
    }
}
