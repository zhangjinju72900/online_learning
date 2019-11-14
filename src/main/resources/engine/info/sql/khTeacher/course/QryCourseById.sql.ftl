select *
from (
select tc.id               as id,
tc.name             as name,
tc.professional_id  as professionalId,
tc.remark           as remark,
tc.course_count     as courseCount,
tc.difficulty_level as difficultyLevel,
f.filename as fileName,
f.file_type as fileType,
f.oss_url as fileOssUrl
from t_course tc
LEFT JOIN t_file_index f on f.id=tc.file_id
where tc.id = #{data.courseId}
) a