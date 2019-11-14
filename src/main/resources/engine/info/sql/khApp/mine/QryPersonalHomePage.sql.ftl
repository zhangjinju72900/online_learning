SELECT
	u.id,
	u.`name`,
	u.nickname as nickName,
	u.sex,
	u.file_id as fileId,
	user_explain as userExplain,
	(
		SELECT
			count(1)
		FROM
			t_user_focus_on
		WHERE
			focus_on_id = #{data.viewUserId} and valid_flag = 0
	) AS fansCount,
	(
		SELECT
			count(1)
		FROM
			t_user_focus_on
		WHERE
			customer_user_id = #{data.viewUserId} and valid_flag = 0
	) AS focusOnCount,
	CASE when (
		SELECT
			count(1)
		FROM
			t_user_focus_on
		WHERE
			customer_user_id = #{data.userId} and focus_on_id = #{data.viewUserId} and valid_flag = 0
	) = 0 then 0 else 1 end  AS focusType,
	(select IFNULL(SUM(like_count),0)  from t_information where release_by = #{data.viewUserId})as likeCount
FROM
	t_customer_user u where u.id = #{data.viewUserId}
