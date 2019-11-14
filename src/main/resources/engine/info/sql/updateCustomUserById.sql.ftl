update t_customer_user
set
tel = #{data.tel}, 
name = #{data.name},
card_num = #{data.cardNum},
nickname = #{data.nickName},
email = #{data.email},
sex = #{data.eq_sex},
school_id = #{data.eq_schoolId},
file_id = #{data.fileId},
province_code = #{data.eq_provinceCode},
user_explain = #{data.userExplain}
where id = #{data.id}