package com.fruitshop.servlet;

import com.fruitshop.entity.Fruit;
import com.fruitshop.service.FruitService;
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
 * 水果Servlet - 处理水果相关的HTTP请求
 */
@WebServlet("/api/fruit/*")
public class FruitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FruitService fruitService = new FruitService();

    /**
     * GET请求处理
     * /api/fruit/list - 查询所有水果
     * /api/fruit/{id} - 根据ID查询水果
     * /api/fruit/category/{category} - 根据分类查询水果
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtil.error(response, "无效的请求路径");
            return;
        }

        try {
            // 查询所有水果
            if ("/list".equals(pathInfo)) {
                List<Fruit> fruits = fruitService.getAllFruits();
                ResponseUtil.success(response, fruits);
                return;
            }

            // 根据分类查询
            if (pathInfo.startsWith("/category/")) {
                String category = pathInfo.substring(10);
                List<Fruit> fruits = fruitService.getFruitsByCategory(category);
                ResponseUtil.success(response, fruits);
                return;
            }

            // 根据ID查询
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2 && !parts[1].isEmpty()) {
                Integer id = Integer.parseInt(parts[1]);
                Fruit fruit = fruitService.getFruitById(id);
                if (fruit != null) {
                    ResponseUtil.success(response, fruit);
                } else {
                    ResponseUtil.error(response, 404, "水果不存在");
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
     * POST请求处理 - 添加水果
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            Fruit fruit = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Fruit.class);
            
            if (fruit == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            boolean success = fruitService.addFruit(fruit);
            if (success) {
                ResponseUtil.success(response, fruit);
            } else {
                ResponseUtil.error(response, "添加水果失败");
            }
        } catch (RuntimeException e) {
            ResponseUtil.error(response, 400, e.getMessage());
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * PUT请求处理 - 更新水果信息
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
                ResponseUtil.error(response, 400, "缺少水果ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            Fruit fruit = JsonUtil.fromJson(request.getReader().lines().reduce("", (a, b) -> a + b), Fruit.class);
            
            if (fruit == null) {
                ResponseUtil.error(response, 400, "请求数据格式错误");
                return;
            }

            // 禁止修改主键ID，始终使用路径参数中的ID
            fruit.setId(id);
            
            boolean success = fruitService.updateFruit(fruit);
            if (success) {
                ResponseUtil.success(response, fruit);
            } else {
                ResponseUtil.error(response, "更新水果失败");
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
     * DELETE请求处理 - 删除水果
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
                ResponseUtil.error(response, 400, "缺少水果ID");
                return;
            }

            Integer id = Integer.parseInt(parts[1]);
            boolean success = fruitService.deleteFruit(id);
            if (success) {
                ResponseUtil.success(response, "删除成功");
            } else {
                ResponseUtil.error(response, "删除水果失败");
            }
        } catch (NumberFormatException e) {
            ResponseUtil.error(response, 400, "参数格式错误");
        } catch (Exception e) {
            ResponseUtil.error(response, "服务器内部错误: " + e.getMessage());
        }
    }
}
