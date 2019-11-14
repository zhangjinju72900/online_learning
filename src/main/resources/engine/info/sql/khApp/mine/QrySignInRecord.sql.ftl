select * from(
select id,user_id as userId from t_sign_in_record 
where TO_DAYS( NOW( ) ) - TO_DAYS( sign_time) = #{data.day} and user_id=#{data.userId}
) a