select *
from (
select '' as value,
'' as text
union
select 0 as value,
0 as text
union
select 1 as value,
1 as text
union
select 2 as value,
2 as text
union
select 3 as value,
3 as text
union
select 4 as value,
4 as text
) a