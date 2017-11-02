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
String start_date,end_date,current_date;
String staff_no,staff_qr;
String fullname,email,phone,current_status,cheque_no,fco,gl_code,date_given,debited_amount,accounted,balance,facility_name,turnaroundtime,last_date_accounted,purpose;
int row_pos;
String report_title;
int staff_added;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        session = request.getSession();
        dbConn conn = new dbConn();
        Manager manager = new Manager();
        
        staff_no=staff_qr="";
        row_pos=0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        
        current_date = dateFormat.format(date);
        System.out.println("current date : "+current_date);
        
        report_title="";
        staff_added=0;
//        start_date = "2017-01-01";
//        end_date = "2017-10-20";
        start_date = request.getParameter("start_date");
        end_date = request.getParameter("end_date");
        report_title = "Comprehensive Report for Advances Issued From ["+start_date+"] To ["+end_date+"]";
        if(start_date.equals("") && end_date.equals("")){
         start_date = "2010-01-01";
         end_date=current_date;
        report_title = "Comprehensive Report for All Advances Issued "; 
        }
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
           staff_qr=" ("+staff_qr+") AND ";
           staff_qr = staff_qr.replace(" OR )", ")");
           }
           else{
           staff_qr="";    
           }
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
    
    

    for (int i=0;i<=15;i++){
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
  shet1.setColumnWidth(i, 6000);     
   }
   if(i==10 || i==11 || i==12 || i==14){
  shet1.setColumnWidth(i, 2000);     
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
    String []  header = {"No.","Staff Name","Email Address","Phone Number","Current Status","FCO"," GL Code","Cheque Number","Date Issued","Purpose","Debit","Credit","Balance","Facility","Turn Around Time (Days)","Day Last Accounted"} ; 
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
        String getReport="SELECT fullname,email,phone,status.name AS current_status,cheque_no,fco,gl_code,debit.date AS date_given, debit.amount AS debited_amount, " +
            "IFNULL(SUM(credit.amount),0) AS accounted, (debit.amount-IFNULL(SUM(credit.amount),0)) AS balance,facility_name,"+
            " DATEDIFF (MAX(credit.date),debit.date) AS turnaroundtime,  MAX(credit.date) AS last_accounted,purpose " +
            "FROM debit LEFT JOIN credit ON debit.debit_id=credit.debit_id " +

            "JOIN staff ON staff.staff_no=debit.staff_no " +
            "LEFT JOIN status ON staff.status_id=status.id " +
            "LEFT JOIN facilities ON debit.facility_id=facilities.id " +
            "WHERE "+staff_qr+" debit.date BETWEEN '"+start_date+"' AND '"+end_date+"' " +
            "GROUP BY debit.debit_id ORDER BY debit.date DESC,debit.timestamp DESC ";
        System.out.println(getReport);
        conn.rs = conn.st.executeQuery(getReport);
        
        while(conn.rs.next()){
         num++;
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
         facility_name = conn.rs.getString(12);
         turnaroundtime = conn.rs.getString(13);
         last_date_accounted = conn.rs.getString(14);
         purpose = conn.rs.getString(15);
         //push to excel
         String [] data = {""+num,fullname,email,phone,current_status,fco,gl_code,cheque_no,date_given,purpose,debited_amount,accounted,balance,facility_name,turnaroundtime,last_date_accounted};
         cell_pos=0;
         for(String value :data){
            XSSFCell  S1cell=rw2.createCell(cell_pos);
            if(isNumeric(value)){
                S1cell.setCellValue(Integer.parseInt(value));
            }
            else{
                S1cell.setCellValue(value);
            }
            S1cell.setCellStyle(stborder); 
            cell_pos++;   
         }
         row_pos++;
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
}
