select *
from (SELECT tmr.id            as id,
             tmr.receiver_id   as receiverId,
             tmr.sender_id     as senderId,
          
             case
               when send_type = 0 || send_type = 2
                       then '互动'
               when send_type = 1 || send_type = 3
                       then '评论'
               when send_type = 9
                       then '通知'
               when send_type = 10
                       then '活动'
                 end as senderType,
             
             case
               when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
                 end as listType,
             tmr.send_time     as senderTime,
             tmr.read_flag     as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end           as readFlagName,
             u.file_id         as ufileId,
             ik.information_id as baseId,
             u.nickname,
             '已给您点赞'           as content
      FROM t_message_record tmr
             left join t_customer_user u on tmr.sender_id = u.id
             left join t_information_like ik on tmr.base_id = ik.id
      where tmr.send_type = 0
        and tmr.valid_flag = 0
        and tmr.receiver_id = #{data.session.userInfo.userId}
      union all
      SELECT tmr.id           as id,
             tmr.receiver_id  as receiverId,
             tmr.sender_id    as senderId,
             
			 case
               when send_type = 0 || send_type = 2
                       then '互动'
               when send_type = 1 || send_type = 3
                       then '评论'
               when send_type = 9
                       then '通知'
               when send_type = 10
                       then '活动'
                 end as senderType,

             case
               when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
                 end as listType,
             tmr.send_time    as senderTime,
             tmr.read_flag    as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end          as readFlagName,
             u.file_id        as ufileId,
             r.information_id as baseId,
             u.nickname,
             r.content
      FROM t_message_record tmr
             left join t_customer_user u on tmr.sender_id = u.id
             left join t_information_review r on tmr.base_id = r.id
      where tmr.send_type = 1
        and tmr.valid_flag = 0
        and tmr.receiver_id = #{data.session.userInfo.userId}
      
 union all
     
      SELECT tmr.id            as id,
             tmr.receiver_id   as receiverId,
             tmr.sender_id     as senderId,
             
              case
               when send_type = 0 || send_type = 2
                       then '互动'
               when send_type = 1 || send_type = 3
                       then '评论'
               when send_type = 9
                       then '通知'
               when send_type = 10
                       then '活动'
                 end as senderType,
             
             case
               when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
                 end as listType,
             tmr.send_time     as senderTime,
             tmr.read_flag     as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end           as readFlagName,
             u.file_id         as ufileId,
             ik.information_id as baseId,
             u.nickname,
             '已给您的评论点赞'        as content
      FROM t_message_record tmr
             left join t_customer_user u on tmr.sender_id = u.id
             left join t_information_like ik on tmr.base_id = ik.id
      where tmr.send_type = 2
        and tmr.valid_flag = 0
        and tmr.receiver_id = #{data.session.userInfo.userId}
      union all
      SELECT tmr.id           as id,
             tmr.receiver_id  as receiverId,
             tmr.sender_id    as senderId,
             
              case
               when send_type = 0 || send_type = 2
                       then '互动'
               when send_type = 1 || send_type = 3
                       then '评论'
               when send_type = 9
                       then '通知'
               when send_type = 10
                       then '活动'
                 end as senderType,
                 
             case
               when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
                 end as listType,
             tmr.send_time    as senderTime,
             tmr.read_flag    as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end          as readFlagName,
             u.file_id        as ufileId,
             r.information_id as baseId,
             u.nickname,
             r.content
      FROM t_message_record tmr
             left join t_customer_user u on tmr.sender_id = u.id
             left join t_information_review r on tmr.base_id = r.id
      where tmr.send_type = 3
        and tmr.valid_flag = 0
        and tmr.receiver_id = #{data.session.userInfo.userId}
      union all
      SELECT tmr.id          as id,
             tmr.receiver_id as receiverId,
             tmr.sender_id   as senderId,
             
			  case
               when send_type = 0 || send_type = 2
                       then '互动'
               when send_type = 1 || send_type = 3
                       then '评论'
               when send_type = 9
                       then '通知'
               when send_type = 10
                       then '活动'
                 end as senderType,

             case
               when send_type = 0 || send_type = 2
                       then 1
               when send_type = 1 || send_type = 3
                       then 2
               when send_type = 9
                       then 3
               when send_type = 10
                       then 4
                 end as listType,
             tmr.send_time   as senderTime,
             tmr.read_flag   as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end         as readFlagName,
             a.file_id       as ufileId,
             a.id   as baseId,
             a.title         as nickname,
             '活动已报名，请准时参加'   as content
      FROM t_message_record tmr
             
             left join t_activity a on tmr.base_id = a.id
      where tmr.send_type = 10
        and tmr.valid_flag = 0
        and tmr.receiver_id = #{data.session.userInfo.userId}
        
        union all
      SELECT tmr.id          as id,
             tmr.receiver_id as receiverId,
             tmr.sender_id   as senderId,
             
			  case
               when send_type = 20 
                       then '系统'
               when send_type = 30
                       then '班级'
                 end  senderType,
             send_type as listType,
             tmr.send_time   as senderTime,
             tmr.read_flag   as readFlag,
             case tmr.read_flag
               when 0 then '未读'
               else '已读'
                 end         as readFlagName,
             (select u.file_id from t_customer_user u where u.id=a.release_by)       as ufileId,
             a.id   as baseId,
             a.title         as nickname,
             a.context   as content
      FROM t_message_record tmr
             
             left join t_notice a on tmr.base_id = a.id
						 LEFT JOIN t_notice_file f on f.notice_id=a.id
      where tmr.send_type in (20,30)
        and tmr.valid_flag = 0
        and tmr.receiver_id =#{data.session.userInfo.userId}
     ) a