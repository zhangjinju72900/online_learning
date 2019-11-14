select 
	id,
	name,
	remark,
	show_order as showOrder,
	valid_flag as validFlag 
from 
t_course
where professional_id = #{data.proId} and valid_flag = '0' and visible_flag = '0'