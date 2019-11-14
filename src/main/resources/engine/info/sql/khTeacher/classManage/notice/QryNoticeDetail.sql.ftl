select * from (
SELECT tn.id       as id,
tn.title    as title,
tn.context  as context,
tab1.fileId as fileId
FROM t_notice tn
left join (select group_concat(file_id) as fileId,notice_id from t_notice_file group by notice_id) tab1
on tab1.notice_id = tn.id
where tn.valid_flag = '0'
) a