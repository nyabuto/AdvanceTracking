<%-- 
    Document   : index
    Created on : Sep 26, 2017, 3:53:14 PM
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
        <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Dancing+Script" />
        <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Merienda+One" />
	<!-- /global stylesheets -->

	<!-- Core JS files -->
	<script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
	<script type="text/javascript" src="assets/js/core/libraries/jquery.min.js"></script>
	<script type="text/javascript" src="assets/js/core/libraries/bootstrap.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/loaders/blockui.min.js"></script>
	<!-- /core JS files -->

	<!-- Theme JS files -->
	<script type="text/javascript" src="assets/js/plugins/forms/styling/uniform.min.js"></script>
        
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>

	<script type="text/javascript" src="assets/js/core/app.js"></script>
	<script type="text/javascript" src="assets/js/pages/login.js"></script>

	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
	<!-- /theme JS files -->
<style>
    .title{
        font-family: Merienda One;
	font-size: 15px;
	font-style: normal;
	font-variant: normal;
	font-weight: 900;
	line-height: 26.4px;
    }    
    
    
</style>
       
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
                                        <form action="login" style="margin-top: 2%;">
                                            
                                            <div class="panel panel-body login-form">
							<div class="text-center">
                                                            <div class="title">Advance & Expenses Tracking System</div>
                                                            <br>
                                                            <div class="icon-object border-slate-300 text-slate-300"><img style="height: 50px;" src="assets/images/aphia/aphia.png"></div>
								<h5 class="content-group">Login to your account </h5>
							</div>

							<div class="form-group has-feedback has-feedback-left">
                                                            <input type="text" class="form-control" id="username" name="username" placeholder="Username">
								<div class="form-control-feedback">
									<i class="icon-user text-muted"></i>
								</div>
							</div>

							<div class="form-group has-feedback has-feedback-left">
                                                            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
								<div class="form-control-feedback">
									<i class="icon-lock2 text-muted"></i>
								</div>
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
                                        <%if(session.getAttribute("login")!=null){
                                        String mess =session.getAttribute("login").toString();
                                        %>
                                        <script type="text/javascript">
                                           $.jGrowl('<%=mess%>', {
                                                position: 'center',
                                                header: 'Login Error.',
                                                theme: 'bg-danger'
                                            });  
                                            </script>
                                            <% session.removeAttribute("login");}%>
                                        
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
