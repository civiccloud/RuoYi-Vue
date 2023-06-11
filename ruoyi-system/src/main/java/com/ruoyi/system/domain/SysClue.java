package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 线索信息
 * 
 * @author ruoyi
 */
@Data
public class SysClue extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    /** ID */
    private Long id;

    /** 线索编号 */
    private String clueSn;

    /** 用户ID */
    private Long userId;

    /** 用户ID */
    @TableField(exist = false)
    private String userName;

    /** 部门ID */
    private Long deptId;

    /** 部门ID */
    @TableField(exist = false)
    private String deptName;

    /** 用户名称 */
    private String name;

    /** 手机号码 */
    private String mobile;

    /** 城市 */
    private String mobileCity;

    /** 城市 */
    private String ipCity;

    /** ip地址 */
    private String ipaddr;

    /** 状态（0正常 1关闭） */
    private Integer status;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
}
