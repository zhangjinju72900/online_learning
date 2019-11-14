select * from (
	select code as value, name as text from t_dict where cata_code = 't_ftp_upload_record.coding_format'
 ) a 