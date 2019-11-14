select cr.id, cr.name, cr.parent_id as parentId, cr.create_time as createTime, cr.file_path as filePath,
cr.create_by as createBy, cr.update_time as updateTime, cr.update_by as updateBy,
csr.course_id as courseId, csr.section_id as sectionId, csr.label_id as labelId, cr.resources_type as resourcesType
 from t_course_section_resources csr left join t_customer_resources cr
on csr.customer_resources_id = cr.id
 where csr.id = #{data.id} and cr.valid_flag = 0;