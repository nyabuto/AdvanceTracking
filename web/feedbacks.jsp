<%-- 
    Document   : feedbacks
    Created on : Apr 10, 2018, 11:47:10 AM
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
	<title>Feedback Module</title>
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

	<!-- /theme JS files -->
        <script type="text/javascript">
          jQuery(document).ready(function(){
          loadFeedbacks(); 
         <%if(session.getAttribute("new_notifications")!=null){if(session.getAttribute("new_notifications").toString().contains(",1,")){%>
          update_notification();
          <%}}%>
          }); 
          
          function loadFeedbacks(){
            $.ajax({
        url:'loadfeedbacks',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var id,description,fullname,email,phone,response,reported_on,status,status_label,solved_on,position,is_dev,turnaroundtime,duration_pending;
             var dataSet=[];
        
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
            position=i+1;
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].description!=null){description = data[i].description;}
            if( data[i].fullname!=null){fullname = data[i].fullname;}
            if( data[i].email!=null){email = data[i].email;}
            if( data[i].phone!=null){phone = data[i].phone;}
            if( data[i].response!=null){response = data[i].response;}
            if( data[i].reported_on!=null){reported_on = data[i].reported_on;}
            if( data[i].status!=null){status = data[i].status;}
            if( data[i].solved_on!=null){solved_on = data[i].solved_on;}
            if( data[i].is_dev!=null){is_dev = data[i].is_dev;}
            if( data[i].turnaroundtime!=null){turnaroundtime = data[i].turnaroundtime;}
            if( data[i].duration_pending!=null){duration_pending = data[i].duration_pending;}
            
            if(status==1){
                status_label='<span class="label label-success">Solved</span>';
            }
            else{
                status_label='<span class="label label-danger">Pending</span> ';
            }
            var output="No action";
            if(is_dev==1 && status==0){
            output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
                output+='<li><button class="btn btn-link" onclick=\"resolve('+position+');\" ><i class="icon-list-unordered position-left"></i> Resolve Issue</button></li>'; 
                output+='<input type="hidden" name="'+position+'" value="'+id+'" id="_'+position+'">';
                output+='</ul></li></ul></div>';
            }
            
            
            
           var minSet = [position,description,fullname,email,phone,response,reported_on,status_label,solved_on,turnaroundtime,duration_pending,output];
           
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
       
            function deleter(position){
          var county_id = $("#_"+position).val();
          var url='delete_county';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Are you sure you want to delete this county?", 
            callback: function(result){
            if(result){
             var form_data = {"county_id":county_id};
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
                   loadCounties();  
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
          
        function resolve(pos){
            var id = $("#_"+pos).val();
 
                bootbox.dialog({
                title: "<div style=\"color:black; font-weight: 900; text-decoration: underline; \">Confirm the issue has been solved</div>",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Developer Comments<b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<textarea id="response" required name="response" placeholder="Enter Developer comment" class="form-control"></textarea>' +
                                '</div>' +
                            '</div>' +
                            
                             
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">The issue is solved<b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input type="checkbox" id="resolved" required name="resolved" checked onclick="return false;" >' +
                                '</div>' +
                            '</div>' +
                            
                         '</form>' +
                    '</div>' +
                    '</div>',
                buttons: {
                    success: {
                        label: "Resolve",
                        className: "btn-success",
                        callback: function () {
                            
                            var response = $("#response").val();
                           
                            var theme="",header="",message="";
                            
                            if( response!="" && response!=null){
                           var url='save_response';
                           var form_data = {"response":response,"id":id};
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
                                       loadFeedbacks(); 
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
                            message = "Enter comments as a developer.";     
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
            
        }
          
          function new_comment(){
              bootbox.dialog({
                title: "<div style=\"color:black; font-weight: 900; text-decoration: underline; \">New Feedback</div>",
                message: '<div class="row">' +
                    '<div class="col-md-12">' +
                        '<form id="new_comment" class="form-horizontal">' +

                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Description <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<textarea id="description" required name="description" style="height:200px;" placeholder="Enter your feedback" class="form-control"></textarea>' +
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
                            
                            var description = $("#description").val();
                             var theme="",header="",message="";
                            
                            if(description!="" && description!=null){
                           var url='save_feedback';
                           var form_data = {"description":description};
                                $.post(url,form_data , function(output) {
                                    var response = JSON.parse(output).data;
                                    var response_code=response.code;
                                    var response_message=response.message;
                                  var  message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadFeedbacks(); 
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
                            message = "Enter feedback.";     
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
         // load data to edit
          }     
          
          
          function update_notification(){
            var url='save_viewed_notification';
            var noti_num = 1;
            var form_data = {"noti_num":noti_num};
                 $.post(url,form_data , function(output) {

                 });  
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
							<h5 class="panel-title" style="color:black; font-weight: 900; text-decoration: underline; ">Feedback Reporting & Management Module</h5>
							<div class="heading-elements">
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div>
						</div>
                                            <%if(session.getAttribute("is_dev")!=null){
                                              if(!session.getAttribute("is_dev").toString().equals("1")){
                                            %>
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_comment();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New Feedback</button>
                                            </div>
                                            <%}
                                            }
                                            %>
                                            <div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>Description</th><th>Reported By</th><th>Email</th><th>Phone</th><th>Developer Comment</th><th>Reported On</th><th>Status</th><th>Solved On</th><th>Turn Around Time</th><th>Pending Duration</th><th>Action</th></tr></thead>
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
  
