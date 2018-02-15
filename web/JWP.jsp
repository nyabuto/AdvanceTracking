<%-- 
    Document   : JWP
    Created on : Feb 7, 2018, 8:46:47 AM
    Author     : GNyabuto
--%>

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

        <link rel="stylesheet" href="assets/dates/css/pickmeup.css" type="text/css" />
        
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

        <script type="text/javascript" src="assets/dates/js/pickmeup.js"></script>
	<!-- /theme JS files -->
        <script type="text/javascript">
            
            addEventListener('DOMContentLoaded', function () {
	pickmeup('.single', {
		flat : true
	});
	pickmeup('.multiple', {
		flat : true,
		mode : 'multiple'
	});
	pickmeup('#end_date', {
		flat : true,
		mode : 'range'
	});
	pickmeup('.range', {
		flat : true,
		mode : 'range'
	});
		
});

          jQuery(document).ready(function(){
          loadJWPs();    
          }); 
          
          
          function loadJWPs(){
            $.ajax({
        url:'loadMOUs',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var position=0,id,mou_no,mou,unique_code,approved_budget,start_date,end_date;
             var dataSet=[];
        
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
            position=i+1;
            id=mou_no=mou=unique_code=approved_budget=start_date=end_date="";
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].mou_no!=null){mou_no = data[i].mou_no;}
            if( data[i].mou!=null){mou = data[i].mou;}
            if( data[i].unique_code!=null){unique_code = data[i].unique_code;}
            if( data[i].approved_budget!=null){approved_budget = data[i].approved_budget;}
            if( data[i].start_date!=null){start_date = data[i].start_date;}
            if( data[i].end_date!=null){end_date = data[i].end_date;}
            
            var info=(mou+" from : <u>"+start_date+"</u> to <u>"+end_date+"</u>").replace(" ","%20");
            var output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
                output+='<li><a class="btn btn-link" href=\"activity_sess?mouno='+id+'&&info='+info+'\" ><i class="icon-list-unordered position-left"></i>View Activities</a></li>';
                output+='<li><button class="btn btn-link" onclick=\"edit('+position+')\" ><i class="icon-list-unordered position-left"></i> Edit MoU</button></li>';
                output+='<li><button class="btn btn-link" onclick=\"deleter('+position+')\" ><i class="icon-list-unordered position-left"></i> Delete MoU</button></li>';
                output+='<input type="hidden" name="'+position+'" value="'+id+'" id="_'+position+'">';
                output+='</ul></li></ul></div>';
                
           var minSet = [position,mou_no,mou,unique_code,start_date,end_date,approved_budget,output];
           
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
        var mou_id = $("#_"+position).val();
        //LOAD DATA
        $.ajax({
        url:'load_edit_mous?mou_id='+mou_id,
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var id,mou_no,mou,unique_code,approved_budget;
       
        var data=raw_data.data;
        if( data.id!=null){id = data.id;}
        if( data.mou_no!=null){mou_no = data.mou_no;}
        if( data.mou!=null){mou = data.mou;}
        if( data.unique_code!=null){unique_code = data.unique_code;}
        if( data.approved_budget!=null){approved_budget = data.approved_budget;}
           
                     bootbox.dialog({
                title: "<b>Edit Master List Data</b>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="edit" method="post" class="form-horizontal">' +
                           
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">MoU No <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_mou_no" required name="edit_mou_no" type="text" value="'+mou_no+'" placeholder="MOU No" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">MoU CHMT,SCHMT,Facility HMTs <b style=\"color:red\"></b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_mou" required name="edit_mou" type="text" value="'+mou+'" disabled placeholder="MoU" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Unique Code <b style=\"color:red\"></b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_unique_code" required name="edit_unique_code" type="text" disabled value="'+unique_code+'" placeholder="Unique Code" class="form-control">' +
                                '</div>' +
                            '</div>' +
                              
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Approved Budget <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="edit_approved_budget" required name="edit_approved_budget" type="text" value="'+approved_budget+'" placeholder="Approved Budget" class="form-control">' +
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
                            
                            var mou_no = $("#edit_mou_no").val();
                            var mou = $("#edit_mou").val();
                            var unique_code = $("#edit_unique_code").val();
                            var approved_budget = $("#edit_approved_budget").val();
                            if(mou_no!=null && mou_no!="" && mou!=null && mou!="" && unique_code!=null && unique_code!="" && approved_budget!=null && approved_budget!=""){
                            var form_data = {"id":id,"mou_no":mou_no,"mou":mou,"unique_code":unique_code,"approved_budget":approved_budget};
                            
                           var url='update_mou';
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
                               loadJWPs(); 
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
                                message = "Enter data on all required fields.";
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
          var mou_id = $("#_"+position).val();
          var url='DeleteMOU';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Do you want to delete this MOU?", 
            callback: function(result){
            if(result){
             var form_data = {"mou_id":mou_id};
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
                   loadJWPs();  
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
   
          function new_jwp(){
                bootbox.dialog({
                title: "<b>Load Master List</b>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" method="post" enctype="multipart/form-data" class="form-horizontal">' +
                           
                             '<b>Upload Guide: Sample Data</b>'+
                              '<div class="form-group">' +
                                '<img src="assets/images/sample_mou.PNG">' +
                            '</div>' +
                            '<b>Note</b><br>'+
                            '1. The file extension MUST be .xlsx<br>'+
                            '2. Ensure you have added borders on the excel data before uploading.<br>'+
                            '3. Unique codes or MOU names must be the same as they exist in the system.<br>'+
                            '4. The order of columns must be as shown above.<br>'+
                            '5. The first column contains headers followed by data. This must be adhered to.'+
                           '<hr/>'+
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select Excel file <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="file" required name="file" type="file" multiple="multiple" value="" placeholder="Load Excel file" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Start Date <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="start_date" required name="start_date" type="date" value="" placeholder="Start Date" class="form-control">' +
                                '</div>' +
                            '</div>' +
                              
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">End Date <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="end_date" required name="end_date" type="date" value="" placeholder="End Date" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                         '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Upload Master List",
                        className: "btn-success",
                        callback: function () {
                            
                            var theme="",header="",message="";
                            var start_date,end_date;
                            start_date = $("#start_date").val();
                            end_date = $("#end_date").val();
                            if(start_date!="" && start_date!=null && end_date!="" && end_date!=null){
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
                           var url='upload_consolidated';
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
                                       loadJWPs(); 
                                    }
                                    else if(response_code==2){
                                       theme = "bg-warning";
                                        header = "Records updated";
                                        message = response_message;  
                                        loadJWPs(); 
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
                        else{
                        theme = "bg-danger";
                        header = "Error";
                        message = "Ensure start and end dates are selected."; 
                        
                        $.jGrowl(message, {
                            position: 'top-center',
                            header: header,
                            theme: theme,
                            life: 10000
                       });
                                   
                        }
                    }
                    }
                }
            }
        );
         // load data to edit
          } 
          
          function new_all_activities(){
                bootbox.dialog({
                title: "<b>Load All activities.</b>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="all_activities" method="post" enctype="multipart/form-data" class="form-horizontal">' +
                           
                             '<b>Upload Guide: Sample Data</b>'+
                              '<div class="form-group">' +
                                '<img src="assets/images/sample_all_activities.PNG">' +
                            '</div>' +
                            '<b>Note</b><br>'+
                            '1. The file extension MUST be .xlsx<br>'+
                            '2. Ensure you have added borders on the excel data before uploading.<br>'+
                            '3. Unique codes or MOU names must be the same as they exist in the system.<br>'+
                            '4. The order of columns must be as shown above.<br>'+
                            '5. The first column contains headers followed by data. This must be adhered to.'+
                           '<hr/>'+
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Select Excel file <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="files" required name="files" type="file" multiple="multiple" value="" placeholder="Load Excel file" class="form-control">' +
                                '</div>' +
                            '</div>' +
                           
                         '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Upload All Activities",
                        className: "btn-success",
                        callback: function () {
                            
                            var theme="",header="",message="";
                            
                            var data = new FormData($('#all_activities')[0]);
                            jQuery.each(jQuery('#files')[0].files, function(i, file) {
                                data.append('file[]', file);
                            });
                            
                             $("#loading").jGrowl("Uploading activities..", {
                                        position: 'top-center',
                                        header: 'Data Uploading..',
                                        theme: 'bg-success',
                                        closer: false,
                                        life: 99999999
                                   });
//                       alert('data:'+data);
                           var url='upload_all_activities';
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
                                       loadJWPs(); 
                                    }
                                    else if(response_code==2){
                                       theme = "bg-warning";
                                        header = "Records updated";
                                        message = response_message;  
                                        loadJWPs(); 
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
                                        life: 9999999
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
                                                    <h5 class="panel-title"><b style="color:black;">Manage Joint Work Plans</b></h5>
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
                                            <div style="clear: both">
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_jwp();" style="margin-left: 1%; margin-bottom: 1%; float: left;"><i class="icon-plus3 position-left"></i> New Master List</button>
                                            </div>
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_all_activities();" style="margin-left: 10%; margin-bottom: 1%; float: left;"><i class="icon-plus3 position-left"></i> Upload Consolidated Activities</button>
                                            </div>
                                            </div>
                                            <%}
                                            }
                                            %>
                                            <div>
                                                <div id="loading" style="margin-left: 30%;">
                                                        </div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>MOU #</th><th>MOUs CHMTs, SCHMTs, Facility HMTs</th><th>Unique Code</th><th>Start Date</th><th>End Date</th><th>Approved Budget</th><th>Action</th></tr></thead>
                                                <tbody>    
                                                </tbody>
                                               </table> 
                                            </div>
					</div>
					<!-- /row selector -->

                                    <div>
                                       
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
  