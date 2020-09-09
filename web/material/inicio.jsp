<!DOCTYPE html>
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

    <body>
        <!-- ============================================================== -->

        <%
            if (session.getAttribute("alumno") != null) {
                response.sendRedirect("inicio-alumno.jsp");
            }
            if (session.getAttribute("profesor") != null) {
                response.sendRedirect("inicio-profesor.jsp");
            }
            if (session.getAttribute("apoderado") != null) {
                response.sendRedirect("inicio-apoderado.jsp");
            }

        %>
        <!-- Preloader - style you can find in spinners.css -->
        <!-- ============================================================== -->
        <div class="preloader">
            <svg class="circular" viewBox="25 25 50 50">
            <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" /> </svg>
        </div>
        <!-- ============================================================== -->
        <!-- Main wrapper - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <section id="wrapper">
           
            <div class="login-register row" style="background-image:url(../imagenes/inicio.jfif);">  
               
                <div class="login-box ">
                    <div class="card-body">
                       
                            <h3 class="box-title m-b-20"></h3>
                          <a class="btn-label btn-facebook btn-lg    " style="position: absolute;left:33%;top:5% " href="#"><icon class="mdi mdi-book-open-variant"></icon> Sistema Web Académico - C.P. MANUEL ANTONIO RIVAS </a>
                            <div class="form-group">
                                <div class="col-md-12">
                             
                                    <br>
                                    <div class="form-group text-center m-t-20">
                                        <div class="col-xs-12">
                                            <a class="btn btn-info btn-lg btn-block text-uppercase waves-effect waves-light" href="login-profesor.jsp"><icon class="mdi mdi-book-open-variant"></icon> Profesor</a>
                                        </div>
                                    </div>




                                </div>
                            </div>
                       
                    </div>
                </div>
                <div class="login-box ">
                    <div class="card-body">
                        
                            <h3 class="box-title m-b-20"></h3>
                      
                            <div class="form-group">
                                <div class="col-md-12">
                               
                                    <br>
                                    <div class="form-group text-center m-t-20">
                                        <div class="col-xs-12">
                                            <a class="btn btn-info btn-lg btn-block text-uppercase waves-effect waves-light" href="login-alumno.jsp"><icon class="fa fa-graduation-cap"></icon> Alumno</a>
                                        </div>
                                    </div>




                                </div>
                            </div>
                       
                    </div>
                </div>
                <div class="login-box ">
                    <div class="card-body">
                   
                            <h3 class="box-title m-b-20"></h3>
                     
                            <div class="form-group">
                                <div class="col-md-12">
                               
                                    <br>
                                    <div class="form-group text-center m-t-20">
                                        <div class="col-xs-12">
                                            <a class="btn btn-info btn-lg btn-block text-uppercase waves-effect waves-light" href="login-apoderado.jsp"><icon class="fa fa-user"></icon> Apoderado</a>
                                        </div>
                                    </div>




                                </div>
                            </div>
                       
                    </div>
                </div>
                </div>
        </section>

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