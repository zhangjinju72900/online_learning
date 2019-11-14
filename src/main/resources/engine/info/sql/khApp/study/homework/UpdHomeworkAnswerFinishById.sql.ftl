UPDATE t_homework_answer
SET status = '2', finish_time = now(), finish_speed = #{data.finishSpeed} where id = #{data.homeworkAnswerId};