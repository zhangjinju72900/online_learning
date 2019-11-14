select * from (
	select	id as id,
	
	name as name
	from t_customer_resources
	where backup_type = '1' and file_type = '' and valid_flag=0

 ) a where a.id=#{data.id}