<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <th>名称</th>
        <th>库存</th>
        <th>开始时间</th>
        <th>结束时间</th>
        <th>创建时间</th>
        <th>详情页</th>
    </tr>
<#list seckills as seckill>
    <tr>
        <td>${seckill.name}</td>
        <td>${seckill.number}</td>
    </tr>
</#list>
</table>

</body>
</html>