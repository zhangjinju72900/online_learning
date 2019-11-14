select * from (
SELECT 
	tls.name AS bzname,
	tls.teaching_count AS teachingCount,
	tls.teaching_content AS steachingContent,
	tls.teaching_assist AS teachingAssist,
	tls.teaching_method AS teachingMethod
FROM t_lesson_step tls 
where tls.lesson_plan_id=#{data.id}
 ) a
