<%-- 
    Document   : ComprehensiveReport
    Created on : Oct 6, 2017, 11:39:01 AM
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
	<title>ATS - Comprehensive report</title>
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
       
        <!--to manage notifications-->
        <script type="text/javascript" src="assets/js/plugins/notifications/bootbox.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/notifications/noty.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/notifications/jgrowl.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
        
	<script type="text/javascript" src="assets/js/core/app.js"></script>
	<script type="text/javascript" src="assets/js/pages/datatables_extension_buttons_print.js"></script>
        <script type="text/javascript" src="assets/js/pages/datatables_extension_select.js"></script>
	<script type="text/javascript" src="assets/js/plugins/ui/ripple.min.js"></script>
        
	<script type="text/javascript" src="assets/js/plugins/pickers/daterangepicker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/anytime.min.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.date.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/picker.time.js"></script>
	<script type="text/javascript" src="assets/js/plugins/pickers/pickadate/legacy.js"></script>
	<!-- /theme JS files -->
        <script type="text/javascript">
          jQuery(document).ready(function(){
          loadStaffs();
           
        var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
        var day = currentDate.getDate();
        var month = currentDate.getMonth();
        var year = currentDate.getFullYear();
      $('.pickadate-disable-range').pickadate({
            format: 'yyyy-mm-dd',
            disable: [
                { from: [year, month, day], to: [3017, 9, 25] }
            ]
    });
    
          }); 
          
          
          function loadStaffs(){
//              alert("called");
            $.ajax({
        url:'loadStaffs',
        type:"post",
        dataType:"json",
        success:function(raw_data){
//            alert(raw_data);
            var staffno,fullname,status,email,phone,position,staffs;
            staffs='';
            var data=raw_data.data;
            for (var i=0; i<data.length;i++){
                position=i+1;
                staffno=fullname=status=email=phone="";
                if( data[i].staffno!=null){staffno = data[i].staffno;}
                if( data[i].fullname!=null){fullname = data[i].fullname;}
                if( data[i].status!=null){status = data[i].status;}
                if( data[i].email!=null){email = data[i].email;}
                if( data[i].phone!=null){phone = data[i].phone;}

                // output the values to data table
               staffs+='<option value="'+staffno+'" >'+fullname+'</option>'; 
            }
//            alert(staffs);
            $("#staffs").html(staffs);
            $("#staffs").selectpicker();
        }
          
        });   
          }
        </script>
        
               <script type="text/javascript">
          jQuery(document).ready(function(){
          loadrepports();
          loadyears();
          loadsemiannuals(0);
          loadquarters(0);
          loadmonths(0);
          });
          
          function loadrepports(){
                $.ajax({
                 url:'load_reports',
                 type:"post",
                 dataType:"json",
                 success:function(raw_data){
         //            alert(raw_data);
                     var id,name;
                     var reports='';
                     var data=raw_data.data;
                     for (var i=0; i<data.length;i++){
                         id=name="";
                         if( data[i].id!=null){id = data[i].id;}
                         if( data[i].name!=null){name = data[i].name;}


                         // output the values to data table
                        reports+='<option value="'+id+'" >'+name+'</option>'; 
                     }
         //            alert(staffs);
                     $("#reports").html(reports);
                     $("#reports").selectpicker();
                 }

                 });    
          }
          
          function loadyears(){
            $.ajax({
                url:'load_years',
                type:"post",
                dataType:"json",
                success:function(raw_data){
        //            alert(raw_data);
                    var year;
                    var years='';
                    var data=raw_data.data;
                    for (var i=0; i<data.length;i++){
                        if( data[i].year!=null){year = data[i].year;}


                        // output the values to data table
                       years+='<option value="'+year+'" >'+year+'</option>'; 
                    }
        //            alert(staffs);
                    $("#years").html(years);
                    $("#years").selectpicker();
                }

                });
          }
          
          function loadsemiannuals(year){
             var url;
             if(year>0){url='load_semiannual?year='+year;} 
             else{url='load_semiannual';}
             
            $.ajax({
                url:url,
                type:"post",
                dataType:"json",
                success:function(raw_data){
        //            alert(raw_data);
                    var id,name;
                    var semi_annual='';
                    var data=raw_data.data;
                    for (var i=0; i<data.length;i++){
                        id=name="";
                        if( data[i].id!=null){id = data[i].id;}
                        if( data[i].name!=null){name = data[i].name;}


                        // output the values to data table
                       semi_annual+='<option value="'+id+'" >'+name+'</option>'; 
                    }
        //            alert(staffs);
                    $("#semi_annual").html(semi_annual);
                    $("#semi_annual").selectpicker();
                    $("#semi_annual").selectpicker('refresh');
                }

                });    
          }
          
          function loadquarters(year){
             var url;
             if(year>0){url='load_quarters?year='+year;} 
             else{url='load_quarters';}
                $.ajax({
               url:url,
               type:"post",
               dataType:"json",
               success:function(raw_data){
       //            alert(raw_data);
                   var id,name;
                   var quarters='';
                   var data=raw_data.data;
                   for (var i=0; i<data.length;i++){
                       id=name="";
                       if( data[i].id!=null){id = data[i].id;}
                       if( data[i].name!=null){name = data[i].name;}


                       // output the values to data table
                      quarters+='<option value="'+id+'" >'+name+'</option>'; 
                   }
       //            alert(staffs);
                   $("#quarters").html(quarters);
                   $("#quarters").selectpicker();
                   $("#quarters").selectpicker('refresh');
               }

               });    
          }
          
          function loadmonths(year){
              var url;
             if(year>0){url='loadMonths?year='+year;} 
             else{url='loadMonths';}
            $.ajax({
                url:url,
                type:"post",
                dataType:"json",
                success:function(raw_data){
                    var id,name;
                    var months='';
                    var data=raw_data.data;
                    for (var i=0; i<data.length;i++){
                        id=name="";
                        if( data[i].id!=null){id = data[i].id;}
                        if( data[i].name!=null){name = data[i].name;}

                        // output the values to data table
                       months+='<option value="'+id+'" >'+name+'</option>'; 
                    }
                    $("#months").html(months);
                    $("#months").selectpicker();
                    $("#months").selectpicker('refresh');
                }

                });   
          }
          
          
        </script>
        
        <script>
            jQuery(document).ready(function(){
                    var currentDate = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
                   var day = currentDate.getDate();
                   var month = currentDate.getMonth();
                   var year = currentDate.getFullYear();
                 $('.pickadate-disable-range').pickadate({
                       format: 'yyyy-mm-dd',
                       disable: [
                           { from: [year, month, day], to: [3017, 9, 25] }
                       ]
               });
               
    $("#custom_dates_row").hide();
    $("#month_row").hide();
    $("#quarter_row").hide();
    $("#semi_annual_row").hide();
    $("#year_row").show();
    
    
    
    $("#reports").change(function(){
     var report_id = $("#reports").val();
     if(parseInt(report_id)<5){
        $("#year_row").show();
         var year = $("#years").val();
          loadsections(year,report_id);
     }
     else if(parseInt(report_id)==5){
        $("#custom_dates_row").show();  
        $("#month_row").hide();
        $("#quarter_row").hide();
        $("#semi_annual_row").hide();
        $("#year_row").hide();
        $("#years").prop("required",false);
        $("#semi_annual").prop("required",false);
        $("#quarters").prop("required",false);
        $("#months").prop("required",false);
        $("#start_date").prop("required",true);
        $("#end_date").prop("required",true);
     }
    });
    
    $("#years").change(function(){
     var year = $("#years").val();
     var report_id = $("#reports").val();
    loadsections(year,report_id);
    });
    
    
          }); 
          
    function loadsections(year,report_id){
        
        $("#custom_dates_row").hide();
        $("#month_row").hide();
        $("#quarter_row").hide();
        $("#semi_annual_row").hide();
        
            $("#years").prop("required",true);
            $("#start_date").prop("required",false);
            $("#end_date").prop("required",false);
            
        if(parseInt(report_id)==2){
            loadsemiannuals(year);
            $("#month_row").hide();
            $("#quarter_row").hide();
            $("#semi_annual_row").show();
            
            
            $("#semi_annual").prop("required",true);
            $("#quarters").prop("required",false);
            $("#months").prop("required",false);
        
         }
         if(parseInt(report_id)==3){
            loadquarters(year);
            $("#month_row").hide();
            $("#quarter_row").show();
            $("#semi_annual_row").hide();
            
            $("#semi_annual").prop("required",false);
            $("#quarters").prop("required",true);
            $("#months").prop("required",false);
         }
         if(parseInt(report_id)==4){
             loadmonths(year);
            $("#month_row").show();
            $("#quarter_row").hide();
            $("#semi_annual_row").hide();
            
            $("#semi_annual").prop("required",false);
            $("#quarters").prop("required",false);
            $("#months").prop("required",true);
         }
    
          }
         
            </script>
            
            <script>
                
        jQuery(document).ready(function(){
         load_health_types();  
          $("#county_row").hide();
          $("#sub_county_row").hide();
          $("#facility_row").hide();
         
         $("#region_id").change(function(){
             var region_id= $("#region_id").val();
//             alert("region id : "+region_id);
                 $("#county_row").hide();
                 $("#sub_county_row").hide();
                 $("#facility_row").hide();
             if(region_id==1){ //counties
               load_counties();
                $("#county_row").show();
             }
             else if(region_id==2){ //sub counties
                load_sub_counties();
                 $("#sub_county_row").show();
             }
             else if(region_id==3){ // facilities
                 load_facilities();
                  $("#facility_row").show();
             }
         });
        });
            function load_health_types(){
               $.ajax({
                    url:'load_facility_accounting_types',
                    type:"post",
                    dataType:"html",
                    success:function(output){
                     var data,res_obj,id,name;
                     res_obj = JSON.parse(output).data;
                     data="<option value=\"\">All Types</option>";
                     for(var i=0;i<res_obj.length;i++){
                         id=res_obj[i].id;
                         name=res_obj[i].name;
                         data+="<option value=\""+id+"\">"+name+"</option>";
                     }
                        
                   $("#region_id").html(data);
                   $("#region_id").selectpicker();
                    }
                });
           }      
                
        function load_counties(){
               $.ajax({
                    url:'all_counties',
                    type:"post",
                    dataType:"html",
                    success:function(output){
                    var data,res_obj,id,name;
                     res_obj = JSON.parse(output).data;
                     data="";
                     for(var i=0;i<res_obj.length;i++){
                         id=res_obj[i].id;
                         name=res_obj[i].CHMT;
                         data+="<option value=\""+id+"\">"+name+"</option>";
                     }
                   $("#county_ids").html(data);
                   $("#county_ids").selectpicker();
                    }
                });
           }
           
                   function load_sub_counties(){
               $.ajax({
                    url:'load_sub_counties',
                    type:"post",
                    dataType:"html",
                    success:function(output){
                     var data,res_obj,id,name;
                     res_obj = JSON.parse(output).data;
                     data="";
                     for(var i=0;i<res_obj.length;i++){
                         id=res_obj[i].sub_county_id;
                         name=res_obj[i].SCHMT;
                         data+="<option value=\""+id+"\">"+name+"</option>";
                     }
                   $("#sub_county_ids").html(data);
                   $("#sub_county_ids").selectpicker();
                    }
                });
           }
           
        function load_facilities(){
               $.ajax({
                    url:'load_facilities',
                    type:"post",
                    dataType:"html",
                    success:function(data){
                    $("#facility_ids").html(data);
                    $("#facility_ids").selectpicker();
                    }
                });
     }
            </script>
            
            
            <style type="text/css">
                table{
                    max-width: 600px;
                }  
                
                </style>
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
							<h5 class="panel-title" style="color:black; font-weight: 900; text-decoration: underline; "><u>Comprehensive Report</u></h5>
							<div class="heading-elements">
								<ul class="icons-list">
			                		<li><a data-action="collapse"></a></li>
			                		<li><a data-action="reload"></a></li>
			                		<li><a data-action="close"></a></li>
			                	</ul>
		                	</div>
						</div>
                                            <div>
                                                <form action="ComprehensiveReport" class="form-control"  style="min-height: 500px;margin-left: 20%;margin-right: 30%; width: 500px;">
                                                    <table id='table' class="table">
                                               <tr>
                                                <td>
                                                    Select Staff:
                                                </td>
                                                <td>
                                                    <select id="staffs" multiple="true" name="staffs" class="form-control bootstrap-select" data-header="Choose Staff" data-live-search="true" >
                                                        <option value="">Loading Staffs</option>
                                                    </select>
                                                </td> 
                                                   </tr>
                                               
                                               <tr>
                                                <td>
                                                    Select Report <font color="red">*</font>:
                                                </td>
                                                <td colspan="2">
                                                    <select id="reports" name="reports" class="form-control bootstrap-select" data-header="Choose Report" data-live-search="true" >
                                                        <option value="">Loading Reports</option>
                                                    </select>
                                                </td> 
                                                   </tr>
                                               <tr id="year_row">
                                                <td>
                                                   Select Reporting Year <font color="red">*</font>:
                                                </td>
                                                <td colspan="2">
                                               <select id="years" name="years" class="form-control bootstrap-select" data-header="Choose Year" data-live-search="true" >
                                                        <option value="">Loading Years</option>
                                                    </select>  
                                                </td>
                                                   
                                                   </tr>
                                               <tr id="semi_annual_row">
                                                <td>
                                                   Select Semi-Annual <font color="red">*</font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="semi_annual" multiple="true" name="semi_annual" class="form-control bootstrap-select" data-header="Choose Semi-Annual" data-live-search="true" >
                                                        <option value="">Loading Semi Annuals</option>
                                                </select>  
                                                </td>
                                                   </tr>
                                               <tr id="quarter_row">
                                                <td>
                                                   Select Quarter <font color="red">*</font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="quarters" multiple="true" name="quarters" class="form-control bootstrap-select" data-header="Choose Quarter" data-live-search="true" >
                                                        <option value="">Loading Quarter</option>
                                                </select>  
                                                </td>
                                                   </tr>
                                               <tr id="month_row">
                                                <td>
                                                   Select Month <font color="red">*</font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="months" multiple="true" name="months" class="form-control bootstrap-select" data-header="Choose Month" data-live-search="true" >
                                                        <option value="">Loading Months</option>
                                                </select>  
                                                </td>
                                                </tr>
                                                   
                                               <tr id="custom_dates_row">
                                                <td>
                                                   Custom Dates <font color="red">*</font>:
                                                </td>
                                               <td> <input id="start_date" name="start_date" type="date" value="" placeholder="Select From Date" class="form-control">   </td>
                                               <td> <input id="end_date" name="end_date" type="date" value="" placeholder="Select To Date" class="form-control">  
                                                </td>
                                                   </tr>
                                                 
                                                 <tr id="region_type_row">
                                                <td>
                                                   Select Region Type <font color="red"></font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="region_id" name="region_id" class="form-control bootstrap-select" data-header="Choose Region Type" data-live-search="true" >
                                                        <option value="">Loading Regions</option>
                                                </select>  
                                                </td>
                                                </tr>
                                                
                                                <tr id="county_row">
                                                <td>
                                                   Select Counties <font color="red"></font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="county_ids" multiple="true" name="county_ids" class="form-control bootstrap-select" data-header="Choose County" data-live-search="true" >
                                                        <option value="">Loading Counties</option>
                                                </select>  
                                                </td>
                                                </tr>
                                                
                                                <tr id="sub_county_row">
                                                <td>
                                                   Select Sub county <font color="red"></font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="sub_county_ids" multiple="true" name="sub_county_ids" class="form-control bootstrap-select" data-header="Choose Sub counties" data-live-search="true" >
                                                        <option value="">Loading sub counties</option>
                                                </select>  
                                                </td>
                                                </tr>
                                                
                                                <tr id="facility_row">
                                                <td>
                                                   Select Facilities <font color="red"></font>:
                                                </td>
                                                <td colspan="2"> 
                                               <select id="facility_ids" multiple="true" name="facility_ids" class="form-control bootstrap-select" data-header="Choose Facilities" data-live-search="true" >
                                                        <option value="">Loading Facilities</option>
                                                </select>  
                                                </td>
                                                </tr>
                                                
                                                 <tr style="margin-top: 140px;">
                                                   <td colspan="3" style="text-align: center;"> <input id="generate" style="width: 200px;" name="generate" type="submit" value="Generate Report" class="form-control btn btn-success">  </td>
                                               
                                                   </tr>
                                               </table>
                                                </form>
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
  
