	select 
	count(card_num) as count
	from 
	t_customer_user
	where 
	card_num= #{data.cardNum};
