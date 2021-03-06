import xml.etree.ElementTree as ET
import logging

#------------------[    Log Part     ]-------------------------------------------------[+]
LOG_FORMAT ="%(asctime)s - %(levelname)s - %(message)s"
logging.basicConfig(filename = 'XmlTest.log',level = logging.INFO,format = LOG_FORMAT)
#------------------[    Log Part     ]-------------------------------------------------[-]

#[XML Format]--------------------------------------------------------------------------[+]
def prettify(element, indent='  '):
    queue = [(0, element)]  # (level, element)
    while queue:
        level, element = queue.pop(0)
        children = [(level + 1, child) for child in list(element)]
        if children:
            element.text = '\n' + indent * (level+1)  # for child open
        if queue:
            element.tail = '\n' + indent * queue[0][0]  # for sibling open
        else:
            element.tail = '\n' + indent * (level-1)  # for parent close
        queue[0:0] = children  # prepend so children come before siblings
#[XML Format]--------------------------------------------------------------------------[-]


logging.info("Create XML Document Start")

xml_doc = ET.Element('DOCUMENT')
logging.info("Create Document Tree")
invoices = ET.SubElement(xml_doc,'INVOICES')
logging.info("Create INVCOICES Tree ")
invoice  = ET.SubElement(invoices,'INVOICE',INVC_SID = '160000000111112145',
                         sbs_no="1",store_no="103",invc_no="793" ,invc_type="0" ,hisec_type="0" ,status="2" ,proc_status="0" ,cust_sid="2009358" ,addr_no="1" ,shipto_cust_sid="2009358",
                         shipto_addr_no="1" ,station="" ,workstation="1" ,orig_store_no="103" ,orig_station="" ,use_vat="1" ,vat_options="0" ,so_no="" ,so_sid="" ,cust_po_no="" ,note="",
                         disc_perc="" ,disc_amt="" ,disc_perc_spread="" ,over_tax_perc="" ,over_tax_perc2="" ,tax_reb_perc="" ,tax_reb_amt="" ,rounding_offset="" ,
                         created_date="2020-09-14T20:43:18" ,modified_date="2020-09-14T16:02:42+08:00" ,post_date="2020-09-14T20:50:12" ,tracking_no="160007056252384587" ,ref_invc_sid="" ,
                         audited="1" ,cms_post_date="2021-02-20T15:55:06" ,ws_seq_no="" ,cust_fld="" ,held="0" ,drawer_no="103" ,controller="8888" ,orig_controller="8888" ,elapsed_time="",
                         till_name="" ,activity_perc="" ,activity_perc2="" ,activity_perc3="" ,activity_perc4="" ,activity_perc5="" ,eft_invc_no="" ,detax="0" ,doc_ref_no="" ,fiscal_doc_id="" ,
                         subloc_code="" ,subloc_id="" ,ship_perc="" ,trans_disc_amt="" ,empl_sbs_no="1" ,empl_name="10005751" ,tax_area_name="CH_VAT" ,tax_area2_name="" ,ref_invc_no="" ,
                         ref_invc_created_date="" ,createdby_sbs_no="1", createdby_empl_name="10005751" ,modifiedby_sbs_no="1" ,modifiedby_empl_name="10005751" ,clerk_sbs_no="1" ,clerk_name="10005751" ,
                         clerk_sbs_no2="" ,clerk_name2="" ,clerk_sbs_no3="" ,clerk_name3="", clerk_sbs_no4="", clerk_name4="", clerk_sbs_no5="" ,clerk_name5="", disbur_reason_type="" ,disbur_reason_name="",
                         doc_reason_code="")
                      
customer =  ET.SubElement(invoice,'CUSTOMER',cust_sid="2009358",cust_id="2009358" ,store_no="71" ,station="" ,first_name="" ,last_name="" ,price_lvl="", detax="0" ,info1="F000494091" ,
                          info2="1505976" ,modified_date="1985-01-01T00:00:00+08:00" ,sbs_no="1" ,cms="0" ,company_name="" ,title="" ,tax_area_name="" ,shipping="0", address1="" ,
                          address2="" ,address3="" ,address4="" ,address5="" ,address6="43" ,zip="", phone1="18688957337" ,phone2="", country_name="" ,alternate_id1="" ,alternate_id2="" )                      

