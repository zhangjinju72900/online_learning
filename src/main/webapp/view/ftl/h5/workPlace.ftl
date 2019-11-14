<!DOCTYPE html>
<html lang="en">
<head>
  <#assign ctx=request.contextPath>
  <meta charset="UTF-8">
  <meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/common.css">
  <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/workPlace.css">
  <link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/foot.css" />
  <title>个人工作台</title>
  <script src="${ctx}/view/common/js/jquery-1.8.3.min.js"></script>
  <#include "pc/common/base.ftl">
</head>
<body>
  <div class="top">
      <span>个人工作台</span>
  </div>
  <div class="kuang" onclick="jump1()">
      <div class="one">分配给我的工作项</div>
      <div class="photo"></div>
  </div>
  <div class="kuang" onclick="jump2()">
      <div class="two">我报告的工作项</div>
      <div class="photo"></div>
  </div>
  <div class="kuang" onclick="jump3()">
      <div class="three">我的测试设计任务</div>
      <div class="photo"></div>
  </div>
  <div class="kuang" onclick="jump4()">
      <div class="four">分配给我测试的工作项</div>
      <div class="photo"></div>
  </div>
  <div class="kuang" onclick="jump5()">
      <div class="five">新增缺陷</div>
      <div class="photo"></div>
  </div>
  <div id="foot">
    <ul>
        <li>
            <a href="${ctx}/workplace" class="color-active">
                <div class="icon icon-1 icon-1-active"></div>
                工作台
            </a>

        </li>
        <li>
            <a href="${ctx}/list/search-all">
                <div class="icon icon-2"></div>
                列表
            </a>

        </li>
        <li>
            <a href="${ctx}/defect/list">
                <div class="icon icon-3"></div>
                缺陷
            </a>

        </li>
        <li>
            <a href="${ctx}/my" >
                <div class="icon icon-4"></div>
                我的
            </a>

        </li>
    </ul>
</div>
</body>
<script src="${ctx}/view/common/assets/h5/js/workPlce.js"></script>
<script type="text/javascript">
	
</script>
</html>