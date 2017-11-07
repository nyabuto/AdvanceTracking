<%-- 
    Document   : Advances.jsp
    Created on : Sep 28, 2017, 3:03:18 PM
    Author     : GNyabuto
--%>

<%@page import="java.util.Calendar"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Staff Advances</title>
        <link rel="shortcut icon" href="assets/images/aphia/aphia.png" style="height: 20px;padding: 0px; margin: 0px;"/>

	<!-- Global stylesheets -->
	<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
	<link href="assets/css/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
	<link href="assets/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="assets/css/core.css" rel="stylesheet" type="text/css">
	<link href="assets/css/components.css" rel="stylesheet" type="text/css">
	<link href="assets/css/colors.css" rel="stylesheet" type="text/css">
	<!-- /global stylesheets -->

	<!-- Core JS files -->
	<script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="assets/js/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="assets/js/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/loaders/blockui.min.js"></script>
	<!-- /core JS files -->
       
	 <!--Theme JS files--> 
	<script type="text/javascript" src="assets/js/plugins/tables/datatables/datatables.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/tables/datatables/extensions/buttons.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/tables/datatables/extensions/select.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/forms/selects/select2.min.js"></script>
       
        <!--to manage notifications-->
        <script type="text/javascript" src="assets/js/plugins/notifications/bootbox.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/notifications/noty.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
        
	<script type="text/javascript" src="assets/js/core/app.js"></script>
	<script type="text/javascript" src="assets/js/pages/datatables_extension_buttons_print.js"></script>
        <script type="text/javascript" src="assets/js/pages/datatables_extension_select.js"></script>
	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
        
	<script type="text/javascript" src="assets/js/plugins/pickers/daterangepicker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/anytime.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.date.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.time.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/legacy.js"></script>
	 <!--/theme JS files--> 
         <script type="text/javascript" language="en">
   function numbers(evt){
        var charCode=(evt.which) ? evt.which : event.keyCode
        if(charCode > 31 && (charCode < 48 || charCode>57))
        return false;
        return true;
  }
