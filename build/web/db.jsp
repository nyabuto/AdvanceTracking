<%-- 
    Document   : db
    Created on : Sep 27, 2017, 4:18:22 PM
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

	<!-- Theme JS files -->
	<script type="text/javascript" src="assets/js/plugins/forms/styling/uniform.min.js"></script>

	<script type="text/javascript" src="assets/js/core/app.js"></script>
	<script type="text/javascript" src="assets/js/pages/login.js"></script>

	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
	<!-- /theme JS files -->

</head>

<body class="login-container">

	<!-- Page container -->
	<div class="page-container">

		<!-- Page content -->
		<div class="page-content">

			<!-- Main content -->
			<div class="content-wrapper">

				<!-- Content area -->
				<div class="content">

					<!-- Advanced login -->
                                        <form action="dbsetup" style="margin-top: 6%;">
						<div class="panel panel-body login-form">
							<div class="text-center">
                                                            <div class="icon-object border-slate-300 text-slate-300"><img style="width: 5opx; height: 50px;" src="assets/images/aphia/aphia.png"></div>
								<h5 class="content-group">Database Configuration </h5>
							</div>

							<div class="form-group ">
                                                            <table> <tr><td><b>Host Name: </b></td> <td><input id="hostname" type=text required name="hostname" placeholder="e.g localhost:3306" value="localhost:3306" style="margin-left: 20px;" class="form-control" ></td></tr></table>
								
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td><b>Database: </b></td> <td> <input id="database"  type=text required name="database" value="advance_tracking"  class="form-control" placeholder="atracking"></td></tr></table>
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td><b>Username: </b></td> <td> <input id="user"  type=text required name="user" class="form-control" value="root" placeholder="e.g root"  ></td></tr></table>
							</div>

							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td><b>Password: </b></td> <td> <input id="password"  type=password  name="password" placeholder="Password" class="form-control"></td></tr></table>
							</div>

							<div class="form-group login-options">
								
							</div>

							<div class="form-group">
								<button type="submit" class="btn bg-pink-400 btn-block">Login <i class="icon-arrow-right14 position-right"></i></button>
							</div>
                                                        </div>
					</form>
					<!-- /advanced login -->
                                     
                                        
                                        <%
                                          Calendar cal = Calendar.getInstance();  
                                          int year= cal.get(Calendar.YEAR);    
                                            
                                            %>
					<!-- Footer -->
					<div class="footer text-muted text-center">
						<p align="center"> &copy <a href="#" title="Version 0.1">Advance Tracking System</a> Aphia Plus | USAID <%=year%></p>
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
