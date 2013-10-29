<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HBrowser</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->

    <script src="js/jquery-latest.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js"></script>
    <!--link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"-->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <script src="js/client.js" type="text/javascript"></script>  
    <script src="js/serverutil.js" type="text/javascript"></script>  


    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }

      .hero-unit {
      padding: 20px;
      margin-bottom: 5px;
      font-size: 16px;
      font-weight: 200;
      }      


      @media (max-width: 980px) {
        /* Enable use of floated navbar text */
        .navbar-text.pull-right {
          float: none;
          padding-left: 5px;
          padding-right: 5px;
        }
      }
    </style>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">HBrowser</a>
          <p class="navbar-text pull-right">
            <a data-toggle="modal" href="#settingsModal" class="navbar-link" >Settings</a></p>
          <div class="nav-collapse collapse">
            <ul class="nav navbar-nav">
              
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

<!--/.Modal pan for settings screen -->
  <div class="modal fade" id="settingsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
          <h4 class="modal-title">Settings</h4>
        </div>
        <div class="modal-body">
          <div>
          <span class="badge">Connection Settings</span>
          <br>
          <br>
            <div>
                <div>hbase.zookeeper.quorum : <input type="text" id="zkQuorum" placeholder="localhost"></div>
                <div>hbase.zookeeper.clientPort : <input type="text" id="zkPort" placeholder="2181" ></div>
            </div> 
          </div>         
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button class="btn" id="clear_config" >Clear</button>          
          <button class="btn btn-primary" id="save" >Save</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->     

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span2">
          <div class="well sidebar-nav" id="tbl_list">
            <ul class="nav nav-list">
              <li class="nav-header">Tables</li>
            </ul>
            <div id="tblName">
            <ul class="nav nav-list" id="TableList">
            </ul>
            </div>
          </div><!--/.well -->
        </div><!--/span-->


        <div class="span10">
          <div class="hero-unit">

            <!--section unit-->
            <div class="tabbable" id="maintabs"> <!-- Only required for left/right tabs -->
              <ul class="nav nav-tabs">
                <li class="active"><a href="#tab1" data-toggle="tab">Scan</a></li>
                <li><a href="#tab2" data-toggle="tab">Create</a></li>
                <li><a href="#tab3" data-toggle="tab">Drop</a></li>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="tab1">
                  <!--Query Contents-->
                  <div class="modal-body">
                  <div id="formdata">
                      <!--form action="/home" method="post"-->
                      <div class="controls" style="display: inline-block;">
                          <select id="sc_cf">
                          </select>
                      </div>                      
                      <div style="display: inline-block; vertical-align: top;">
                        <button class="btn btn-primary" id="scan" ><i class="icon-white icon-hand-right"></i>Scan</button>
                        <a data-toggle="modal" href="#insertModal" class="btn btn-success btn-lg" >Insert</a>
                      </div>
                      <!--/form-->
                  </div>
                  <!-- Data list -->
                  <div>
                      <table class="table table-striped table-bordered table-condensed table-hover" id="dataTable">
                      <!-- Data list populated here-->

                      </table>
                  </div>


                  </div>
                  <!--Query Contents end-->

                <div class="modal fade" id="insertModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Insert Row</h4>
                      </div>
                      <div class="modal-body">
                        <div>Column Qualifier : <select id="isert_cq"> </select></div>
                        <div>Row Key : &nbsp&nbsp&nbsp&nbsp &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type="text" id="insert_rowkey" ></div>
                        <div>Row Value : &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type="text" id="insert_rowvalue"></div>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="insert">Save changes</button>
                      </div>
                    </div><!-- /.modal-content -->
                  </div><!-- /.modal-dialog -->
                </div><!-- /.modal -->

                </div>
                <div class="tab-pane" id="tab2">
                  <form id="createForm" method="POST">
                  <div>Table Name : <input type="text" id="table_name" required></div>
                    <div id="TextBoxesGroup">
                      <div id="TextBoxDiv1">
                          <div id="TextBoxDiv1">Family #1 : <input type="text" id="CF1" required></div>
                      </div>
                    </div>
                    <input type="button" value="Add" id="addButton">
                    <input type="button" value="Remove" id="removeButton">
                    </form>
                    <button class="btn btn-primary" id="create" >Create</button>
                    
                </div> 
                <div class="tab-pane" id="tab3">
                    <div>
                      <div>
                          <div>Table Name : <input type="text" id="dropTableName" placeholder="student"></div>
                      </div>
                    </div>
                    <button class="btn btn-danger" id="drop" >Drop</button>
                  </div>                                         
                </div>
              </div>
            </div>          


    </div>


          </div>
          
          
        </div><!--/span-->
      </div><!--/row-->

      <hr>

      <footer>
        <p>© Hbrowse 2013</p>
      </footer>

    </div>

</body></html>