select * from (
	select  
	code as value,
	name as text
	from t_dict 
	where cata_code = 't_homework.homework_type'
 ) a 