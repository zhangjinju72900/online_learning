select r.id, DATE_FORMAT(start_time, '%H:%i') as attendClassDate,c.id as classId,
c.`name` as courseName, s.`name` as sectionName, CONCAT(u.`name`,'老师') as teacherName,
CONCAT(cl.grade,'级',cl.`name`) as className,
CONCAT(r.real_student_count,'/',r.student_count) as signStatus,
(select case when count(1)=1 then 0 else 1 end status  from t_attend_class_sign_record s where attend_class_record_id = r.id
and student_id =  #{data.userId} and s.valid_flag='0') as signStatus2 
 from t_attend_class_record r 
LEFT JOIN t_course c on r.course_id = c.id left JOIN t_section s on r.section_id = s.id
LEFT JOIN t_customer_user u on r.teacher_id = u.id
LEFT JOIN t_class cl on r.class_id = cl.id
where r.id =#{data.id}