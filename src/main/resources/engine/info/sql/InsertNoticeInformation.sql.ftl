insert into t_notice 
(title,
context,
source,
release_time,
release_by,
update_time,
update_by,
create_time,
create_by,
release_status) 
values
(#{data.title},
#{data.context},
'0',
now(),
2,
now(),
#{data.updateBy},
now(),
2 ,
1
)