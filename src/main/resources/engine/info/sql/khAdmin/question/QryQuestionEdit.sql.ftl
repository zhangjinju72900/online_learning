select * from (
     select
          id as id,
          name as name,
          remark as remark,
          create_time as createTime,
          create_by as createBy,
          update_time as updateTime,
          update_by as updateBy,
          #{data.questionClassifyId} as questionClassifyId
     from t_question_classify

 ) a