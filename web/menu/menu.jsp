<%-- 
    Document   : menu
    Created on : Sep 26, 2017, 4:03:45 PM
    Author     : GNyabuto
--%>
<!DOCTYPE html>
<html lang="en">
<head>

</head>

<body>

				<div class="sidebar-content">

					<!-- User menu -->
					<div class="sidebar-main-toggle">
                                            <div class="category-content">
                                                    <div class="sidebar-user-material-content">
                                                            <a href="#"><img src="assets/images/aphia/icon.png" class="img-circle img-responsive" alt=""></a>
                                                            <h6>
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
									<a href="#"><i class="icon-grid"></i> <span>Advances</span></a>
									<ul>
										<li><a href="Staffs.jsp">Manage Advances</a></li>
									</ul>
								</li>
								<li>
									<a href="#"><i class="icon-grid"></i> <span>Manage Entries</span></a>
									<ul>
										<li><a href="FCO.jsp">FCO</a></li>
										<li><a href="GLCodes.jsp">GL Codes</a></li>
										<li><a href="Counties.jsp">Counties</a></li>
										<li><a href="Facilities.jsp">Facilities</a></li>
										<li><a href="Users.jsp">Users</a></li>
									</ul>
								</li>
                                                                <li>
									<a href="#"><i class="icon-grid"></i> <span>Reports</span></a>
									<ul>
										<li><a href="ComprehensiveReport.jsp">Comprehensive Report</a></li>
										<li><a href="journalentries.jsp">Journal Entries</a></li>
									</ul>
								</li>
                                                                <li><a href="logout"><i class="icon-grid"></i> <span>Logout</span></a>
								</li>
							</ul>
						</div>
					</div>
					<!-- /main navigation -->

				</div>

</body>
</html>
