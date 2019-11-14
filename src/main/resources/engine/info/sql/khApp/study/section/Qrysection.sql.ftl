select * from (
	SELECT 
	id,
	name,
	course_id as courseId,
	show_order as showOrder,
	remark
	from 
	t_section where valid_flag = '0'
) a 
