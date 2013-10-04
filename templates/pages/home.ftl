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
    
  $("#runquery").click(function(){
  qu = $("#query").val()
  get_results(qu,'None');
  });


  $("#create").click(function(){
  tname = $("#table_name").val()
  cfobj['table_name'] = tname;
  get_form_values();
  console.log(cfobj);
  create_table(tname,cfobj);
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

function create_table(tname,cFamily)
{
  console.log(JSON.stringify(cFamily));
  $("#create").button('loading')
  $.post("/createTable",
  {
    tname: tname,
    cFamily: JSON.stringify(cFamily)
  },
  function(data,status){
    console.log(status);
    $("#create").button('reset')
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
                <li class="active"><a href="#tab1" data-toggle="tab">Query</a></li>
                <li><a href="#tab2" data-toggle="tab">Create</a></li>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="tab1">

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
                <div class="tab-pane" id="tab2">
                  <div>Table Name : <input type="text" id="table_name"></div>
                    <div id="TextBoxesGroup">
                      <div id="TextBoxDiv1">
                          <div id="TextBoxDiv1">Family #1 : <input type="text" id="CF1"></div>
                      </div>
                    </div>
                    <input type="button" value="Add" id="addButton">
                    <input type="button" value="Remove" id="removeButton">
                    <button class="btn btn-primary" id="create" >Create</button></div>                    
                </div>
              </div>
            </div>          


    </div>


          </div>
          
          
        </div><!--/span-->
      </div><!--/row-->

      <hr>

      <footer>
        <p>© Company 2013</p>
      </footer>

    </div>

</body></html>