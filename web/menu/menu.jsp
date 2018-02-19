<%-- 
    Document   : menu
    Created on : Sep 26, 2017, 4:03:45 PM
    Author     : GNyabuto
--%>
<!DOCTYPE html>
<html lang="en">
<head>
        <link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Merienda+One" />
        <style>
    .title{
        font-family: Merienda One;
	font-size: 15px;
	font-style: normal;
	font-variant: normal;
	font-weight: 400;
	line-height: 26.4px;
    }    
    
    
</style>
</head>

<body>

    <div class="sidebar-content">

					<!-- User menu -->
					<div class="sidebar-main-toggle">
                                            <div class="category-content">
                                                    <div class="sidebar-user-material-content">
                                                            <a href="#"><img src="assets/images/aphia/icon.png" class="img-circle img-responsive" alt=""></a>
                                                            <h6 class="title">
                                                                <%
                                                                    if(session.getAttribute("fullname")!=null){
                                                                        out.println(session.getAttribute("fullname").toString());      
                                                                    }
                                                                %>
                                                            </h6>
                                                            <span class="text-size-small">User</span>
                                                    </div>
                                            </div>
					</div>
					<!-- /user menu -->


					<!-- Main navigation -->
					<div class="sidebar-category sidebar-category-visible">
						<div class="category-content no-padding">
							<ul class="navigation navigation-main navigation-accordion">

								<!-- Main -->
								<li class="navigation-header"><span>Main</span> <i class="icon-menu" title="Main pages"></i></li>
								<li>
									<a href="#"><i class="icon-lan3"></i> <span>Advances & Expenses</span></a>
									<ul>
										<li><a href="Staffs.jsp">Manage Advances</a></li>
                                                                                <%if(session.getAttribute("approve_expenses")!=null){
                                                                                    if(session.getAttribute("approve_expenses").toString().equals("1")){
                                                                                %>
										<li><a href="PendingExpenses.jsp">Approve Expenses</a></li>
                                                                                <%}}%>
									</ul>
								</li>
                                                                <%if(session.getAttribute("level")!=null){
                                                                   if(session.getAttribute("level").toString().equals("1")){
                                                                %>
								<li>
									<a href="#"><i class="icon-portfolio"></i> <span>Joint Work plans</span></a>
									<ul>
										<li><a href="JWP.jsp">Manage JWPs</a></li>
                                                                        </ul>
								</li>
                                                                <%}}%>
                                                                
								<%if(session.getAttribute("level")!=null){
                                                                if(session.getAttribute("level").toString().equals("1")){
                                                                %>
								<li>
									<a href="#"><i class="icon-puzzle3"></i> <span>Manage Entries</span></a>
									<ul>
										<li><a href="FCO.jsp">FCO</a></li>
										<li><a href="GLCodes.jsp">GL Codes</a></li>
										<li><a href="Counties.jsp">Counties</a></li>
										<li><a href="SubCounties.jsp">Sub Counties</a></li>
										<li><a href="Facilities.jsp">Facilities</a></li>
										<li><a href="Users.jsp">Users</a></li>
									</ul>
								</li>
                                                                <%}}%>
                                                                <li>
									<a href="#"><i class="icon-statistics"></i> <span>Reports</span></a>
									<ul>
										<li><a href="ComprehensiveReport.jsp">Comprehensive Report</a></li>
										<li><a href="Journalentries.jsp">Journal Entries</a></li>
										<li><a href="Rebanking.jsp">Rebanking Report</a></li>
									</ul>
								</li>
                                                                <li><a href="logout"><i class="icon-lock2"></i> <span>Logout</span></a>
								</li>
							</ul>
						</div>
					</div>
					<!-- /main navigation -->

				</div>

</body>
</html>
