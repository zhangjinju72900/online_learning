select *
from (SELECT ui.id              as id,
             ui.base_order_type as baseOrderType,
             case when ui.change_type = 10 then ui.remark 
				else d1.name end             as type,
             ui.integral        as integral,
             ui.change_time     as changeTime
      FROM t_customer_user u
             RIGHT JOIN t_customer_user_integral_record ui ON u.id = ui.customer_user_id
             LEFT JOIN t_dict d1 ON d1.cata_code = 'integral_record.type' and ui.base_order_type = d1.code
      WHERE u.id = #{data.id})a