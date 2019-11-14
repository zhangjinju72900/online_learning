update `t_customer_resources` set backup_id = #{data.backupId}, `update_time` = #{data.updateTime}, update_by = #{data.updateBy} where backup_id = #{data.oldBackupId};
