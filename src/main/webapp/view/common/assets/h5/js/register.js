  
    const schoolList = document.querySelector('#schoolList');
    const schoolValue = document.querySelector('#schoolValue');
    const schoolInp = document.querySelector('#schoolInp');
// 获取学校列表
    function getSchoolList(text, callback) {
      console.log('学校框里的值:', text);

      const rows = [
        { text: '北京市 大兴区 亦庄经济开发区达内软件大厦1层', value: '1' },
        { text: '北京市 海淀区 中鼎大厦B座312', value: '2' },
        { text: '广东市 珠海区 新港中路354号珠影大院蓝谷2新港中路354号珠影大院蓝谷2', value: '3' },
      ];

      callback(rows);
    }

    // 监听学校输入框
    function onSchoolInp(e) {
   	    const text = e.target.value;
		ajaxPost(APIS.frmPersonalInfoList.jsquery, {
			eq_schoolName : text
		}, function(data) {
			alert(data);
		});

	
    
     /*  if (!text) {
        return;
      }

      getSchoolList(text, function(rows) {
        let lis = [];
        for (const item of rows) {
        	let li = "<li value='1'>1</li>";
          lis.push(li);
        }

        schoolList.innerHTML = lis.join('');
        schoolList.style.display = 'block';
      }); */
    }

    // 监听选择学校事件
/*    schoolList.addEventListener('click', function(e) {
      const target = e.target;
      schoolInp.value = target.innerText;
      schoolValue.value = target.getAttribute('value');
      schoolList.style.display = 'none';
    });*/

    function onSubmit() {
      const form = document.querySelector('form');
      const inputs = form.querySelectorAll('input');
      for (const input of inputs) {
        console.log(input.value);
      }
      return false;
    }