select u.id,u.name as teacherName,c.`name` as courseName,concat(ar.start_time,'') as startTime from t_attend_class_record ar
LEFT JOIN t_customer_user u on u.id=ar.teacher_id
LEFT JOIN t_course c on c.id =ar.course_id where ar.id=#{data.attendClassRecordId}
;