insert t_community (
id,
name,
parent_id,
create_by,
create_time
)
values(
#{data.id},
#{data.name},
'1',
'test',
NOW()
)