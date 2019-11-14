select * from (
	select 
	name,
	parent_id
	from  t_professional 
	where valid_flag = '0' and parent_id = #{data.ctlParent}
 ) a 