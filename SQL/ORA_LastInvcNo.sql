select nvl(max(invc_no)+1,'1') from cms.invoice t ,cms.default_store df where df.sbs_no=1 and df.default_doc_type=0 and df.store_no=t.store_no and length(t.tracking_no)>6 order by t.invc_no 
