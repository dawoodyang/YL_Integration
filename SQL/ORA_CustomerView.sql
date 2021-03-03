--Customer

--          0          1         2        3             4           5     6  
select t.cust_sid,t.cust_id,t.store_no,t.first_name,t.last_name,t.info1,t.info2,
--                 7
to_char(t.modified_date,'YYYY-mm-DD"T"HH24:MI:SSTZH:TZM') modifys
--        8
,t.modified_date
,(select nvl(max(phone1),'') phone1 from cms.cust_address a where t.cust_sid=a.cust_sid) from cms.customer t where  t.info1='1010073508' 

