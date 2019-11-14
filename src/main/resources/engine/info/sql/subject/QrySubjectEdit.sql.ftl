SELECT * FROM(
select 
	i.id as id,
	i.title as title,
	i.content as content,
	i.release_by as relBy,
	i.release_time as relTime,
	i.release_status as relStatus,
	i.recommend_flag as reFlag,
	i.top_flag as tFlag,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recFlag,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlag,
	cu.name as relByName,
	fi.filename as file,
	dic.name as releaseStatusName
	from t_subject i
	left join t_customer_user cu on i.release_by = cu.id
	LEFT JOIN (select code, name from t_dict where cata_code = 't_subject.release_status')
	dic on i.release_status = dic.`code` 
	left join t_file_index fi on fi.id=i.file_id 
	where i.valid_flag=0
)a