select * from (
	SELECT 
	o.id as id,
	o.customer_user_id as customerUserId,
	o.focus_on_id as focusOnId,
	u.nickname as nickName,
	u.file_id as fileId,
	u.file_id as uFileId,
	fi.oss_url as fileOssUrl,
	u.user_explain as userExplain,
	0 as focusType
	from t_user_focus_on o
	LEFT JOIN t_customer_user u ON o.focus_on_id=u.id
	left join t_file_index fi on u.file_id = fi.id
	where o.customer_user_id = #{data.session.userInfo.userId}
	and o.valid_flag=0
) a 