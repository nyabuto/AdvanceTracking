<%-- 
    Document   : Users
    Created on : Oct 3, 2017, 11:34:40 AM
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
                
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>
	<!-- /theme JS files -->
        <script type="text/javascript">
          jQuery(document).ready(function(){
          loadUsers();    
          }); 
          
          
          function loadUsers(){
            $.ajax({
        url:'all_users',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var id,fullname,email,phone,status,status_label,level,level_label,username;
            var dataSet=[];
        
        var code = raw_data.code;
        var message = raw_data.message;
        if(code==1){
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
           var pos=i+1;
            id=fullname=email=phone=status=status_label=level=level_label=username="";
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].fullname!=null){fullname = data[i].fullname;}
            if( data[i].email!=null){email = data[i].email;}
            if( data[i].phone!=null){phone = data[i].phone;}
            if( data[i].username!=null){username = data[i].username;}
            if( data[i].status_label!=null){status_label = data[i].status_label;}
            if( data[i].level_label!=null){level_label = data[i].level_label;}
            if( data[i].level!=null){level = data[i].level;}
            if( data[i].status!=null){status = data[i].status;}
            
            if(level==1){
           level_label=  '<span class="label label-success">'+level_label+'</span> ';  
            }
            else{
            level_label=  '<span class="label label-warning">'+level_label+'</span> ';      
            }
            if(status==1){
           status_label=  '<span class="label label-success">'+status_label+'</span> ';  
            }
            
            else{
            status_label=  '<span class="label label-danger">'+status_label+'</span> ';       
            }
          
           var minSet = [pos,fullname,email,phone,username,level_label,status_label];
           
           dataSet.push(minSet);
            // output the values to data table
        }
        $('#table').dataTable({
            data: dataSet,
            responsive: true,
            className:'',
            columnDefs: []
            
        });
  
        }
        else{
           $.jGrowl(message, {
            position: 'top-center',
            header: 'Access Error.',
            theme: 'bg-danger'
            });    
        }
        }
          
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
							<h5 class="panel-title">User Management Module</h5>
							<div class="heading-elements">
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div>
                                                        
						</div>
                                             <%
                                        if(session.getAttribute("level")!=null){ 
                                        if(session.getAttribute("level").toString().equals("1")){    
                                                            %>
                                                             <div>
                                                            <a href="AddUser.jsp" class="form-control btn-success btn-lg" style="width: 120px; margin-left: 20px;">New User</a>  
                                                            </div>
                                                                    <% } }%>
                                            <div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>Full Name</th><th>Email Address</th><th>Telephone Number</th><th>Username</th><th>Level</th><th>Status</th></tr></thead>
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
  