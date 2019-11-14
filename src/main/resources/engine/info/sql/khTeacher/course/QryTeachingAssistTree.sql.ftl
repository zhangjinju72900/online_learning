
select concat(pid, id),
id,
pid,
text,
type,
typeName,
remark,
fileType,
a.fileId,
ossKey,
bucketName
from (
	SELECT
	ta.id as csrId,
	ta.id,
	'0' as pid,
  'assist'   	as type,
	ta.`name` as text,
  '我的资料'  as typeName,
  ''    as remark,
	f.file_type as fileType,
  999  as show_order,
  	case when ta.oss_key ='' or ta.oss_key is null then f.oss_key else ta.oss_key end ossKey,
	case when ta.bucket_name ='' or ta.bucket_name is null  then f.oss_url else ta.bucket_name end bucketName,
  case when ta.bucket_name ='' then f.oss_url else ta.bucket_name end ossUrl,
  ta.section_id as sectionId,
	'' as labelId,
	0 as resourcesType,
    f.path as filePath,
	f.id as fileId
	from t_teaching_assist ta 
	left join t_professional p
	on p.id = ta.professional_id
	left join t_course c
	on c.id = ta.course_id
	left join t_section s
	on s.id = ta.section_id
	left join t_file_index f
	on f.id = ta.file_id
	where ta.teacher_id = #{data.session.userInfo.userId}  and ta.valid_flag = 0 and  ta.section_id =  #{data.sectionId}
	
	UNION all 

SELECT
	0 as csrId,
	0 as id,
	'' as pid,
  'assist'   	as type,
	'我的资料' as text,
  '我的资料'  as typeName,
  ''    as remark,
	 '' as fileType,
  999  as show_order,
	 '' as ossKey,
   '' as bucketName,
   '' as ossUrl,
  '' as sectionId,
	'' as labelId,
	0 as resourcesType,
   '' as filePath,
	 '' as fileId
	from dual
	) a
ORDER BY show_order, id