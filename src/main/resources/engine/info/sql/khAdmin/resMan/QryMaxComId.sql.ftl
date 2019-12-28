select
CONCAT('1',
LPAD(count(1),1,0)) id
from t_community c
