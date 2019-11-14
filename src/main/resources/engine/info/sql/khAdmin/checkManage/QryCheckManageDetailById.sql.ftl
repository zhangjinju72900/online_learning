select * from (
	select
	concat('info','-',i.id) as id,
	i.title as title,
	c.name as reviewName,
	i.release_time as reviewTime,
	(select name from t_school where id = c.school_id) as school,
	i.content as content,
	d.NAME as status,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlagName,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'用户动态' as checkType
	from t_information i
	left join t_dict d on d.code = i.check_status
	left join t_customer_user c on c.id = i.release_by
	where i.valid_flag = 0 and i.source = 0 and d.cata_code='t_information.check_status' 
	
	union all
	
	select 
	concat('info_review','-',ir.id) as id,
	i.title as title,
	c.name as reviewName,
	ir.review_time as reviewTime,
	(select name from t_school where id = c.school_id) as school,
	ir.content as content,
	d.NAME as status,
	case 
	when   ir.top_flag='0'   then '否'
    when   ir.top_flag='1'   then '是' 
	end as topFlagName,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'资讯评论' as checkType
	from t_information_review ir
	left join t_information i on ir.information_id = i.id
	left join t_dict d on d.code = ir.check_status
	left join t_customer_user c on c.id = ir.review_by
	where ir.valid_flag = 0 and ir.review_id = 0 and d.cata_code='t_information.check_status' 
	
	union all
	
	select 
	i.title as title,
	concat('info_review_review','-',ir.id) as id,
	c.name as reviewName,
	ir.review_time as reviewTime,
	(select name from t_school where id = c.school_id) as school,
	ir.content as content,
	d.NAME as status,
	case 
	when   ir.top_flag='0'   then '否'
    when   ir.top_flag='1'   then '是' 
	end as topFlagName,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'资讯评论-评论' as checkType
	from t_information_review ir
	left join t_information i on ir.information_id = i.id
	left join t_dict d on d.code = ir.check_status
	left join t_customer_user c on c.id = ir.review_by
	where ir.valid_flag = 0 and ir.review_id != 0 and d.cata_code='t_information.check_status' 
	
	union all
	
	select
	concat('ac_join_pic','-',i.id) as id,
	i.title as title,
	c.name as reviewName,
	i.release_time as reviewTime,
	(select name from t_school where id = c.school_id) as school,
	i.content as content,
	d.NAME as status,
	case 
	when   i.top_flag='0'   then '否'
    when   i.top_flag='1'   then '是' 
	end as topFlagName,
	case 
	when   i.recommend_flag='0'   then '否'
    when   i.recommend_flag='1'   then '是' 
	end as recommendFlag,
	'晒图评论' as checkType
	from t_information i
	left join t_dict d on d.code = i.check_status
	left join t_customer_user c on c.id = i.release_by
	where i.valid_flag = 0 and i.source = 2 and d.cata_code='t_information.check_status' 

	union all
	
	select
	concat('live_review','-',l.id) as id,
	ac.title as title,
	c.name as reviewName,
	l.review_time as reviewTime,
	(select name from t_school where id = c.school_id) as school,
	l.content as content,
	d.NAME as status,
	case 
	when   l.top_flag='0'   then '否'
    when   l.top_flag='1'   then '是' 
	end as topFlagName,
	'无' as recommendFlag,
	'直播评论' as checkType
	from t_live_review l
	left join t_dict d on d.code = l.check_status
	left join t_customer_user c on c.id = l.review_by
	left join t_activity ac on l.live_id = ac.id
	where l.valid_flag = 0 and d.cata_code='t_information.check_status' 
 ) a where id=#{data.id}
