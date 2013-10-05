<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>HBrowser</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->

<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>

<script src="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<!--link href="https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"-->



    <link href="http://getbootstrap.com/2.3.2/assets/css/bootstrap.css" rel="stylesheet">
    <link href="http://getbootstrap.com/2.3.2/assets/css/bootstrap-responsive.css" rel="stylesheet">

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
<!--Script for dynamic text box-->
<script type="text/javascript">

    $(document).ready(function(){

      var counter = 2;
      var cfobj = {};
      get_conn_config();
    
      $("#addButton").click(function () {
        
      if(counter>10){
            alert("Only 10 fields allowed");
            return false;
        }   
      
      var newTextBoxDiv = $(document.createElement('div')).attr("id", 'TextBoxDiv' + counter);
                newTextBoxDiv.after().html('Family #'+ counter + ' : ' +
        '<input type="text" name="textbox' + counter + 
        '" id="CF' + counter + '" value="" >');
            
      newTextBoxDiv.appendTo("#TextBoxesGroup");
        
        counter++;
      });

      $("#removeButton").click(function () {
        if(counter==1){
            alert("No more textbox to remove");
            return false;
        }   
          counter--;
      
          $("#TextBoxDiv" + counter).remove();
    });


      function get_form_values()
      {
      var msg = '';
      var columnF_vals = [];

      for(i=1; i<counter; i++){
        msg += "\n Column Family #" + i + " : " + $('#CF' + i).val();
        columnF_vals.push($('#CF' + i).val());
      }
          cfobj['column_family'] = columnF_vals;
      }      
    
    function set_conn_config(conObj)
    {
      if (typeof(localStorage) == 'undefined' ) {
        alert('Your browser does not support HTML5 localStorage. Try upgrading.');
      } else {
        try {
          localStorage.removeItem("conn_config"); //deletes the matching item from the database
          localStorage.setItem('conn_config', JSON.stringify(conObj)); //saves to the database, "key", "value"
          alert('Successfully Saved!');
        } catch (e) {
           if (e == QUOTA_EXCEEDED_ERR) {
             alert('Quota exceeded!'); //data wasn't successfully saved due to quota exceed so throw an error
          }
        }
      }      
    }      

    function get_conn_config()
    {
      if (localStorage.getItem("conn_config") === null) {
          alert('Please enter Connection details!');
      }
      else
      {
        var confObj = JSON.parse(localStorage.getItem('conn_config'));
        console.log('confObj: ', confObj);  
        $("#zkQuorum").val(confObj.zkQuorum);
        $("#zkPort").val(confObj.zkPort);
      }      
    }


    function get_config()
    {
      var confObj = {};
      confObj['zkQuorum'] = $("#zkQuorum").val();
      confObj['zkPort'] = $("#zkPort").val();
      return confObj;
    }    

  $("#runquery").click(function(){
  qu = $("#query").val()
  get_results(qu,'None');
  });


  $("#create").click(function(){
  cfobj['table_name'] = $("#table_name").val();
  cfobj['conn'] = get_config();
  get_form_values();
  create_table(cfobj);
  });  

  $("#save").click(function(){
    var confObj = get_config();
    set_conn_config(confObj);
    var retrievedObject = localStorage.getItem('conn_config');
    console.log('retrievedObject: ', JSON.parse(retrievedObject));  
  }); 

  $("#drop").click(function(){
    var cfobj = {};
    cfobj['table_name'] = $("#dropTableName").val();
    cfobj['conn'] = get_config();
    drop_table(cfobj);
   });   

  $("#clear_config").click(function(){
    localStorage.removeItem("conn_config"); 
    $("#zkQuorum").val('');
    $("#zkPort").val('');

    console.log('Connection Configuration cleared !');  
  });     

    
  });
</script> 


<script>
function get_results(qus,bqus)
{
  $.post("/get_result",
  {
    qu: qus,
    bqu: bqus
  },
  function(data,status){
    console.log(status);
    // alert("Data: " + data + "\nStatus: " + status);
  });

}

function drop_table(dataObj)
{
  $("#drop").button('loading');
  $.post("/dropTable",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    alert("Data: " + data + "\nStatus: " + status);
    $("#drop").button('reset');
  });

}


function create_table(dataObj)
{
  console.log(JSON.stringify(dataObj));
  $("#create").button('loading');
  $.post("/createTable",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    $("#create").button('reset');
    alert("Data: " + data + "\nStatus: " + status);
  });

}


</script>

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
          <div class="nav-collapse collapse">
            <p class="navbar-text pull-right">
              Welcome <a href="#" class="navbar-link">Naveen</a>
            </p>
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#">About</a></li>
              <li><a href="#">Contact</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span2">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li class="nav-header">Tables</li>
              <li><a href="#">Table 1 <i class="icon-chevron-right"></i></a></li>
              <li><a href="#">Table 2<i class="icon-chevron-right"></i></a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->


        <div class="span10">
          <div class="hero-unit">

            <!--section unit-->
            <div class="tabbable"> <!-- Only required for left/right tabs -->
              <ul class="nav nav-tabs">
                <li class="active"><a href="#tab1" data-toggle="tab">Config</a></li>
                <li><a href="#tab2" data-toggle="tab">Query</a></li>
                <li><a href="#tab3" data-toggle="tab">Create</a></li>
                <li><a href="#tab4" data-toggle="tab">Drop</a></li>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="tab1">
                    <div>
                      <div>
                          <div>hbase.zookeeper.quorum : <input type="text" id="zkQuorum" placeholder="localhost"></div>
                          <div>hbase.zookeeper.clientPort : <input type="text" id="zkPort" placeholder="2181" ></div>
                      </div>
                    </div>
                    <button class="btn btn-primary" id="save" >Save</button>
                    <button class="btn" id="clear_config" >Clear</button>
                  </div>                    


                <div class="tab-pane" id="tab2">

                  <!--Query Contents-->
                  <div class="modal-body">
                  <div id="formdata">
                      <!--form action="/home" method="post"-->
                      <textarea class="form-control" id="query" name="query" placeholder="Enter Hbase Query" rows="3" style="width: 100%;" required></textarea>
                      <div>
                      <button class="btn btn-primary" id="runquery" ><i class="icon-white icon-hand-right"></i> Run Query</button></div>
                      <!--/form-->
                  </div>
                  <!-- Data list -->
                  <div>
                      <table class="table table-striped table-bordered table-condensed table-hover">
                      <thead><tr> <th>Letter</th><th>Phonetic Letter</th></tr></thead>
                      <tr><td>A</td><td>Alpha</td></tr>
                      <tr><td>B</td><td>Bravo</td></tr>
                      <tr><td>C</td><td>Charlie</td></tr>
                      <tr><td>D</td><td>James</td></tr>                      
                      <tr><td>E</td><td>Michle</td></tr>                      
                      </table>
                  </div>


                  </div>
                  <!--Query Contents end-->
                </div>
                <div class="tab-pane" id="tab3">
                  <div>Table Name : <input type="text" id="table_name"></div>
                    <div id="TextBoxesGroup">
                      <div id="TextBoxDiv1">
                          <div id="TextBoxDiv1">Family #1 : <input type="text" id="CF1"></div>
                      </div>
                    </div>
                    <input type="button" value="Add" id="addButton">
                    <input type="button" value="Remove" id="removeButton">
                    <button class="btn btn-primary" id="create" >Create</button>
                </div> 

                <div class="tab-pane" id="tab4">
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