<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
            <div class="navbar-default navbar-static-side" role="navigation">
                <div class="sidebar-collapse">
                    <ul class="nav" id="side-menu">
                        <li>
                           <img src="${pageContext.request.contextPath}/apps/resources/webapp/images/Logo-crop-border.png"/>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/panel"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                        </li>
                        <sec:authorize ifAnyGranted="PERMISSION_ADMIN">
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/users/manage"><i class="fa fa-user fa-fw"></i> Manage users</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/roles/manage"><i class="fa fa-tag fa-fw"></i> Manage roles</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/permissions/manage"><i class="fa fa-ticket fa-fw"></i> Manage permissions</a>
                        </li>
    			        </sec:authorize>
    			        <sec:authorize ifAnyGranted="PERMISSION_EXPERT">
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/users/manage"><i class="fa fa-user fa-fw"></i> Teachers and Students</a>
                        </li>
    			        </sec:authorize>
    			        <sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/screening"><i class="fa fa-picture-o fa-fw"></i> Screening</a>
                        </li>
                        </sec:authorize>
                        <sec:authorize ifAnyGranted="PERMISSION_TEACHER">
                        <li>
                            <a href="${pageContext.request.contextPath}/apps/users/students"><i class="fa fa-user fa-fw"></i> View Students</a>
                        </li>
    			        </sec:authorize>
    			        <sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT,PERMISSION_TEACHER">
                        <li class="active">
                            <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> View reports<span class="fa arrow"></span></a>
                            <ul style="height: auto;" class="nav nav-second-level collapse in">
                                <li>
                                    <a href="${pageContext.request.contextPath}/apps/reports/overview">Overview</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/apps/reports/skill">Skill breakdown</a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/apps/reports/activity">Activity breakdown</a>
                                </li>
                            </ul>
                        </li>
                        </sec:authorize>
    			        <sec:authorize ifAnyGranted="PERMISSION_STUDENT">
                        <li>
                            <a href="tables.html"><i class="fa fa-table fa-fw"></i> Personal</a>
                        </li>
    			        </sec:authorize>
    			        <sec:authorize ifAnyGranted="PERMISSION_ADMIN">
						<li class="active">
                            <a href="#"><i class="fa fa-wrench fa-fw"></i> Debug<span class="fa arrow"></span></a>
                            <ul style="height: auto;" class="nav nav-second-level collapse">
                                <li>
                                    <a href="${pageContext.request.contextPath}/apps/tests/logs">Send logs</a>
                                </li>
                            </ul>
                            <!-- /.nav-second-level -->
                        </li>
                        </sec:authorize>
                    </ul>
                    <!-- /#side-menu -->
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
