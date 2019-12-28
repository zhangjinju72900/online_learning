insert t_community (

name,
parent_id,
create_by,
create_time
)
values(

#{data.name},
'1',
'test',
NOW()
)