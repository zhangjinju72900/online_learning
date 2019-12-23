<!DOCTYPE html>
<html>
<head>
    <title>HTML5 Video Pseudostreaming</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
    <script>
        $(document).ready(function(){
            var video = $('#myvideo');

            $("#play").click(function(){  video[0].play();  });
            $("#pause").click(function(){ video[0].pause(); });
            $("#go10").click(function(){  video[0].currentTime+=10;  });
            $("#back10").click(function(){  video[0].currentTime-=10;  });
            $("#rate1").click(function(){  video[0].playbackRate+=2;  });
            $("#rate0").click(function(){  video[0].playbackRate-=2;  });
            $("#volume1").click(function(){  video[0].volume+=0.1;  });
            $("#volume0").click(function(){  video[0].volume-=0.1;  });
            $("#muted1").click(function(){  video[0].muted=true;  });
            $("#muted0").click(function(){  video[0].muted=false;  });
            $("#full").click(function(){
                video[0].webkitEnterFullscreen(); // webkit类型的浏览器
                video[0].mozRequestFullScreen();  // FireFox浏览器
            });
            $("#myvideo").find("source").attr("src",getRootPath_web()+"/getVideo?id=${id}");
        });
        function getRootPath_web() {
            //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
            var curWwwPath = window.document.location.href;
            //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            //获取主机b地址，如： http://localhost:8083
            var localhostPath = curWwwPath.substring(0, pos);
            //获取带"/"的项目名，如：/uimcardprj
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

            var rootPath = localhostPath + projectName;
            console.log(rootPath);
            if(rootPath.search("view") == -1){
                return (rootPath);
            }else{
                return localhostPath;
            }
        }
    </script>
</head>
<body>
<video id="myvideo" width="80%" height="80%" controls="controls">
    <source />
    <!--<source src="stream?video=美丽的风景.mp4" type="video/mp4" />
    <source src="stream?video=美丽的风景.mp4" type="video/webM" />
    <source src="stream?video=美丽的风景.mp4" type="video/ogg" />-->
    你的浏览器不支持html5
</video>
<hr>
<button id="play">播放</button>
<button id="pause">暂停</button>
<button id="go10">快进10秒</button>
<button id="back10">快退10秒</button>
<button id="rate1">播放速度+</button>
<button id="rate0">播放速度-</button>
<button id="volume1">声音+</button>
<button id="volume0">声音-</button>
<button id="muted1">静音</button>
<button id="muted0">解除静音</button>
<button id="full">全屏</button>
</body>
</html> 