//-->
</script>
        <script type="text/javascript">
          jQuery(document).ready(function(){
              //load staff name
              $.ajax({
        url:'staffname',
        type:"post",
        dataType:"html",
        success:function(staff_name){
        $("#staff_name").html(staff_name);    
        }
    });     
        loadAdvances();      
        
          }); 
          
          function loadAdvances(){
                 $.ajax({
        url:'load_debits',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var debit_id,cheque_no,fco,gl_code,amount,date,output,purpose,facility_name,status,status_id,balance,timestamp,currency;
            output='';
        var data=raw_data.data;
        var pos=0;
        var dataSet=[];
        for (var i=0; i<data.length;i++){
            pos++;
            debit_id=cheque_no=fco=gl_code=amount=date=purpose=facility_name=status=status_id=balance=currency=timestamp="";
            if( data[i].debit_id!=null){debit_id = data[i].debit_id;}
            if( data[i].cheque_no!=null){cheque_no = data[i].cheque_no;}
            if( data[i].fco!=null){fco = data[i].fco;}
            if( data[i].gl_code!=null){gl_code = data[i].gl_code;}
            if( data[i].amount!=null){amount = data[i].amount;}
            
            if( data[i].date!=null){date = data[i].date;}
            if( data[i].purpose!=null){purpose = data[i].purpose;}
            if( data[i].facility_name!=null){facility_name = data[i].facility_name;}
            if( data[i].status!=null){status = data[i].status;}
            if( data[i].status_id!=null){status_id = data[i].status_id;}
            if( data[i].timestamp!=null){timestamp = data[i].timestamp;}
            if( data[i].balance!=null){balance = data[i].balance;}
            if( data[i].currency!=null){currency = data[i].currency;}
            
           var identifier=""+debit_id+"X"+balance;
            // output the values to data table
         output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
            if(status_id==1){
                output+='<li><button class="btn btn-link" onclick=\"accounting_history('+pos+');\" ><i class="icon-list-unordered position-left"></i> Accounting History</button></li>';  
                
      }
      else if(status_id==2){
          <%if(session.getAttribute("staff_status")!=null){
           if(session.getAttribute("staff_status").toString().equals("1")){   
          %>
            output+='<li><button class="btn btn-link" onclick=\"add_credit('+pos+');\" ><i class="icon-plus3 position-left"></i> Account</button></li>';
            <%}}%>
            output+='<li><button class="btn btn-link" onclick=\"accounting_history('+pos+');\" ><i class="icon-list-unordered position-left"></i>Accounting History</button></li>';   
        }
      else if(status_id==3){
          <%if(session.getAttribute("staff_status")!=null){
             if(session.getAttribute("staff_status").toString().equals("1")){   
          %>
            output+='<li><button class="btn btn-link" onclick=\"add_credit('+pos+');\" ><i class="icon-plus3 position-left"></i> Account</button></li>';
       <%}}%>
        }
        <%if(session.getAttribute("staff_status")!=null){
             if(session.getAttribute("staff_status").toString().equals("1")){   
          %>
        output+='<li><button class="btn btn-link" onclick=\"edit('+pos+');\" ><i class="icon-pencil3 position-left"></i> Edit</button></li>'; 
          <%}}%>
        output+='<input type="hidden" name="'+pos+'" value="'+identifier+'" id="'+pos+'">';
        output+='</ul></li></ul></div>';
         amount=currency+'. '+amount;
           var minSet = [pos,cheque_no,fco,gl_code,amount,date,purpose,facility_name,status,output];
           
           dataSet.push(minSet);
            // output the values to data table
        }
        var table = $('#table').DataTable();
        table.destroy();
        $('#table').dataTable({
            data: dataSet,
            responsive: true,
            className:'',
            columnDefs: []
            
        });
        }
          
        });
          }
          
          function add_credit(pos){
          var value=$("#"+pos).val().split("X");
          var debit_id,balance;
          debit_id = value[0];
          balance = parseInt(value[1]);
          
            var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
            var day = currentDate.getDate();
            var month = currentDate.getMonth();
            var year = currentDate.getFullYear();


          // pop up
          bootbox.dialog({
                title: "Account for Advance .",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Amount : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="amount" required name="amount"  onkeypress="return numbers(event)" type="text" placeholder="Enter amount" class="form-control">' +
                                    '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Date : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="date" name="date" required type="date" placeholder="Select Date" class="form-control pickadate-disable-range">' +
                                    '</div>' +
                            '</div>' +
                        '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Update",
                        className: "btn-success",
                        callback: function () {
                            var amount = $("#amount").val();
                            var date = $("#date").val();
                            var theme="",header="",message="";
                            
                            if(amount!="" && date!=""){
                               
                            if(amount>balance){
                            theme = "bg-danger";
                            header = "Error";
                            message = "You have accounted for more than the current balance.";
                            }
                            else{
                           var url='save_credit';
                           var form_data = {"debit_id":debit_id,"amount":amount,"date":date};
                                $.post(url,form_data , function(output) {
                                    var response = JSON.parse(output).data;
                                    var response_code=response.code;
                                   var response_message=response.message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadAdvances(); 
                                    }
                                    else{
                                        theme = "bg-danger";
                                        header = "Error";
                                        message = response_message;   
                                    }
                                    $.jGrowl(message, {
                                        position: 'top-center',
                                        header: header,
                                        theme: theme
                                    });  
                                 });
                            }
                        }
                        else{
                        theme = "bg-danger";
                        header = "Error";
                        message = "Enter amount and date.";   
                        
                        $.jGrowl(message, {
                        position: 'top-center',
                        header: header,
                        theme: theme
                         });
                        }
                        }
                    }
                }
            }
        );

        $('.pickadate-disable-range').pickadate({
            format: 'yyyy-mm-dd',
                disable: [
                    { from: [year, month, day], to: [3017, 9, 25] }
                ]
            });
          }
        </script>
        <script type="text/javascript">
             jQuery(document).ready(function(){
//                 $("#facility_group").hide();
             });
           function load_fcos(){
               $.ajax({
                    url:'load_fco',
                    type:"post",
                    dataType:"html",
                    success:function(data){
                   $("#fco").html(data);
                   $("#fco").selectpicker();
                    }
                });
           } 
           function load_gl_codes(){
               $.ajax({
                    url:'load_glcodes',
                    type:"post",
                    dataType:"html",
                    success:function(data){
                    $("#gl_code").html(data);
                     $("#gl_code").selectpicker();
                    }
                });
    
           }
           
           function load_facilities(){
               $.ajax({
                    url:'load_facilities',
                    type:"post",
                    dataType:"html",
                    success:function(data){
                    $("#facility").html(data);
                     $("#facility").selectpicker();
                    }
                });
    
           }
           
           function gl_changed(){
               var gl_code = $("#gl_code").val();
               if(gl_code==529 || gl_code==528){
//               // for facilities, show facilities
               $("#facility_group").show();
               load_facilities();
               }
               else{
////                   hide facilities
                $("#facility_group").hide();
               }
           }
           
            </script>
        <script type="text/javascript">
            function new_advance(){
                var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
                var day = currentDate.getDate();
                var month = currentDate.getMonth();
                var year = currentDate.getFullYear();

               // pop up
          bootbox.dialog({
                title: "New Advance.",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Cheque No : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="cheque_no" required name="cheque_no" type="text" placeholder="Enter Cheque Number" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select FCO : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="fco" required name="fco" class="form-control bootstrap-select" data-header="Choose FCO" data-live-search="true" style="max-height:100px;">' +
                                   '<option value="">Loading FCO</option>'+
                                    '</select>' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select GL Code : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="gl_code" required name="gl_code" onChange="gl_changed();" class="form-control bootstrap-select" data-header="Choose GL Code" data-live-search="true" style="max-height:100px;">'+
                                      '<option value="">Loading GL Codes</option>'+ 
                                        '</select>' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Amount : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="amount2" required name="amount2" onkeypress="return numbers(event)" type="text" placeholder="Enter amount" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Date : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="date2" name="date2" required type="date" placeholder="Select Date" class="form-control pickadate-disable-range">' +
                                '</div>' +
                            '</div>' +
                           
                            '<div class="form-group" id="facility_group" hidden>' +
                                '<label class="col-md-4 control-label">Select Facility : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="facility" required name="facility" class="form-control bootstrap-select" data-header="Choose Facility" data-live-search="true" style="max-height:100px;">'+
                                      '<option value="">No facility</option>'+ 
                                        '</select>' +
                                '</div>' +
                                
                            '</div>' +
                             '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Purpose : </label>' +
                                '<div class="col-md-8">' +
                                    '<textarea id="purpose" name="purpose" required placeholder="Enter purpose" class="form-control"></textarea>' +
                                    '</div>' +
                            '</div>' +
                        '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Save",
                        className: "btn-success",
                        callback: function () {
                            var amount = $("#amount2").val();
                            var date = $("#date2").val();
                            var cheque_no = $("#cheque_no").val();
                            var fco = $("#fco").val();
                            var gl_code = $("#gl_code").val();
                            var facility =  $("#facility").val();
                            var purpose =  $("#purpose").val();
                            
                            var theme="",header="",message="";
                            
                            if(amount!="" && date!="" && cheque_no!="" && fco!="" && gl_code!=""){
                           var url='save_debit';
                           var form_data = {"amount":amount,"date":date,"cheque_no":cheque_no,"fco":fco,"gl_code":gl_code,"facility":facility,"purpose":purpose};
                                $.post(url,form_data , function(output) {
                                    var response = JSON.parse(output).data;
                                    var response_code=response.code;
                                   var response_message=response.message;
                                   message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadAdvances(); 
                                    }
                                    else{
                                       theme = "bg-danger";
                                        header = "Error";
                                        message = response_message;   
                                    }
                                  $.jGrowl(message, {
                                        position: 'top-center',
                                        header: header,
                                        theme: theme
                                   });  
                                 });
                           
                        }
                        else{
                            theme = "bg-danger";
                            header = "Error";
                            message = "Enter all required information";  
                                        
                        $.jGrowl(message, {
                            position: 'top-center',
                            header: header,
                            theme: theme
                        });
                        }
                    }
                    }
                }
            }
        ); 
