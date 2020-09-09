<%@page import="Entidades.Notas"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="Controladores.NotasJpaController"%>
<%@page import="Entidades.Calificacion"%>
<%@page import="Controladores.CalificacionJpaController"%>
<%@page import="Entidades.Periodo"%>
<%@page import="Controladores.PeriodoJpaController"%>
<%@page import="Controladores.ApoderadoJpaController"%>
<%@page import="Entidades.Matricula"%>
<%@page import="Controladores.MatriculaJpaController"%>
<%@page import="Entidades.Alumnoapoderado"%>
<%@page import="Controladores.AlumnoapoderadoJpaController"%>
<%@page import="Entidades.Apoderado"%>
<%@page import="Entidades.Alumno"%>
<%@page import="Entidades.Ano"%>
<%@page import="Controladores.AnoJpaController"%>
<%@page import="Entidades.Clase"%>
<%@page import="Controladores.ClaseJpaController"%>
<%@page import="Entidades.Profesor"%>
<!DOCTYPE html>
<%@page session="true" %>

<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- Tell the browser to be responsive to screen width -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <!-- Favicon icon -->
        <link rel="icon" type="image/png" sizes="16x16" href="../assets/images/favicon.png">
        <title>Sistema Web Académico</title>
        <!-- Bootstrap Core CSS -->
        <link href="../assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="css/style.css" rel="stylesheet">
        <!-- You can change the theme colors from here -->
        <link href="css/colors/blue.css" id="theme" rel="stylesheet">
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    </head>

    <body class="fix-header fix-sidebar card-no-border">



        <%

            String usuario = "";
            String nombre = "";
            int id = 0;
            Apoderado apoderado = null;
            Matricula matricula = null;
            int m = Integer.parseInt(request.getParameter("m"));

            if (session.getAttribute("rol") == "apoderado" & session.getAttribute("apoderado") != null) {
                ApoderadoJpaController apc = new ApoderadoJpaController();
                MatriculaJpaController mc = new MatriculaJpaController();
                apoderado = (Apoderado) session.getAttribute("apoderado");
                usuario = apoderado.getUsuario();
                nombre = apoderado.getNombre() + " " + apoderado.getApPaterno() + " " + apoderado.getApMaterno();
                id = apoderado.getId();

                if (apc.validarMatricula(m, id)) {
                    matricula = mc.findMatricula(m);

                } else {
                    response.sendRedirect("inicio-apoderado.jsp");
                }

            } else {

                response.sendRedirect("error-acceso.html");
            }

        %>
        <!-- ============================================================== -->
        <!-- Preloader - style you can find in spinners.css -->
        <!-- ============================================================== -->
        <div class="preloader">
            <svg class="circular" viewBox="25 25 50 50">
            <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" /> </svg>
        </div>
        <!-- ============================================================== -->
        <!-- Main wrapper - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <div id="main-wrapper">
            <!-- ============================================================== -->
            <!-- Topbar header - style you can find in pages.scss -->
            <!-- ============================================================== -->
            <header class="topbar">
                <nav class="navbar top-navbar navbar-expand-md navbar-light">
                    <!-- ============================================================== -->
                    <!-- Logo -->
                    <!-- ============================================================== -->
                    <div class="navbar-header">
                        <a class="navbar-brand" href="index.html">
                            <!-- Logo icon --><b>
                                <!--You can put here icon as well // <i class="wi wi-sunset"></i> //-->
                                <!-- Dark Logo icon -->
                                <img src="../assets/images/logo-icon.png" alt="homepage" class="dark-logo" />
                                <!-- Light Logo icon -->
                                <img src="../assets/images/logo-light-icon.png" alt="homepage" class="light-logo" />
                            </b>
                            <!--End Logo icon -->
                            <!-- Logo text --><span>
                                <!-- dark Logo text -->
                                <img src="../assets/images/logo-text.png" alt="homepage" class="dark-logo" />
                                <!-- Light Logo text -->    
                                <img src="../assets/images/logo-light-text.png." class="light-logo" alt="C.P. MAR" /></span> </a>
                    </div>
                    <!-- ============================================================== -->
                    <!-- End Logo -->
                    <!-- ============================================================== -->
                    <div class="navbar-collapse">
                        <!-- ============================================================== -->
                        <!-- toggle and nav items -->
                        <!-- ============================================================== -->
                        <ul class="navbar-nav mr-auto mt-md-0">
                            <!-- This is  -->
                            <li class="nav-item"> <a class="nav-link nav-toggler hidden-md-up text-muted waves-effect waves-dark" href="javascript:void(0)"><i class="mdi mdi-menu"></i></a> </li>
                            <li class="nav-item"> <a class="nav-link sidebartoggler hidden-sm-down text-muted waves-effect waves-dark" href="javascript:void(0)"><i class="ti-menu"></i></a> </li>
                            <!-- ============================================================== -->


                        </ul>
                        <!-- ============================================================== -->
                        <!-- User profile and search -->
                        <!-- ============================================================== -->
                        <ul class="navbar-nav my-lg-0">
                            <!-- ============================================================== -->

                            <!-- ============================================================== -->

                            <!-- ============================================================== -->
                            <!-- ============================================================== -->
                            <!-- Profile -->
                            <!-- ============================================================== -->
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-muted waves-effect waves-dark" href="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img src="../assets/images/users/1.jpg" alt="user" class="profile-pic" /></a>
                                <div class="dropdown-menu dropdown-menu-right scale-up">
                                    <ul class="dropdown-user">
                                        <li>
                                            <div class="dw-user-box">
                                                <div class="u-img"><img src="../assets/images/users/1.jpg" alt="user"></div>
                                                <div class="u-text">
                                                    <h4><%=usuario%></h4>
                                                    <br>
                                                    <a href="profile.html" class="btn btn-rounded btn-danger btn-sm">Ver Perfil</a></div>
                                            </div>
                                        </li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#"><i class="ti-user"></i> <%=nombre%></a></li>

                                        <li role="separator" class="divider"></li>
                                        <li><a href="../logout"><i class="fa fa-power-off"></i> Salir</a></li>
                                    </ul>
                                </div>
                            </li>
                            <!-- ============================================================== -->

                        </ul>
                    </div>
                </nav>
            </header>
            <!-- ============================================================== -->
            <!-- End Topbar header -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->
            <!-- Left Sidebar - style you can find in sidebar.scss  -->
            <!-- ============================================================== -->
            <aside class="left-sidebar">
                <!-- Sidebar scroll-->
                <div class="scroll-sidebar">
                    <!-- User profile -->
                    <div class="user-profile" style="background: url(../imagenes/bloggif_5e1e270cc44f1.png) no-repeat;">

                        <br>
                        <br>
                        <br>
                        <br>
                        <br>
                        <br>
                        <br>
                        <br>

                    </div>
                    <!-- End User profile text-->
                    <!-- Sidebar navigation-->
                    <nav class="sidebar-nav">
                        <ul id="sidebarnav">
                            <li class="nav-small-cap">Académico</li>
                            <li> <a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false"><i class="fa fa-users"></i><span class="hide-menu">Hijos</span></a>
                                        <%AlumnoapoderadoJpaController ac = new AlumnoapoderadoJpaController(); %>
                                <ul aria-expanded="false" class="collapse">

                                    <%for (Alumnoapoderado a : ac.alumnosxapoderado(id)) {%>
                                    <li><a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false"><%=a.getIdalumno().getNombre() + " " + a.getIdalumno().getApPaterno() + " " + a.getIdalumno().getApMaterno()%></a>

                                        <ul aria-expanded="false" class="collapse">
                                            <%MatriculaJpaController mc = new MatriculaJpaController(); %>
                                            <%for (Matricula ma : mc.listarxalumno(a.getIdalumno().getId())) {%>
                                            <li><a  href="alumno-apoderado.jsp?m=<%=ma.getId()%>" ><icon class="fa fa-book"></icon> Cursos <%=ma.getIdano().getDescripcion()%></a>
                                                        <% }%>
                                        </ul>
                                        <ul>   
                                            <li><a  href="asistencia-alumno-apoderado.jsp?alumno=<%=a.getIdalumno().getId()%>" ><icon class="fa fa-calendar"></icon> Asistencia</a>
                                        </ul>
                                        <ul> 
                                            <li><a  href="pagos-alumno-apoderado.jsp?alumno=<%=a.getIdalumno().getId()%>" ><icon class="fa fa-money"></icon> Pagos</a>
                                        </ul>
                                    </li>

                                    <% }%>
                                </ul>
                            </li>




                        </ul>
                    </nav>
                    <!-- End Sidebar navigation -->
                </div>
                <!-- End Sidebar scroll-->
                <!-- Bottom points-->
                <div class="sidebar-footer">
                    <!-- item--><a href="#" class="link" data-toggle="tooltip" title="074-256218"><i class="fa fa-phone"></i></a>
                    <!-- item--><a href="https://www.facebook.com/CP-Manuel-Antonio-Rivas-710283062406235/" class="link" data-toggle="tooltip" title="Facebook"><i class="fa fa-facebook"></i></a>
                    <!-- item--><a href="" class="link" data-toggle="tooltip" title="Salir"><i class="mdi mdi-power"></i></a> </div>
                <!-- End Bottom points-->
            </aside>
            <!-- ============================================================== -->
            <!-- End Left Sidebar - style you can find in sidebar.scss  -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->
            <!-- Page wrapper  -->
            <!-- ============================================================== -->
            <div class="page-wrapper">
                <!-- ============================================================== -->
                <!-- Container fluid  -->
                <!-- ============================================================== -->
                <div class="container-fluid">
                    <!-- ============================================================== -->
                    <!-- Bread crumb and right sidebar toggle -->
                    <!-- ============================================================== -->
                    <div class="row page-titles">
                        <div class="col-md-5 col-8 align-self-center">
                            <h3 class="text-themecolor"><%=nombre%></h3>

                        </div>

                    </div>
                    <!-- ============================================================== -->
                    <!-- End Bread crumb and right sidebar toggle -->
                    <!-- ============================================================== -->
                    <!-- ============================================================== -->
                    <!-- Start Page Content -->
                    <!-- ============================================================== -->
                    <div class="row">
                        <!-- Column -->

                        <%ClaseJpaController cc = new ClaseJpaController(); %>
                        <%for (Clase c : cc.listarxmatricula(matricula.getId())) {%>
                        <div class="col-lg-4 col-md-4">
                            <div class="card card-inverse card-success">
                                <div class="card-body">
                                    <div class="d-flex">
                                        <div class="m-r-20 align-self-center">
                                            <h1 class="text-white"><i class="ti-pie-chart"></i></h1></div>
                                        <div>
                                            <form action="clase-alumno-apoderado.jsp" method="post">
                                                <input type="hidden" name="clase" value="<%=c.getId()%>" >
                                                <input type="hidden" name="matricula" value="<%=matricula.getId()%>" >
                                                <input type="submit" class="card-title btn-success font-20" value="<%=c.getIdcurso().getNombre()%>">
                                                <h6 class="card-subtitle"><%=c.getIdseccion().getIdgrado().getNombre() + "  " + c.getIdseccion().getNombre() + " - " + c.getIdseccion().getIdgrado().getIdnivel().getTipo()%></h6> 
                                                <h6 class="card-subtitle"><%=c.getIdprofesor().getNombre() + " " + c.getIdprofesor().getApPaterno() + " " + c.getIdprofesor().getApMaterno()%></h6> 
                                            </form>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                        <% }%>

                    </div>

                    
                      <!--LIBRETA DE NOTAS-->
                        <div class="col-12">
                            <div class="card">

                                <div class="card-body">
                                    <h2 class="text-dark">Libreta de notas:</h2>
                                    <div class="table-responsive m-t-40">
                                        <table id="tableCalificacion" class="full-color-table full-inverse-table hover-table text-center color-bordered-table inverse-bordered-table color-table inverse-table display nowrap table table-hover  table-bordered text-dark" cellspacing="0" width="100%">

                                            <thead>
                                                <tr>
                                                    <th>Curso</th>
                                                    <th>Competencia</th>
                                                        <%PeriodoJpaController pc = new PeriodoJpaController(); %>
                                                        <%for (Periodo pd : pc.findPeriodoEntities()) {%><!--MUESTRA LOS PERIODOS EN EL ENCABEZADO -->
                                                    <th><%=pd.getDescripcion()%></th>
                                                        <% }%>
                                                    <th>Promedio final</th>
                                                </tr>
                                            </thead>
                                            <%ClaseJpaController cjc = new ClaseJpaController(); %>

                                            <%for (Clase cl : cjc.listarxmatricula(matricula.getId())) {%><!--UN BODY POR CADA CLASE DEL ALUMNO -->

                                            <tbody>
                                                <%CalificacionJpaController calc = new CalificacionJpaController();%>
                                                <tr >

                                                    <td class="" rowspan="<%=(cl.getCalificacionList().size() + 1)%>" ><b><%=cl.getIdcurso().getNombre()%>  </b> </td>


                                                </tr>
                                                <%for (Calificacion cal : calc.listarxclase(cl.getId())) {%>
                                                <tr>


                                                    <td><%=cal.getDescripcion()%></td>
                                                    <%PeriodoJpaController pc1 = new PeriodoJpaController(); %>
                                                    <%for (Periodo pd : pc1.findPeriodoEntities()) {%><!--RECORRE C/PERIODO PARA LAS NOTAS -->
                                                    <%NotasJpaController nc = new NotasJpaController(Persistence.createEntityManagerFactory("colegiosPU")); %>
                                                    <%Double suma = 0.0; %>
                                                    <%int i = 0; %>
                                                    <!--NOTAS POR CADA CALIFICACIÓN DEL PERIODO EN CUESTIÓN -->
                                                    <%for (Notas n : nc.calificacion_periodo_matriculaclase(pd.getId(), cal.getId(), Integer.parseInt(matricula.getId().toString() + cl.getId().toString()))) {%>
                                                    <%i = i + 1;%>
                                                    <!--SE VAN SUMANDO LAS NOTAS  -->
                                                    <% suma = suma + n.getNota(); %>


                                                    <% }%>
                                                    <!--PROMEDIO POR CALIFICACION : SUMA DE NOTAS ENTRE # DE CALIFICACIONES  -->
                                                    <td><%=(double) Math.round((suma / i) * 100) / 100%></td>

                                                    <% }%>

                                                </tr>



                                                <% }%>
                                                <!--PROMEDIO BIMESTRALES  -->
                                                <tr>
                                                    <td></td>
                                                    <td ><b> </b></td>
                                                    <%PeriodoJpaController pc1 = new PeriodoJpaController(); %>

                                                    <%Double total = 0.0; %>
                                                    <%Double t = 0.0; %>
                                                    <%int k = 0;%>
                                                    <!--RECORRER PERIODO  -->
                                                    <%for (Periodo pd : pc1.findPeriodoEntities()) {%>
                                                    <%k = k + 1;%>
                                                    <%int i = 0; %>
                                                    <!--RECORRER POR CALIFICACION  -->
                                                    <%CalificacionJpaController calc1 = new CalificacionJpaController();%>
                                                    <%for (Calificacion cal : calc1.listarxclase(cl.getId())) {%>
                                                    <%i = i + 1;%>
                                                    <%NotasJpaController nc = new NotasJpaController(Persistence.createEntityManagerFactory("colegiosPU")); %>
                                                    <%Double suma2 = 0.0; %>
                                                    <%int j = 0; %>
                                                    <!--NOTAS DE C/CALIFICACION Y PERIODO  -->
                                                    <%for (Notas n : nc.calificacion_periodo_matriculaclase(pd.getId(), cal.getId(), Integer.parseInt(matricula.getId().toString() + cl.getId().toString()))) {%>

                                                    <%j = j + 1;%>
                                                    <!--SUMANDO POR CALIFICACION  -->
                                                    <% suma2 = suma2 + n.getNota(); %>


                                                    <% }%>
                                                    <!--SUMANDO POR PERIODO  -->
                                                    <%total = total + suma2 / j; %>

                                                    <% }%>
                                                    <!--SE MUESTRA PROMEDIO POR PERIODO  -->
                                                    <td><b class="btn btn-dark"><%=(double) Math.round((total / i) * 100) / 100%></b></td>
                                                        <%t = t + total / i; %>
                                                        <%total = 0.0; %>
                                                        <% }%>
                                                    <!--SE MUESTRA PERIODO POR AÑO  -->
                                                    <td><b class="btn btn-dark"><%=(double) Math.round((t / k) * 100) / 100%></b></td>
                                                </tr>



                                            </tbody>

                                            <% }%>

                                        </table>

                                    </div>
                                </div>
                            </div>
                        </div>
                        
                </div>
                <!-- Row -->
                <!-- Row -->

                <!-- ============================================================== -->
                <!-- End PAge Content -->
                <!-- ============================================================== -->
                <!-- ============================================================== -->
                <!-- Right sidebar -->
                <!-- ============================================================== -->
                <!-- .right-sidebar -->
                <div class="right-sidebar">
                    <div class="slimscrollright">
                        <div class="rpanel-title"> Service Panel <span><i class="ti-close right-side-toggle"></i></span> </div>
                        <div class="r-panel-body">
                            <ul id="themecolors" class="m-t-20">
                                <li><b>With Light sidebar</b></li>
                                <li><a href="javascript:void(0)" data-theme="default" class="default-theme">1</a></li>
                                <li><a href="javascript:void(0)" data-theme="green" class="green-theme">2</a></li>
                                <li><a href="javascript:void(0)" data-theme="red" class="red-theme">3</a></li>
                                <li><a href="javascript:void(0)" data-theme="blue" class="blue-theme working">4</a></li>
                                <li><a href="javascript:void(0)" data-theme="purple" class="purple-theme">5</a></li>
                                <li><a href="javascript:void(0)" data-theme="megna" class="megna-theme">6</a></li>
                                <li class="d-block m-t-30"><b>With Dark sidebar</b></li>
                                <li><a href="javascript:void(0)" data-theme="default-dark" class="default-dark-theme">7</a></li>
                                <li><a href="javascript:void(0)" data-theme="green-dark" class="green-dark-theme">8</a></li>
                                <li><a href="javascript:void(0)" data-theme="red-dark" class="red-dark-theme">9</a></li>
                                <li><a href="javascript:void(0)" data-theme="blue-dark" class="blue-dark-theme">10</a></li>
                                <li><a href="javascript:void(0)" data-theme="purple-dark" class="purple-dark-theme">11</a></li>
                                <li><a href="javascript:void(0)" data-theme="megna-dark" class="megna-dark-theme ">12</a></li>
                            </ul>
                            <ul class="m-t-20 chatonline">
                                <li><b>Chat option</b></li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/1.jpg" alt="user-img" class="img-circle"> <span>Varun Dhavan <small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/2.jpg" alt="user-img" class="img-circle"> <span>Genelia Deshmukh <small class="text-warning">Away</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/3.jpg" alt="user-img" class="img-circle"> <span>Ritesh Deshmukh <small class="text-danger">Busy</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/4.jpg" alt="user-img" class="img-circle"> <span>Arijit Sinh <small class="text-muted">Offline</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/5.jpg" alt="user-img" class="img-circle"> <span>Govinda Star <small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/6.jpg" alt="user-img" class="img-circle"> <span>John Abraham<small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/7.jpg" alt="user-img" class="img-circle"> <span>Hritik Roshan<small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="../assets/images/users/8.jpg" alt="user-img" class="img-circle"> <span>Pwandeep rajan <small class="text-success">online</small></span></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- ============================================================== -->
                <!-- End Right sidebar -->
                <!-- ============================================================== -->
            </div>
            <!-- ============================================================== -->
            <!-- End Container fluid  -->
            <!-- ============================================================== -->
            <!-- ============================================================== -->
            <!-- footer -->
            <!-- ============================================================== -->
            <footer class="footer"> © 2020 SISWA </footer>
            <!-- ============================================================== -->
            <!-- End footer -->
            <!-- ============================================================== -->
        </div>
        <!-- ============================================================== -->
        <!-- End Page wrapper  -->
        <!-- ============================================================== -->
    </div>
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->
    <!-- ============================================================== -->
    <!-- All Jquery -->
    <!-- ============================================================== -->
    <script src="../assets/plugins/jquery/jquery.min.js"></script>
    <!-- Bootstrap tether Core JavaScript -->
    <script src="../assets/plugins/bootstrap/js/popper.min.js"></script>
    <script src="../assets/plugins/bootstrap/js/bootstrap.min.js"></script>

    <!-- slimscrollbar scrollbar JavaScript -->
    <script src="js/jquery.slimscroll.js"></script>
    <!--Wave Effects -->
    <script src="js/waves.js"></script>
    <!--Menu sidebar -->
    <script src="js/sidebarmenu.js"></script>
    <!--stickey kit -->
    <script src="../assets/plugins/sticky-kit-master/dist/sticky-kit.min.js"></script>
    <script src="../assets/plugins/sparkline/jquery.sparkline.min.js"></script>

    <!--Custom JavaScript -->
    <script src="js/custom.min.js"></script>
    <!-- ============================================================== -->

</body>

</html>