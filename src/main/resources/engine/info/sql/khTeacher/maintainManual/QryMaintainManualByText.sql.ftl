select
m.id as id,
CONCAT(m.`file_name`,'.',m.file_type) as fileName,
oss_key as ossKey,
oss_url as ossUrl,
file_path as filePath,
resources_type as resourcesType
from t_maintain_manual m where m.valid_flag = 0 and m.data_flag = 1 and m.`file_name` like concat('%',#{data.text},'%') 