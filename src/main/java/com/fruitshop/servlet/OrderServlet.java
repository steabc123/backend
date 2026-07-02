package com.fruitshop.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fruitshop.entity.Order;
import com.fruitshop.entity.OrderItem;
import com.fruitshop.service.OrderService;
import com.fruitshop.util.JsonUtil;
import com.fruitshop.util.ResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 订单Servlet - 处理订单相关的HTTP请求
 */
@WebServlet("/api/order/*")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService = new OrderService();

    /**
     * GET请求处理
     * /api/order/list - 查询所有订单
     * /api/order/{id} - 根据ID查询订单
     * /api/order/no/{orderNo} - 根据订单编号查询
     * /api/order/customer/{customerId} - 根据顾客ID查询订单
     * /api/order/status/{status} - 根据状态查询订单
     * /api/order/{id}/items - 查询订单明细
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            // 查询所有订单
            if ("/list".equals(pathInfo)) {
                List<Order> orders = orderService.getAllOrders();
                ResponseUtil.success(response, orders);
                return;
            }

            // 根据订单编号查询
            if (pathInfo.startsWith("/no/")) {
                String orderNo = pathInfo.substring(4);
                Order order = orderService.getOrderByOrderNo(orderNo);
                if (order != null) {
                    ResponseUtil.success(response, order);
                } else {
                    ResponseUtil.error(response, 404, "订单不存在");
                }
                return;
            }

            // 根据顾客ID查询
            if (pathInfo.startsWith("/customer/")) {
                String customerIdStr = pathInfo.substring(10);
                // 检查是否是订单统计请求: /api/order/customer/{id}/stats
                if (customerIdStr.endsWith("/stats")) {
                    customerIdStr = customerIdStr.substring(0, customerIdStr.length() - 6);
                    Integer customerId = Integer.parseInt(customerIdStr);
                    Map<String, Integer> stats = orderService.getOrderStats(customerId);
                    ResponseUtil.success(response, stats);
                    return;
                }
                Integer customerId = Integer.parseInt(customerIdStr);
                List<Order> orders = orderService.getOrdersByCustomerId(customerId);
                ResponseUtil.success(response, orders);
                return;
            }

            // 根据状态查询
            if (pathInfo.startsWith("/status/")) {
                String status = pathInfo.substring(8);
                List<Order> orders = orderService.getOrdersByStatus(status);
                ResponseUtil.success(response, orders);
                return;
            }

            // 查询订单明细
            if (pathInfo.matches("/\\d+/items")) {
                String[] parts = pathInfo.split("/");
                Integer orderId = Integer.parseInt(parts[1]);
                List<OrderItem> items = orderService.getOrderItems(orderId);
                ResponseUtil.success(response, items);
                return;
            }

            // 根据ID查询
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2 && !parts[1].isEmpty()) {
                Integer id = Integer.parseInt(parts[1]);
                Order order = orderService.getOrderById(id);
                if (order != null) {
                    ResponseUtil.success(response, order);
                } else {
                    ResponseUtil.error(response, 404, "订单不存在");
                }
                return;
            }

            ResponseUtil.error(response, "无效的请求路径");
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * POST请求处理 - 创建订单
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            // 读取请求体
            String requestBody = request.getReader().lines().reduce("", (a, b) -> a + b);
            System.out.println("订单请求数据: " + requestBody);
            
            // 解析请求体，期望格式: {"order": {...}, "items": [...]}
            Map<String, Object> requestData = JsonUtil.fromJson(requestBody, Map.class);
            
            if (requestData == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            Order order = JsonUtil.fromJson(JsonUtil.toJson(requestData.get("order")), Order.class);
            List<OrderItem> items = JsonUtil.fromJson(
                JsonUtil.toJson(requestData.get("items")), 
                new TypeReference<List<OrderItem>>() {}
            );
            
            System.out.println("解析后的订单: " + order);
            System.out.println("解析后的订单项数量: " + (items != null ? items.size() : 0));
            
            if (order == null || items == null || items.isEmpty()) {
                ResponseUtil.error(response, 400, "订单信息或明细不能为空");
                return;
            }

            boolean success = orderService.createOrder(order, items);
            if (success) {
                ResponseUtil.success(response, order);
            } else {
                ResponseUtil.error(response, "创建订单失败");
            }
        } catch (RuntimeException e) {
            // 返回具体的业务异常信息
            ResponseUtil.error(response, 400, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // 记录详细错误日志
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PUT请求处理 - 更新订单状态
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            String[] parts = pathInfo.split("/");
            if (parts.length < 2 || parts[1].isEmpty()) {
                ResponseUtil.error(response, 400, "缺少订单ID");
                return;
            }

            Integer orderId = Integer.parseInt(parts[1]);
            
            // 解析请求体获取新状态
            Map<String, Object> requestData = JsonUtil.fromJson(
                request.getReader().lines().reduce("", (a, b) -> a + b), 
                Map.class
            );
            
            if (requestData == null || requestData.get("status") == null) {
                ResponseUtil.error(response, 400, "缺少订单状态");
                return;
            }

            String status = (String) requestData.get("status");
            boolean success = orderService.updateOrderStatus(orderId, status);
            if (success) {
                ResponseUtil.success(response, "订单状态更新成功");
            } else {
                ResponseUtil.error(response, "更新订单状态失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (RuntimeException e) {
            ResponseUtil.error(response, 400, e.getMessage());
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * DELETE请求处理 - 删除订单
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            String[] parts = pathInfo.split("/");
            if (parts.length < 2 || parts[1].isEmpty()) {
                ResponseUtil.error(response, 400, "缺少订单ID");
                return;
            }

            Integer orderId = Integer.parseInt(parts[1]);
            boolean success = orderService.deleteOrder(orderId);
            if (success) {
                ResponseUtil.success(response, "删除成功");
            } else {
                ResponseUtil.error(response, "删除订单失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }
}
