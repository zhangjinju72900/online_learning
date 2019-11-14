SELECT
	tacr.teacher_id AS teacherId,
	DATE_FORMAT(start_time, '%Y-%m-%d') as givenDate,
	case when SUM(student_count) = sum(real_student_count) then 1 else 0 end as absenteeismFlag
FROM
	t_attend_class_record tacr
WHERE
	teacher_id = #{data.teacherId}
	and DATE_FORMAT(start_time, '%Y-%m-%d') >= #{data.startGivenDate}
	and DATE_FORMAT(start_time, '%Y-%m-%d') <= #{data.endGivenDate}
AND valid_flag = 0
GROUP BY
	DATE_FORMAT(start_time, '%Y-%m-%d')