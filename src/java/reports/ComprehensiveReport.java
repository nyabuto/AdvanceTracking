/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import Db.dbConn;
import advancetracking.Manager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author GNyabuto
 */
public class ComprehensiveReport extends HttpServlet {
HttpSession session;
String staff_no,staff_qr,startdate,enddate;
String fullname,email,phone,current_status,cheque_no,fco,gl_code,date_given,debited_amount,accounted,balance,facility_name,turnaroundtime,last_date_accounted,purpose;
int row_pos;
String report_title,currency,debit_id;
int staff_added;
int budget,pos,year;
String date_between,report_id,passed_year,region,region_query;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        session = request.getSession();
        dbConn conn = new dbConn();
        Manager manager = new Manager();
        
        staff_no=staff_qr="";
        row_pos=0;
      
        report_title="";
        staff_added=0;
        startdate=enddate="";
        
        staff_qr=""; 
        date_between=" "; 
      report_id= request.getParameter("reports");
      if(report_id.equals("5")){
          String start_date=request.getParameter("start_date");
          String end_date=request.getParameter("end_date");
          date_between +=" debit.date BETWEEN '"+start_date+"' AND '"+end_date+"' ";  
      }
      else{
          passed_year=request.getParameter("years");
          year=Integer.parseInt(passed_year);
            switch (report_id) {
                case "1":
                    date_between +=" debit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-09-31' ";
                    break;
//        end of period
                case "2":
                    String semiannual[] = request.getParameterValues("semi_annual");
                    for (String sannual : semiannual){
                        if(sannual.equals("1")){
                            date_between +=" debit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-03-31' ";
                        }
                        else if(sannual.equals("2")){
                            date_between +=" OR debit.date BETWEEN '"+year+"-04-01' AND '"+year+"-09-31' ";
                        }
                    }          break;
                case "3":
                    String quarters[] = request.getParameterValues("quarters");
                    String qtrs[] ={(year-1)+"-10-01",(year-1)+"-12-31",year+"01-01",year+"03-31",year+"04-01",year+"06-31",year+"07-01",year+"09-31",};
                    for (String quarter : quarters){
                        if(!quarter.equals("")){
                            int qr=Integer.parseInt(quarter);
                            date_between +=" debit.date BETWEEN '"+qtrs[(qr*2)-2]+"' AND '"+qtrs[(qr*2)-1]+"' OR ";
                        }
                    }          date_between=removeLastChars(date_between, 3);
                    break;
                case "4":
                    String months[] = request.getParameterValues("months");
                    for (String month : months){
                        if(!month.equals("")){
                            int mn=Integer.parseInt(month);
                            if(mn<10){
                                date_between +=" debit.date BETWEEN '"+year+"-0"+month+"-01' AND '"+year+"-0"+month+"-31' OR ";
                            }
                            else{
                                date_between +=" debit.date BETWEEN '"+(year-1)+"-"+month+"-01' AND '"+(year-1)+"-"+month+"-31' OR ";
                            }
                            
                        }
                    }          date_between=removeLastChars(date_between, 3);
                    break;
                default:
                    break;
            }
      }  
    // county,subcounty,facilities
    region = request.getParameter("region_id");
    if(region.equals("")){
    region_query = "";    
    }
    else if(region.equals("1")){ //county
     region_query = "";
     String counties[] = request.getParameterValues("county_ids");
     if(counties!=null){
     for(String county:counties){
         if(!county.equals("")){ // add it to the list
           region_query+=" health_id="+county+" AND";
         }
     }
     }
    }
    
    else if(region.equals("2")){//sub county
      region_query = "";
     String sub_counties[] = request.getParameterValues("sub_county_ids");
     if(sub_counties!=null){
     for(String sub_county:sub_counties){
         if(!sub_county.equals("")){ // add it to the list
           region_query+= " health_id="+sub_county+" AND";
         }
     }   
     }   
    }
    
    else if(region.equals("3")){//facilities
           region_query = "";
     String facilities[] = request.getParameterValues("facility_ids");
     if(facilities!=null){
     for(String facility_id:facilities){
         if(!facility_id.equals("")){ // add it to the list
           region_query+= " health_id="+facility_id+" AND";
         }
     }
     }
    }
    
//    clean the script
    if(!region_query.equals("")){ 
      region_query=removeLastChars(region_query,3);
      region_query=" AND ("+region_query+") AND health_type_id="+region+" ";
     }
    else{
        if(region.equals("")){
            region_query="";     
        }
        else{
            region_query=" AND health_type_id="+region+" "; 
        }
    }
   System.out.println("region query:"+region_query); 
        
    report_title="Comprehensive Report";
        
