/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/24/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
    $(document).ready(function(){

      var counter = 2;
      var cfobj = {};
      get_conn_config();


      if (localStorage.getItem("conn_config") != null) {
          console.log('Querying Database Tables');
          cfobj['conn'] = get_config();
          initilize(cfobj);
      }  

    
      $("#addButton").click(function () {
        
      if(counter>10){
            alert("Only 10 fields allowed");
            return false;
        }   
      
      var newTextBoxDiv = $(document.createElement('div')).attr("id", 'TextBoxDiv' + counter);
                newTextBoxDiv.after().html('Family #'+ counter + ' : ' +
        '<input type="text" name="textbox' + counter + 
        '" id="CF' + counter + '" value="" required>');
            
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

	  function get_insert_values()
	  {
	  	var RowCount = $("#insert_table tr").length;
	  	var columnF_vals = [];
		var obj = {};
		var obj1 = {};
		obj['table_name'] = $("#tblName li.active a").text();
    	obj['column_family'] = $("#sc_cf").val();
    	obj['row_key'] = $("#row_key").val();

      	for(i=1; i<RowCount; i++){
        	obj1[$('#qualifier_' + i).val()] = $('#value_' + i).val();;
      	}
    	obj['values'] = obj1;
        insert_into_table(obj);
	}
	  

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
          alert('Please Configure Connection details in Settings!');
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

  $("#scan").click(function(){
    var cfobj = {};
    cfobj['table_name'] = $("#tblName li.active a").text();
    cfobj['column_family'] = $("#sc_cf").val();
    scanColumnFamily(cfobj);
  });
  
  $("#close").click(function(){
    $("#scan").click();
  });
  
  $("#insert_screen").click(function(){
	  $("#coln_fam").val($("#sc_cf").val());
  });

  $("#insert").click(function(){
  	  var temp = $("#sc_cf").val();
	  $("#coln_fam").val(temp);
  	  get_insert_values();
  });


  $("#create").click(function(){
      cfobj['table_name'] = $("#table_name").val();
      cfobj['conn'] = get_config();
      get_form_values();
      create_table(cfobj);    
  });


  $("#save").click(function(){
    var cfobj = {};
    var confObj = get_config();
    set_conn_config(confObj);
    var retrievedObject = localStorage.getItem('conn_config');
    console.log('retrievedObject: ', JSON.parse(retrievedObject)); 
    cfobj['conn'] = get_config();
    initilize(cfobj);
    $("#close_settings").click();
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