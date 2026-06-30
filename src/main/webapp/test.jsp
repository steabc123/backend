<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>API 连通性测试</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: "Microsoft YaHei", sans-serif; background: #f0f2f5; padding: 24px; }
        h1 { text-align: center; margin-bottom: 24px; color: #333; }
        .summary { text-align: center; margin-bottom: 20px; font-size: 16px; color: #555; }
        .summary span { margin: 0 12px; font-weight: bold; }
        .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; max-width: 1100px; margin: 0 auto; }
        .card { background: #fff; border-radius: 10px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
        .card h2 { font-size: 18px; margin-bottom: 12px; display: flex; align-items: center; gap: 8px; }
        .card h2 .icon { font-size: 22px; }
        .btn { padding: 8px 18px; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; margin: 4px; transition: 0.2s; }
        .btn:hover { opacity: 0.85; }
        .btn-get { background: #1890ff; color: #fff; }
        .btn-all { background: #52c41a; color: #fff; }
        .btn-clear { background: #d9d9d9; color: #333; }
        .result { margin-top: 12px; background: #fafafa; border: 1px solid #e8e8e8; border-radius: 6px; padding: 12px; min-height: 80px; max-height: 300px; overflow-y: auto; font-family: Consolas, monospace; font-size: 13px; white-space: pre-wrap; word-break: break-all; }
        .status { display: inline-block; padding: 2px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; margin-left: 8px; }
        .status-ok { background: #f6ffed; color: #52c41a; border: 1px solid #b7eb8f; }
        .status-fail { background: #fff2f0; color: #ff4d4f; border: 1px solid #ffccc7; }
        .status-wait { background: #fffbe6; color: #faad14; border: 1px solid #ffe58f; }
        .loading { color: #1890ff; }
    </style>
</head>
<body>

<h1>🍉 水果商店 API 连通性测试</h1>
<div class="summary">
    测试结果：
    <span id="sum-ok" style="color:#52c41a">✅ 成功: 0</span>
    <span id="sum-fail" style="color:#ff4d4f">❌ 失败: 0</span>
    <span id="sum-wait" style="color:#faad14">⏳ 等待: 4</span>
</div>

<div class="grid">
    <!-- 水果模块 -->
    <div class="card">
        <h2><span class="icon">🍎</span> 水果模块 <span id="st-fruit" class="status status-wait">等待测试</span></h2>
        <div style="margin-bottom:10px">
            <button class="btn btn-get" onclick="testApi('fruit','/api/fruit/list')">测试查询所有水果</button>
            <button class="btn btn-get" onclick="testApi('fruit','/api/fruit/1')">测试查询ID=1</button>
            <button class="btn btn-clear" onclick="clearResult('fruit')">清空</button>
        </div>
        <div id="result-fruit" class="result">点击按钮开始测试...</div>
    </div>

    <!-- 顾客模块 -->
    <div class="card">
        <h2><span class="icon">👤</span> 顾客模块 <span id="st-customer" class="status status-wait">等待测试</span></h2>
        <div style="margin-bottom:10px">
            <button class="btn btn-get" onclick="testApi('customer','/api/customer/list')">测试查询所有顾客</button>
            <button class="btn btn-get" onclick="testApi('customer','/api/customer/1')">测试查询ID=1</button>
            <button class="btn btn-clear" onclick="clearResult('customer')">清空</button>
        </div>
        <div id="result-customer" class="result">点击按钮开始测试...</div>
    </div>

    <!-- 订单模块 -->
    <div class="card">
        <h2><span class="icon">📦</span> 订单模块 <span id="st-order" class="status status-wait">等待测试</span></h2>
        <div style="margin-bottom:10px">
            <button class="btn btn-get" onclick="testApi('order','/api/order/list')">测试查询所有订单</button>
            <button class="btn btn-get" onclick="testApi('order','/api/order/1')">测试查询ID=1</button>
            <button class="btn btn-clear" onclick="clearResult('order')">清空</button>
        </div>
        <div id="result-order" class="result">点击按钮开始测试...</div>
    </div>

    <!-- 供应商模块 -->
    <div class="card">
        <h2><span class="icon">🏭</span> 供应商模块 <span id="st-supplier" class="status status-wait">等待测试</span></h2>
        <div style="margin-bottom:10px">
            <button class="btn btn-get" onclick="testApi('supplier','/api/supplier/list')">测试查询所有供应商</button>
            <button class="btn btn-get" onclick="testApi('supplier','/api/supplier/1')">测试查询ID=1</button>
            <button class="btn btn-clear" onclick="clearResult('supplier')">清空</button>
        </div>
        <div id="result-supplier" class="result">点击按钮开始测试...</div>
    </div>
</div>

<div style="text-align:center; margin-top:24px;">
    <button class="btn btn-all" onclick="testAll()" style="font-size:16px; padding:12px 36px;">🚀 一键测试全部API</button>
</div>

<script>
    var BASE = '<%= request.getContextPath() %>';
    var results = { fruit: null, customer: null, order: null, supplier: null };

    function testApi(module, url) {
        url = BASE + url;
        var resultEl = document.getElementById('result-' + module);
        var statusEl = document.getElementById('st-' + module);
        resultEl.innerHTML = '<span class="loading">⏳ 请求中...</span>';
        statusEl.className = 'status status-wait';
        statusEl.textContent = '测试中...';

        var startTime = Date.now();
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.setRequestHeader('Accept', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var elapsed = Date.now() - startTime;
                var text = xhr.responseText || '(空响应)';
                try {
                    var json = JSON.parse(text);
                    text = JSON.stringify(json, null, 2);
                } catch(e) {}

                if (xhr.status === 200) {
                    try {
                        var json = JSON.parse(xhr.responseText);
                        if (json.code === 200) {
                            statusEl.className = 'status status-ok';
                            statusEl.textContent = '✅ 成功 (' + elapsed + 'ms)';
                            results[module] = true;
                        } else {
                            statusEl.className = 'status status-fail';
                            statusEl.textContent = '❌ 业务错误 (' + elapsed + 'ms)';
                            results[module] = false;
                        }
                    } catch(e) {
                        statusEl.className = 'status status-fail';
                        statusEl.textContent = '❌ 解析失败 (' + elapsed + 'ms)';
                        results[module] = false;
                    }
                } else {
                    statusEl.className = 'status status-fail';
                    statusEl.textContent = '❌ HTTP ' + xhr.status + ' (' + elapsed + 'ms)';
                    results[module] = false;
                }

                resultEl.textContent = 'HTTP状态: ' + xhr.status + '\n耗时: ' + elapsed + 'ms\n\n' + text;
                updateSummary();
            }
        };
        xhr.onerror = function () {
            var elapsed = Date.now() - startTime;
            statusEl.className = 'status status-fail';
            statusEl.textContent = '❌ 网络错误 (' + elapsed + 'ms)';
            resultEl.textContent = '无法连接到服务器，请确认后端是否启动。\nURL: ' + url;
            results[module] = false;
            updateSummary();
        };
        xhr.send();
    }

    function testAll() {
        results = { fruit: null, customer: null, order: null, supplier: null };
        testApi('fruit', '/api/fruit/list');
        testApi('customer', '/api/customer/list');
        testApi('order', '/api/order/list');
        testApi('supplier', '/api/supplier/list');
    }

    function clearResult(module) {
        document.getElementById('result-' + module).textContent = '点击按钮开始测试...';
        document.getElementById('st-' + module).className = 'status status-wait';
        document.getElementById('st-' + module).textContent = '等待测试';
        results[module] = null;
        updateSummary();
    }

    function updateSummary() {
        var ok = 0, fail = 0, wait = 0;
        for (var k in results) {
            if (results[k] === true) ok++;
            else if (results[k] === false) fail++;
            else wait++;
        }
        document.getElementById('sum-ok').textContent = '✅ 成功: ' + ok;
        document.getElementById('sum-fail').textContent = '❌ 失败: ' + fail;
        document.getElementById('sum-wait').textContent = '⏳ 等待: ' + wait;
    }
</script>

</body>
</html>
