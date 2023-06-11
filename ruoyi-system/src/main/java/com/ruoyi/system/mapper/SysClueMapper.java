package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysClue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 线索情况信息 数据层
 * 
 * @author ruoyi
 */
public interface SysClueMapper
{
    /**
     * 新增线索
     * 
     * @param sysClue 访问线索
     */
    public int insertSysClue(SysClue sysClue);

    /**
     * 查询线索集合
     * 
     * @param sysClue 访问线索
     * @return 登录记录集合
     */
    public List<SysClue> selectSysClueList(SysClue sysClue);


    /**
     * 获取线索信息
     * @param sysClue
     * @return
     */
    SysClue selectSysClueByInfo(SysClue sysClue);

    /**
     * 查询线索集合
     *
     * @param clueIds 访问线索
     * @return 登录记录集合
     */
    public List<SysClue> selectSysClueListByIds(@Param("ids") List<Long> clueIds);

    /**
     * 批量删除线索
     * 
     * @param infoIds 需要删除的登录线索ID
     * @return 结果
     */
    public int deleteSysClueByIds(Long[] infoIds);

    /**
     * 生产线索编号
     * @return
     */
    public String checkProjectSn();

    /**
     * 清空线索
     * 
     * @return 结果
     */
    public int cleanSysClue();

    public int updateClue(SysClue sysClue);
}
