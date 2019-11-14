select * from (
	select
	c.id as id,
	c.id as classId,
	c.name as name,
	c.class_type as classType,
	c.school_id as schoolId,
	dictgrade. NAME as grade,
	c.start_class_time as startClassTime,
	c.valid_flag as validFlag,
	c.create_time as createTime,
	c.create_by as createBy,
	c.update_time as updateTime,
	c.update_by as updateBy,
	s.name as schoolName,
	d.name as classTypeName,
	cu.name as createName,
	cu1.name as updateName,
	c.curriculum_id as curriculumId,
	tc.name as curriculumName,
	r.`name` as regionName,
	(select count(1) from t_customer_user_role ur join t_customer_user_class u on ur.customer_user_id = u.customer_user_id
				join t_customer_user cu on ur.customer_user_id = cu.id where ur.role_id = 10 and u.class_id = c.id and cu.school_id = c.school_id) as studentCount,
	(select count(1) from t_customer_user_role ur join t_customer_user_class u on ur.customer_user_id = u.customer_user_id
				join t_customer_user cu on ur.customer_user_id = cu.id where ur.role_id = 11 and u.class_id = c.id and cu.school_id = c.school_id) as teacherCount
	from t_class c
	left join t_dict d on d.code = c.class_type and d.cata_code = 't_class.class_type'
	LEFT JOIN t_dict dictgrade ON c.grade = dictgrade. CODE AND dictgrade.cata_code = 't_customer_user.grade'
	left join t_customer_user cu on cu.id = c.create_by and cu.valid_flag = 0
	left join t_customer_user cu1 on cu1.id = c.update_by and cu1.valid_flag = 0
	left join t_school s on s.id = c.school_id
	left join t_curriculum tc on c.curriculum_id = tc.id
	LEFT JOIN t_region r on r.id=s.region_id
	where c.valid_flag = 0
 ) a
