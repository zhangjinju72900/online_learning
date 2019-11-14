select
	i.id as infoId,
	i.title as infoTitle,
	i.content as infoContent,
	i.file_id as fileId,
	fi.oss_url as fileOssUrl,
	i.like_count,
	i.click_count as infClickCount,
	i.collect_count,
	i.release_by,
	i.release_time,
	i.information_label_id 
from  t_subject_information tsi
 left join t_information i on  i.id =tsi.information_id
 left join t_file_index fi on i.file_id = fi.id
where subject_id = #{data.id} and check_status = 1 and valid_flag = 0 and release_status = 1
order by i.top_flag desc ,i.top_time desc,i.id desc