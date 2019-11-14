update  
t_attend_class_sign_record as acs  
INNER JOIN  
t_attend_class_record as ac 
on acs.attend_class_record_id=ac.id  
INNER JOIN  t_customer_user  as  tcu  
ON tcu.id=acs.student_id

set acs.sign_type=1 ,
	acs.valid_flag=0,
	ac.real_student_count=ac.real_student_count+1 
where  
	
	acs.id=#{data.id}  and  acs.valid_flag=1
