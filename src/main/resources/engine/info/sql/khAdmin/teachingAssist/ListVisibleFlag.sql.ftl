select * from(
	SELECT 
	t.value as value,
	case t.value when 0 then '公开'
	else '不公开' end as text
	FROM (select 0 as value)t

	union ALL
	
	SELECT 
	t1.value as value,
	case t1.value when 0 then '公开'
	else '不公开' end as text
	FROM (select 1 as value)t1
) a