load_gl_codes();
load_fcos();
load_facilities();

    $('.pickadate-disable-range').pickadate({
        format: 'yyyy-mm-dd',
        disable: [
            { from: [year, month, day], to: [3017, 9, 25] }
        ]
    });
            }
            
            
            function accounting_history(posion){
          var value=$("#"+posion).val().split("X");
          var debit_id,balance;
          debit_id = value[0];
          balance = parseInt(value[1]);
          $("#tt").val("0");
          
        var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
        var day = currentDate.getDate();
        var month = currentDate.getMonth();
        var year = currentDate.getFullYear();
                
          $.ajax({
                    url:'advance_credits?id='+debit_id,
                    type:"post",
                    dataType:"json",
                    success:function(output){
                   var data = output.data;
                   output='<table class="table datatable-button-print-rows datatable-select-basic"><thead>\n\
                 <tr><th>Position</th><th>Amount</th><th>Date </th>';
                    <%if(session.getAttribute("staff_status")!=null){
                    if(session.getAttribute("staff_status").toString().equals("1")){   
                    %> 
                    output+='<th>Edit/Remove</th>';
                    <%}}%>
                output+='</tr></thead><tbody>';
                
                   var credit_id,amount,date,timestamp,pos=0;
                    for (var i=0; i<data.length;i++){
                        pos++;
                        credit_id = data[i].id;
                        amount = data[i].amount;
                        date = data[i].date;
                        timestamp = data[i].timestamp;
                         output+='<tr id="row_'+pos+'">\n\
                                <td><p id="num_'+pos+'">'+pos+'</p></td>\n\
                                <input type="hidden" value="'+credit_id+'" id="edit_'+pos+'">\n\
                                <td><input type="text" name="edit_amount'+pos+'" id="edit_amount'+pos+'" value="'+amount+'" disabled onkeypress="return numbers(event)" class="form-control"></td>\n\
                                <td><input type="text" name="edit_date'+pos+'" id="edit_date'+pos+'" value="'+date+'" disabled placeholder="Select Date" class="form-control pickadate-disable-range" ></td>';
                           <%if(session.getAttribute("staff_status")!=null){
                            if(session.getAttribute("staff_status").toString().equals("1")){   
                            %>    
                         output+='<td><img id="editor_'+pos+'" onclick="edit_accounting('+pos+');" src="assets/images/aphia/edit.png"></td>';
                         <%}}%>
                         output+='</tr>';
                    }
                    output+='</tbody></table>';
                    
                    $('.pickadate-disable-range').pickadate({
                        format: 'yyyy-mm-dd',
                        disable: [
                            { from: [year, month, day], to: [3017, 9, 25] }
                            ]
                    });
                    // show results.
                    bootbox.dialog({
                title: "View Accounting History.",
                message: output,
                buttons: {
                    success: {
                        label: "Close",
                        className: "btn-info"
                        }
                        }    
                    });    
                    }
                });
                
            }
            
            function edit_accounting(pos){
              var credit_id=$("#edit_"+pos).val();
              var tt=$("#tt").val();
              if(tt=="0"){
                $("#edit_amount"+pos).prop("disabled", false);
                $("#edit_date"+pos).prop("disabled", false);
                $("#editor_"+pos).prop('src',"assets/images/aphia/upload.png");
                $("#editor_"+pos).prop('title',"Click to save updates.");
                $("#tt").val("1");
            }
            else{
               var amount,credit_id,date; 
                amount= $("#edit_amount"+pos).val();
                if(amount==""){amount=0;}
                date= $("#edit_date"+pos).val();
                var url='update_credit';
                var form_data = {"amount":amount,"date":date,"credit_id":credit_id};
                    $.post(url,form_data , function(output) {
                    var data = JSON.parse(output).data;
                    var prev_amount=data.prev_amount;
                    var prev_date=data.prev_date;
                    var code=data.code;
                    var message=data.message;

                    var theme="bg-success";
                    var header="Success";

                    if(code==0){
                        $("#edit_amount"+pos).val(prev_amount);    
                        $("#edit_date"+pos).val(prev_date);
                        theme = "bg-danger";
                        header = "Error";
                    } 
                    else{
                        var tr = $("#row_"+pos);
                        if(amount==0){
                        tr.hide();
                        tr.remove(); 
                        var i=pos;
                        while(i<200){
                        var elem=$("#"+i);
                        if (elem.length > 0) {
                        // exists.
                        var new_pos=i-1;
                        $("#num_"+i).html(new_pos);
                        }
                        else{
                            break;
                        }
                        i++;    
                        }
                        }
                        loadAdvances();
                    } 
                    $.jGrowl(message, {
                        position: 'top-center',
                        header: header,
                        theme: theme
                    });
                    });
                
                
                $("#edit_amount"+pos).prop("disabled", true);
                $("#edit_date"+pos).prop("disabled", true);
                $("#editor_"+pos).prop('src',"assets/images/aphia/edit.png");
                $("#editor_"+pos).prop('title',"Click to edit values.");
                $("#tt").val("0");    
            }
            
            }
            
             function edit(pos){
          var value=$("#"+pos).val().split("X");
          var debit_id,balance;
          debit_id = value[0];
          balance = parseInt(value[1]);
          var counter=0;
            var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
            var day = currentDate.getDate();
            var month = currentDate.getMonth();
            var year = currentDate.getFullYear();
            
            var cheque_no,fco,gl_code,amount,date,facility,purpose;
            $.ajax({
                    url:'load_edit_advance?id='+debit_id,
                    type:"post",
                    dataType:"json",
                    success:function(res){
                   var data=res.data;
                   
                        cheque_no = data.cheque_no;
                        fco = data.fco;
                        gl_code = data.gl_code;
                        amount = data.amount;
                        date = data.date;
                        purpose = data.purpose;
                        facility = data.facility;
                        
                               // pop up
                   bootbox.dialog({
                title: "Edit Advance Details.",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Cheque No : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_cheque_no" required name="edit_cheque_no" type="text" value="'+cheque_no+'" placeholder="Enter Cheque Number" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select FCO : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="edit_fco" required name="edit_fco" class="form-control bootstrap-select" data-header="Choose FCO" data-live-search="true" style="max-height:100px;">' +
                                   ''+fco+''+
                                    '</select>' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select GL Code : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="edit_gl_code" required name="edit_gl_code" onChange="gl_changed();" class="form-control bootstrap-select" data-header="Choose GL Code" data-live-search="true" style="max-height:100px;">'+
                                      ''+gl_code+''+ 
                                        '</select>' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Amount : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_amount2" required name="edit_amount2" value="'+amount+'" onkeypress="return numbers(event)" type="text" placeholder="Enter amount" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Date : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_date2" name="edit_date2" required type="date" value="'+date+'" placeholder="Select Date" class="form-control pickadate-disable-range">' +
                                '</div>' +
                            '</div>' +
                           
                            '<div class="form-group" id="facility_group" hidden>' +
                                '<label class="col-md-4 control-label">Select Facility : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="edit_facility" required name="edit_facility" class="form-control bootstrap-select" data-header="Choose Facility" data-live-search="true" style="max-height:100px;">'+
                                      ''+facility+''+ 
                                        '</select>' +
                                '</div>' +
                                
                            '</div>' +
                             '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Purpose : </label>' +
                                '<div class="col-md-8">' +
                                    '<textarea id="edit_purpose" name="edit_purpose" required placeholder="Enter purpose" class="form-control">'+purpose+'</textarea>' +
                                    '</div>' +
                            '</div>' +
                        '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Update",
                        className: "btn-success",
                        callback: function () {
                            
                            var amount = $("#edit_amount2").val();
                            var date = $("#edit_date2").val();
                            var cheque_no = $("#edit_cheque_no").val();
                            var fco = $("#edit_fco").val();
                            var gl_code = $("#edit_gl_code").val();
                            var facility =  $("#edit_facility").val();
                            var purpose =  $("#edit_purpose").val();
                            
                            var theme="",header="",message="";
                            
                            if(amount!="" && date!="" && cheque_no!="" && fco!="" && gl_code!=""){
                           var url='save_edited_debit';
                           var form_data = {"debit_id":debit_id,"amount":amount,"date":date,"cheque_no":cheque_no,"fco":fco,"gl_code":gl_code,"facility":facility,"purpose":purpose};
                                $.post(url,form_data , function(output) {
                                    var response = JSON.parse(output).data;
                                    var response_code=response.code;
                                    var response_message=response.message;
                                   message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadAdvances(); 
                                       counter=1;
                                    }
                                    else{
                                       theme = "bg-danger";
                                        header = "Error";
                                        message = response_message;  
                                        counter=1;
                                    }
                                  $.jGrowl(message, {
                                        position: 'top-center',
                                        header: header,
                                        theme: theme
                                   });  
                                 });
                           
                        }
                        else{
                            theme = "bg-danger";
                            header = "Error";
                            message = "Enter all required information.";     
                        $.jGrowl(message, {
                            position: 'top-center',
                            header: header,
                            theme: theme
                        });
                    }
                        }
                    }
                }
            }
        );
        $("#edit_fco").selectpicker();
        $("#edit_gl_code").selectpicker();
        $("#edit_facility").selectpicker();
        $('.pickadate-disable-range').pickadate({
            format: 'yyyy-mm-dd',
                disable: [
                    { from: [year, month, day], to: [3017, 9, 25] }
                ]
            });   
                    }
                });
          }
          
            </script>