    if(request.getParameterValues("staffs")!=null){
           String[] staff_nos=request.getParameterValues("staffs");
           for(String staff_no_new : staff_nos){
          if(!staff_no_new.equals("")){ 
              staff_added++;
            staff_no = staff_no_new;
            staff_qr+="debit.staff_no='"+staff_no+"' OR ";
        }
           }
           if(staff_added>0){
           staff_qr=" 1=1 AND ("+staff_qr+") AND ";
           staff_qr = staff_qr.replace(" OR )", ")");
           }
           else{
           staff_qr=" 1=1 AND ";    
           }
        }
        else{
         staff_qr=" 1=1 AND ";     
        }
        
        System.out.println("staff q : "+staff_qr);
         //            ^^^^^^^^^^^^^CREATE STATIC AND WRITE STATIC DATA TO THE EXCELL^^^^^^^^^^^^
    XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet1=wb.createSheet("Comprehensive Report");
    XSSFFont font=wb.createFont();
    font.setFontHeightInPoints((short)18);
    font.setFontName("Cambria");
    font.setColor((short)0000);
    
    CellStyle style=wb.createCellStyle();
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFCellStyle styleHeader = wb.createCellStyle();
    styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
    styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleHeader.setBorderTop(BorderStyle.THIN);
    styleHeader.setBorderBottom(BorderStyle.THIN);
    styleHeader.setBorderLeft(BorderStyle.THIN);
    styleHeader.setBorderRight(BorderStyle.THIN);
    styleHeader.setAlignment(HorizontalAlignment.CENTER);
    
    XSSFFont fontHeader = wb.createFont();
    fontHeader.setColor(HSSFColor.BLACK.index);
    fontHeader.setBold(true);
    fontHeader.setFamily(FontFamily.MODERN);
    fontHeader.setFontName("Cambria");
    fontHeader.setFontHeight(15);
    styleHeader.setFont(fontHeader);
    styleHeader.setWrapText(true);
    
    XSSFCellStyle stylex = wb.createCellStyle();
    stylex.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    stylex.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    stylex.setBorderTop(BorderStyle.THIN);
    stylex.setBorderBottom(BorderStyle.THIN);
    stylex.setBorderLeft(BorderStyle.THIN);
    stylex.setBorderRight(BorderStyle.THIN);
    stylex.setAlignment(HorizontalAlignment.LEFT);
    
    XSSFFont fontx = wb.createFont();
    fontx.setColor(HSSFColor.BLACK.index);
    fontx.setBold(true);
    fontx.setFamily(FontFamily.MODERN);
    fontx.setFontName("Cambria");
    stylex.setFont(fontx);
    stylex.setWrapText(true);
    
    XSSFCellStyle stborder = wb.createCellStyle();
    stborder.setBorderTop(BorderStyle.THIN);
    stborder.setBorderBottom(BorderStyle.THIN);
    stborder.setBorderLeft(BorderStyle.THIN);
    stborder.setBorderRight(BorderStyle.THIN);
    stborder.setAlignment(HorizontalAlignment.LEFT);
    
    XSSFFont font_cell=wb.createFont();
    font_cell.setColor(HSSFColor.BLACK.index);
    font_cell.setFamily(FontFamily.MODERN);
    font_cell.setFontName("Cambria");
    stborder.setFont(font_cell);
    stborder.setWrapText(true);
    
    XSSFCellStyle currencyStyle=wb.createCellStyle();
    currencyStyle.setBorderTop(BorderStyle.THIN);
    currencyStyle.setBorderBottom(BorderStyle.THIN);
    currencyStyle.setBorderLeft(BorderStyle.THIN);
    currencyStyle.setBorderRight(BorderStyle.THIN);
    currencyStyle.setAlignment(HorizontalAlignment.LEFT);
    
    currencyStyle.setFont(font_cell);
    currencyStyle.setWrapText(true);
    
    XSSFCellStyle totalcurrencyStyle=wb.createCellStyle();
    totalcurrencyStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    totalcurrencyStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    totalcurrencyStyle.setBorderTop(BorderStyle.THIN);
    totalcurrencyStyle.setBorderBottom(BorderStyle.THIN);
    totalcurrencyStyle.setBorderLeft(BorderStyle.THIN);
    totalcurrencyStyle.setBorderRight(BorderStyle.THIN);
    totalcurrencyStyle.setAlignment(HorizontalAlignment.LEFT);
    
    totalcurrencyStyle.setFont(fontx);
    totalcurrencyStyle.setWrapText(true);
    
    
   DataFormat df = wb.createDataFormat();
   
   // fetch currency
   
