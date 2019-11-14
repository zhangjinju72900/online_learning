select * from (
SELECT 
	
	tls.id AS id,
	tls.lesson_plan_id AS lessonPlanId,
	tls.name AS bzname,
	tls.teaching_count AS teachingCount,
	tls.teaching_content AS steachingContent,
	tls.teaching_assist AS teachingAssist,
	tls.teaching_method AS teachingMethod,
	tls.create_time AS createTime,
	tls.create_by AS createBy,
	tls.update_time AS updateTime,
	tls.update_by AS updateBy 
FROM t_lesson_step tls 
where tls.lesson_plan_id=#{data.id}
 ) a