</head>

<body style="position:relative; z-index:10; font-size: 10px;">
    
    <!-- Main navbar -->
	<div class="navbar navbar-default header-highlight">

		<div class="navbar-collapse collapse" id="navbar-mobile">
			<ul class="nav navbar-nav">
				<li><a class="sidebar-control sidebar-main-toggle hidden-xs"><i class="icon-paragraph-justify3"></i></a></li>

			</ul>

			<div class="navbar-right">
                            <p class="navbar-text">Hi, <b>
                                    <%
                                        if(session.getAttribute("fullname")!=null){
                                            
                                      out.println(session.getAttribute("fullname").toString());      
                                        }
                                        
                                        %>
                                    
                                </b>!</p>
				<p class="navbar-text"><span class="label bg-success">Online</span></p>
				
				<ul class="nav navbar-nav">				
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-bell2"></i>
							<span class="visible-xs-inline-block position-right">Activity</span>
							<span class="status-mark border-pink-300"></span>
						</a>

						<div class="dropdown-menu dropdown-content">
							<div class="dropdown-content-heading">
								Activity
								<ul class="icons-list">
									<li><a href="#"><i class="icon-menu7"></i></a></li>
								</ul>
							</div>

							<ul class="media-list dropdown-content-body width-350">
								<li class="media">
									<div class="media-left">
										<a href="#" class="btn bg-success-400 btn-rounded btn-icon btn-xs"><i class="icon-mention"></i></a>
									</div>

									<div class="media-body">
										<a href="#">Taylor Swift</a> mentioned you in a post "Angular JS. Tips and tricks"
										<div class="media-annotation">4 minutes ago</div>
									</div>
								</li>

							</ul>
						</div>
					</li>

					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							<i class="icon-bubble8"></i>
							<span class="visible-xs-inline-block position-right">Messages</span>
							<span class="status-mark border-pink-300"></span>
						</a>
						
						<div class="dropdown-menu dropdown-content width-350">
							<div class="dropdown-content-heading">
								Messages
								<ul class="icons-list">
									<li><a href="#"><i class="icon-compose"></i></a></li>
								</ul>
							</div>

							<ul class="media-list dropdown-content-body">
								<li class="media">
									<div class="media-left">
										<img src="assets/images/placeholder.jpg" class="img-circle img-sm" alt="">
										<span class="badge bg-danger-400 media-badge">5</span>
									</div>

									<div class="media-body">
										<a href="#" class="media-heading">
											<span class="text-semibold">James Alexander</span>
											<span class="media-annotation pull-right">04:58</span>
										</a>

										<span class="text-muted">who knows, maybe that would be the best thing for me...</span>
									</div>
								</li>
							</ul>

							<div class="dropdown-content-footer">
								<a href="#" data-popup="tooltip" title="All messages"><i class="icon-menu display-block"></i></a>
							</div>
						</div>
					</li>					
				</ul>
			</div>
		</div>
	</div>
	<!-- /main navbar -->

        <!-- Page container -->
	<div class="page-container">

		<!-- Page content -->
		<div class="page-content">

			<!-- Main sidebar -->
			<div class="sidebar sidebar-main">
			<%@include file="menu/menu.jsp"%>	
			</div>
			<!-- /main sidebar -->


			<!-- Main content -->
			<div class="content-wrapper">

				<!-- Content area -->
				<div class="content">
                                    <!-- Row selector -->
					<div class="panel panel-flat">
						<div class="panel-heading">
                                                    <h5 class="panel-title" id='staff_name' style="text-decoration: underline">Aphia Plus Staffs</h5>
							<div class="heading-elements">
                                                            
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div>
						</div>
                                            <%if(session.getAttribute("staff_status")!=null){
                                              if(session.getAttribute("staff_status").toString().equals("1")){
                                            %>
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_advance();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New Advance</button>
                                            </div>
                                            <%}}%>
                                            
                                            <div  style="height: auto;">
                                                <table id='table' class="table" style="height: auto;">
                                                <thead>
                                                    <tr><th>No.</th><th>Cheque Number</th><th>FCO</th><th>GL Code </th><th>Amount</th><th>Date</th><th>Purpose</th><th>Health Facility</th><th>Status</th><th>Action</th></tr></thead>
                                                
                                                <tbody  style="height: auto;">    
                                                
                                                </tbody>
                                               </table> 
                                            </div>
					</div>
					<!-- /row selector -->
                                        <input type="hidden" value="0" id="tt" name="tt">
                                    <div class="">
                                        
                                        
                                    </div>

                                            <%
                                                Calendar cal = Calendar.getInstance();  
                                                int year= cal.get(Calendar.YEAR); 
                                            %>
					<!-- Footer -->
                                        <div class="footer text-muted">
                                            <p align="center" style="text-align: center;"> &copy <a href="#" title="Version 0.1">Advance Tracking System</a> Aphia Plus | USAID <%=year%></p>
					</div>
					<!-- /footer -->

				</div>
				<!-- /content area -->

			</div>
			<!-- /main content -->

		</div>
		<!-- /page content -->

	</div>
	<!-- /page container -->

</body>
</html>
  