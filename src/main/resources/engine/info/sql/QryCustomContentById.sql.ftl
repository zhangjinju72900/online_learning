select * from (
	select id,feedback
	from t_feedback
	where valid_flag = '0' and feedback not like ""
 ) a 