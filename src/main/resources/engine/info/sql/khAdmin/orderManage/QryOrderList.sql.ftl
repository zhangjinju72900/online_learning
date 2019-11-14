select * from (
SELECT
t.good_name as orderName,
t_order.id,
t_order.user_id as userId,
case when tcu.nickname='' or tcu.nickname is null then tcu.`name` else tcu.nickname end userName,
t_order.`code` as code,
t_order.order_time as orderTime,
t_order.order_status as orderStatus,
d.name as orderStatusName,
t_order.integral as integral,
t_order.amount as amount,
t_order.real_integral as realIntegral,
t_order.real_amount as realAmount,
case when real_amount=0 then '积分' else '微信支付' end payMathod,
CONCAT(tp.address,tp.address_detail) as consignAddressId,
t_order.logistic_amount as logisticAmount,
t_order.logistic as logistic,
t_order.logistic_code as logisticCode,
t_order.create_time as createTime,
t_order.create_by as createBy,
tc.name as createName,
t_order.update_time as updateTime,
t_order.update_by as updateBy,
tu.name as updateName,
remark,
tp.tel,
tp.`name`

FROM
t_order 
LEFT JOIN  t_order_detail  t
on t_order.id=t.order_id
LEFT JOIN  t_customer_user tcu
on t_order.user_id=tcu.id
LEFT JOIN  t_customer_user tc
on t_order.create_by=tc.id
LEFT JOIN  t_customer_user tu
on t_order.update_by=tu.id
LEFT JOIN  t_dict  d
 on d.code = t_order.order_status and d.cata_code = 't_order.order_status'
left JOIN t_consign_address tp on tp.id=consign_address_id
)a