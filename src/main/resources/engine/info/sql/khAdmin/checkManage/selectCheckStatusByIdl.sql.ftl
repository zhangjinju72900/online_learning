select * from (
select check_status from t_live_review where #{data.type}='live_review' and id=#{data.id}
union all
select check_status from t_information_review where #{data.type}='info_review' and id = #{data.id}
union all
select check_status from t_information where #{data.type}='info' and id = #{data.id} 
union all
select check_status from t_information_review where #{data.type}='info_review_review' and id = #{data.id} 
union all
select check_status from t_information where #{data.type}='ac_join_pic' and id = #{data.id} 
)a