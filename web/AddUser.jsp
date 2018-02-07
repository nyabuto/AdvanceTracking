<%-- 
    Document   : AddUser
    Created on : Sep 28, 2017, 11:03:58 AM
    Author     : GNyabuto
--%>
<%@page import="java.util.Calendar"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Add New User</title>
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
	<script type="text/javascript" src="assets/js/plugins/visualization/d3/d3.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/visualization/d3/d3_tooltip.js"></script>
	<script type="text/javascript" src="assets/js/plugins/forms/styling/switchery.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/forms/styling/uniform.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_multiselect.js"></script>
	<script type="text/javascript" src="assets/js/plugins/ui/moment/moment.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/daterangepicker.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/select2.min.js"></script>

        <script type="text/javascript" src="assets/js/plugins/forms/styling/uniform.min.js"></script>
	<script type="text/javascript" src="assets/js/core/app.js"></script>
        <script type="text/javascript" src="assets/js/pages/login.js"></script>
	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
        
        	<!-- Theme JS files -->
	<script type="text/javascript" src="assets/js/plugins/forms/styling/uniform.min.js"></script>
        
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>
	<!-- /theme JS files -->
    <script type="text/javascript">
    
            function checkPasswords() {
                var password = document.getElementById('password');
                var conf_password = document.getElementById('conf_password');

                if (password.value != conf_password.value) {
                    conf_password.setCustomValidity('Passwords do not match');
                } else {
                    conf_password.setCustomValidity('');
                }
                
          
        
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
                                    <!--//content values here-->
                                    <div class="" style="width: 500px; margin-left: 25%;">
                                      <!-- Advanced login -->
                                        <form action="AddUser" style="margin-top: 20%;">
						<div class="panel panel-body login-form">
							<div class="text-center">
                                                            <!--<div class="icon-object border-slate-300 text-slate-300"><img style="width: 5opx; height: 50px;" src="assets/images/aphia/aphia.png"></div>-->
								<h5 class="content-group">New User Registration </h5>
							</div>

							<div class="form-group ">
                                                            <table> <tr><td style="width: 150px;"><b>Full Name : <font color="red">*</font></b></td> <td><input id="fullname" type=text required name="fullname" maxlength="50" placeholder="User fullname" value="" style="margin-left: 20px;" class="form-control" ></td></tr></table>
								
							</div>
							<div class="form-group has-feedback has-feedback-left">
                                                            <table> <tr><td style="width: 150px;"><b>Phone : <font color="red">*</font></b></td> <td> <input id="phone"  type=number maxlength="10" required name="phone" value=""  class="form-control" placeholder="072133....."></td></tr></table>
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td style="width: 150px;"><b>Email : <font color="red">*</font></b></td> <td> <input id="email"  type=email required maxlength="30" name="email" class="form-control" value="" placeholder="email address"  ></td></tr></table>
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td style="width: 150px;"><b>Username : <font color="red">*</font></b></td> <td> <input id="username"  type=text required maxlength="30" name="username" class="form-control" value="" placeholder="username"  ></td></tr></table>
							</div>

							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td style="width: 150px;"><b>Password : <font color="red">*</font></b></td> <td> <input id="password"  type=password maxlength="30"  name="pass" placeholder="Password" oninput="checkPasswords()" class="form-control"></td></tr></table>
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table> <tr><td style="width: 150px;"><b>Repeat Password : <font color="red">*</font></b></td> <td> <input id="conf_password" maxlength="30"  type=password  name="pass2" oninput="checkPasswords()" placeholder="Password" class="form-control"></td></tr></table>
							</div>
							<div class="form-group has-feedback has-feedback-left">
								<table>
                                                                    <tr><td style="width: 150px;"><b>User Type : <font color="red">*</font></b></td> <td> 
                                                                     <select id="user_level" required name="user_level" class="form-control bootstrap-select" data-header="Choose User Level" data-live-search="true" style="max-height:100px; min-width: 200px;">
                                                                     <option value="">Loading users  </option>
                                                                     </select>  
                                                                        </td></tr>
                                                                </table>
							</div>
                                                    
							<div class="form-group has-feedback has-feedback-left">
								<table>
                                                                    <tr><td style="width: 150px;"><b>Finance Staff? <font color="red">*</font></b></td> <td> 
                                                                     <select id="is_finance" required name="is_finance" class="form-control bootstrap-select" data-header="Is a finance staff" data-live-search="true" style="max-height:100px; min-width: 200px;">
                                                                     <option value="1">Yes</option>
                                                                     <option value="0">No</option>
                                                                     </select>  
                                                                        </td></tr>
                                                                </table>
							</div>

							<div class="form-group login-options">
								
							</div>

							<div class="form-group">
								<button type="submit" class="btn bg-pink-400 btn-block">Register <i class="icon-arrow-right14 position-right"></i></button>
							</div>
                                                        </div>
					</form>
					<!-- /advanced login -->  
                                        
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
                                           
                                        <%if(session.getAttribute("adduser")!=null){
                                        String mess =session.getAttribute("adduser").toString();
                                        %>
                                        <script type="text/javascript">
                                           $.jGrowl('<%=mess%>', {
                                                position: 'center',
                                                header: 'Register Information.',
                                                theme: 'bg-info'
                                            });  
                                            </script>
                                            <% session.removeAttribute("adduser");}%>
</body>
<script>
 jQuery(document).ready(function(){
       var url='load_levels';
       var user_levels="";
            $.post(url,'' , function(output) {
               var data = JSON.parse(output).data;
               
               user_levels = '<option value=\"\">Choose Option</option>';
               for (var i=0; i<data.length;i++){   
               var id = data[i].id;
               var name = data[i].name;
               
               user_levels+='<option value=\"'+id+'\">'+name+'</option>';
               // input them to the respectie elements
           }
           
           $("#user_level").html(user_levels);
           $("#user_level").select2({ width: 'element' });
           $("#user_level").selectpicker();  
           });
           
           $("#is_finance").select2({ width: 'element' });
           $("#is_finance").selectpicker();  
          });   
</script>
</html>
