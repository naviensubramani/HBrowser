/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/26/13
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */

function get_cf(dataObj)
{
  console.log(JSON.stringify(dataObj));
  $.post("/getCF",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    // alert("Data: " + data + "\nStatus: " + status);
    var tbcf = JSON.parse(data);
    console.log(tbcf);
    $( "#sc_cf" ).empty();
    for (i=0;i<tbcf.length;i++)
    {
      $( "#sc_cf" ).append( "<option value="+tbcf[i]+">"+tbcf[i]+"</option>" );
    }
  });
}

function drop_table(dataObj)
{
  // Open the full wait screen
  $("#overlay").show();  
  $("#drop").button('loading');
  $.post("/dropTable",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    alert("Data: " + data + "\nStatus: " + status);
    $("#drop").button('reset');
    $('#TableList').html('');
    getTableNames(dataObj); 
    // close the full wait screen
    $("#overlay").hide();        
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
    $('#TableList').html('');
    getTableNames(dataObj);        
  });

}


function getTableNames(dataObj)
{
  console.log(JSON.stringify(dataObj));

  $.post("/listTablesNames",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    var tnm = JSON.parse(data);
    console.log(tnm);
    for (i=0;i<tnm['TableNames'].length;i++)
    {
      $( "#TableList" ).append("<li><div id="+tnm['TableNames'][i]+"><a href='#' onclick='get_cols(this)'>"+tnm['TableNames'][i]+"</a> <span onclick='drop_me(this)' class='label label-important' onmouseover='' style='cursor: pointer;'><i class='fa fa-trash-o'></i></span></div></li>");
    }
   
    // alert("Data: " + data + "\nStatus: " + status);
  });

}

function scanColumnFamily(dataObj)
{
  console.log(JSON.stringify(dataObj));

  $.post("/scanTable",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    $('#dataTable').html('');
    var rows = JSON.parse(data);
    keys = Object.keys(rows);
    keys.sort();
    console.log(rows);
    $( "#dataTable" ).append( "<thead><tr> <th>Row Key</th><th>Column + Cell</th></tr></thead>" );
	for (i = 0; i < keys.length; i++)
	{
	    Rowkey = keys[i];
	    var Rowvalue = JSON.stringify(rows[Rowkey]);
	    $( "#dataTable" ).append( "<tr><td>"+Rowkey+"</td><td>"+Rowvalue+"</td><td><span onclick='drop_row(this)' id='"+Rowkey+"' class='label label-important' onmouseover='' style='cursor: pointer;'><i class='fa fa-trash-o'></i></span></td></tr>" );
	}
  });

}

// Insert Record in to database
function insert_into_table(dataObj)
{
  console.log(JSON.stringify(dataObj));
  $("#insert").button('loading');

   $.post("/insertTable",
   {
     data: JSON.stringify(dataObj)
   },
   function(data,status){
   console.log(status);
   $("#insert").button('reset');
   $("#close").click();
   alert(data);
   });

}

// Drop Row key
function delete_record(dataObj)
{
  // Open the full wait screen
  $("#overlay").show();  
   $.post("/deleteRec",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    alert("Data: " + data + "\nStatus: " + status);
    scanColumnFamily(dataObj);
    // close the full wait screen
    $("#overlay").hide();        
  });

}




function initilize(dataObj)
{
  console.log(JSON.stringify(dataObj));

  $.post("/saveConfig",
  {
    data: JSON.stringify(dataObj)
  },
  function(data,status){
    console.log(status);
    getTableNames(dataObj);
    // alert("Data: " + data + "\nStatus: " + status);
  });

}



function get_cols(obj){
  console.log("List Column Families");
  console.log(obj);
  console.log((obj.parentNode).id);
  console.log((obj.parentNode).parentNode);
  $((obj.parentNode).parentNode).addClass("active").siblings().removeClass("active");
  var cfobj = {};
  cfobj['table_name'] = (obj.parentNode).id
  get_cf(cfobj);    
}


// Drop The selected table
function drop_me(obj){
  console.log("Drop Table");
  console.log((obj.parentNode).id);
if (confirm('Are you sure you want to Drop the database?')) {
    // Drop it!
    var cfobj = {};
    cfobj['table_name'] = (obj.parentNode).id;
    drop_table(cfobj);        

} else {
    // Do nothing!
};  

}

// Drop the selected Rowkey
function drop_row(obj){
  console.log("Drop row key");
  console.log(obj.id);
if (confirm('Are you sure you want to Drop?')) {
    // Drop it!
    var cfobj = {};
    cfobj['table_name'] = $("#tblName li.active a").text();
    cfobj['column_family'] = $("#sc_cf").val();
    cfobj['row_key'] = obj.id;
    delete_record(cfobj);        

} else {
    // Do nothing!
};  

}
