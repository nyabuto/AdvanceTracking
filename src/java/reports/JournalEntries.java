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
import java.util.Arrays;
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
public class JournalEntries extends HttpServlet {
HttpSession session;
int row_pos=0;
String report_id;
String date_between;
String passed_year,currency;
int year,region_counter;
String region;
String region_query="";
String credit_id,credit_fco,credit_gl_code,amount_credited,credit_date,debit_id,staff_no,debit_fco,debit_gl_code,cheque_no,amount_debited,debit_date,purpose,fullname,email,phone,credit_gl_account,credit_gl_account_name,debit_gl_account,debit_gl_account_name;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        dbConn conn = new dbConn();
        session = request.getSession();
        Manager manager = new Manager();
       
        String headers[] = {"FCO","ID CODE","GL Acct/Det Code (Old or New)","GL Acct/Detail Code (New)","GL Account Name","ID Code/Speedkey (Reqd)","External Doc. No.","Award (Reqd)","Restriction (Reqd)","ID Code (Reqd)","Subaward (Option.)","Debit","Credit","Posting Description","JE Description (Optional Not Posted)"};
        
        row_pos=0;
       date_between=" WHERE "; 
      report_id= request.getParameter("reports");
      if(report_id.equals("5")){
          String start_date=request.getParameter("start_date");
          String end_date=request.getParameter("end_date");
          date_between +=" credit.date BETWEEN '"+start_date+"' AND '"+end_date+"' ";
      }
      else{
          passed_year=request.getParameter("years");
          year=Integer.parseInt(passed_year);
            switch (report_id) {
                case "1":
                    date_between +=" credit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-09-31' ";
                    break;
//        end of period
                case "2":
                    String semiannual[] = request.getParameterValues("semi_annual");
                    for (String sannual : semiannual){
                        if(sannual.equals("1")){
                            date_between +=" credit.date BETWEEN '"+(year-1)+"-10-01' AND '"+year+"-03-31' ";
                        }
                        else if(sannual.equals("2")){
                            date_between +=" OR credit.date BETWEEN '"+year+"-04-01' AND '"+year+"-09-31' ";
                        }
                    }          break;
                case "3":
                    String quarters[] = request.getParameterValues("quarters");
                    String qtrs[] ={(year-1)+"-10-01",(year-1)+"-12-31",year+"01-01",year+"03-31",year+"04-01",year+"06-31",year+"07-01",year+"09-31",};
                    for (String quarter : quarters){
                        if(!quarter.equals("")){
                            int qr=Integer.parseInt(quarter);
                            date_between +=" credit.date BETWEEN '"+qtrs[(qr*2)-2]+"' AND '"+qtrs[(qr*2)-1]+"' OR ";
                        }
                    }          date_between=removeLastChars(date_between, 3);
                    break;
                case "4":
                    String months[] = request.getParameterValues("months");
                    for (String month : months){
                        if(!month.equals("")){
                            int mn=Integer.parseInt(month);
                            if(mn<10){
                                date_between +=" credit.date BETWEEN '"+year+"-0"+month+"-01' AND '"+year+"-0"+month+"-31' OR ";
                            }
                            else{
                                date_between +=" credit.date BETWEEN '"+(year-1)+"-"+month+"-01' AND '"+(year-1)+"-"+month+"-31' OR ";
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
        
    XSSFWorkbook wb=new XSSFWorkbook();
    XSSFSheet shet1=wb.createSheet("Journal Entry");
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

    for (int i=0;i<=15;i++){
   shet1.setColumnWidth(i, 3000);
   if(i==4){
  shet1.setColumnWidth(i, 8000);     
   }
   if(i==13){
  shet1.setColumnWidth(i, 15000);     
   }
   if(i>=11 && i<=12){
  shet1.setColumnWidth(i, 5000);     
   }
  }
    
     //Create headers

     XSSFRow rw1=shet1.createRow(row_pos);  
     
     int cell_pos=0;
        for (String header_name : headers) {
            // headers
        XSSFCell  S1cell=rw1.createCell(cell_pos);
        S1cell.setCellValue(header_name);
        S1cell.setCellStyle(stylex); 
        cell_pos++;
        }
        
     //        end of currency
        row_pos++;
        int num=0;   
      String getReport="SELECT credit.credit_id AS credit_id,credit.debit_id,credit.fco AS credit_fco,"+
               "credit.gl_code AS credit_gl_code,credit.amount AS amount_credited,"+
               "credit.date AS credit_date,debit.debit_id AS debit_id,debit.staff_no AS staff_no,"+
               "debit.fco AS debit_fco,debit.gl_code AS debit_gl_code,debit.amount AS amount_debited,"+
                "debit.cheque_no AS cheque_no,debit.date AS debit_date,debit.purpose AS purpose," +
                "staff.fullname AS fullname,staff.email AS email, staff.phone AS phone," +
                "gl_code.account AS credit_gl_account,gl_code.account_name AS credit_gl_account_name " +
                "FROM credit " +
                "LEFT JOIN debit ON credit.debit_id=debit.debit_id " +
                "LEFT JOIN gl_code ON credit.gl_code=gl_code.code " +
                "LEFT JOIN staff ON debit.staff_no=staff.staff_no " +
                 ""+date_between+" "+region_query+" " +
                "GROUP BY credit.credit_id " +
                "ORDER BY debit.date ";  
        
        System.out.println(getReport);
        conn.rs = conn.st.executeQuery(getReport);
        
        while(conn.rs.next()){
         num++;
         credit_id = conn.rs.getString("credit_id");
         debit_id = conn.rs.getString("debit_id");
         credit_fco = conn.rs.getString("credit_fco");
         credit_gl_code = conn.rs.getString("credit_gl_code");
         amount_credited = conn.rs.getString("amount_credited");
         credit_date = conn.rs.getString("credit_date");
         debit_id = conn.rs.getString("debit_id");
         staff_no = conn.rs.getString("staff_no");
         debit_fco = conn.rs.getString("debit_fco");
         debit_gl_code = conn.rs.getString("debit_gl_code");
         amount_debited = conn.rs.getString("amount_debited");
         cheque_no = conn.rs.getString("cheque_no");
         debit_date = conn.rs.getString("debit_date");
         purpose = conn.rs.getString("purpose");
         fullname = conn.rs.getString("fullname");
         email = conn.rs.getString("email");
         phone = conn.rs.getString("phone");
         credit_gl_account = conn.rs.getString("credit_gl_account");
         credit_gl_account_name = conn.rs.getString("credit_gl_account_name");
        String getdebitgls="SELECT account,account_name FROM gl_code WHERE code='"+debit_gl_code+"'";
        conn.rs1=conn.st1.executeQuery(getdebitgls);
        if(conn.rs1.next()){
         debit_gl_account = conn.rs1.getString(1);
         debit_gl_account_name = conn.rs1.getString(2);  
        }
         
        String[] debit_data={debit_fco,"",debit_gl_code,debit_gl_account,debit_gl_account_name,"","chq#"+cheque_no,"","","","","",amount_credited,fullname+"-"+purpose,""};
        String[] credit_data={credit_fco,"",credit_gl_code,credit_gl_account,credit_gl_account_name,"","chq#"+cheque_no,"","","","",amount_credited,"",fullname+"-"+purpose,""};
         
         //push to excel
         
         cell_pos=0;
          XSSFRow rw_credit=shet1.createRow(row_pos); 
         for(String value :credit_data){
            XSSFCell  S1cell=rw_credit.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Integer.parseInt(value));
            }
            else{
                S1cell.setCellValue(value);
            }
            if(cell_pos>10 && cell_pos<=12){
             S1cell.setCellStyle(currencyStyle);    
            }
            else{
            S1cell.setCellStyle(stborder); 
            }
            cell_pos++;   
         }
         row_pos++;
         
                 XSSFRow rw_debit=shet1.createRow(row_pos);
            cell_pos=0;
         for(String value :debit_data){
            XSSFCell  S1cell=rw_debit.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Integer.parseInt(value));
            }
            else{
                S1cell.setCellValue(value);
            }
            if(cell_pos>10 && cell_pos<=12){
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
       
       XSSFCell  cell_debit=rwtotal.createCell(11);
       XSSFCell  cell_credit=rwtotal.createCell(12);
       
      //row totals
      
      int data_start=2;
      int data_end=row_pos;
      
        
        for (int i=0;i<=10;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellValue("Totals : ");
        cell_title.setCellStyle(stylex);
        }
        for (int i=13;i<=14;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellStyle(stylex);
        }
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,0,10));
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,13,14));
        
        String formulae_debit= "SUM(L"+data_start+":L"+data_end+")";
        cell_debit.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_debit.setCellFormula(formulae_debit);

        String formulae_credit= "SUM(M"+data_start+":M"+data_end+")";
        cell_credit.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_credit.setCellFormula(formulae_credit);


        cell_debit.setCellStyle(totalcurrencyStyle);
        cell_credit.setCellStyle(totalcurrencyStyle);        

     }  
        
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=JournalEntries_"+manager.getdatekey()+".xlsx");
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
        Logger.getLogger(JournalEntries.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(JournalEntries.class.getName()).log(Level.SEVERE, null, ex);
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
