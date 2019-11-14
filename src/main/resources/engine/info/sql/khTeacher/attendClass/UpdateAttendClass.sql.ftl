update t_attend_class_record
set sign_qr_file_id         = #{data.fileId},
sign_qr_oss_key      = #{data.ossKey},
sign_qr_oss_url = #{data.ossUrl}
where id = #{data.id}