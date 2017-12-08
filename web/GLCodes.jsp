<%-- 
    Document   : GLCodes
    Created on : Oct 3, 2017, 12:47:06 PM
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
          loadGLCodes();    
          }); 
          
          
          function loadGLCodes(){
            $.ajax({
        url:'all_glcodes',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var code,account,account_name,position,status,output,status_value;
             var dataSet=[];
        
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
            position=i+1;
            code=account=account_name="";
            if( data[i].code!=null){code = data[i].code;}
            if( data[i].account!=null){account = data[i].account;}
            if( data[i].account_name!=null){account_name = data[i].account_name;}
            if( data[i].status!=null){status = data[i].status;}
           
           if(status==1){
             status_value = "<span style=\"width:150px; height: 30px; text-align:center;\" font-size:10px; class=\"label label-success\">Active</span> ";
            }
            else{
             status_value= "<span style=\"width:150px; height: 30px; text-align:center;\" font-size:10px;  class=\"label label-danger\">Inactive</span> ";   
            }
           
           var  output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
                output+='<li><button class="btn btn-link" onclick=\"edit_gl_code('+position+');\" ><i class="icon-list-unordered position-left"></i> Edit</button></li>'; 
                if(status==1){
                  output+='<li><button class="btn btn-link" onclick=\"de_activate('+position+');\" ><i class="icon-list-unordered position-left"></i> De-activate</button></li>';  
                }
                else{
                    output+='<li><button class="btn btn-link" onclick=\"activate('+position+');\" ><i class="icon-list-unordered position-left"></i> Activate</button></li>';
                }
                output+='<li><button class="btn btn-link" onclick=\"deleter('+position+');\" ><i class="icon-list-unordered position-left"></i> Delete</button></li>';
                output+='<input type="hidden" name="'+position+'" value="'+code+'" id="_'+position+'">';
                output+='</ul></li></ul></div>';
           
           var minSet = [position,code,account,account_name,status_value,output];
           
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
        </script>
        <script type="text/javascript">
         
        function activate(position){
            var gl_code = $("#_"+position).val();
            var status = 1;
            activation(gl_code,status);
        }
        function de_activate(position){
            var gl_code = $("#_"+position).val();
            var status = 0;
            activation(gl_code,status);
        }
        function activation(gl_code,status){
        var url='update_gl_code_status';
        
        var theme="",header="",message="";
         var form_data = {"gl_code":gl_code,"status":status};
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
                   loadGLCodes(); 
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
        
        function deleter(position){
          var gl_code = $("#_"+position).val();
          var url='delete_gl_code';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Are you sure you want to delete GL Code "+gl_code+"?", 
            callback: function(result){
            if(result){
             var form_data = {"gl_code":gl_code};
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
                   loadGLCodes(); 
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
           
    
    
          function edit_gl_code(position){
          
          var gl_code = $("#_"+position).val();
              //load data
          var form_data = {"gl_code":gl_code};
          
          var url='load_edit_gl_code';
            $.post(url,form_data , function(output) {
               var data = JSON.parse(output).data;
               for (var i=0; i<data.length;i++){   
               var code = data.code;
               var account = data.account;
               var account_name = data[i].account_name;
               var gl_code_type = data[i].types;
               
               // input them to the respectie elements
           } 
           // pop up
                   bootbox.dialog({
                title: "Update GL Code",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Code <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="code" required name="code" onkeypress="return numbers(event)" disabled value="'+code+'" type="text" placeholder="Enter GL Code" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Account <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="account" required name="account" type="text" value="'+account+'" placeholder="Enter GL Account" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Account Name <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="account_name" required name="account_name" type="text" value="'+account_name+'" placeholder="Enter GL Account Name" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Code Type <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="gl_code_type" required name="gl_code_type" class="form-control bootstrap-select" data-header="Choose GL Code Type" data-live-search="true" style="max-height:100px;">' +
                                   ''+gl_code_type+''+
                                    '</select>' +
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
                            
                            var code = $("#code").val();
                            var account = $("#account").val();
                            var account_name = $("#account_name").val();
                            var gl_code_type = $("#gl_code_type").val();
                           
                            var theme="",header="",message="";
                            
                            if(code!="" && account!="" && account_name!="" && gl_code_type!="" && code!=null && account!=null && account_name!=null && gl_code_type!=null){
                           var url='update_gl_code';
                           var form_data = {"code":code,"account":account,"account_name":account_name,"gl_code_type":gl_code_type};
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
                                       loadGLCodes(); 
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
        $("#gl_code_type").selectpicker();       
            });
          // load data to edit
          }  
            
    
    
          function new_gl_code(){
              //load types
          var url='load_gl_code_types';
            $.post(url,'' , function(output) {
               var data = JSON.parse(output).data;
               var gl_code_type="";
               for (var i=0; i<data.length;i++){   
               var id = data[i].id;
               var name = data[i].name;
               
               gl_code_type+='<option value=\"'+id+'\">'+name+'</option>';
               // input them to the respectie elements
           } 
           // pop up
                   bootbox.dialog({
                title: "New GL Code",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Code <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="code" required name="code" onkeypress="return numbers(event)" type="text" value="" placeholder="Enter GL Code" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Account <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="account" required name="account" type="text" value="" placeholder="Enter GL Account" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Account Name <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="account_name" required name="account_name" type="text" value="" placeholder="Enter GL Account Name" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">GL Code Type <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="gl_code_type" required name="gl_code_type" class="form-control bootstrap-select" data-header="Choose GL Code Type" data-live-search="true" style="max-height:100px;">' +
                                   ''+gl_code_type+''+
                                    '</select>' +
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
                            
                            var code = $("#code").val();
                            var account = $("#account").val();
                            var account_name = $("#account_name").val();
                            var gl_code_type = $("#gl_code_type").val();
                           
                            var theme="",header="",message="";
                            
                            if(code!="" && account!="" && account_name!="" && gl_code_type!="" && code!=null && account!=null && account_name!=null && gl_code_type!=null){
                           var url='save_gl_code';
                           var form_data = {"code":code,"account":account,"account_name":account_name,"gl_code_type":gl_code_type};
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
                                       loadGLCodes(); 
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
        $("#gl_code_type").selectpicker();       
            });
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
							<h5 class="panel-title">GL Codes Management Module</h5>
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
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_gl_code();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New GL Code</button>
                                            </div>
                                            <%}
                                            }
                                            %>
                                            <div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>GL Code</th><th>GL Account </th><th>GL Account Name</th><th>Status</th><th>Manage</th></tr>
                                                </thead>
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
  