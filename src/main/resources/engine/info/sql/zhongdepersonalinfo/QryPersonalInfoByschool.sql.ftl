select * from (
SELECT
s.id,
s.`name` as schoolName

from t_school  s where s.valid_flag = 0

 ) a 