   String getcurrency = "SELECT name FROM currency";
   conn.rs=conn.st.executeQuery(getcurrency);
   if(conn.rs.next()){
       currency = conn.rs.getString(1);
   }
   else{
       currency="$";
   }
//currencyStyle.setDataFormat(df.getFormat("_(\""+currency+"\"* #,##0.00_);_(\""+currency+"\"* (#,##0.00);_(\""+currency+"\"* \"0\"??_);_(@_)"));
//totalcurrencyStyle.setDataFormat(df.getFormat("_(\""+currency+"\"* #,##0.00_);_(\""+currency+"\"* (#,##0.00);_(\""+currency+"\"* \"0\"??_);_(@_)"));
//   
    for (int i=0;i<15;i++){
   shet1.setColumnWidth(i, 5000);
   if(i==0){
  shet1.setColumnWidth(i, 1000);     
   }
   if(i==3 || i==4){
  shet1.setColumnWidth(i, 3000);     
   }
   if(i==5 || i==6){
  shet1.setColumnWidth(i, 2000);     
   }
   if(i==8){
  shet1.setColumnWidth(i, 4000);     
   }
   if(i==9){
  shet1.setColumnWidth(i, 9000);     
   }
   if(i==10 || i==11 || i==12 || i==15){
  shet1.setColumnWidth(i, 5000);     
   }
   
    }
    
  shet1.addMergedRegion(new CellRangeAddress(0,0,0,15));
    //Create headers
    XSSFRow rw0=shet1.createRow(row_pos); 
        rw0.setHeightInPoints(25);
        XSSFCell  S0cell=rw0.createCell(0);
        S0cell.setCellValue(report_title);
        S0cell.setCellStyle(styleHeader);  
    row_pos++;
     XSSFRow rw1=shet1.createRow(row_pos);  
    String []  header = {"No.","Staff Name","Email Address","Phone Number","Current Status","FCO"," GL Code","Cheque Number","Date Issued","Purpose","Debit","Credit","Not Accounted","Budget","Turn Around Time (Days)","Day Last Accounted"} ; 
        int cell_pos=0;
        for (String header_name : header) {
            // headers
        XSSFCell  S1cell=rw1.createCell(cell_pos);
        S1cell.setCellValue(header_name);
        S1cell.setCellStyle(stylex); 
        cell_pos++;
        }
        
        //get currency
        
        
//        end of currency
        row_pos++;
        int num=0;
        String getReport="SELECT fullname,email,phone,status.name AS current_status,cheque_no,debit.fco AS fco,debit.gl_code AS gl_code,debit.date AS date_given, debit.amount AS debited_amount, " +
            "IFNULL(SUM(credit.amount),0) AS accounted, (debit.amount-IFNULL(SUM(credit.amount),0)) AS balance,"+
            " DATEDIFF (MAX(credit.date),debit.date) AS turnaroundtime,  MAX(credit.date) AS last_accounted,purpose,debit.debit_id AS debit_id " +
            "FROM debit LEFT JOIN credit ON debit.debit_id=credit.debit_id " +

            "JOIN staff ON staff.staff_no=debit.staff_no " +
            "LEFT JOIN status ON staff.status_id=status.id " +
            "WHERE "+staff_qr+" ("+date_between+") "+region_query+" " +
            "GROUP BY debit.debit_id ORDER BY debit.date DESC,debit.timestamp DESC ";
        System.out.println(getReport);
        conn.rs = conn.st.executeQuery(getReport);
        
