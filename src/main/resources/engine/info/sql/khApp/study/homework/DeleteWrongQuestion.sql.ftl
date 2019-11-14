delete from t_wrong_question_set where homework_id= #{data.homeworkId} and homework_detail_id=#{data.homeworkDetailId}  and student_id = #{data.userId} and question_id = #{data.id};
