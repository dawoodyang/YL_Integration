--Item and invn info
select 
--   0        1         2        3       4           5           6         7                8             9              10           11
t.item_sid,p.price,t.local_upc,t.alu,t.style_sid,t.dcs_code,t.vend_code,t.description1,t.description2,t.description3,t.description4,t.siz
--  11       12     13     14       15       16      17     18
,t.text1,t.text2,t.text3,t.text4,t.text5,t.text6,t.text7,t.text8

from
cms.invn_sbs t
,cms.invn_sbs_price p
 where t.alu=2900000000049 and t.item_sid=p.item_sid(+) and p.price_lvl(+)=1
