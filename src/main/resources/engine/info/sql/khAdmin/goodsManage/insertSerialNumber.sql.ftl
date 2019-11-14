insert into t_serial_number(ser_type, ser_date, num,create_time,create_by,update_time,update_by )
            select
            #{data.serType},
            now(),
            IFNULL((select num FROM t_serial_number WHERE ser_type = #{data.serType} order by num desc LIMIT 0,1),0)+1 as num,
            now(),
            #{data.session.userInfo.userId},
            now(),
            #{data.session.userInfo.userId};