select * from (
	select 
	CODE as value,
	case NAME
	when '已发布' then '发布'
	when '未发布' then '暂不发布'
	end as text
	from t_dict where cata_code='t_information.release_status'
 ) a 