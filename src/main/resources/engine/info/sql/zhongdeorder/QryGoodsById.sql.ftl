select 
    g.id as goodsId,
    g.`name` as goodSName,
    g.quantity as goodsCount,
    g.code as goodsCode,
    g.sale_status as saleStatus,
    p.amount,
    p.integral

 from t_goods g LEFT JOIN t_goods_pay_detail p on p.good_id=g.id
 where g.id=#{data.goodsId} and p.id=#{data.payDetailId} and g.quantity>0