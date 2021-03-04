import cx_Oracle as cx


dns = cx.makedsn('192.168.10.27','1521','rproods')
connection = cx.connect('reportuser','report',dns)
cur = connection.cursor()
cur.execute("select nvl(max(invc_no)+1,'1') from cms.invoice t ,cms.default_store df where df.sbs_no=1 and df.default_doc_type=0 and df.store_no=t.store_no and length(t.tracking_no)>6 order by t.invc_no ")
result = cur.fetchone()
if result!=None:
   print(result)
else:
   print('Not found'+str(result))

#cur.execute("select Table_name from all_tables where table_name = upper('ESHOP_INVC')")
#result = cur.fetchone()
#if result!=None:
#    print('Table Found')
#else: 
#    cur.execute("create table ESHOP_INVC( billnum VARCHAR2(30),created_date DATE,invc_sid NUMBER(19),note VARCHAR2(50) )")
#    connection.commit()
#    print('Created Table')
#
#    cur.execute("select Table_name from all_tables where table_name = upper('ESHOP_INVC')")
#    result = cur.fetchone()
#    if result!=None:
#       print('Recheck: Table Found!')
#    else:
#       print('Recheck: Table Not found')    




#print('Print result')
#for i in cur:
#    print(str(i))


cur.close
connection.close