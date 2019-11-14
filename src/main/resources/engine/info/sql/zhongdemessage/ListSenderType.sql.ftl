select *
from (select 1 as value, "互动" as text
      UNION
      select 2 as value, "评论" as text
      UNION
      select 3 as value, "通知" as text
      UNION
      select 4 as value, "活动" as text) a