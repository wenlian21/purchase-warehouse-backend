package com.pharmacy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.model.dto.purchase.PurchaseOrderAuditRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderCreateRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderQueryRequest;
import com.pharmacy.model.entity.PurchaseOrder;
import com.pharmacy.model.vo.PurchaseOrderVO;
import com.pharmacy.model.vo.PurchaseSuggestionVO;
import java.util.List;

/**
 * 采购订单服务
 */
public interface PurchaseOrderService {

    /**
     * 创建采购订单
     *
     * @param request 创建请求
     * @return 订单 ID
     */
    Long createOrder(PurchaseOrderCreateRequest request);

    /**
     * 分页查询采购订单列表
     *
     * @param request 查询请求
     * @return 分页数据
     */
    Page<PurchaseOrderVO> listOrders(PurchaseOrderQueryRequest request);

    /**
     * 获取采购订单详情
     *
     * @param orderId 订单 ID
     * @return 订单详情
     */
    PurchaseOrderVO getOrderDetail(Long orderId);

    /**
     * 审核采购订单
     *
     * @param request 审核请求
     * @return 是否成功
     */
    boolean auditOrder(PurchaseOrderAuditRequest request);

    /**
     * 删除采购订单
     *
     * @param orderId 订单 ID
     * @return 是否成功
     */
    boolean deleteOrder(Long orderId);

    /**
     * 生成智能补货建议列表
     *
     * @return 补货建议列表
     */
    List<PurchaseSuggestionVO> generatePurchaseSuggestions();

    /**
     * 一键生成采购单（基于智能补货建议）
     *
     * @param supplierId 供应商 ID
     * @param medicineIds 药品 ID 列表（可选，为空则全部添加）
     * @return 订单 ID
     */
    Long generatePurchaseOrderFromSuggestions(Long supplierId, List<Long> medicineIds);

    /**
     * 采购入库确认
     *
     * @param orderId 采购订单 ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     * @return 是否成功
     */
    boolean confirmPurchaseInbound(Long orderId, String operatorName, String remark);

    /**
     * 导出采购订单为 Excel
     *
     * @param request 查询请求
     * @return Excel 数据
     */
    List<com.pharmacy.model.excel.PurchaseOrderExportRow> exportOrders(PurchaseOrderQueryRequest request);
}
