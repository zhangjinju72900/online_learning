 SELECT
    CONCAT('G',LPAD(num,6,'0')) AS code
    FROM
    t_serial_number
    WHERE
    ser_type = #{data.serType}
    order by num desc LIMIT 0,1;