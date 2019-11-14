insert t_maintain_manual (
name,
parent_id,
source,
data_flag,
valid_flag
)
values(
#{data.name},
#{data.parentId},
'1',
'0',
'0'
)