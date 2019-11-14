INSERT INTO t_section_label(section_id,label_id,create_time,create_by,update_time,update_by)
select #{data.id},label_id,NOW(),#{data.createBy},NOW(),#{data.createBy}
FROM t_section_label
where section_id in(
	select id from t_section where course_id=#{data.courseId}
)
group by label_id