shiptocustomer = ET.SubElement(invoice,'SHIPTO_CUSTOMER',cust_sid="2009358",cust_id="2009358" ,store_no="71" ,station="" ,first_name="" ,last_name="" ,price_lvl="", detax="0" ,info1="F000494091" ,
                               info2="1505976" ,modified_date="1985-01-01T00:00:00+08:00" ,sbs_no="1" ,cms="0" ,company_name="" ,title="" ,tax_area_name="" ,shipping="0", address1="" ,
                               address2="" ,address3="" ,address4="" ,address5="" ,address6="43" ,zip="", phone1="18688957337" ,phone2="", country_name="" ,alternate_id1="" ,alternate_id2="" )

INVC_SUPPLS   = ET.SubElement(invoice,'INVC_SUPPLS')
INVC_COMMENTS = ET.SubElement(invoice,'INVC_COMMENTS')
INVC_COMMENT  = ET.SubElement(INVC_COMMENTS,'INVC_COMMENT',comment_no="1" ,comments="快递配送")
INVC_EXTRAS   = ET.SubElement(invoice,'INVC_EXTRAS')
INVC_FEES     = ET.SubElement(invoice,'INVC_FEES')
INVC_FEE      = ET.SubElement(INVC_FEES,'INVC_FEE',fee_type="0", tax_perc="0", tax_incl="0", amt="-468" ,fee_name="Fee")
INVC_TENDERS  = ET.SubElement(invoice,'INVC_TENDERS')
INVC_TENDER   = ET.SubElement(INVC_TENDERS,'INVC_TENDER',tender_type="2", tender_no="1" ,taken="3488" ,given="0" ,amt="3488",doc_no="" ,auth="" ,reference="" ,chk_company="", chk_first_name="",
                              chk_last_name="", chk_work_phone="" ,chk_home_phone="" ,chk_state_code="" ,chk_dl="", chk_dl_exp_date="" ,chk_dob_date="", crd_exp_month="" ,crd_exp_year="" ,
                              crd_normal_sale="", crd_contr_no="" ,crd_present="" ,crd_zip="" ,crd_proc_fee="", gft_crd_trace_no="", gft_crd_int_ref_no="" ,gft_crd_balance="", charge_net_days="",
                              charge_disc_days="" ,charge_disc_perc="" ,pmt_date="", pmt_remark="", matched="", manual_name="" ,manual_remark="" ,transaction_id="" ,avs_code="", chk_type="" ,cashback_amt="" ,
                              l2_result_code="", signature_map="", orig_crd_name="", tender_state="0" ,failure_msg="" ,proc_date="" ,orig_currency_name="", eftdata0="", eftdata1="" ,eftdata2="", eftdata3="" ,
                              eftdata4="" ,eftdata5="", eftdata6="", eftdata7="" ,eftdata8="" ,eftdata9="" ,cardholder_name="", give_rate="" ,take_rate="" ,base_taken="" ,base_given="" ,cent_txn_id="" ,
                              balance_remaining="", cent_commit_txn="", emv_aid="" ,emv_applabel="", emv_card_exp_date="", emv_cyrpto_type="", emv_cryptogram="" ,emv_pin_statement="" ,cayan_sf_id="" ,
                              crd_name="" ,currency_name="")
INVC_COUPONS  = ET.SubElement(invoice,'INVC_COUPONS')

