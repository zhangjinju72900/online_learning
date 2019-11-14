UPDATE `t_question`
SET 
 `content` = #{data.content},
 `answer_thought` = #{data.answerThought},
 `update_time` = now(),
 `update_by` = #{data.session.userInfo.userId}
WHERE
	`id` = #{data.id}

