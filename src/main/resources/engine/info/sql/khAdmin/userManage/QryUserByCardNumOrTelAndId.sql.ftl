select id from t_customer_user where (card_num = #{data.cardNum} or tel = #{data.cardNum} or card_num = #{data.tel} or tel = #{data.tel}) and id != #{data.id} and valid_flag = 0