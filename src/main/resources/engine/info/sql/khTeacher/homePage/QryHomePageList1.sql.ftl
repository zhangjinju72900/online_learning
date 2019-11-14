select * from(
SELECT  
 tacr.id AS id,
 CONCAT(tc.grade,"级",tc.name) AS className,
 DATE_FORMAT(tacr.start_time, '%Y-%m-%d') As classDate,
 tacr.student_count AS studentCount,
 tacr.real_student_count AS realStuCount,
 (tacr.student_count - tacr.real_student_count) AS absenceStuCount
FROM t_attend_class_record tacr 
LEFT JOIN t_class tc on tacr.class_id=tc.id 
WHERE  tacr.valid_flag = 0 
ORDER BY id desc
)a