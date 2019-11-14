SELECT * FROM(
select 
	i.id,
	i.title,
	i.content,
	i.release_by as releaseBy,
	i.release_time as releaseTime,
	i.release_status as releaseStatus,
	i.create_by as createBy,
	i.create_time as createTime,
	i.update_by as updateBy,
	i.update_time as updateTime,
	i.information_label_id as subjectId,
	i.file_id as videoId,
	s.title as subjectName,
	cu.name as releaseName,
	cu1.name as updateName,
	f.filename as videoName,
	dic.name as releaseStatusName
	from (select id, title, content, release_by, release_time, release_status,create_time,create_by,update_time,update_by,information_label_id,file_id from t_information ) i
	left join t_information_label s
	on i.information_label_id = s.id 
	left join (select id,name from t_customer_user) cu on i.release_by = cu.id
	left join (select id,name from t_customer_user) cu1 on i.update_by=cu1.id
	LEFT JOIN (select code, name from t_dict where cata_code = 't_information.release_status')
	dic on i.release_status = dic.`code`
	left join t_file_index f on i.file_id = f.id

)a