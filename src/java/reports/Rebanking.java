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
public class Rebanking extends HttpServlet {
HttpSession session;
String start_date,end_date,current_date;
String staff_no,staff_qr;
String cheque_no,staffname,activity,amount,dates,receipt_no;
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
           region_query+=" mous.health_id="+county+" AND";
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
           region_query+= " mous.health_id="+sub_county+" AND";
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
           region_query+= " mous.health_id="+facility_id+" AND";
         }
     }
     }
    }
    
//    clean the script
    if(!region_query.equals("")){ 
      region_query=removeLastChars(region_query,3);
      region_query=" AND ("+region_query+") AND mous.type_id="+region+" ";
     }
    else{
        if(region.equals("")){
            region_query="";     
        }
        else{
            region_query=" AND mous.type_id="+region+" "; 
        }
    }
   System.out.println("region query:"+region_query); 
        
    report_title="Rebanking Report";
        
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
    XSSFSheet shet1=wb.createSheet("Rebanking Report");
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
    for (int i=0;i<6;i++){
   shet1.setColumnWidth(i, 6000);
   if(i==0){
  shet1.setColumnWidth(i, 2000);     
   }
   if(i==3){
  shet1.setColumnWidth(i, 10000);     
   }
   
    }
    
  shet1.addMergedRegion(new CellRangeAddress(0,0,0,6));
    //Create headers
    XSSFRow rw0=shet1.createRow(row_pos); 
        rw0.setHeightInPoints(25);
        XSSFCell  S0cell=rw0.createCell(0);
        S0cell.setCellValue(report_title);
        S0cell.setCellStyle(styleHeader);  
    row_pos++;
     XSSFRow rw1=shet1.createRow(row_pos);  
    String []  header = {"No.","Cheque No","Staff Name","Activity","Amount","Date","Receipt No"} ; 
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
        
        String getReport="SELECT staff_no,fullname,email,phone,debit_id,cheque_no,debit_fco,debit_glcode,debit_amount,debit_date," +
                "credit_id,credit_fco,credit_glcode,credit_amount,credit_date,activity_amount,CONCAT(unique_code,sc_unique_code,fac_unique_code) AS unique_code," +
                "CONCAT(CHMT,SCHMT,facility_name) AS mou, CONCAT(activity,others) AS activity,activity_code,receipt_no " +
                "FROM (SELECT staff.staff_no AS staff_no,fullname,email,phone,debit.debit_id AS debit_id,cheque_no, debit.fco AS debit_fco," +
                " debit.gl_code AS debit_glcode,debit.amount AS debit_amount, debit.date AS debit_date," +
                "credit.credit_id AS credit_id,credit.fco AS credit_fco,credit.gl_code AS credit_glcode," +
                "credit.amount AS credit_amount,credit.date AS credit_date,activities.amount AS activity_amount," +
                "ifnull(CASE WHEN mous.type_id=1 THEN county.CHMT END,\"\") AS CHMT,  " +
                "ifnull(CASE WHEN mous.type_id=1 THEN county.unique_code END,\"\") AS unique_code,   " +
                "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.SCHMT END,\"\") AS SCHMT,  " +
                "ifnull(CASE WHEN mous.type_id=2 THEN sub_county.unique_code END,\"\") AS sc_unique_code, " +
                "ifnull(CASE WHEN mous.type_id=3 THEN facilities.facility_name END,\"\") AS facility_name,  " +
                "ifnull(CASE WHEN mous.type_id=3 THEN facilities.unique_code END,\"\") AS fac_unique_code," +
                "ifnull(advanced_activities.others,\"\") AS others," +
                "ifnull(activities.code,\"\") AS activity_code," +
                "IFNULL(activities.description,\"\") AS activity, "+
                "credit.receipt_no AS receipt_no " +
                "FROM credit " +
                "LEFT JOIN debit ON credit.debit_id=debit.debit_id " +
                "LEFT JOIN staff ON debit.staff_no=staff.staff_no " +
                "LEFT JOIN expensed_activities ON credit.credit_id=expensed_activities.credit_id " +
                "LEFT JOIN advanced_activities ON expensed_activities.advanced_activities_id = advanced_activities.id " +
                "LEFT JOIN activities ON advanced_activities.activity_id = activities.id " +
                "LEFT JOIN mous ON activities.mou_id=mous.id " +
                "LEFT JOIN county ON mous.health_id=county.id  " +
                "LEFT JOIN sub_county ON mous.health_id=sub_county.id  " +
                "LEFT JOIN facilities ON mous.health_id=facilities.id  WHERE credit.gl_code=608 AND "+staff_qr+" ("+date_between+") "+region_query+"  ) AS act_608";
        System.out.println(getReport);
        conn.rs = conn.st.executeQuery(getReport);
        
        while(conn.rs.next()){
         num++;
         budget = pos = 0;
         
         XSSFRow rw2=shet1.createRow(row_pos); 
         cheque_no = conn.rs.getString("cheque_no");
         staffname = conn.rs.getString("fullname");
         activity = conn.rs.getString("unique_code")+" "+conn.rs.getString("activity_code")+" "+conn.rs.getString("activity");
         amount = conn.rs.getString("credit_amount");
         dates = conn.rs.getString("credit_date");
         receipt_no = conn.rs.getString("receipt_no");

         //push to excel
         String [] data = {""+num,cheque_no,staffname,activity,amount,dates,receipt_no};
         cell_pos=0;
         for(String value :data){
            XSSFCell  S1cell=rw2.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Double.parseDouble(value));
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
       
      
       XSSFCell  cell_amount=rwtotal.createCell(4);
       
      //row totals
      
      int data_start=3;
      int data_end=row_pos;
      
        
        for (int i=0;i<=3;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellValue("Totals : ");
        cell_title.setCellStyle(stylex);
        }
        for (int i=5;i<=6;i++){
        XSSFCell  cell_title=rwtotal.createCell(i);
        cell_title.setCellStyle(stylex);
        }
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,0,3));
        shet1.addMergedRegion(new CellRangeAddress(row_pos,row_pos,5,6));
        
        String formulae_amount= "SUM(E"+data_start+":E"+data_end+")";
        cell_amount.setCellType(XSSFCell.CELL_TYPE_FORMULA);
        cell_amount.setCellFormula(formulae_amount);

               
        cell_amount.setCellStyle(totalcurrencyStyle);        

     }
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=Rebanking_Report_"+manager.getdatekey()+".xlsx");
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
        Logger.getLogger(Rebanking.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(Rebanking.class.getName()).log(Level.SEVERE, null, ex);
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
