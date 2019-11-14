select * from (
SELECT
    tn.release_by as releaseBy,
    tab2.roleId,
    tab2.roleName
    
    FROM t_notice tn
    join (select tcur.customer_user_id, group_concat(tcur.role_id) as roleId, group_concat(tr.name)as roleName from t_customer_user_role tcur 
left join t_role tr on tr.id=tcur.role_id group by tcur.customer_user_id)tab2 on tn.release_by = tab2.customer_user_id
 ) a 