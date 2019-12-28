select * from (
select
'0' as id,
'' as pid,
'在线学习系统' as text,
'closed' as state
from dual

union ALL

select
'11' as id,
'0' as pid,
'JavaSE' as text,
'closed' as state
from dual

union ALL

select
'13' as id,
'0' as pid,
'JavaScript' as text,
'closed' as state
from dual

union all

select
'1' as id,
'11' as pid,
'java基础程序设计' as text,
'closed' as state
from dual
)c
ORDER BY concat(pid,id)