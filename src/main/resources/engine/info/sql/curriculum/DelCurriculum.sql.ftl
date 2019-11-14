UPDATE t_curriculum set valid_flag = 1, update_by = #{data.session.userInfo.empId}, update_time = now()
WHERE id = #{data.id};
DELETE FROM t_curriculum_course WHERE curriculum_id = #{data.id};
update t_class set curriculum_id = 0 WHERE curriculum_id = #{data.id};