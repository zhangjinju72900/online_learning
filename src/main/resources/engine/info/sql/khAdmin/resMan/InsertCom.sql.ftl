insert t_community (
flag,
name,
parent_id,
create_by,
create_time
)
values(
'0',
#{data.name},
'1',
'test',
NOW()
)