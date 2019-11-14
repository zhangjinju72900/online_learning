SELECT
 tab1.customer_user_id as studentId,
 us.`name`,
 '缺勤' AS title
FROM
 (
 select r.student_id as customer_user_id from t_attend_class_sign_record r 
LEFT JOIN t_customer_user u on u.id=r.student_id
   JOIN t_customer_user_role ur ON r.student_id = ur.customer_user_id
   WHERE
    r.attend_class_record_id =#{data.id}
   AND ur.role_id = 10
  and  r.valid_flag = 1
  
 ) tab1
LEFT JOIN t_customer_user us ON tab1.customer_user_id = us.id



