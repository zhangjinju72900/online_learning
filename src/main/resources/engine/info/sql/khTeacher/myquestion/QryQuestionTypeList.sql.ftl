select * from (
	select  
	code as value,
	name as text
	from t_dict 
	where cata_code = 't_question_question_type' and name != '主观'
 ) a 