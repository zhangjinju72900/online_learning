select 
r.id,
r.oss_key ossKey,
r.file_type fileType,
r.bucket_name bucketName,
r.file_id fileId,
r.`name` as fileName,
r.parent_id
from t_customer_resources r 

where (r.file_type='mp4' or r.file_type='flv') and r.oss_key like '%.%'