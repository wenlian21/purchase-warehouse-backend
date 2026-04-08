package com.pharmacy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.common.BaseResponse;
import com.pharmacy.common.ResultUtils;
import com.pharmacy.model.dto.purchase.PurchaseOrderAuditRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderCreateRequest;
import com.pharmacy.model.dto.purchase.PurchaseOrderQueryRequest;
import com.pharmacy.model.excel.PurchaseOrderExportRow;
import com.pharmacy.model.vo.PurchaseOrderVO;
import com.pharmacy.model.vo.PurchaseSuggestionVO;
import com.pharmacy.service.PurchaseOrderService;
import com.pharmacy.utils.ExcelUtils;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购订单控制器
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseOrderController {

    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 创建采购订单
     *
     * @param request 创建请求
     * @return 订单 ID
     */
    @PostMapping("/create")
    public BaseResponse<Long> createOrder(@RequestBody PurchaseOrderCreateRequest request) {
        return ResultUtils.success(purchaseOrderService.createOrder(request));
    }

    /**
     * 分页查询采购订单列表
     *
     * @param request 查询请求
     * @return 分页数据
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PurchaseOrderVO>> listOrders(@RequestBody(required = false) PurchaseOrderQueryRequest request) {
        return ResultUtils.success(purchaseOrderService.listOrders(request == null ? new PurchaseOrderQueryRequest() : request));
    }

    /**
     * 获取采购订单详情
     *
     * @param orderId 订单 ID
     * @return 订单详情
     */
    @GetMapping("/detail/{orderId}")
    public BaseResponse<PurchaseOrderVO> getOrderDetail(@PathVariable Long orderId) {
        return ResultUtils.success(purchaseOrderService.getOrderDetail(orderId));
    }

    /**
     * 审核采购订单
     *
     * @param request 审核请求
     * @return 是否成功
     */
    @PostMapping("/audit")
    public BaseResponse<Boolean> auditOrder(@RequestBody PurchaseOrderAuditRequest request) {
        return ResultUtils.success(purchaseOrderService.auditOrder(request));
    }

    /**
     * 删除采购订单
     *
     * @param orderId 订单 ID
     * @return 是否成功
     */
    @DeleteMapping("/delete/{orderId}")
    public BaseResponse<Boolean> deleteOrder(@PathVariable Long orderId) {
        return ResultUtils.success(purchaseOrderService.deleteOrder(orderId));
    }

    /**
     * 生成智能补货建议
     *
     * @return 补货建议列表
     */
    @GetMapping("/suggestions")
    public BaseResponse<List<PurchaseSuggestionVO>> generateSuggestions() {
        return ResultUtils.success(purchaseOrderService.generatePurchaseSuggestions());
    }

    /**
     * 一键生成采购单
     *
     * @param supplierId 供应商 ID
     * @param medicineIds 药品 ID 列表（逗号分隔，可选）
     * @return 订单 ID
     */
    @PostMapping("/generate")
    public BaseResponse<Long> generatePurchaseOrder(
            @RequestParam Long supplierId,
            @RequestParam(required = false) List<Long> medicineIds) {
        return ResultUtils.success(purchaseOrderService.generatePurchaseOrderFromSuggestions(supplierId, medicineIds));
    }

    /**
     * 采购入库确认
     *
     * @param orderId 订单 ID
     * @param operatorName 操作人姓名
     * @param remark 备注
     * @return 是否成功
     */
    @PostMapping("/inbound/{orderId}")
    public BaseResponse<Boolean> confirmInbound(
            @PathVariable Long orderId,
            @RequestParam String operatorName,
            @RequestParam(required = false) String remark) {
        return ResultUtils.success(purchaseOrderService.confirmPurchaseInbound(orderId, operatorName, remark));
    }

    /**
     * 导出采购订单为 Excel
     *
     * @param request 查询请求
     * @param response HTTP 响应
     */
    @PostMapping("/export")
    public void exportOrders(@RequestBody(required = false) PurchaseOrderQueryRequest request, HttpServletResponse response) {
        List<PurchaseOrderExportRow> rows = purchaseOrderService.exportOrders(request == null ? new PurchaseOrderQueryRequest() : request);
        ExcelUtils.writeExcel(response, "采购订单", "采购订单", PurchaseOrderExportRow.class, rows);
    }
}