        while(conn.rs.next()){
         num++;
         budget = pos = 0;
         
         XSSFRow rw2=shet1.createRow(row_pos); 
         fullname = conn.rs.getString(1);
         email = conn.rs.getString(2);
         phone = conn.rs.getString(3);
         current_status = conn.rs.getString(4);
         cheque_no = conn.rs.getString(5);
         fco = conn.rs.getString(6);
         gl_code = conn.rs.getString(7);
         date_given = conn.rs.getString(8);
         debited_amount = conn.rs.getString(9);
         accounted = conn.rs.getString(10);
         balance = conn.rs.getString(11);
         turnaroundtime = conn.rs.getString(12);
         last_date_accounted = conn.rs.getString(13);
         purpose = conn.rs.getString(14);
         debit_id = conn.rs.getString(15);
         
         if(!purpose.equals("")){
             pos++;
          purpose =pos+". "+ conn.rs.getString(14)+"\n";   
         }
//         get purpose

String get_data = "SELECT id,code,CONCAT(description,others) AS description,amount,CONCAT(CHMT,SCHMT,facility_name) AS mou,  " +
                        "CONCAT(unique_code,sc_unique_code,fac_unique_code) AS unique_code,status FROM( " +
                        "SELECT advanced_activities.id AS id," +
                        "IFNULL(code,\"\") AS code," +
                        "IFNULL(description,\"\") AS description," +
                        "IFNULL(amount,\"\") AS amount, " +
                        "ifnull(others,\"\") AS others," +
                        "ifnull(CASE WHEN mous.type_id=1 THEN county.CHMT END,\"\") AS CHMT,  " +
                        "ifnull(CASE WHEN mous.type_id=1 THEN county.unique_code END,\"\") AS unique_code,   " +
                        "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.SCHMT END,\"\") AS SCHMT,  " +
                        "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.unique_code END,\"\") AS sc_unique_code, " +
                        "ifnull(CASE WHEN mous.type_id=3 THEN facilities.facility_name END,\"\") AS facility_name,  " +
                        "ifnull(CASE WHEN mous.type_id=3 THEN facilities.unique_code END,\"\") AS fac_unique_code,"+
                        "advanced_activities.status AS status   " +
                        "FROM advanced_activities  " +
                        "LEFT JOIN activities ON advanced_activities.activity_id  = activities.id " +
                        "LEFT JOIN mous ON activities.mou_id = mous.id  " +
                        "LEFT JOIN county ON mous.health_id=county.id  " +
                        "LEFT JOIN sub_county ON mous.health_id=sub_county.id  " +
                        "LEFT JOIN facilities ON mous.health_id=facilities.id  " +
                        "WHERE advanced_activities.debit_id=? ORDER BY advanced_activities.id) AS activiti_data";
                  
               conn.pst1 = conn.conn.prepareStatement(get_data);
               conn.pst1.setString(1, debit_id);
            
               conn.rs1 = conn.pst1.executeQuery();
               while(conn.rs1.next()){
                   
                if(!conn.rs1.getString("unique_code").equals("")){
                    if(!purpose.contains(conn.rs1.getString("unique_code")+" - "+conn.rs1.getString("description"))){
                        pos++;
                        purpose+=pos+". "+conn.rs1.getString("unique_code")+" - "+conn.rs1.getString("description")+"\n";
                    }
                }
                else{
                    if(!purpose.contains(conn.rs1.getString("description"))){
                        pos++;
                        purpose+=pos+". "+conn.rs1.getString("description")+"\n"; 
                    }
                } 
                budget+=conn.rs1.getInt(4);
               }
              
         //push to excel
         String [] data = {""+num,fullname,email,phone,current_status,fco,gl_code,cheque_no,date_given,purpose,debited_amount,accounted,balance,""+budget,turnaroundtime,last_date_accounted};
         cell_pos=0;
         for(String value :data){
            XSSFCell  S1cell=rw2.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Integer.parseInt(value));
            }
            else{
                S1cell.setCellValue(value);
            }
            if(cell_pos>9 && cell_pos<=12){
             S1cell.setCellStyle(currencyStyle);    
            }
            else{
            S1cell.setCellStyle(stborder); 
            }
            cell_pos++;   
         }
         row_pos++;
        }
        
     if(num>0) {  
       XSSFRow rwtotal=shet1.createRow(row_pos); 
       rwtotal.setHeightInPoints(28);
       
       XSSFCell  cell_debit=rwtotal.createCell(10);
       XSSFCell  cell_credit=rwtotal.createCell(11);
       XSSFCell  cell_balance=rwtotal.createCell(12);
       XSSFCell  cell_budget=rwtotal.createCell(13);
       
      //row totals
      
      int data_start=3;
      int data_end=row_pos;
      
        
        for (int i=0;i<=9;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellValue("Totals : ");
        cell_title.setCellStyle(stylex);
        }
        for (int i=13;i<=15;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellStyle(stylex);
        }
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,0,9));
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,14,15));
        
        String formulae_debit= "SUM(K"+data_start+":K"+data_end+")";
        cell_debit.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_debit.setCellFormula(formulae_debit);

        String formulae_credit= "SUM(L"+data_start+":L"+data_end+")";
        cell_credit.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_credit.setCellFormula(formulae_credit);

        String formulae_balance= "SUM(M"+data_start+":M"+data_end+")";
        cell_balance.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_balance.setCellFormula(formulae_balance);
        
        String formulae_budget= "SUM(N"+data_start+":N"+data_end+")";
        cell_budget.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_budget.setCellFormula(formulae_budget);

        cell_debit.setCellStyle(totalcurrencyStyle);
        cell_credit.setCellStyle(totalcurrencyStyle);
        cell_balance.setCellStyle(totalcurrencyStyle);        
        cell_budget.setCellStyle(totalcurrencyStyle);        

     }
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=ComprehensiveReport_"+manager.getdatekey()+".xlsx");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    try {
        processRequest(request, response);
    } catch (SQLException ex) {
        Logger.getLogger(ComprehensiveReport.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    try {
        processRequest(request, response);
    } catch (SQLException ex) {
        Logger.getLogger(ComprehensiveReport.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
   private static String removeLastChars(String str, int num) {
    return str.substring(0, str.length() - num);
}
}
