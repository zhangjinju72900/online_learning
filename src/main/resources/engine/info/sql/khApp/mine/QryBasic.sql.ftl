SELECT * FROM(
		SELECT
			(select case when count(1)=0 then 1 else 0 end sysReadFlag from t_message_record tmr where tmr.send_type='20' and tmr.receiver_id=#{data.userId} and tmr.read_flag=0) sysReadFlag,
			cu.id,
			cu.`name`,
			cu.integral,
			cu.file_id AS fileId,
			cu.user_explain as userExplain,
			cu.nickname,
			IFNULL(tab4.c,0) AS publishCount,
			IFNULL(tab3.c,0) AS msgCount,
			IFNULL(tab2.c,0) AS collectCount,
			ts.`name` as schoolName,
			case when EXISTS (select integral from t_customer_user_integral_record cui where cui.create_by= #{data.userId} and to_days(create_time) = TO_DAYS(CURRENT_DATE())) then (select integral from t_customer_user_integral_record cui where cui.create_by=#{data.userId} and  base_order_type=0 and to_days(create_time) = TO_DAYS(CURRENT_DATE())) else 0 end todayIntegral,
			(SELECT 
				count(1) focusCount
				from t_user_focus_on o
				where o.customer_user_id = #{data.userId} and valid_flag = '0') as focusCount,
			IFNULL(sir.id,0) AS signType,
			case rec.unread_count when 0 then 0
			else 1
			end as readFlag,
			case rec.unread_count when 0 then "无"
			else "有"
			end as readFlagName
			
		FROM
			t_customer_user cu
			LEFT JOIN t_school ts on ts.id=cu.school_id
		LEFT JOIN (
			SELECT
				release_by,
				COUNT(1) AS c
			FROM
				t_information
			WHERE
				release_by = #{data.userId}
			AND check_status = 1
			AND valid_flag = 0
			AND release_status = 1
		) tab4 ON cu.id = tab4.release_by
		LEFT JOIN (
			SELECT
				receiver_id,
				COUNT(1) AS c
			FROM
				t_message_record
			WHERE
				receiver_id = #{data.userId}
		) tab3 ON cu.id = tab3.receiver_id
		LEFT JOIN (
			SELECT
				collect_by,
				COUNT(1) AS c
			FROM
				(
					SELECT
						c.collect_by
					FROM
						t_information_collect c left join t_information i on c.information_id = i.id 
					WHERE
						c.collect_by = #{data.userId} and i.check_status = 1 and valid_flag = 0 and release_status = 1
					UNION ALL
						SELECT
							gc.collect_by
						FROM
							t_goods_collect gc left join t_goods g on gc.good_id = g.id
						WHERE
							gc.collect_by = #{data.userId} and g.sale_status = 0 and valid_flag = 0
				) tab1
		) tab2 ON cu.id = tab2.collect_by
		LEFT JOIN (select 1 as id, user_id from t_sign_in_record where user_id = #{data.userId} and to_days(sign_time) = TO_DAYS(CURRENT_DATE())) sir on cu.id = sir.user_id
		LEFT JOIN(select count(*) as unread_count ,tcu.id as id from t_message_record tmr
							left join t_customer_user tcu
							on tmr.receiver_id = tcu.id
				 where tcu.id = #{data.userId} and tmr.valid_flag = 0 and tmr.read_flag = 0) rec
				 on rec.id = cu.id
		WHERE
			cu.id = #{data.userId}
	) a