update `t_customer_resources` set backup_id = #{data.backupId}, backup_type = #{data.backupType}, `update_time` = #{data.updateTime}, update_by = #{data.updateBy} where id = #{data.id};
