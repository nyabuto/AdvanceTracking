<%-- 
    Document   : Activities
    Created on : Feb 7, 2018, 5:50:48 PM
    Author     : GNyabuto
--%>
<%if(session.getAttribute("level")==null){
    response.sendRedirect("../AdvanceTracking");
}%>
<%@page import="java.util.Calendar"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Join Work Plans</title>
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

	<script type="text/javascript" src="assets/js/core/app.js"></script>
	<script type="text/javascript" src="assets/js/pages/datatables_extension_buttons_print.js"></script>
        <script type="text/javascript" src="assets/js/pages/datatables_extension_select.js"></script>
	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
                
	         <!--to manage notifications-->
        <script type="text/javascript" src="assets/js/plugins/notifications/bootbox.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/notifications/noty.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
        
	<script type="text/javascript" src="assets/js/plugins/pickers/daterangepicker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/anytime.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.date.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.time.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/legacy.js"></script>

  <script type="text/javascript" language="en">
   function numbers(evt){
        var charCode=(evt.which) ? evt.which : event.keyCode
        if(charCode > 31 && (charCode < 48 || charCode>57))
        return false;
        return true;
  }
//-->
</script>

	<!-- /theme JS files -->
        <script type="text/javascript">
          jQuery(document).ready(function(){
          loadactivities();    
          }); 
          
          
          function loadactivities(){
            $.ajax({
        url:'load_activities',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var position=0,id,code,description,amount;
             var dataSet=[];
        
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
            position=i+1;
            id=code=description=amount="";
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].code!=null){code = data[i].code;}
            if( data[i].description!=null){description = data[i].description;}
            if( data[i].amount!=null){amount = data[i].amount;}
            
            
            var output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
                output+='<li><button class="btn btn-link" onclick=\"edit('+position+');\" ><i class="icon-list-unordered position-left"></i> Edit</button></li>';
                output+='<li><button class="btn btn-link" onclick=\"deleter('+position+');\" ><i class="icon-list-unordered position-left"></i> Delete</button></li>';
                output+='<input type="hidden" name="'+position+'" value="'+id+'" id="_'+position+'">';
                output+='</ul></li></ul></div>';
                
           var minSet = [position,code,description,amount,output];
           
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
    
    function edit(position){
                var id = $("#_"+position).val();
        //LOAD DATA
        $.ajax({
        url:'load_edit_activity?id='+id,
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var id,code,description,amount,mou_id;
       
        var data=raw_data.data;
        if( data.id!=null){id = data.id;}
        if( data.code!=null){code = data.code;}
        if( data.description!=null){description = data.description;}
        if( data.amount!=null){amount = data.amount;}
        if( data.mou_id!=null){mou_id = data.mou_id;}
           
                     bootbox.dialog({
                title: "<b>Edit Activity Data.</b>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="edit" method="post" class="form-horizontal">' +
                           
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Activity Code <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_code" required name="edit_code" type="text" value="'+code+'" placeholder="Activity Code" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Activity Description <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<textarea id="edit_description" required name="edit_description" placeholder="Activity Decription" class="form-control">'+description+'</textarea>' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Amount <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_amount" onkeypress="return numbers(event)" required name="edit_amount" type="text" value="'+amount+'" placeholder="Amount" class="form-control">' +
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
                            
                            var theme="",header="",message="";
                            
                            var code = $("#edit_code").val();
                            var description = $("#edit_description").val();
                            var amount = $("#edit_amount").val();
                            
                            if(code!=null && code!="" && description!=null && description!="" && amount!=null && amount!="" && mou_id!=null && mou_id!=""){
                            var form_data = {"id":id,"code":code,"description":description,"amount":amount,"mou_id":mou_id};
                            
                           var url='update_activity';
//                           
                            $.post(url,form_data,function(output){
                               var response = JSON.parse(output);
                            var response_code=response.code;
                            var response_message=response.message;
                           message=response_message;
                            if(response_code==1){
                                theme = "bg-success";
                                header = "Success";
                                message = response_message;
                                //reload data in table
                               loadactivities(); 
                            }
                            else{
                               theme = "bg-danger";
                                header = "Error";
                                message = response_message;  
                            }

                          $.jGrowl(message, {
                                position: 'top-center',
                                header: header,
                                theme: theme,
                                life: 3000
                           });   
                             }
                         
                         );
                        }
                        else{
                         theme = "bg-danger";
                                header = "Error";
                                message = "Ensure all input fields are entered.";  
                          $.jGrowl(message, {
                                position: 'top-center',
                                header: header,
                                theme: theme,
                                life: 3000
                           });       
                        }
                        }
                    }
                }
            }
        );
        }
        });
    }
    
    function deleter(position){
              var id = $("#_"+position).val();
          var url='DeleteActivity';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Do you want to delete this Activity?", 
            callback: function(result){
            if(result){
             var form_data = {"id":id};
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
                   loadactivities();  
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
               
            }
               }
           });    
    }
    
    function view_activities(position){
          var credit_id = $("#_"+position).val();
          var url='approve_expenses';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Do you want to approve this expense?", 
            callback: function(result){
            if(result){
             var form_data = {"credit_id":credit_id};
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
                   loadactivities();  
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
               
            }
               }
           });
          }
   
          function new_activities(){
                bootbox.dialog({
                title: "<b>Load Activities</b>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" method="post" enctype="multipart/form-data" class="form-horizontal">' +
                            
                             '<b>Upload Guide: Sample Data</b>'+
                              '<div class="form-group">' +
                                '<img src="assets/images/sample_activity.PNG">' +
                            '</div>' +
                            '<b>Note</b><br>'+
                            '1. The file extension MUST be .xlsx<br>'+
                            '2. Ensure you have added borders on the excel data before uploading.<br>'+
                            '3. The order of columns must be as shown above.<br>'+
                            '4. The first column contains headers followed by data. This must be adhered to.<br>'+
                            '5. These activities belongs to the MoU earlier selected.<br>'+
                            '6. The excel file MUST be closed before it is uploaded to the system.'+
                           '<hr/>'+
                           
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select Excel file <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="file" required name="file" type="file" multiple="multiple" value="" placeholder="Load Excel file" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                         '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Upload Activities",
                        className: "btn-success",
                        callback: function () {
                            
                            var theme="",header="",message="";
                            
                            var data = new FormData($('form')[0]);
                            jQuery.each(jQuery('#file')[0].files, function(i, file) {
                                data.append('file[]', file);
                            });
                            
                             $("#loading").jGrowl("Uploading master list data..", {
                                        position: 'top-center',
                                        header: 'Data Uploading..',
                                        theme: 'bg-success',
                                        closer: false,
                                        life: 99999999
                                   });
//                       alert('data:'+data);
                           var url='upload_activities';
//                           var form_data = {"data":data,"start_date":start_date,"end_date":end_date};
                           
                                    $.ajax({
                                        url: url,
                                        cache: false,
                                        contentType: false,
                                        processData: false,
                                        type: 'POST',
                                        data: data,
                                        success: function(output){
                                       var response = JSON.parse(output);
                                    var response_code=response.code;
                                    var response_message=response.message;
                                   message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadactivities(); 
                                    }
                                    else if(response_code==2){
                                       theme = "bg-warning";
                                        header = "Records updated";
                                        message = response_message;  
                                        loadactivities(); 
                                    }
                                    else{
                                       theme = "bg-danger";
                                        header = "Error";
                                        message = response_message;  
                                    }
                                    //close all notifications
                                    $("#loading").jGrowl('shutdown');
                                    //end
                                  
                                  $.jGrowl(message, {
                                        position: 'top-center',
                                        header: header,
                                        theme: theme,
                                        life: 10000
                                   });   
                                     }
                                 });
                        }
                    }
                }
            }
        );
         // load data to edit
          }    
          
        </script>
</head>

<body>
    
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
                                                    <h5 class="panel-title"><b style="color:black;">Manage Activities for 
                                                            <%if(session.getAttribute("info")!=null){
                                                                out.println(session.getAttribute("info").toString());
                                                            }%>
                                                        </b></h5>
							<div class="heading-elements">
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div>
                                                    
						</div>
                                            <%if(session.getAttribute("level")!=null){
                                              if(session.getAttribute("level").toString().equals("1")){
                                            %>
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_activities();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New Activities</button>
                                            </div>
                                            <%}
                                            }
                                            %>
                                            <div>
                                                <div id="loading" style="margin-left: 30%;">
                                                        </div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>Activity Code</th><th>Activity Description</th><th>Amount</th><th>Action</th></tr></thead>
                                                <tbody>    
                                                </tbody>
                                               </table> 
                                            </div>
					</div>
					<!-- /row selector -->

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
  