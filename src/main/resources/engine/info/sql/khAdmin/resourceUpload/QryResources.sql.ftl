SELECT * FROM(
	SELECT
		ftp.id AS id,
		ftp.parent_id AS parentId,
		ftp.filename AS fileName,
		ftp.coding_format AS codingFormat,
		ftp.file_type AS fileType,
		case when ftp.execute_result='0' then '未处理'
				 when ftp.execute_result='1' then '处理中'
				 when ftp.execute_result='2' then '已完成'
				 when ftp.execute_result='3' then '已出错'
		end AS executeResult,
		case when ftp.resource_type = 0 then '用户资源模块'
			else '维修手册模块' end as resourcesTypeName,		 
		ftp.remark AS remark,
		ftp.create_time AS createTime,
		ftp.create_by	AS createBy,
		ftp.update_time AS updateTime,
		ftp.update_by AS updateBy,
		case when ftp.resource_type = 0 then cr.name
			else mm.name end AS parentName,
		d.name as codingFormatName,
		d2.name as fileTypeName
	FROM t_ftp_upload_record ftp
	LEFT JOIN t_customer_resources cr ON cr.id=ftp.parent_id
	LEFT JOIN t_maintain_manual mm ON mm.id=ftp.parent_id
	LEFT JOIN (select code, name from t_dict where cata_code = 't_ftp_upload_record.coding_format') d on ftp.coding_format = d.code
	LEFT JOIN (select code, name from t_dict where cata_code = 't_ftp_upload_record.file_type') d2 on ftp.file_type = d2.code
	where  ftp.valid_flag=0
	
	ORDER BY createTime
)a