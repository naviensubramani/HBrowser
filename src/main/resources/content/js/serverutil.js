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
      $( "#TableList" ).append( "<li ><a href='#''>"+tnm['TableNames'][i]+"<i class='icon-chevron-right'></i></a></li>" );
    }
   
    // alert("Data: " + data + "\nStatus: " + status);
  });

}
