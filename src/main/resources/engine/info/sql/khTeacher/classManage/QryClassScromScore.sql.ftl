select * from (
	SELECT
	r4.*,
	cu.name as customerUserName,
	cu.file_id as fileId,
	f.oss_url as ossUrl
	from (
	select r3.customerUserId,
	GROUP_CONCAT(r3.customerResourcesId ORDER BY r3.createTime asc) as customerResourcesIds,
	GROUP_CONCAT(r3.resourceName ORDER BY r3.createTime asc) as resourceNames,
	GROUP_CONCAT(r3.scoreShow ORDER BY r3.createTime asc) as scores,
	IFNULL(CONVERT(AVG(r3.score),DECIMAL(8,2)),'-') as avgScoreShow,
	CONVERT(AVG(r3.score),DECIMAL(8,2)) as avgScores
	from(
	select r1.*,
	r2.*,
	case when ISNULL(r2.score) then '-'
	else r2.score end as scoreShow
	from
		(select 
		c1.customerUserId,
		c2.customerResourcesId,
		c2.resourceName,
		c2.createTime
		from (select
				 cuc.customer_user_id as customerUserId
				 from t_customer_user_class cuc
				 where cuc.class_id = #{data.classId}
		)c1
		JOIN (SELECT
					DISTINCT csr.customer_resources_id as customerResourcesId,
					case  when  csr.course_resources_name = '' or ISNULL(csr.course_resources_name) then cr.name
					else csr.course_resources_name end as resourceName,
					csr.create_time as createTime
					from t_course_section_resources csr
					left join t_customer_resources cr 
					on csr.customer_resources_id = cr.id
					where csr.course_id = #{data.courseId} and cr.resources_type = 14)c2
					)r1

		left join (
					select id,customer_resources_id,customer_user_id,score,pass_score,percent_score,pass_percent
					from t_course_resources_score 
					where course_id =  #{data.courseId}
		)r2
	on r1.customerUserId = r2.customer_user_id and r1.customerResourcesId = r2.customer_resources_id
	)r3
	GROUP BY r3.customerUserId
	ORDER BY r3.customerUserId asc
)r4
left join t_customer_user cu 
on cu.id = r4.customerUserId
LEFT JOIN t_customer_user_role cuso on cuso.customer_user_id=cu.id 
left join t_file_index f
on cu.file_id = f.id where cuso.role_id=10
 ) a