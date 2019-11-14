select *
from (select code as value, name as text FROM t_dict WHERE cata_code = 't_customer_user.grade') a