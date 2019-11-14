select 
c.id, 
c.name as curriculumName,
c.create_time as createTime,	
c.execute_time as executeTime,
c.school_id as schoolId,
tab1.oneCourseId as courseId1,
tab2.twoCourseId as courseId2,
tab3.threeCourseId as courseId3,
tab4.fourCourseId as courseId4,
tab5.fiveCourseId as courseId5,
tab1.oneCourseName as courseName1,
tab2.twoCourseName as courseName2,
tab3.threeCourseName as courseName3,
tab4.fourCourseName as courseName4,
tab5.fiveCourseName as courseName5,
s.name as schoolName,
p.id as proId,
c.create_time as createTime,
c.update_time as updateTime,
c.create_by as createBy,
c.update_by as updateBy,
p.name as professionalName
from t_curriculum c 
LEFT JOIN t_professional p
ON c.professional_id = p.id
LEFT JOIN t_school s
ON c.school_id = s.id
LEFT JOIN (select cc1.curriculum_id, GROUP_CONCAT(course_id) as oneCourseId, GROUP_CONCAT(c1.`name`) as oneCourseName
 from t_curriculum_course cc1 LEFT JOIN t_course c1 on cc1.course_id = c1.id where curriculum_id = #{data.id} and cc1.grade_type = 1 GROUP BY cc1.curriculum_id)tab1 on c.id =  tab1.curriculum_id
LEFT JOIN (select cc1.curriculum_id, GROUP_CONCAT(course_id) as twoCourseId, GROUP_CONCAT(c1.`name`) as twoCourseName
 from t_curriculum_course cc1 LEFT JOIN t_course c1 on cc1.course_id = c1.id where curriculum_id = #{data.id} and cc1.grade_type = 2 GROUP BY cc1.curriculum_id)tab2 on c.id =  tab2.curriculum_id  
LEFT JOIN (select cc1.curriculum_id, GROUP_CONCAT(course_id) as threeCourseId, GROUP_CONCAT(c1.`name`) as threeCourseName
 from t_curriculum_course cc1 LEFT JOIN t_course c1 on cc1.course_id = c1.id where curriculum_id = #{data.id} and cc1.grade_type = 3 GROUP BY cc1.curriculum_id)tab3 on c.id =  tab3.curriculum_id 
LEFT JOIN (select cc1.curriculum_id, GROUP_CONCAT(course_id) as fourCourseId, GROUP_CONCAT(c1.`name`) as fourCourseName
 from t_curriculum_course cc1 LEFT JOIN t_course c1 on cc1.course_id = c1.id where curriculum_id = #{data.id} and cc1.grade_type = 4 GROUP BY cc1.curriculum_id)tab4 on c.id =  tab4.curriculum_id 
LEFT JOIN (select cc1.curriculum_id, GROUP_CONCAT(course_id) as fiveCourseId, GROUP_CONCAT(c1.`name`) as fiveCourseName
 from t_curriculum_course cc1 LEFT JOIN t_course c1 on cc1.course_id = c1.id where curriculum_id = #{data.id} and cc1.grade_type = 5 GROUP BY cc1.curriculum_id)tab5 on c.id =  tab5.curriculum_id  
where c.id = #{data.id}