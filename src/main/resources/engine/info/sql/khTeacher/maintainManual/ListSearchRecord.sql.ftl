SELECT
	
	SUBSTRING_INDEX(GROUP_CONCAT(r.id order by r.id desc),',',1)  AS value,
	CASE
WHEN m.`name` IS NULL THEN
	concat('全部', r.content)
ELSE
	concat(m.`name`, r.content)
END AS text,
SUBSTRING_INDEX(GROUP_CONCAT(search_time ORDER BY r.id desc ),',',1)searchTime
FROM
	t_maintain_manual_search_record r
LEFT JOIN t_maintain_manual m ON r.maintain_manual_id = m.id
WHERE
	r.teacher_id = #{data.session.userInfo.userId}
AND r.valid_flag = 0 GROUP BY text ORDER BY searchTime  desc LIMIT 0,6