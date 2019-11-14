select * from (
SELECT DISTINCT
	tic.information_id AS id,
	tab1.title AS title,
	tab1.collect_count AS collectCount,
	tic.collect_by AS collectBy,
	tab1.valid_flag AS validFlag,
	tab1.source AS source,
	'info' AS type,
	tab1.file_id as fileId,
	fi.oss_url as fileOssUrl,
	tab1.release_time AS releaseTime,
	tab1.releaseBy,
	u.nickname,
	u.file_id as uFileId,
	fi2.oss_url as ufileOssUrl,
	case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
	case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = tab1.information_id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
		    case
		        when (select ifnull (count(id),0) from t_information_like where information_id = tab1.information_id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
	tab1.likeCount,
	tab1.clickCount,
	tab1.count,
	tic.collect_time as collectTime		   		
FROM
	t_information_collect tic
LEFT JOIN (
	SELECT
		ti.id as information_id,
		ti.title,
		ti.collect_count,
		ti.valid_flag,
		ti.source,
		ti.file_id,
		ti.release_time,
		ti.check_status as checkStatus,
		ti.release_by as releaseBy,
		ti.like_count as likeCount,
		ti.click_count as clickCount, 
		ti.review_count as count
	FROM
	t_information ti
) tab1 ON tab1.information_id = tic.information_id
left join t_file_index fi on tab1.file_id = fi.id
left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on tab1.releaseBy = ufo.focus_on_id
left join t_customer_user u on tab1.releaseBy = u.id
left join t_file_index fi2 on u.file_id = fi2.id
WHERE
	tic.collect_by = #{data.session.userInfo.userId} and tab1.valid_flag = 0 and tab1.source = 1 and tab1.checkStatus = 1
	
union all

SELECT DISTINCT
	tic.information_id AS id,
	tab1.title AS title,
	tab1.collect_count AS collectCount,
	tic.collect_by AS collectBy,
	tab1.valid_flag AS validFlag,
	tab1.source AS source,
	'info' AS type,
	tab2.fileId as fileId,
	tab2.fileOssUrl,
	tab1.release_time AS releaseTime,
	tab1.releaseBy,
	u.nickname,
	u.file_id as uFileId,
	fi2.oss_url as ufileOssUrl,
	case when ufo.focus_on_id is null
		   		then '0'
		   	else '1' 
		   		end as focusType,
	case
		        when (select ifnull (count(id),0) from t_information_collect where information_id = tab1.information_id and collect_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as collectType,
		    case
		        when (select ifnull (count(id),0) from t_information_like where information_id = tab1.information_id and like_by = #{data.session.userInfo.userId}) > 0 then '1'
		    else '0'
		     	end as likeType,
	tab1.likeCount,
	tab1.clickCount,
	tab1.count,
	tic.collect_time as collectTime		   		
FROM
	t_information_collect tic
LEFT JOIN (
	SELECT
		ti.id as information_id,
		ti.title,
		ti.collect_count,
		ti.valid_flag,
		ti.source,
		ti.file_id,
		ti.release_time,
		ti.check_status as checkStatus,
		ti.release_by as releaseBy,
		ti.like_count as likeCount,
		ti.click_count as clickCount, 
		ti.review_count as count
	FROM
	t_information ti
) tab1 ON tab1.information_id = tic.information_id
left join (select GROUP_CONCAT(ip.pic_id) as fileId, ip.information_id, GROUP_CONCAT(fi.oss_url) as fileOssUrl from t_information_pic ip left join t_file_index fi on ip.pic_id = fi.id group by ip.information_id) tab2 on tab2.information_id = tic.information_id
left join (select focus_on_id from t_user_focus_on where customer_user_id = #{data.session.userInfo.userId} and valid_flag = '0' group by focus_on_id) ufo on tab1.releaseBy = ufo.focus_on_id
left join t_customer_user u on tab1.releaseBy = u.id
left join t_file_index fi2 on u.file_id = fi2.id
WHERE
	tic.collect_by = #{data.session.userInfo.userId} and tab1.valid_flag = 0 and tab1.source != 1 and tab1.checkStatus = 1
	
union all

SELECT DISTINCT
	tgc.good_id AS id,
	tab1. NAME AS title,
	tab1.collect_count AS collectCount,
	tgc.collect_by AS collectBy,
	tab1.valid_flag AS validFlag,
	'1' AS source,
	'good' AS type,
	tab1.pic_id as fileId,
	tab1.fileOssUrl,
	'' AS releaseTime,
	0 as releaseBy,
	'' as nickname,
	0 as uFileId,
	'' as ufileOssUrl,
	'0' as focusType,
	'0' as collectType, 
	'0' as likeType,
	0 as likeCount,
	0 as clickCount,
	0 as count,
	tgc.collect_time as collectTime
FROM
	t_goods_collect tgc
LEFT JOIN (
	SELECT
		tgp.good_id,
		tg.`name`,
		tg.collect_count,
		GROUP_CONCAT(tgp.pic_id) as pic_id,
		tg.valid_flag,
		tg.sale_status,
		GROUP_CONCAT(fi.oss_url) as fileOssUrl
	FROM
		t_goods_pic tgp
	LEFT JOIN t_goods tg ON tgp.good_id = tg.id
	left join t_file_index fi on tgp.pic_id = fi.id group by tgp.good_id
) tab1 ON tgc.good_id = tab1.good_id
WHERE
	tgc.collect_by = #{data.session.userInfo.userId} and tab1.sale_status = 0 and tab1.valid_flag = 0
 ) a 
