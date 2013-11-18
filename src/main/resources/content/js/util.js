/**
 * Created with IntelliJ IDEA.
 * User: naveen
 * Date: 10/24/13
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
    $(document).ready(function(){

        var counter = 1;
        jQuery('a.add-item').click(function(event){
            event.preventDefault();
            counter++;
            var newRow = jQuery('<tr><td><input type="text" placeholder="Row Key" name="cq_' +
                counter + '"/></td><td><input type="text" placeholder="Value" name="value_' +
                counter + '"/></td><td><span onclick="deleteme(this)" onmouseover="" style="cursor: pointer;vertical-align: 10px;"><i class="fa fa-trash-o fa-lg"></i></span></td></tr>');
            jQuery('table.items-list').append(newRow);
        });

  });

function deleteme(obj){
    console.log(obj);
    console.log(obj.parentNode);
    $(obj.parentNode).parents('tr').first().remove();
}