select *
from (
select tc.id               as id,
tc.name             as name,
tc.professional_id  as professionalId,
tc.course_count     as courseCount,
tc.difficulty_level as difficultyLevel,
fi.oss_url as fileOssUrl
from t_course tc
left join t_file_index fi on tc.file_id = fi.id
where tc.professional_id = #{data.professionalId}
and tc.visible_flag = 0 and tc.valid_flag = 0 order by tc.show_order
) a