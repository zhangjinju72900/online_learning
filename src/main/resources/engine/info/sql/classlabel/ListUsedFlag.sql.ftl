	select * from (
	select 
	CODE as value,
	name as text 
	from t_dict where cata_code='t_label.used_flag'
    )a