select * from (
	select
	i.id as id,
	i.title as title,
	i.release_time as releaseTime,
	i.release_by as releaseBy,
	i.click_count as clickCount,
	i.like_count as likeCount,
	i.collect_count as collectCount,
	i.review_count as reviewCount,
	p.pic_id as picId,
	p.pic_id as fileId,
	p.fileOssUrl,
	u.nickname as releaseName,
	u.file_id as uFileId,
	fi2.oss_url as ufileOssUrl
	from t_information i
	left JOIN (
		select ip.information_id as inforId,
		GROUP_CONCAT(ip.pic_id) as pic_id,
		GROUP_CONCAT(fi.oss_url) as fileOssUrl
		from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id group by ip.information_id)p
	on p.inforId=i.id
	LEFT JOIN t_customer_user u on u.id=i.release_by
	left join t_file_index fi2 on u.file_id = fi2.id
	where i.source=2
	and i.valid_flag=0
	and i.check_status=1
	and i.activity_id=#{data.id}
) a
