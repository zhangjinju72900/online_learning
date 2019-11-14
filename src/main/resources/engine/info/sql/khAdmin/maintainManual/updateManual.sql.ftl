update t_maintain_manual set 
name=#{data.name},
file_type=#{data.type},
file_path=#{data.path},
file_id=#{data.fileId},
file_name=#{data.fileName},
oss_key=#{data.ossKey},
oss_url=#{data.ossUrl},
update_time=now(),update_by=#{data.session.userInfo.userId}
where id=#{data.id};