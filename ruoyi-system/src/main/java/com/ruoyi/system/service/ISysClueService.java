package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysClue;

import java.util.List;
import java.util.Map;

/**
 * 访问线索情况信息 服务层
 * 
 * @author ruoyi
 */
public interface ISysClueService
{
    /**
     * 新增线索
     *
     * @param sysClue 访问线索对象
     */
    public int insertSysClue(SysClue sysClue);

    /**
     * 查询线索集合
     *
     * @param sysClue 访问线索对象
     * @return 记录集合
     */
    public List<SysClue> selectSysClueList(SysClue sysClue);

    /**
     * 查询线索集合
     *
     * @param sysClue 访问线索对象
     * @return 记录集合
     */
    public SysClue selectSysClueByInfo(SysClue sysClue);

    /**
     * 查询线索集合
     *
     * @param clueIds 线索IDS
     * @return 记录集合
     */
    public List<SysClue> selectSysClueList(List<Long> clueIds);

    /**
     * 批量删除线索
     *
     * @param infoIds 需要删除的线索ID
     * @return 结果
     */
    public int deleteSysClueByIds(Long[] infoIds);

    /**
     * 获取线索编号
     *
     * @return 结果
     */
    public String checkProjectSn();

    /**
     * 清空线索
     */
    public void cleanSysClue();

    public int updateClue(SysClue sysClue);
}
