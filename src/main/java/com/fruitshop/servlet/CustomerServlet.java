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
import java.util.Map;

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
     * POST请求处理
     * /api/customer/login - 顾客登录
     * /api/customer/register - 顾客注册
     * 其他 - 添加顾客
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        String pathInfo = request.getPathInfo();

        try {
            // 顾客登录
            if ("/login".equals(pathInfo)) {
                Map<String, String> loginData = JsonUtil.fromJson(
                    request.getReader().lines().reduce("", (a, b) -> a + b), Map.class);
                if (loginData == null || loginData.get("name") == null || loginData.get("password") == null) {
                    ResponseUtil.error(response, 400, "用户名和密码不能为空");
                    return;
                }
                Customer customer = customerService.login(loginData.get("name"), loginData.get("password"));
                if (customer != null) {
                    // 登录成功，将用户信息存入Session
                    request.getSession().setAttribute("loginUser", customer);
                    ResponseUtil.success(response, customer);
                } else {
                    ResponseUtil.error(response, 401, "用户名或密码错误");
                }
                return;
            }

            // 顾客注册
            if ("/register".equals(pathInfo)) {
                Customer customer = JsonUtil.fromJson(
                    request.getReader().lines().reduce("", (a, b) -> a + b), Customer.class);
                if (customer == null) {
                    ResponseUtil.error(response, 400, "请求数据格式错误");
                    return;
                }
                boolean success = customerService.register(customer);
                if (success) {
                    ResponseUtil.success(response, customer);
                } else {
                    ResponseUtil.error(response, "注册失败");
                }
                return;
            }

            // 添加顾客（原有逻辑）
            Customer customer = JsonUtil.fromJson(
                request.getReader().lines().reduce("", (a, b) -> a + b), Customer.class);
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
            String requestBody = request.getReader().lines().reduce("", (a, b) -> a + b);
            System.out.println("更新顾客请求 - ID: " + id + ", 数据: " + requestBody);
            
            Customer customer = JsonUtil.fromJson(requestBody, Customer.class);
            
            if (customer == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            customer.setId(id);
            System.out.println("开始更新顾客...");
            boolean success = customerService.updateCustomer(customer);
            System.out.println("更新结果: " + (success ? "成功" : "失败"));
            if (success) {
                ResponseUtil.success(response, customer);
            } else {
                ResponseUtil.error(response, "更新顾客失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (RuntimeException e) {
            System.err.println("更新顾客业务异常: " + e.getMessage());
            e.printStackTrace();
            ResponseUtil.error(response, 400, e.getMessage());
        } catch (Exception e) {
            System.err.println("更新顾客服务器异常: " + e.getMessage());
            e.printStackTrace();
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
