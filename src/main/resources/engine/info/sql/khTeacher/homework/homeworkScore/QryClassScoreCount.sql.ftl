select 
 GROUP_CONCAT(a.homeworkId order by a.homeworkId desc) as homeworkId,
 GROUP_CONCAT(a.homeworkName order by a.homeworkId desc) as homeworkName,
 a.classId as classId,
 GROUP_CONCAT(a.homeworkType order by a.homeworkId desc) as homeworkType,
 GROUP_CONCAT(a.score order by a.homeworkId desc) as score,
 a.studentName,
 GROUP_CONCAT(a.distributeTime order by a.homeworkId desc) as distributeTime
 from 
(select 
 date(h.distribute_time) distributeTime,
 h.`name` homeworkName,
 h.id homeworkId,
 h.class_id as classId,
 h.homework_type homeworkType,
 case when h.homework_type=0  then a.objective_real_score else a.subjective_score_level end score,
 a.student_id,
 u.name studentName
 from t_homework_detail_answer a
LEFT JOIN t_homework h on a.homework_id = h.id
LEFT JOIN t_customer_user u on u.id=a.student_id
where DATE(h.distribute_time) >=DATE_SUB(CURDATE(),INTERVAL 1 MONTH) 
)a GROUP BY a.student_id