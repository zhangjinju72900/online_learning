select * from (
	select 
	concat(parent_id,id),
	id as id,
    CASE
		WHEN parent_id = 0 THEN
			''
		ELSE
			parent_id
		END AS pid,
	name as text
	from t_question_classify
	where valid_flag=0

 ) a 