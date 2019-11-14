select * from(
	SELECT
		c.name as curriculumName,
		c.execute_time as executeTime,
		c.create_time as createTime,
		c.create_by as createBy,
		c.update_time as updateTime,
		c.update_by as updateBy
	FROM t_curriculum c where c.valid_flag = '0'
) tab