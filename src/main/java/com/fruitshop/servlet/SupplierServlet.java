package com.fruitshop.servlet;

import com.fruitshop.entity.Supplier;
import com.fruitshop.service.SupplierService;
import com.fruitshop.util.JsonUtil;
import com.fruitshop.util.ResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 供应商Servlet - 处理供应商相关的HTTP请求
 */
@WebServlet("/api/supplier/*")
public class SupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SupplierService supplierService = new SupplierService();

    /**
     * GET请求处理
     * /api/supplier/list - 查询所有供应商
     * /api/supplier/{id} - 根据ID查询供应商
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            // 查询所有供应商
            if ("/list".equals(pathInfo)) {
                List<Supplier> suppliers = supplierService.getAllSuppliers();
                ResponseUtil.success(response, suppliers);
                return;
            }

            // 根据ID查询
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2 && !parts[1].isEmpty()) {
                Integer id = Integer.parseInt(parts[1]);
                Supplier supplier = supplierService.getSupplierById(id);
                if (supplier != null) {
                    ResponseUtil.success(response, supplier);
                } else {
                    ResponseUtil.error(response, 404, "供应商不存在");
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
     * POST请求处理 - 添加供应商
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            Supplier supplier = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Supplier.class);
            
            if (supplier == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            boolean success = supplierService.addSupplier(supplier);
            if (success) {
                ResponseUtil.success(response, supplier);
            } else {
                ResponseUtil.error(response, "添加供应商失败");
            }
        } catch (RuntimeException e) {
            ResponseUtil.error(response, 400, e.getMessage());
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * PUT请求处理 - 更新供应商信息
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
                ResponseUtil.error(response, 400, "缺少供应商ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            Supplier supplier = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Supplier.class);
            
            if (supplier == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            supplier.setId(id);
            boolean success = supplierService.updateSupplier(supplier);
            if (success) {
                ResponseUtil.success(response, supplier);
            } else {
                ResponseUtil.error(response, "更新供应商失败");
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
     * DELETE请求处理 - 删除供应商
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
                ResponseUtil.error(response, 400, "缺少供应商ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            boolean success = supplierService.deleteSupplier(id);
            if (success) {
                ResponseUtil.success(response, "删除成功");
            } else {
                ResponseUtil.error(response, "删除供应商失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }
}
