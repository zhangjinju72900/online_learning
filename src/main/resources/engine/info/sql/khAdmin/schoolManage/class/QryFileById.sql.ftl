select 
	f.id,
	f.path,
	f.uuid,
	f.file_type as fileType 
from t_file_index f where id=#{data.fileId};