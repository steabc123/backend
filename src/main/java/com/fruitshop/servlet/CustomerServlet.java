package com.fruitshop.servlet;

import com.fruitshop.entity.Customer;
import com.fruitshop.service.CustomerService;
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
 * 顾客Servlet - 处理顾客相关的HTTP请求
 */
@WebServlet("/api/customer/*")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerService customerService = new CustomerService();

    /**
     * GET请求处理
     * /api/customer/list - 查询所有顾客
     * /api/customer/{id} - 根据ID查询顾客
     * /api/customer/phone/{phone} - 根据手机号查询顾客
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            // 查询所有顾客
            if ("/list".equals(pathInfo)) {
                List<Customer> customers = customerService.getAllCustomers();
                ResponseUtil.success(response, customers);
                return;
            }

            // 根据手机号查询
            if (pathInfo.startsWith("/phone/")) {
                String phone = pathInfo.substring(7);
                Customer customer = customerService.getCustomerByPhone(phone);
                if (customer != null) {
                    ResponseUtil.success(response, customer);
                } else {
                    ResponseUtil.error(response, 404, "顾客不存在");
                }
                return;
            }

            // 根据ID查询
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2 && !parts[1].isEmpty()) {
                Integer id = Integer.parseInt(parts[1]);
                Customer customer = customerService.getCustomerById(id);
                if (customer != null) {
                    ResponseUtil.success(response, customer);
                } else {
                    ResponseUtil.error(response, 404, "顾客不存在");
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
     * POST请求处理 - 添加顾客
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            Customer customer = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Customer.class);
            
            if (customer == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            boolean success = customerService.addCustomer(customer);
            if (success) {
                ResponseUtil.success(response, customer);
            } else {
                ResponseUtil.error(response, "添加顾客失败");
            }
        } catch (RuntimeException e) {
            ResponseUtil.error(response, 400, e.getMessage());
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * PUT请求处理 - 更新顾客信息
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
                ResponseUtil.error(response, 400, "缺少顾客ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            Customer customer = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Customer.class);
            
            if (customer == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            customer.setId(id);
            boolean success = customerService.updateCustomer(customer);
            if (success) {
                ResponseUtil.success(response, customer);
            } else {
                ResponseUtil.error(response, "更新顾客失败");
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
     * DELETE请求处理 - 删除顾客
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
                ResponseUtil.error(response, 400, "缺少顾客ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            boolean success = customerService.deleteCustomer(id);
            if (success) {
                ResponseUtil.success(response, "删除成功");
            } else {
                ResponseUtil.error(response, "删除顾客失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }
}
