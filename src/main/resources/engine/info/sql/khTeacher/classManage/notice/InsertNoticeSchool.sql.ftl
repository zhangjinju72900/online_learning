INSERT INTO `t_notice_school` (
`notice_id`,
`school_id`,
`class_id`,
`create_time`,
`create_by`,
`update_time`,
`update_by`
)
VALUES
(
#{data.noticeId},
#{data.schoolId},
#{data.classId},
now(),
#{data.session.userInfo.userId},
now(),
#{data.session.userInfo.userId}
);