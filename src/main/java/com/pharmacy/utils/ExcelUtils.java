package com.pharmacy.utils;

import com.alibaba.excel.EasyExcel;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public final class ExcelUtils {
    /**
     * 私有构造函数，防止实例化
     */
    private ExcelUtils() {
    }
/*
 * 将数据写入 Excel 并通过 HTTP 响应下载
 */
    public static <T> void writeExcel(HttpServletResponse response, String fileName, String sheetName, Class<T> head,
            List<T> data) {
        try {
            //设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置响应字符集
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            //设置响应文件名
            String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            //设置响应头，控制文件下载
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedName + ".xlsx");
            //写入数据
            EasyExcel.write(response.getOutputStream(), head).sheet(sheetName).doWrite(data);
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }
}
