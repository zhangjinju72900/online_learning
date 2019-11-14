update t_information i
set review_count = (SELECT count(*)
                    FROM t_information_review
                    WHERE information_id = (select information_id from t_information_review where id = #{data.id})
                      and check_status = 1 and valid_flag = 0 and review_id = 0)
where id = (select information_id from t_information_review where id = #{data.id})