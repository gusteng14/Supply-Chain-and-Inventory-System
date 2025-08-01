
//
//var collapsibleElem = document.querySelector('.collapsible');
//var collapsibleInstance = M.Collapsible.init(collapsibleElem, {});

$(document).ready(function() {

    var subtotal = 0;
    var etc = 0;
    var total = 0;
    var selected = 0;

    $(document).on('click', '#transactionHistoryBtn', function() {
        if($('#orderDetails').hasClass('active')) {
            $('#orderDetails').removeClass('active');
            $('#transactionHistory').addClass('active');
            $('#order-history-header').text("Transaction History")
        } else {
            $('#orderDetails').addClass('active');
            $('#transactionHistory').removeClass('active');
            $('#order-history-header').text("Order List")

        }
    });

    var PHP = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'PHP',
    });

    $(document).on('click', '.add-order', function() {
        total = 0;
        subtotal = 0;
        var productId = $(this).children('p[name^="productId"]').text();
        var productImg = "/" + productId + "/product_image";
        var productName = $(this).children('.card-content').children('p[name^="productName"]').text();
        var productPrice = $(this).children('.card-action').children('span[name^="productPrice"]').text();
        console.log('name: ' + productName, 'price: ' + productPrice);

        if(!$(this).hasClass('selected')) {
            selected++;
            $(this).addClass('selected');
            $('#order-list').append('<div class="row p'+ productId + '" data-id="' + productId +'" >'
            + '<div style="margin: 0px; padding: 0px;" class=row><div style="float: right;" class=col l12>'
            + '<span><i class="material-icons-round delete-order">cancel</i><p class="hiddenId" hidden="hidden">' + productId + '</p></span></div></div>'
            + '<div class="col l3" style="width: fit-content; height: fit-content;">'
            + '<img class="productImg" src="' + productImg + '"></div>'
            + '<div class="col l5 nameprice">'
            + '<span name="itemName">' + productName + '</span><br><span style="color: gray;">P</span><span class="listPrice" style="color: gray;">' + productPrice + '</span></div>'
            + '<input hidden="hidden" name="item" value="' + productName + '">'
            + '<div class="col l4 listPriceTotalCol">'
            + '<div class="rightSpan" style="float: right;"><span>&#8369;</span><span class="listPriceTotal" style="float: right;">' + productPrice + '</span></div><br>'
            + '<input hidden="hidden" name="lptotal" value="' + productPrice + '">'
            + '<div style="float: right;">'
            + '<button type="button" class="reduce-order-button-list">-</button>'
            + '<span class="orderQty">1</span>'
            + '<input hidden="hidden" name="qty" value="1">'
            + '<button type="button" class="add-order-button-list teal lighten-3">+</button>'
            + '</div></div></div>');

            $('.listPriceTotal').each(function() {
                subtotal += parseFloat($(this).text());
            })
            $('#subtotal').text(PHP.format(subtotal));

            $('.listPriceTotal').each(function() {
                total += parseFloat($(this).text());
            })
            $('#total').text(PHP.format(total));
            $('#total').siblings('input[name^="total"]').val(total);


        } else {
            selected--;
            $(this).removeClass('selected');

            let val = parseFloat($('.p' + productId).children('.listPriceTotalCol').children('.rightSpan').children('.listPriceTotal').text());
            $('.listPriceTotal').each(function() {
                subtotal += parseFloat($(this).text());
            })
            $('#subtotal').text(PHP.format(subtotal));

            $('.listPriceTotal').each(function() {
                total += parseFloat($(this).text());
            })
            console.log("Total: " + total, "Val: " + val);
            total -= val;
            subtotal -= val;
            $('#total').text(PHP.format(total));
            $('#subtotal').text(PHP.format(subtotal));
            $('#total').siblings('input[name^="total"]').val(total);


            $('.p' + productId).remove();
        }

        if(selected > 0) {
            $('.emptyorderimg').attr('hidden', true);
        } else {
            $('.emptyorderimg').attr('hidden', false);

        }
    });

    $(document).on('click', '.add-order-button-list', function() {
        let dataId = $(this).parent().parent().parent().attr('data-id');
        let itemStock = parseInt($('#p'+dataId).children('.card-action').children('.right').children('.stock').text());
        let listPrice = parseFloat($(this).parent().parent().siblings('.nameprice').children('.listPrice').text());
        let orderQty = parseFloat($(this).siblings('.orderQty').text());
        total = 0;
        subtotal = 0;
        if(orderQty < itemStock) {
            orderQty++;
            $(this).siblings('.orderQty').text(orderQty);
            $(this).siblings('input[name^="qty"]').val(orderQty);


            let totalPerProduct = listPrice * orderQty;
            $(this).parent().siblings().children('.listPriceTotal').text(totalPerProduct);

            $('.listPriceTotal').each(function() {
                subtotal += parseFloat($(this).text());
            })
            $('#subtotal').text(PHP.format(subtotal));

            $('.listPriceTotal').each(function() {
                total += parseFloat($(this).text());
            })
            $('#total').text(PHP.format(total));
            $('#total').siblings('input[name^="total"]').val(total);
        }
    });

    $(document).on('click', '.reduce-order-button-list', function() {
        let listPrice = parseFloat($(this).parent().parent().siblings('.nameprice').children('.listPrice').text());
        let orderQty = parseInt($(this).siblings('.orderQty').text());
        total = 0;
        subtotal = 0;

        if(orderQty >= 1) {
            orderQty--;
            $(this).siblings('.orderQty').text(orderQty);
            $(this).siblings('input[name^="qty"]').val(orderQty);
        }

        let totalPerProduct = listPrice * orderQty;
        $(this).parent().siblings().children('.listPriceTotal').text(totalPerProduct);

        $('.listPriceTotal').each(function() {
            subtotal += parseFloat($(this).text());
        })
        $('#subtotal').text(PHP.format(subtotal));

        $('.listPriceTotal').each(function() {
            total += parseFloat($(this).text());
        })
        $('#total').text(PHP.format(total));
        $('#total').siblings('input[name^="total"]').val(total);

    });

    $(document).on('click', '.delete-order', function() {
        let productId = $(this).siblings('.hiddenId').text();
        $(this).parent().parent().parent().parent().remove();
        $('#p' + productId).removeClass('selected');
        let val = parseFloat($(this).parent().parent().parent().siblings('.listPriceTotalCol').children('.rightSpan').children('.listPriceTotal').text());
        total -= val;
        subtotal -=val;
        $('#total').text(PHP.format(total));
        $('#subtotal').text(PHP.format(subtotal));
    });

    var filterArray = [];
    var hasSelected = 0;
    $(document).on('click', '.filter', function() {
        let selectedFilter = $(this).text();

        if(!$(this).hasClass('selectedFilter')) {
            hasSelected++;
            $(this).addClass('selectedFilter');
            filterArray.push(selectedFilter);
        } else {
            hasSelected--;
            $(this).removeClass('selectedFilter');
            let indexToRemove = filterArray.indexOf(selectedFilter)
            filterArray.splice(indexToRemove);
        }

        $('.add-order').parent().hide(200);

        for(let i = 0; i < filterArray.length; i++) {
            $('.add-order[data-category="' + filterArray[i] + '"').parent().show(100);
        }

        if (hasSelected <= 0) {
            $('.add-order').parent().show(100);
        }
    });
});