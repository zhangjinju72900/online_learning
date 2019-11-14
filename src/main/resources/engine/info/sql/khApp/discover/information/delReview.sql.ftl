update t_information_review set valid_flag = 1
where id = #{data.id} and review_by = #{data.session.userInfo.userId};
update t_information i
set review_count = (SELECT count(1)
                    FROM t_information_review
                    WHERE information_id = (select information_id from t_information_review where id = #{data.id})
                      and check_status = 1 and valid_flag = 0)
where id = (select information_id from t_information_review where id = #{data.id});