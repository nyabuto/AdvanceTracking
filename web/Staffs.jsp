<%-- 
    Document   : Template
    Created on : Sep 26, 2017, 4:05:51 PM
    Author     : GNyabuto
--%>
<%@page import="java.util.Calendar"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Advance Tracking System</title>
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
	<!-- /theme JS files -->
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
          loadstaffs();    
          }); 
          
          
          function loadstaffs(){
            $.ajax({
        url:'loadStaffs',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var staff_no,fullname,email,phone,status,status_label,output,pos;
           
        var data=raw_data.data;
        var dataSet=[];
        var accounting="",edit="";
        for (var i=0; i<data.length;i++){
            pos=i+1;
            staff_no=fullname=email=phone=status=status_label="";
            if( data[i].staffno!=null){staff_no = data[i].staffno;}
            if( data[i].fullname!=null){fullname = data[i].fullname;}
            if( data[i].email!=null){email = data[i].email;}
            if( data[i].phone!=null){phone = data[i].phone;}
            if( data[i].status!=null){status = data[i].status;}
            
            if(status==1){
           status_label=  '<span class="label label-success">Active</span> '; 
           accounting='<li><a onclick=\"de_activate('+pos+');\" ><i class="icon-plus3 position-left"></i> De-activate</a></li>';
            }
            else{
            status_label=  '<span class="label label-danger">Inactive</span> '; 
            accounting='<li><a onclick=\"activate('+pos+');\" ><i class="icon-plus3 position-left"></i> Activate</a></li>';
            }
            edit='<li><a onclick=\"edit('+pos+');\" ><i class="icon-pencil3 position-left"></i> Edit</a></li>';
            <%if(session.getAttribute("level")!=null){if(!session.getAttribute("level").toString().equals("1")){%>
              accounting="";
              edit="";
              <%}}%>          
            // output the values to data table
         output='<div class="text-right"><ul class="icons-list"><li class="dropdown">\n\
           <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a>\n\
           <ul class="dropdown-menu dropdown-menu-right">\n\
           <li><a href="staff_session?src=Advances&&axn='+staff_no+'"><i class="icon-file-pdf"></i> Manage Advances</a></li>\n\
           '+edit+'\
           '+accounting+'\
           </ul></li></ul></div>';
            
         output+='<input type="hidden" name="'+pos+'" value="'+staff_no+'" id="'+pos+'">';
         
           var minSet = [pos,fullname,email,phone,status_label,output];
           
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
          
                function new_staff(){
               // pop up
          bootbox.dialog({
                title: "New Staff.",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_staff" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Name <font color="red">*</font> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="staff_name" required name="staff_name" type="text" placeholder="Enter Staff name" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Email: </label>' +
                                '<div class="col-md-8">' +
                                 '<input id="email" required name="email" type="email" placeholder="Enter Email" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Phone No. <font color="red">*</font> : </label>' +
                                '<div class="col-md-8">' +
                                 '<input id="phone" required name="phone"  onkeypress="return numbers(event)" type="text" max-length="10" placeholder="Enter Phone Number" class="form-control">' +
                                '</div>' +
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
                            var staff_name = $("#staff_name").val();
                            var email = $("#email").val();
                            var phone = $("#phone").val();
                            
                            var theme="",header="",message="";
                            
                            if(staff_name!="" && phone!=""){
                           var url='save_staff';
                           var form_data = {"staff_name":staff_name,"email":email,"phone":phone};
                                $.post(url,form_data , function(output) {
                                    var response_code=JSON.parse(output).code;
                                   var response_message=JSON.parse(output).message;
                                   message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadstaffs();
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
            }
            
       function de_activate(pos){
        var staff_no=$("#"+pos).val();
        activator(staff_no,0);
       }  
       
       function activate(pos){
        var staff_no=$("#"+pos).val();   
        activator(staff_no,1);
       }
       
       function activator(staff_no,status){
        var theme="",header="",message="";
         var url='activate_staff';
            var form_data = {"staff_no":staff_no,"status":status};
                 $.post(url,form_data , function(output) {
                     var response_code=JSON.parse(output).code;
                    var response_message=JSON.parse(output).message;
                    message=response_message;
                     if(response_code==1){
                         theme = "bg-success";
                         header = "Success";
                         message = response_message;
                         //reload data in table
                        loadstaffs();
                        
                         $.jGrowl(message, {
                         position: 'top-center',
                         header: header,
                         theme: theme
                    });
                     }
                     else{
                        theme = "bg-danger";
                         header = "Error";
                         message = response_message;  
                          $.jGrowl(message, {
                         position: 'top-center',
                         header: header,
                         theme: theme
                    });
                     }
                    
                  });
        
       }
       
       function edit(pos){
        var staff_no=$("#"+pos).val();    
        var url='get_staff_details';
            var form_data = {"staff_no":staff_no};
                 $.post(url,form_data , function(output) {
                    var fullname=JSON.parse(output).fullname;
                    var email=JSON.parse(output).email;
                    var phone=JSON.parse(output).phone;
                    
                             bootbox.dialog({
                title: "Edit Staff Details:",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_staff" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Name <font color="red">*</font> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="staff_name" required name="staff_name" value="'+fullname+'" type="text" placeholder="Enter Staff name" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Email: </label>' +
                                '<div class="col-md-8">' +
                                 '<input id="email" required name="email" type="email" value="'+email+'" placeholder="Enter Email" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Staff Phone No. <font color="red">*</font> : </label>' +
                                '<div class="col-md-8">' +
                                 '<input id="phone" required name="phone" value="'+phone+'"  onkeypress="return numbers(event)" type="text" max-length="10" placeholder="Enter Phone Number" class="form-control">' +
                                '</div>' +
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
                            var staff_name = $("#staff_name").val();
                            var email = $("#email").val();
                            var phone = $("#phone").val();
                            
                            var theme="",header="",message="";
                            
                            if(staff_name!="" && phone!=""){
                           var url='update_staff';
                           var form_data = {"staff_no":staff_no,"staff_name":staff_name,"email":email,"phone":phone};
                                $.post(url,form_data , function(output) {
                                    var response_code=JSON.parse(output).code;
                                   var response_message=JSON.parse(output).message;
                                   message=response_message;
                                    if(response_code==1){
                                        theme = "bg-success";
                                        header = "Success";
                                        message = response_message;
                                        //reload data in table
                                       loadstaffs();
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
							<h5 class="panel-title">Aphia Plus Staffs</h5>
							<div class="heading-elements">
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div></div>
                                              <%if(session.getAttribute("level")!=null){
                                              if(session.getAttribute("level").toString().equals("1")){
                                            %>
                                            <div>
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_staff();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New Staff</button>
                                            </div>
                                            <%}}%>
                                            <div>
                                                <table id='table' class="table" style="height: auto;">
                                                <thead>
                                                    <tr><th>No.</th><th>Full Name</th><th>Email Address</th><th>Telephone Number</th><th>Status</th><th>Action</th></tr></thead>
                                                 <tbody  style="height: auto;">    
                                                
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
  