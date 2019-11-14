select p.id as id from t_professional p
where p.parent_id=#{data.id} and valid_flag = '0'
union all 
select c.id  as id from  t_course c
where c.professional_id=#{data.id} and valid_flag = '0'