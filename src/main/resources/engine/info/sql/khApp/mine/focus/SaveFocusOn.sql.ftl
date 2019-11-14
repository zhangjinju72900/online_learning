INSERT INTO `t_user_focus_on` 
(`customer_user_id`, `focus_on_time`, `focus_on_id`, `create_time`, `create_by`, `update_time`, `update_by`)
 select #{data.session.userInfo.userId}, now(), #{data.focusOnId}, now(), #{data.session.userInfo.userId}, now(), #{data.session.userInfo.userId} from dual
 WHERE not EXISTS(
      SELECT focus_on_id
      FROM t_user_focus_on
      WHERE customer_user_id = #{data.session.userInfo.userId} and focus_on_id = #{data.focusOnId} and valid_flag = 0
)
