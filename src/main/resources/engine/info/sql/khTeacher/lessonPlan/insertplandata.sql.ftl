insert into t_lesson_plan
(id,name,school_id,class_id,course_id,section_id,course_count,student_count,
guide_teacher_id,teaching_object,teaching_site,equipment,customer_task,teaching_goal,teaching_content,
homework_content,teacher_id,valid_flag,create_time, create_by, update_time, update_by)

values(#{data.id},#{data.name},'0',#{data.classId},#{data.courseId},#{data.sectionId},#{data.courseCount},
#{data.studentCount},#{data.guideTeacherId},#{data.teachingObject},#{data.teachingSite},#{data.equipment},#{data.customerTask},
#{data.teachingGoal},#{data.teachingContent},#{data.homeworkContent},#{data.session.userInfo.userId},'0',
now(), #{data.session.userInfo.userId}, now(), #{data.session.userInfo.userId})