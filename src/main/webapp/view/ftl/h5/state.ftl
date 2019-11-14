<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>状态</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/weui/1.1.2/style/weui.min.css">
    <link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.0/css/jquery-weui.min.css">    
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/common.css">
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/state.css">
</head>
<body>
    <div class="outermost outermost-title" >
        <span class="title"><b class="return" onclick="back()"></b>状态</span>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s11">
            <div class="weui-cell__bd">
              <p class="font-size">打开</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s11" checked="check">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s12">
            <div class="weui-cell__bd">
              <p class="font-size">处理中</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s12">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s13">
            <div class="weui-cell__bd">
              <p class="font-size">已解决</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s13">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s14">
            <div class="weui-cell__bd">
              <p class="font-size">测试中</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s14">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s15">
            <div class="weui-cell__bd">
              <p class="font-size">已关闭</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s15">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
        <label class="weui-cell weui-check__label" for="s16">
            <div class="weui-cell__bd">
              <p class="font-size">已取消</p>
            </div>
            <div class="weui-cell__hd">
            <input type="radio" class="weui-check" name="checkbox1" id="s16">
            <i class="weui-icon-checked"></i>
            </div>
        </label>
    </div>
    <div class="weui-cells weui-cells_checkbox outermost">
            <label class="weui-cell weui-check__label" for="s17">
                <div class="weui-cell__bd">
                  <p class="font-size">重新打开</p>
                </div>
                <div class="weui-cell__hd">
                <input type="radio" class="weui-check" name="checkbox1" id="s17">
                <i class="weui-icon-checked"></i>
                </div>
            </label>
        </div>
    
    <script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery-weui/1.2.0/js/jquery-weui.min.js"></script>
    <script src="${ctx}/view/common/assets/h5/js/state.js"></script>
</body>
</html>