select * from
(select b.*,case when c.price is null then 10 else c.price end price,case when c.freeshipping is null then 98 else c.freeshipping end freeshipping from 
(SELECT 
	id,
	user_id as userId,
	name,
	tel,
	address,
	address_detail as addressDetail,
    is_default as isDefault,
	case when SUBSTRING_INDEX(address,' ',1) is null then 8 else SUBSTRING_INDEX(address,' ',1) end province
	from t_consign_address
	where valid_flag=0 order by create_time desc) b
left JOIN
(select c.`name`,p.price,p.freeshipping from t_postage p
LEFT JOIN t_city c on c.`code`=p.province)c on c.`name`= b.province)a

