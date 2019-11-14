select * from (
	select 
	sub.id as id,
	sub.title as name
	 from t_information_label sub
 ) a 