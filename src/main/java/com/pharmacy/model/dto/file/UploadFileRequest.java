package com.pharmacy.model.dto.file;

import java.io.Serializable;

import lombok.Data;

/**
 * 文件上传请求
 */
@Data
public class UploadFileRequest implements Serializable {

    private String biz;//业务类型标识，用于区分不同业务场景的文件上传

    private static final long serialVersionUID = 1L;// 序列化版本号
}