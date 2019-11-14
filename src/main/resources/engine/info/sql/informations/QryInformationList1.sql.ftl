SELECT * FROM(
select 
	i.id as id,
	i.title as title,
	i.like_count as likeCount,
	i.click_count as clickCount,
	i.release_by as releaseBy,
	i.release_time as releaseTime,
	i.release_status as releaseStatus,
	i.information_label_id as subjectId,
	i.recommend_flag as reFlag,
	i.top_flag as tFlag,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlag,
	case when ISNULL(s.title) then '' 
	else s.title end as subjectName,
	cu.name as releaseName,
	dic.name as releaseStatusName
	,i.clickPeoCount
	from (select i.id, i.title, i.like_count, i.click_count, i.release_by, i.release_time, i.release_status,i.information_label_id,i.source,i.recommend_flag,i.top_flag,i.valid_flag,
	(select count(DISTINCT c.click_by) from t_information_click c where c.information_id=i.id  GROUP BY c.information_id) as clickPeoCount
	 from t_information i where  source=1 and (release_by=#{data.userInfo.userId} or #{data.userInfo.userId}='2') and (title LIKE CONCAT('%',#{data.text},'%') or content LIKE CONCAT('%',#{data.text},'%'))) i
	left join t_information_label s
	on i.information_label_id = s.id 
	left join t_customer_user cu on i.release_by = cu.id
	LEFT JOIN (select code, name from t_dict where cata_code = 't_information.release_status')
	dic on i.release_status = dic.`code`
	where i.valid_flag=0 and IF(#{data.subjectId}='',1=1,i.information_label_id=#{data.subjectId}) 
)a