
select (r.id-50) as id,r.`name` as text,'0' as pid,'closed' as state from t_region r where r.valid_flag=0
UNION all
select s.id,s.`name` as text,(s.region_id-50) as pid,'closed' as state from t_school s where s.valid_flag = 0
UNION all
select 0 as id,'在线学习总校区' as text,'' as pid,'closed' as state from dual;