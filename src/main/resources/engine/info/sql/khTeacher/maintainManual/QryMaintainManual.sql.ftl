select
m.id as id,
CONCAT(m.`file_name`,'.',m.file_type) as fileName,
oss_key as ossKey,
oss_url as ossUrl,
file_path as filePath,
resources_type as resourcesType
from t_maintain_manual m join (select id from (
              select t1.id,
              if(find_in_set(parent_id, @pids) > 0, @pids := concat(@pids, ',', id), 0) as ischild
              from (
                   select id,parent_id from t_maintain_manual t where t.valid_flag = 0  order by parent_id, id
                  ) t1,
                  (select @pids := #{data.id} ) t2
             ) t3 where ischild != 0)tab1 on m.id = tab1.id
where m.valid_flag=0 and m.data_flag=1 and m.`file_name` like concat('%',#{data.text},'%') 