INVC_ITEMS    = ET.SubElement(invoice,'INVC_ITEMS')
INVC_ITEM     = ET.SubElement(INVC_ITEMS,'INVC_ITEM',item_pos="1",item_sid="233280576226555", qty="1" ,orig_price="5880" ,orig_tax_amt="854.35897" ,price="3488", tax_code="0" ,tax_perc="13" ,tax_amt="506.80342" ,
                              tax_code2="" ,tax_perc2="" ,tax_amt2="" ,cost="0" ,price_lvl="1" ,spif="0" ,sched_no="" ,comm_code="" ,comm_amt="" ,cust_fld="" ,scan_upc="" ,serial_no="" ,lot_number="" ,kit_flag="0" ,
                              pkg_item_sid="", pkg_seq_no="", orig_cmpnt_item_sid="" ,detax="0" ,usr_disc_perc="0", udf_value1="", udf_value2="", udf_value3="" ,udf_value4="" ,activity_perc="100" ,activity_perc2="0",
                              activity_perc3="0" ,activity_perc4="0",activity_perc5="0" ,comm_amt2="0" ,comm_amt3="0" ,comm_amt4="0" ,comm_amt5="0", so_sid="", so_orig_item_pos="" ,item_origin="" ,pkg_no="" ,
                              shipto_cust_sid="" ,shipto_addr_no="" ,orig_cost="" ,item_note1="" ,item_note2="" ,item_note3="" ,item_note4="" ,item_note5="" ,item_note6="", item_note7="" ,item_note8="" ,item_note9="" ,item_note10="pp;1704;1",
                              promo_flag="0" ,gift_activation_code="", gift_transaction_id="" ,returned_qty="" ,ref_item_pos="", ref_invc_sid="" ,gift_add_value="0" ,ref_invc_no="" ,alt_upc="" ,alt_alu="" ,alt_cost="",
                              alt_vend_code="" ,orig_prc_bdt="" ,prc_bdt="" ,gift_eftdata0="",gift_eftdata1="" ,gift_eftdata2="" ,gift_eftdata3="" ,gift_eftdata4="", gift_eftdata5="" ,gift_eftdata6="" ,gift_eftdata7="" ,gift_eftdata8="",
                              gift_eftdata9="" ,subloc_code="" ,subloc_id="" ,tender_state="0" ,failure_msg="" ,proc_date="" ,cent_commit_txn="0", price_flag="0", force_orig_tax="" ,sn_qty="", sn_active="", sn_received="" ,sn_sold="" ,
                              sn_transferred="" ,sn_so_reserved="" ,sn_returned="" ,sn_returned_to_vnd="" ,sn_adjusted="" ,tax_perc_lock="0" ,ref_store_no="", tax_area2_name="" ,empl_sbs_no="1" ,empl_name="10005751", disc_reason_name="" ,
                              empl_sbs_no2="", empl_name2="" ,empl_sbs_no3="" ,empl_name3="" ,empl_sbs_no4="" ,empl_name4="" ,empl_sbs_no5="", empl_name5="" ,ship_method="" ,item_reason_type="" ,item_reason_name="")
INVN_BASE_ITEM = ET.SubElement(INVC_ITEM,'INVN_BASE_ITEM',item_sid="233280576226555", upc="7290018007075", alu="", style_sid="747510953771833344", dcs_code="I  IB IBA" ,vend_code="586", scale_no="" ,description1="7290018007075" ,
                              description2="！TRIPOLLARSTOPX射频美容仪（粉", description3="TRIPOLLARSTOPX射频美容仪（粉色" ,description4="IN5860002", attr="", siz="IL" ,use_qty_decimals="0" ,tax_code="0" ,flag="0", ext_flag="0" ,item_no="" ,
                              udf3_value="EA" ,udf4_value="以色列" ,udf5_value="", udf6_value="" ,aux1_value="586" ,aux2_value="TRIPOLLAR", aux3_value="I", aux4_value="IB", aux5_value="IBA", aux6_value="美妆工具仪器", aux7_value="仪器" ,aux8_value="面部仪器")

logging.info("Format XML")
prettify(xml_doc)

tree = ET.ElementTree(xml_doc)
logging.info("Create XML File Tree")
tree.write('text.xml',encoding='UTF-8',xml_declaration=True)
logging.info("Write XML FilE")
