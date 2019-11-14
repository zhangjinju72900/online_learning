SELECT * FROM(
select 
	u.id as id,
	u.file_id as fileId,
	f.filename as fileName
	from t_customer_user  u
	left join t_file_index f on u.file_id = f.id
	where u.id=#{data.id}
)a