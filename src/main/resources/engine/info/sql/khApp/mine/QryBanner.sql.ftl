select * from (
	SELECT 
	id,
	pic_id as  picId,
	show_order as showOrder,
	banner_type as bannerType,
	alink as aLink
	from t_banner where valid_flag = '0'
) a 