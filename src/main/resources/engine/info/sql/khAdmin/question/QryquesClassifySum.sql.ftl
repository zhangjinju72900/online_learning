SELECT *
FROM (
SELECT
 count(*) as sum
FROM t_professional
where valid_flag = 0 and name = #{data.name} 
) a