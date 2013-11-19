<!DOCTYPE html>
<html lang="en"><head>
    <title>HBrowser</title>

    <!-- Le styles -->

    <script src="js/jquery-latest.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js"></script>
    <!--link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"-->
    <link href="css/bootstrap.css" rel="stylesheet"></link>
    <link href="css/bootstrap-responsive.css" rel="stylesheet"></link>
    <script src="js/client.js" type="text/javascript"></script>  
    <script src="js/serverutil.js" type="text/javascript"></script>  
    <script src="js/util.js" type="text/javascript"></script> 
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

</head>

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

      .active>div>a {
                color: #ffffff;
        text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.2);
        background-color: #0088cc;
      }

        /* For blocking the screen */
      #overlay {
          position:fixed;
          top: 0;
          right: 0;
          bottom: 0;
          left: 0;
          background-color:#000;
          opacity: .75;
          z-index: 9999999;
          text-align: center;
          font: bold 55px/200px Helvetica, Sans-Serif;
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
    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span2">
          <div class="well sidebar-nav" id="tbl_list">
            <ul class="nav nav-list">
              <li class="nav-header">Tables</li>
            </ul>
            <div id="tblName">
            <ul class="nav nav-list" id="TableList" style="display: inline-block;">
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
                <li><a href="#tab3" data-toggle="tab" style="display: none;">Drop</a></li>
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
                        <button class="btn btn-primary" id="scan" >Scan</button>
                        <a data-toggle="modal" href="#insertModal" id="insert_screen" class="btn btn-success btn-lg" >Insert</a>
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

                </div>
                <div class="tab-pane" id="tab2">
                  <form id="createForm" method="POST">
                  <div>Table Name : <input type="text" id="table_name" required="true"/></div>
                    <div id="TextBoxesGroup">
                      <div id="TextBoxDiv1">
                          <div id="TextBoxDiv1">Family #1 : <input type="text" id="CF1" required="true"/></div>
                      </div>
                    </div>
                    <input type="button" value="Add" id="addButton"/>
                    <input type="button" value="Remove" id="removeButton"/>
                    </form>
                    <button class="btn btn-primary" id="create" >Create</button>
                    
                </div> 
                <div class="tab-pane" id="tab3">
                    <div>
                      <div>
                          <div>Table Name : <input type="text" id="dropTableName" placeholder="student"/></div>
                      </div>
                    </div>
                    <button class="btn btn-danger" id="drop" >Drop</button>
                  </div>                                         
                </div>
              </div>
            </div>          
          </div>
</div>
    </div>
          
          
      <footer>
        <p>© Hbrowse 2013</p>
      </footer>
<!--/.Modal pan for settings screen -->
  <div class="modal fade panel" id="settingsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
          <h4 class="modal-title">Settings</h4>
        </div>
        <div class="modal-body">
          <div>
          <span class="label label-info">Connection Settings</span>
          <br></br>
            <div>
              <table>
                <tr><td><strong>hbase.zookeeper.quorum</strong></td></tr>
                <tr><td><input type="text" id="zkQuorum" placeholder="localhost"/></td></tr>
                <tr><td><strong>hbase.zookeeper.clientPort</strong></td></tr>
                <tr><td><input type="text" id="zkPort" placeholder="2181"/></td></tr>
                <tr><td><strong>hbase.master</strong></td></tr>
                <tr><td><input type="text" id="hbMaster" placeholder="localhost"/></td></tr>
              </table>
            </div> 
          </div>         
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" id="close_settings">Close</button>
          <button class="btn btn-warning" id="clear_config" >Clear</button>          
          <button class="btn btn-success" id="save" >Save</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->

<!--/.Modal pan for Insert screen -->
  <div class="modal fade panel" id="insertModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" id="close" data-dismiss="modal" aria-hidden="true"></button>
          <h4 class="modal-title">Insert Row</h4>
        </div>
        <div class="modal-body">
            <!--Content for adding multiple rows-->
            <table class="items-list" id="insert_table">
                <tr id="insert_table1"><td><input type="text" id="coln_fam" readonly="true" placeholder="Column Family"/></td><td><input type="text" id="row_key" placeholder="Row Key"/></td></tr>
                <tr id="insert_table2"><td><input type="text" name="qualifier_1" id="qualifier_1" placeholder="Column Qualifier" /></td><td><input type="text" name="value_1" id="value_1" placeholder="Value"/></td><td></td></tr>
            </table>
            <a href="#" title="" class="add-item">Add</a>
            </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" id="insert">Insert</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->        
<div id="overlay" style="display:none;">Loading ...</div>
</body></html>