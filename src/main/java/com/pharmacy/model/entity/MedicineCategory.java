package com.pharmacy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("medicineCategory")
public class MedicineCategory implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;//分类ID

    private String categoryName;//分类名称

    private String categoryRemark;//分类备注

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//更新时间

    @TableLogic
    private Integer isDelete;//逻辑删除标识（0-未删除，1-已删除）

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;//序列化版本号
}
