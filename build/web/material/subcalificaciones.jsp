<%@page import="jdk.nashorn.internal.ir.RuntimeNode.Request"%>
<%@page import="Entidades.Calificacion"%>
<%@page import="java.util.List"%>
<%@page import="Controladores.CalificacionJpaController"%>
<%@page import="Entidades.Subcalificacion"%>
<%@page import="Controladores.SubcalificacionJpaController"%>
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
        <title>Sistema Web Acad�mico</title>
        <!-- Bootstrap Core CSS -->
        <link href="../assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        
        <!-- Custom CSS -->
        <link href="css/style.css" rel="stylesheet">
        <!-- You can change the theme colors from here -->
        <link href="css/colors/blue.css" id="theme" rel="stylesheet">
        <!--alerts CSS -->
        <link href="../assets/plugins/sweetalert/sweetalert.css" rel="stylesheet" type="text/css">
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    </head>

    <body class="fix-header fix-sidebar card-no-border">



        <%  String usuario = "";

            int clase = Integer.parseInt(request.getParameter("clase"));
            int cal = Integer.parseInt(request.getParameter("cal"));
            Calificacion calificacion = null;
            Clase c = null;
            Profesor profesor = null;
            if (session.getAttribute("rol") == "profesor" & session.getAttribute("profesor") != null) {
                profesor = (Profesor) session.getAttribute("profesor");
                usuario = profesor.getUsuario();

                if (profesor.getClaseList().contains(new Clase(clase))) {
                    ClaseJpaController cc = new ClaseJpaController();
                    c = cc.findClase(clase);
                    if (c.getCalificacionList().contains(new Calificacion(cal))) {
                        CalificacionJpaController scc = new CalificacionJpaController();
                        calificacion = scc.findCalificacion(cal);

                    } else {
                        response.sendRedirect("inicio-profesor.jsp");
                    }
                } else {
                    response.sendRedirect("inicio-profesor.jsp");

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

                            <!-- ============================================================== -->
                        </ul>
                        <!-- ============================================================== -->
                        <!-- User profile and search -->
                        <!-- ============================================================== -->
                        <ul class="navbar-nav my-lg-0">
                            <!-- ============================================================== -->

                            <!-- ============================================================== -->

                            <!-- ============================================================== -->
                            <!-- Profile -->
                            <!-- ============================================================== -->
                           <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-muted waves-effect waves-dark" href="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img src="../../colegios/archivos/<%=profesor.getImagen() %>" alt="user" class="profile-pic" /></a>
                                <div class="dropdown-menu dropdown-menu-right scale-up">
                                    <ul class="dropdown-user">
                                        <li>
                                            <div class="dw-user-box">
                                                <div class="u-img"><img src="../../colegios/archivos/<%=profesor.getImagen() %>" alt="user"></div>
                                                <div class="u-text">
                                                    <h4><%=profesor.getUsuario()%></h4>
                                                    <br>
                                                    <a href="#" class="btn btn-rounded btn-danger btn-sm">Ver perfil</a></div>
                                            </div>
                                        </li>
                                        <li role="separator" class="divider"></li>
                                        <li><a href="#"><i class="ti-user"></i> <%=profesor.getNombre() %></a></li>
                                     
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
                    <div class="user-profile" style="background: url(../assets/images/background/user-info.jpg) no-repeat;">
                        <!-- User profile image -->
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
                            <li class="nav-small-cap">PERSONAL</li>
                            <li> <a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false"><i class="mdi mdi-gauge"></i><span class="hide-menu">Clases </span></a>
                                        <%AnoJpaController ac = new AnoJpaController(); %>
                                <ul aria-expanded="false" class="collapse">
                                    <%for (Ano a : ac.findAnoEntities()) {%>
                                    <li><a href="clases.jsp?a=<%=a.getId()%>"><%=a.getDescripcion()%></a></li>
                                        <% }%>
                                </ul>
                            </li>
                            <li> <a class=" waves-effect waves-dark" href="calificaciones.jsp?clase=<%=request.getParameter("clase")%>" aria-expanded="false"><i class="mdi mdi-view-grid"></i><span class="hide-menu">Calificaciones </span></a> </li>





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
                        <div class="col-md-auto col-20 align-self-center">
                            <h3 class="text-themecolor">Profesor</h3>
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="clase.jsp?clase=<%=c.getId()%>"><%=c.getIdcurso().getNombre() + " " + c.getIdseccion().getIdgrado().getNombre() + " " + c.getIdseccion().getNombre() + " "%></a></li>
                                <li class="breadcrumb-item active"><a href="calificaciones.jsp?clase=<%=c.getId()%>">Calificaciones</a></li>

                                <li class="breadcrumb-item"><%=calificacion.getDescripcion()%></li>

                            </ol>
                        </div>

                    </div>
                    <!-- ============================================================== -->
                    <!-- End Bread crumb and right sidebar toggle -->
                    <!-- ============================================================== -->
                    <!-- ============================================================== -->
                    <!-- Start Page Content -->
                    <!-- ============================================================== -->


                    <div class="row">
                        <div class="col-12">
                            <div class="card">

                                <div class="card-body">


                                    <h4 class="card-title"><%=calificacion.getDescripcion()%></h4>

                                    <button  data-toggle="modal" data-target="#ModalRG" class="btn btn-dropbox" style="position: absolute;top: 3%;right: 1%">A�adir</button>
                                    <input type="hidden" id="calificacion" value="<%=request.getParameter("cal")%>"> 
                                    <input type="hidden" id="clase" value="<%=request.getParameter("clase")%>"> 

                                    <h6 class="card-subtitle"></h6>
                                    <div class="table-responsive m-t-40">
                                        <table id="tableCalificacion" class="display nowrap table table-hover table-striped table-bordered text-center text-dark" cellspacing="0" width="100%">
                                            <thead>
                                                <tr>
                                                    <th>Descripci�n</th>
                                                    <th></th>

                                                    <th></th>
                                                </tr>
                                            </thead>

                                            <tbody>


                                            </tbody>
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
                <footer class="footer"> � 2020 SISWA </footer>
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
       
        <!-- ============================================================== -->
        <!-- This is data table -->
        <script src="../assets/plugins/datatables/jquery.dataTables.min.js"></script>
        <!-- start - This is for export functionality only -->
        <script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
        <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
        <script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
        <script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
        <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
        <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
        <!-- end - This is for export functionality only -->
       
        <!-- Sweet-Alert  -->
        <script src="../assets/plugins/sweetalert/sweetalert.min.js"></script>
        <script src="../assets/plugins/sweetalert/jquery.sweet-alert.custom.js"></script>

        <script >
            $(document).on('submit', '#formRgrado', function () {
                $.ajax({
                    type: 'POST',
                    url: '../RegistrarSubcalificacion',
                    data: {
                        descripcion: $("#nombreRG").val(),
                        calificacion: $("#calificacion").val()
                    },
                    success: function (result) {
                        if (result == ":)") {
                            swal("Correcto", result, "success");
                        } else {
                            swal("Error", result, "error");
                        }


                        document.forms[0].reset();
                        $('#tableCalificacion').DataTable().ajax.reload();
                        $('#ModalRG').modal("hide");
                    }
                });
                return false;
            });
            $(document).on('submit', '#formEgrado', function () {
                $.ajax({
                    type: 'POST',
                    url: '../EditarSubcalificacion',
                    data: {
                        id: $("#idEG").val(),
                        descripcion: $("#nombreEG").val(),
                        calificacion: $("#calificacion").val()
                    },
                    success: function (result) {
                        $('#ModalEG').modal("hide");
                        if (result == ":)") {
                            swal("Correcto", result, "success");
                        } else {
                            swal("Error", result, "error");
                        }
                        $('#tableCalificacion').DataTable().ajax.reload();
                    }
                });
                return false;
            });
            $(document).on('submit', '#formELgrado', function () {
                $.ajax({
                    type: 'POST',
                    url: '../EliminarNotas',
                    data: {
                        id: $("#idELG").val()
                    },
                    success: function () {
                        $.ajax({
                            type: 'POST',
                            url: '../EliminarSubcalificacion',
                            data: {
                                id: $("#idELG").val()
                            },
                            success: function (result) {
                                $('#ModalELG').modal("hide");
                                if (result == ":)") {
                                    swal("Correcto", result, "success");
                                } else {
                                    swal("Error", result, "error");
                                }
                                $('#tableCalificacion').DataTable().ajax.reload();
                            }
                        });
                    }
                });
                return false;
            });
            $('#tableCalificacion').DataTable({
                dom: 'Bfrtip',
                responsive: true,
                language: {
                    "decimal": "",
                    "emptyTable": "No hay datos",
                    "info": "Mostrando _START_ a _END_ de _TOTAL_ datos",
                    "infoEmpty": "Mostrando 0 datos",
                    "infoFiltered": "(Filtrado de _MAX_ total entradas)",
                    "infoPostFix": "",
                    "thousands": ",",
                    "lengthMenu": "Mostrar _MENU_ datos",
                    "loadingRecords": "Cargando...",
                    "processing": "Procesando...",
                    "search": "Buscar:",
                    "zeroRecords": "Sin resultados encontrados",
                    "paginate": {
                        "first": "Primero",
                        "last": "Ultimo",
                        "next": "Siguiente",
                        "previous": "Anterior"
                    }
                },
                buttons: [
                    'copy', 'csv', 'excel', 'pdf', 'print'
                ],
                ajax: {
                    url: "../ListarSubcalificacion?calificacion=" + $("#calificacion").val() + "&clase=" + $("#clase").val(),
                    method: "post",
                    dataSrc: "datos"

                },
                columns: [
                    {data: "descripcion"},
                    {data: "editar"},
                    {data: "eliminar"}
                ]

            });
            function editarCalificacion(id, nombre) {

                $("#ModalEG").modal("show");
                $("#nombreEG").val(nombre);
                $("#idEG").val(id);
            }
            ;
            function eliminarCalificacion(id) {

                $("#ModalELG").modal("show");
                $("#idELG").val(id);
            }
            ;

        </script>


        <!-- sample modal content -->
        <div id="ModalRG" class="modal fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
                        <h4 class="modal-title">Registrar Subcalificaci�n</h4>
                    </div>
                    <form id="formRgrado">
                        <div class="modal-body">

                            <div class="form-group">
                                <label for="recipient-name" class="control-label">Descripci�n</label>
                                <input type="text" class="form-control" id="nombreRG" required />
                            </div>




                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cerrar</button>
                            <button type="submit" class="btn btn-danger waves-effect waves-light">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- /.modal -->


        <!-- sample modal content -->
        <div id="ModalEG" class="modal fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
                        <h4 class="modal-title">Editar grado</h4>
                    </div>
                    <form id="formEgrado">
                        <div class="modal-body">

                            <div class="form-group">
                                <label for="recipient-name" class="control-label">Descripci�n</label>
                                <input type="text" class="form-control" id="nombreEG" required />
                                <input type="hidden" class="form-control" id="idEG" required />


                            </div>



                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cerrar</button>
                            <button type="submit" class="btn btn-danger waves-effect waves-light">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- /.modal -->
        <!-- sample modal content -->
        <div id="ModalELG" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
                        <h4 class="modal-title">Eliminar subcalificaci�n</h4>
                    </div>
                    <form id="formELgrado">
                        <div class="modal-body">

                            <div class="form-group">
                                <label for="recipient-name" class="control-label text-dark">�Est� seguro de eliminar este subcalificaci�n?</label>

                                <input  class="form-control" id="idELG" type="hidden" />
                            </div>


                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default waves-effect" data-dismiss="modal">Cerrar</button>
                            <button type="submit" class="btn btn-danger waves-effect waves-light">Eliminar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- /.modal -->
    </body>

</html>