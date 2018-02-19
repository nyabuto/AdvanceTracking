<%-- 
    Document   : Users
    Created on : Oct 3, 2017, 11:34:40 AM
    Author     : GNyabuto
--%>
<%if(session.getAttribute("level")==null || session.getAttribute("level").toString().equals("2") ){
    response.sendRedirect("../AdvanceTracking");
}%>
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
            var advance,expenses,approve_expenses,rebanking,output="";
            var dataSet=[];
        
        var code = raw_data.code;
        var message = raw_data.message;
        if(code==1){
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
           var pos=i+1;
            id=fullname=email=phone=status=status_label=level=level_label=username="";
            advance=expenses=approve_expenses=rebanking=output="";
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].fullname!=null){fullname = data[i].fullname;}
            if( data[i].email!=null){email = data[i].email;}
            if( data[i].phone!=null){phone = data[i].phone;}
            if( data[i].username!=null){username = data[i].username;}
            if( data[i].status_label!=null){status_label = data[i].status_label;}
            if( data[i].level_label!=null){level_label = data[i].level_label;}
            if( data[i].level!=null){level = data[i].level;}
            if( data[i].status!=null){status = data[i].status;}
            if( data[i].advance!=null){advance = data[i].advance;}
            if( data[i].expenses!=null){expenses = data[i].expenses;}
            if( data[i].approve_expenses!=null){approve_expenses = data[i].approve_expenses;}
            if( data[i].rebanking!=null){rebanking = data[i].rebanking;}
            
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
          
          
          output='<input type="hidden" name="'+pos+'" value="'+id+'" id="'+pos+'">';
          
          var label_advance,label_expenses,label_approve_expenses,label_rebanking;
          
          
          label_advance='<select id="advance_'+pos+'" required name="advance_'+pos+'" onchange=\"advance('+pos+')\" class="form-control" data-header="Advance" data-live-search="true" style="max-height:100px; min-width: 100px;">\n\
                        "'+advance+'"\n\
                        </select>';
          label_expenses='<select id="expenses_'+pos+'" required name="expenses_'+pos+'"  onchange=\"expenses('+pos+')\" class="form-control" data-header="Expenses" data-live-search="true" style="max-height:100px; min-width: 100px;">\n\
                        "'+expenses+'"\n\
                        </select>';
           if(level==1){
          label_approve_expenses='<select id="approve_expenses_'+pos+'" required name="approve_expenses_'+pos+'"  onchange=\"approve_expenses('+pos+')\" class="form-control" data-header="Approve Expenses" data-live-search="true" style="max-height:100px; min-width: 100px;">\n\
                        "'+approve_expenses+'"\n\
                        </select>';
                }
                else{
                label_approve_expenses='<select id="approve_expenses_'+pos+'" required name="approve_expenses_'+pos+'" readonly=\"true\"  onchange=\"approve_expenses('+pos+')\" class="form-control" data-header="Approve Expenses" data-live-search="true" style="max-height:100px; min-width: 100px;">\n\
                        "'+approve_expenses+'"\n\
                        </select>';    
                }
          label_rebanking='<select id="rebanking_'+pos+'" required name="rebanking_'+pos+'"  onchange=\"rebanking('+pos+')\" class="form-control" data-header="Rebanking" data-live-search="true" style="max-height:100px; min-width: 100px;">\n\
                        "'+rebanking+'"\n\
                        </select>';
            
           output+=status_label;
           var minSet = [pos,fullname,email,phone,username,level_label,label_advance,label_expenses,label_approve_expenses,label_rebanking,output];
           
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
        <script>
           function advance(pos){
          var  user_id =  $("#"+pos).val();
          var choice = $("#advance_"+pos).val();
          var lable = 'advance';
          update_access(user_id,choice,lable);
           } 
           
           function expenses(pos){
           var  user_id =  $("#"+pos).val();
           var choice = $("#expenses_"+pos).val();
           var lable = 'expenses';
           update_access(user_id,choice,lable);
           } 
           
           function approve_expenses(pos){
            var  user_id =  $("#"+pos).val();
            var choice = $("#approve_expenses_"+pos).val();
            var lable = 'approve_expenses';
            update_access(user_id,choice,lable);
           } 
           
           function rebanking(pos){
            var  user_id =  $("#"+pos).val();
            var choice = $("#rebanking_"+pos).val();
            var lable = 'rebanking';
            update_access(user_id,choice,lable);
           } 
           
           function update_access(user_id,choice,lable){
               
            var theme="",header="",message="";
         var url='update_access';
            var form_data = {"user_id":user_id,"choice":choice,"label":lable};
                 $.post(url,form_data , function(output) {
                     var response_code=JSON.parse(output).code;
                    var response_message=JSON.parse(output).message;
                    message=response_message;
                     if(response_code==1){
                         theme = "bg-success";
                         header = "Success";
                         message = response_message;
                         //reload data in table
                        loadUsers();
                        
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
							<h5 class="panel-title"><b><u>User Management Module</u></b></h5>
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
                                                    <tr><th>No.</th><th>Full Name</th><th>Email Address</th><th>Telephone Number</th><th>Username</th><th>Level</th><th>Issue Advances</th><th>Enter Expenses</th><th>Approve Expenses</th><th>Process Re-banking</th><th>Status</th></tr></thead>
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
  