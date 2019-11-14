select * from (
	select 
	CODE as value,
	NAME as text
	from t_dict where cata_code='t_ftp_upload_record.file_type'
 ) a 