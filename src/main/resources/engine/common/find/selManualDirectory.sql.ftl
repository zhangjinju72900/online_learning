select * from (
	select id, `name` from t_maintain_manual where data_flag = '0' and valid_flag = 0 and parent_id = 0
	union ALL
	select m.id, CONCAT(m1.`name`,"-", m.`name`) as `name` from t_maintain_manual m LEFT JOIN t_maintain_manual m1 on m.parent_id = m1.id 
	where m.data_flag = '0' and m.valid_flag = 0 and m.parent_id != 0
 ) a 