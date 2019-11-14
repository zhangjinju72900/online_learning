select * from (
	select 
	a.id,
	a.name,
	o.name as orgName 
  from 
	t_proj_emp pe left join t_employee a on pe.emp_id=a.id 
	left join t_org o on a.org_id=o.id where  (a.status='reserve' or a.status='work' or a.status='employee')
	GROUP BY a.id
 ) a 