select * from (
	select
	concat('info','-',i.id) as id,
	(select title from t_activity where id = i.activity_id) as title,
	i.file_id as informationId,
	i.release_by as reviewBy,
	c.name as reviewName,
	i.release_time as reviewTime,
	SUBSTRING_INDEX(i.content,'<',1) as content,
	i.content as searchContent,
	i.check_status as checkStatus,
	i.top_flag as tFlag,
	i.top_time as topTime,
	i.recommend_flag as reFlag,
	i.like_count as likeCount,
	(select name from t_school where id = c.school_id) as school,
	d.NAME as status,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlag,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'能' as ableRecommend,
	i.valid_flag as validFlag,
	'用户动态' as checkType,
	'info' as type,
	 CASE  WHEN r.readPeople is NULL THEN 0 ELSE r.readPeople END as readPeople
	from t_information i
	left join t_dict d on d.code = i.check_status
	left join t_customer_user c on c.id = i.release_by
	 LEFT JOIN (
     select 
     information_id, 
     count(DISTINCT (click_by)) as readPeople
     from t_information_click  
     GROUP BY information_id 
     )r
	 on r.information_id=i.id
	where i.valid_flag = 0 and i.source = 0 and d.cata_code='t_information.check_status' and i.content like CONCAT('%', "", '%')
	and title like CONCAT('%', "", '%')

	union all
	
	select 
	concat('info_review','-',ir.id) as id,
	(select title from t_activity where id = i.activity_id) as title,
	ir.information_id as informationId,
	ir.review_by as reviewBy,
	c.name as reviewName,
	ir.review_time as reviewTime,
	ir.content as content,
	ir.content as searchContent,
	ir.check_status as checkStatus,
	ir.top_flag as tFlag,
	ir.top_time as topTime,
	ir.like_count as likeCount,
	0 as reFlag,
	(select name from t_school where id = c.school_id) as school,
	d.NAME as status,
	case 
	when   ir.top_flag='0'   then '否'
    when   ir.top_flag='1'   then '是' 
	end as topFlag,
	'否' as recommendFlag,
	'不能' as ableRecommend,
	ir.valid_flag as validFlag,
	'资讯评论' as checkType,
	'info_review' as type,
	0 as readPeople
	from t_information_review ir
	left join t_information i on ir.information_id = i.id
	left join t_dict d on d.code = ir.check_status
	left join t_customer_user c on c.id = ir.review_by
	where ir.valid_flag = 0 and ir.review_id = 0 and d.cata_code='t_information.check_status' and ir.content like CONCAT('%', "", '%')
	and title like CONCAT('%', "", '%')

	union all
	
	select 
	concat('info_review_review','-',ir.id) as id,
	(select title from t_activity where id = i.activity_id) as title,
	ir.information_id as informationId,
	ir.review_by as reviewBy,
	c.name as reviewName,
	ir.review_time as reviewTime,
	ir.content as content,
	ir.content as searchContent,
	ir.check_status as checkStatus,
	ir.top_flag as tFlag,
	ir.top_time as topTime,
	ir.like_count as likeCount,
	0 as reFlag,
	(select name from t_school where id = c.school_id) as school,
	d.NAME as status,
	case 
	when   ir.top_flag='0'   then '否'
    when   ir.top_flag='1'   then '是' 
	end as topFlag,
	'否' as recommendFlag,
	'不能' as ableRecommend,
	ir.valid_flag as validFlag,
	'资讯评论-评论' as checkType,
	'info_review_review' as type,
	0 as readPeople
	from t_information_review ir
	left join t_information i on ir.information_id = i.id
	left join t_dict d on d.code = ir.check_status
	left join t_customer_user c on c.id = ir.review_by
	where ir.valid_flag = 0 and ir.review_id != 0 and d.cata_code='t_information.check_status' and ir.content like CONCAT('%', "", '%')
	and title like CONCAT('%', "", '%')
	union all
	
	select
	concat('ac_join_pic','-',i.id) as id,
	(select title from t_activity where id = i.activity_id) as title,
	i.file_id as informationId,
	i.release_by as reviewBy,
	c.name as reviewName,
	i.release_time as reviewTime,
	SUBSTRING_INDEX(i.content,'<',1) as content,
	i.content as searchContent,
	i.check_status as checkStatus,
	i.top_flag as tFlag,
	i.top_time as topTime,
	i.recommend_flag as reFlag,
	i.like_count as likeCount,
	(select name from t_school where id = c.school_id) as school,
	d.NAME as status,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlag,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'能' as ableRecommend,
	i.valid_flag as validFlag,
	'晒图评论' as checkType,
	'ac_join_pic' as type,
	0 as readPeople
	from t_information i
	left join t_dict d on d.code = i.check_status
	left join t_customer_user c on c.id = i.release_by
	where i.valid_flag = 0 and i.source = 2 and d.cata_code='t_information.check_status' and i.content like CONCAT('%', "", '%')
	and title like CONCAT('%', "", '%')
	union all
	
	select
	concat('live_review','-',l.id) as id,
	ac.title as title,
	l.live_id as informationId,
	l.review_by as reviewBy,
	c.name as reviewName,
	l.review_time as reviewTime,
	l.content as content,
	l.content as searchContent,
	l.check_status as checkStatus,
	l.top_flag as tFlag,
	l.top_time as topTime,
	ac.like_count as likeCount,
	0 as reFlag,
	(select name from t_school where id = c.school_id) as school,
	d.NAME as status,
	case 
	when   l.top_flag='0'   then '否'
    when   l.top_flag='1'   then '是' 
	end as topFlag,
	'否' as recommendFlag,
	'不能' as ableRecommend,
	l.valid_flag as validFlag,
	'直播评论' as checkType,
	'live_review' as type,
	0 as readPeople
	from t_live_review l
	left join t_dict d on d.code = l.check_status
	left join t_customer_user c on c.id = l.review_by
	left join t_activity ac on l.live_id = ac.id
	where l.valid_flag = 0 and d.cata_code='t_information.check_status' and l.content like CONCAT('%', "", '%')
	and title like CONCAT('%', "", '%')
 ) a 
