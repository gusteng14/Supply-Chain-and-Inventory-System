$(document).ready(function() {
    var vars = {};
    var i = 0;

    var dtlCount = 0;

    $('button[name^="removeRow"]').each(function(index) {
        dtlCount++;
    });

    if(dtlCount == 1) {
        $('button[name^="removeRow"]').each(function(index) {
            $(this).prop('disabled', true);
        })
    } else {
        $('button[name^="removeRow"]').each(function(index) {
            $(this).prop('disabled', false);
        })
    }

    $('#registerForm').on('change load', function() {
        var cost = parseFloat($('#itemList option:selected').attr('data-cost'));
        var qty = parseFloat($('#qty').val());
        var total = cost * qty;

        let PHP = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'PHP',
        });


        $('#total').val(total);

        var imgId = $('#itemList option:selected').attr('data-id');
        $('#product-image').attr('src', '/' + imgId + '/' + 'product_image');

        //            vars['cost'+i] = parseFloat($('#itemList' + i +' option:selected').attr('data-cost'));
        //            console.log("Cost " + i + ": " + vars['cost'+i])
        //            vars['qty'+i] = parseFloat($('#qty'+i).val());
        //            console.log("Qty " + i + ": " + vars['qty'+i])
        //            vars['total'+i] = vars['cost'+i] * vars['qty'+i];
        //            $('#total'+i).val(vars['total'+i]);
        //
        //            vars['imgId'+i] = $('#itemList' + i + ' option:selected').attr('data-id');
        //            $('#product-image'+i).attr('src', '/' + vars['imgId'+i] + '/' + 'product_image');

        var grandTotal = 0;
        $("input[name='total[]']").each(function(index) {
            var value = parseInt($(this).val());
            grandTotal += value;
        });

        if(!isNaN(grandTotal)) {
            $('#grandTotal').html(PHP.format(grandTotal));
        } else {
            $('#grandTotal').html('- - - - -');
        }

        var dtlCount = 0;

        $('button[name^="removeRow"]').each(function(index) {
            dtlCount++;
        });

        if(dtlCount == 1) {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', true);
            })
        } else {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', false);
            })
        }


    });

    $('#add').on('click change', function() {
        i++;

        $('#requestDetail').append(
            "<tr class='newRow'>"
            + "<td class='img' style='padding-right: 0px; margin-right: 0px;'><a><img class='productImg' id='product-image" + i +"' src=''></a></td>"
            + "<td class='item' id='selectRow" + i +"'></td>"
            + "<td class='quantity'><input class='archivo-narrow-a' style='color: black;'  id='qty" + i +"' name='qty[]' type='number' required></td>"
            + "<td class='total'><input readonly class='archivo-narrow-a ' style='color: black;' id='total" + i +"' name='total[]' type='number' required></td>"
            + "<td><button type='button' id='removeRow' name='removeRow' class='btn red right removeRow'><i class='material-icons-round left'>delete</i>Remove</button></td>"
            + "</tr>");
        $('select#itemList').clone().attr('id','itemList'+i).css('display','block').appendTo('#selectRow' + i);

        var dtlCount = 0;

        $('button[name^="removeRow"]').each(function(index) {
            dtlCount++;
        });

        if(dtlCount == 1) {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', true);
            })
        } else {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', false);
            })
        }
    });

    $(document).on('change', '.newRow', '#registerForm', function() {
        var costx = parseFloat($(this).children('.item').children('select').children('option:selected').attr('data-cost'));
        var qtyx = parseFloat($(this).children('.quantity').children().val());
        var totalx = costx * qtyx;

        var imgIdX = $(this).children('.item').children('select').children('option:selected').attr('data-id');
        $(this).children('.img').children().children().attr('src', '/' + imgIdX + '/' + 'product_image');

        $(this).children('.total').children().val(totalx);

        var grandTotal = 0;

        let PHP = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'PHP',
        });

        $("input[name='total[]']").each(function(index) {
            var value = parseInt($(this).val());
            grandTotal += value;
        });

        if(!isNaN(grandTotal)) {
            $('#grandTotal').html(PHP.format(grandTotal));
        } else {
            $('#grandTotal').html('- - - - -');
        }

        var dtlCount = 0;
        $('button[name^="removeRow"]').each(function(index) {
            dtlCount++;
        });

        if(dtlCount == 1) {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', true);
            })
        } else {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', false);
            })
        }


        console.log('ImgIdX: ' + imgIdX);
        console.log('CostX: ' + costx);
        console.log('QtyX: ' + qtyx);
        console.log('Total: ' + totalx);
        console.log('Grand Total: ' + grandTotal);


    });

    $(document).on('click', 'button[id^="removeRow"]', function() {
        $(this).parent().parent().remove();

        var dtlCount = 0;

        $('button[name^="removeRow"]').each(function(index) {
            dtlCount++;
        });

        if(dtlCount == 1) {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', true);
            })
        } else {
            $('button[name^="removeRow"]').each(function(index) {
                $(this).prop('disabled', false);
            })
        }

        var grandTotal = 0;

        let PHP = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'PHP',
        });

        $("input[name='total[]']").each(function(index) {
            var value = parseInt($(this).val());
            grandTotal += value;
        });

        if(!isNaN(grandTotal)) {
            $('#grandTotal').html(PHP.format(grandTotal));
        } else {
            $('#grandTotal').html('- - - - -');
        }
    });

});