select * from (
	select tu.id from t_class tu
		LEFT JOIN t_dict dictgrade ON tu.grade = dictgrade.`name`
	where tu.school_id = #{data.schoolId} and tu.name = #{data.name} and dictgrade.code = #{data.gradeName}
	AND dictgrade.cata_code = 't_customer_user.grade'
)a
