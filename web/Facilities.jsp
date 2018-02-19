<%-- 
    Document   : Facilities
    Created on : Oct 3, 2017, 2:59:33 PM
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
          loadFacilities();    
          }); 
          
          
          function loadFacilities(){
            $.ajax({
        url:'all_facilities',
        type:"post",
        dataType:"json",
        success:function(raw_data){
            var facility_id,unique_code,facility_name,sub_county,county,position,mfl_code,status,status_label,output;
            var dataSet=[];
        
        var data=raw_data.data;
        for (var i=0; i<data.length;i++){
            position=i+1;
            facility_id=unique_code=facility_name=sub_county=county=status_label="";
            if( data[i].facility_id!=null){facility_id = data[i].facility_id;}
            if( data[i].unique_code!=null){unique_code = data[i].unique_code;}
            if( data[i].facility_name!=null){facility_name = data[i].facility_name;}
            if( data[i].sub_county!=null){sub_county = data[i].sub_county;}
            if( data[i].county!=null){county = data[i].county;}
            if( data[i].mfl_code!=null){mfl_code = data[i].mfl_code;}
            if( data[i].status!=null){status = data[i].status;}
          
          if(status==1){
             status_label = "<span style=\"width:150px; height: 30px; text-align:center;\" font-size:10px; class=\"label label-success\">Active</span> ";
            }
            else{
             status_label= "<span style=\"width:150px; height: 30px; text-align:center;\" font-size:10px;  class=\"label label-danger\">Inactive</span> ";   
            }
           
           var  output='<div style="position: absolute; z-index: 0;"><ul class="icons-list"><li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-menu9"></i></a><ul class="dropdown-menu dropdown-menu-right">';
               // output+='<li><button class="btn btn-link" onclick=\"edit_facility('+position+');\" ><i class="icon-list-unordered position-left"></i> Edit</button></li>'; 
                if(status==1){
                  output+='<li><button class="btn btn-link" onclick=\"de_activate('+position+');\" ><i class="icon-list-unordered position-left"></i> De-activate</button></li>';  
                }
                else{
                    output+='<li><button class="btn btn-link" onclick=\"activate('+position+');\" ><i class="icon-list-unordered position-left"></i> Activate</button></li>';
                }
                output+='<li><button class="btn btn-link" onclick=\"deleter('+position+');\" ><i class="icon-list-unordered position-left"></i> Delete</button></li>';
                output+='<input type="hidden" name="'+position+'" value="'+facility_id+'" id="_'+position+'">';
                output+='</ul></li></ul></div>';
            
           var minSet = [position,county,sub_county,facility_name,unique_code,mfl_code,status_label,output];
           
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
         
             
        function activate(position){
            var facility_id = $("#_"+position).val();
            var status = 1;
            activation(facility_id,status);
        }
        function de_activate(position){
            var facility_id = $("#_"+position).val();
            var status = 0;
            activation(facility_id,status);
        }
        function activation(facility_id,status){
        var url='update_facility_status';
        
        var theme="",header="",message="";
         var form_data = {"facility_id":facility_id,"status":status};
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
                   loadFacilities();  
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
          var facility_id = $("#_"+position).val();
          var url='delete_facility';
          var theme="",header="",message="";
          bootbox.confirm({ 
            size: "small",
            message: "Are you sure you want to delete this facility?", 
            callback: function(result){
            if(result){
             var form_data = {"facility_id":facility_id};
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
                   loadFacilities();  
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
           
          
          function new_facility(){
              //load types
          var url='all_counties';
            $.post(url,'' , function(output) {
               var data = JSON.parse(output).data;
               var counties="";
               counties+='<option value=\"\">Choose County</option>';
               for (var i=0; i<data.length;i++){   
               var id = data[i].id;
               var name = data[i].name;
               
               counties+='<option value=\"'+id+'\">'+name+'</option>';
               // input them to the respectie elements
           } 
           // pop up
                   bootbox.dialog({
                title: "<div style=\"color:black; font-weight: 900; text-decoration: underline; \">New Health Facility</div>",
                message: '<div class="row">  ' +
                    '<div class="col-md-12">' +
                        '<form id="new_advance" class="form-horizontal">' +
                            
                             '<div class="form-group">' +
                                '<label class="col-md-4 control-label">County <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="county_id" required name="county_id" onchange=\"get_sub_counties();\" class="form-control bootstrap-select" data-header="Choose County" data-live-search="true" style="max-height:100px;">' +
                                   ''+counties+''+
                                    '</select>' +
                                '</div>' +
                            '</div>' +
                            
                             '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Sub County <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<select id="sub_county_id" required name="sub_county_id" class="form-control bootstrap-select" data-header="Choose Sub County" data-live-search="true" style="max-height:100px;">' +
                                    '<option value="">Nothing selected</option>'+
                                    '</select>' +
                                '</div>' +
                            '</div>' +
                    
                              '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Facility Name <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="facility_name" required name="facility_name" type="text" value="" placeholder="Enter Facility Name" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">Unique Code <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="unique_code" required name="unique_code" type="text" value="" placeholder="Enter Unique Code" class="form-control">' +
                                '</div>' +
                            '</div>' +
                            
                            
                            '<div class="form-group">' +
                                '<label class="col-md-4 control-label">MFL Code <b style=\"color:red\">*</b> : </label>' +
                                '<div class="col-md-8">' +
                                    '<input id="mfl_code" required name="mfl_code"  onkeypress="return numbers(event)"  type="text" value="" placeholder="Enter MFL Code" class="form-control">' +
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
                            
                            var county_id = $("#county_id").val();
                            var sub_county_id = $("#sub_county_id").val();
                            var facility_name = $("#facility_name").val();
                            var unique_code = $("#unique_code").val();
                            var mfl_code = $("#mfl_code").val();
                           
                            var theme="",header="",message="";
                            
                            if(county_id!="" && sub_county_id!="" && facility_name!="" && unique_code!="" && mfl_code!="" && county_id!=null && sub_county_id!=null && facility_name!=null && unique_code!=null && mfl_code!=null){
                           var url='save_facility';
                           var form_data = {"county_id":county_id,"sub_county_id":sub_county_id,"facility_name":facility_name,"unique_code":unique_code,"mfl_code":mfl_code};
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
                                       loadFacilities(); 
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
        $("#county_id").selectpicker();       
        $("#sub_county_id").selectpicker();       
            });
          // load data to edit
          }  
          
    function get_sub_counties(){
     var county = $("#county_id").val();
      var url='load_sub_counties';
      var form_data={"county_id":county};
            $.post(url,form_data , function(output) {
               var data = JSON.parse(output).data;
               var sub_counties="";
               for (var i=0; i<data.length;i++){   
               var id = data[i].sub_county_id;
               var name = data[i].sub_county;
             sub_counties+='<option value=\"'+id+'\">'+name+'</option>';  
        }
       $("#sub_county_id").html(sub_counties);
       $("#sub_county_id").selectpicker();
       $("#sub_county_id").selectpicker('refresh');
       
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
							<h5 class="panel-title" style="color:black; font-weight: 900; text-decoration: underline; ">Facility Management Module</h5>
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
                                                <button type="button" class="btn btn-success btn-raised" onclick="new_facility();" style="margin-left: 1%; margin-bottom: 1%;"><i class="icon-plus3 position-left"></i> New Facility</button>
                                            </div>
                                            <%}
                                            }
                                            %>
                                            <div>
                                                <table id='table' class="table ">
                                                <thead>
                                                    <tr><th>No.</th><th>County</th><th>Sub County</th><th>Facility Name </th><th>Unique Code</th><th>MFL Code</th><th>Status</th><th>Manage</th></tr></thead>
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
  