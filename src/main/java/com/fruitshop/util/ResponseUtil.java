package com.fruitshop.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应工具类
 */
public class ResponseUtil {

    /**
     * 返回成功响应
     */
    public static void success(HttpServletResponse response, Object data) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);
        
        sendJsonResponse(response, result);
    }

    /**
     * 返回失败响应
     */
    public static void error(HttpServletResponse response, int code, String message) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        
        sendJsonResponse(response, result);
    }

    /**
     * 返回失败响应（默认错误码500）
     */
    public static void error(HttpServletResponse response, String message) throws IOException {
        error(response, 500, message);
    }

    /**
     * 发送JSON响应
     */
    private static void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JsonUtil.toJson(data));
        writer.flush();
        writer.close();
    }
}
