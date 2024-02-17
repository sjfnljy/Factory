package com.sjf.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 实体类基类：用于保存实体类共有的字段
 * @Author: SJF
 * @Date: 2023/1/7
 **/
@Data
@ApiModel(value = "基础实体类，是所有实体类的基类")
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    @TableField("create_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @TableField("update_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    @TableLogic
    @TableField("is_deleted")
    @ExcelIgnore
    private Integer isDeleted;
}
