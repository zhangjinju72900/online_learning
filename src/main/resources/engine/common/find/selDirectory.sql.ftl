	SELECT * from (select 
	DISTINCT
		tcr.id,
		tcr.name parentName
    	
	from 
		t_customer_resources  tcr 	
where tcr.backup_type = '1' and tcr.file_type = '' and tcr.valid_flag = 0)a 