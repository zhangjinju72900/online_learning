insert into t_customer_user(
	name,
	card_num,
	password,
	sex,
	school_id,
	create_time,
	update_time
)values(
	#{data.name},
	#{data.cardNum},
	#{data.password},
	#{data.sex},
	#{data.schoolId},
	now(),
	now()
)