package com.pharmacy.model.dto.user;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求 DTO
 * 用于封装用户查询条件，支持分页和排序
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 搜索关键词（支持姓名、账号、工号、手机、邮箱）
     */
    private String keyword;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 用户状态
     */
    private String userStatus;

    private static final long serialVersionUID = 1L;
}
