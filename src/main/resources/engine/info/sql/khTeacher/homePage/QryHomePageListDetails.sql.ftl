select * from(
SELECT  
	tacr.id AS id,
	tacr.class_id AS classId,
	CONCAT(tc.grade,"级",tc.name) AS className,
	DATE_FORMAT(start_time, '%Y-%m-%d') as startDate,
	DATE_FORMAT(start_time, '%H:%i') AS startTime,
	DATE(tacr.start_time) AS givenDate,
	tacr.student_count AS studentCount,
	tacr.real_student_count AS realStuCount,
	(tacr.student_count - tacr.real_student_count) AS absenceStuCount,
	tacr.teacher_id AS teacherId
FROM t_attend_class_record tacr 
LEFT JOIN t_class tc on tacr.class_id=tc.id 
WHERE tacr.teacher_id = #{data.session.userInfo.userId} and DATE(tacr.start_time)=#{data.givenDate} and tacr.valid_flag = 0 ORDER BY id desc
)a