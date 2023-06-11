package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.SysClue;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.mapper.SysClueMapper;
import com.ruoyi.system.service.ISysClueService;
import com.ruoyi.system.service.ISysUserOnlineService;
import me.ihxq.projects.pna.PhoneNumberInfo;
import me.ihxq.projects.pna.PhoneNumberLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 访问线索情况信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SyClueServiceImpl implements ISysClueService
{

    @Autowired
    private SysClueMapper sysClueMapper;

    @Resource
    private ISysUserOnlineService sysUserOnlineService;

    @Resource
    private RedisCache redisCache;

    /**
     * 新增线索
     * 
     * @param sysClue 访问线索对象
     */
    @Override
    public int insertSysClue(SysClue sysClue)
    {
        String ipaddr = IpUtils.getIpAddr();
        if (StringUtils.isNotEmpty(ipaddr)) {
            sysClue.setIpaddr(ipaddr);
            String cityJsonStr = AddressUtils.getRealAddressByIP(ipaddr);
            JSONObject cityJson = new JSONObject();
            try {
                cityJson = JSONObject.parseObject(cityJsonStr);
            } catch (JSONException e) {
                cityJson.put("city", cityJsonStr);
            }
            sysClue.setIpCity(cityJson.getString("city"));
        }
        if (StringUtils.isNotEmpty(sysClue.getMobile())) {
            SysClue sysClueData = selectSysClueByInfo(sysClue);
            if (sysClueData != null) {
                return 0;
            }
            try{
                PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup();
                PhoneNumberInfo found = phoneNumberLookup.lookup(sysClue.getMobile()).orElseThrow(RuntimeException::new);
                sysClue.setMobileCity(found.getAttribution().getCity());
            } catch (RuntimeException e) {
                sysClue.setMobileCity("未知");
            }
        }

        autoUserAndDept(sysClue);
        sysClue.setClueSn(sysClueMapper.checkProjectSn());
        sysClue.setCreateBy("admin");
        return sysClueMapper.insertSysClue(sysClue);
    }

    private void autoUserAndDept(SysClue sysClue){
        // 获取在线用户列表
        Map<Long, SysUserOnline> onlineList = sysUserOnlineService.selectOnlineList().stream().collect(Collectors.toMap(
                SysUserOnline::getId,
                t -> t
        ));
        // 获取在线用户IDS
        List<Long> onlineIds = new ArrayList<>(onlineList.keySet());
        List<Integer> userIds = redisCache.getCacheObject(CacheConstants.CLUE_USER_LIST);
        if (userIds == null) {
            userIds = new ArrayList<>();
        }
        // 取出在线用户不在缓存的数据
        List<Long> finalUserIds = userIds.stream().map(Long::valueOf).collect(Collectors.toList());
        List<Long> onCacheIds = onlineIds.stream().filter(id -> !finalUserIds.contains(id)).collect(Collectors.toList());
        // 取出缓存用户且不在线的数据
        List<Long> onLineIds = finalUserIds.stream().filter(id -> !onlineIds.contains(id)).collect(Collectors.toList());
        // 先删除已缓存且不在线的用户数据
        finalUserIds.removeIf(onLineIds::contains);
        // 合并在线不在缓存中的用户数据->可分配资源
        finalUserIds.addAll(onCacheIds);
        // 进一步将数据进行分配
        if (finalUserIds.size() > 0) {
            Long userId = finalUserIds.get(0);

            SysUserOnline user = onlineList.get(userId);
            sysClue.setStatus(1);
            sysClue.setUserId(user.getId());
            sysClue.setDeptId(user.getDeptId());

            // 删除第一个元素并且将其插入到最后
            finalUserIds.remove(0);
            finalUserIds.add(userId);

        }
        // 再次更新分配过的用户列表数据
        redisCache.setCacheObject(CacheConstants.CLUE_USER_LIST, finalUserIds);
    }

    /**
     * 查询线索集合
     * 
     * @param sysClue 访问线索对象
     * @return 登录记录集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysClue> selectSysClueList(SysClue sysClue)
    {
        return sysClueMapper.selectSysClueList(sysClue);
    }

    /**
     * 查询线索集合
     *
     * @param sysClue 访问线索对象
     * @return 登录记录集合
     */
    @Override
    public SysClue selectSysClueByInfo(SysClue sysClue)
    {
        return sysClueMapper.selectSysClueByInfo(sysClue);
    }

    /**
     * 查询线索集合
     *
     * @param clueIds 访问线索对象
     * @return 登录记录集合
     */
    @Override
    public List<SysClue> selectSysClueList(List<Long> clueIds)
    {
        return sysClueMapper.selectSysClueListByIds(clueIds);
    }

    /**
     * 批量删除线索
     * 
     * @param infoIds 需要删除的线索ID
     * @return 结果
     */
    @Override
    public int deleteSysClueByIds(Long[] infoIds)
    {
        return sysClueMapper.deleteSysClueByIds(infoIds);
    }

    /**
     * 获取线索编号
     *
     * @return 结果
     */
    @Override
    public String checkProjectSn()
    {
        return sysClueMapper.checkProjectSn();
    }

    /**
     * 清空线索
     */
    @Override
    public void cleanSysClue()
    {
        sysClueMapper.cleanSysClue();
    }

    @Override
    public int updateClue(SysClue sysClue) {
        return sysClueMapper.updateClue(sysClue);
    }
}
