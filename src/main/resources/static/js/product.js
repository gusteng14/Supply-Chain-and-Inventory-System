$(document).ready(function() {
    $('#productsTable').DataTable({
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });

    $('#image').on('change', function() {

        //console.log(this.files);
        var file = this.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#thumbnail').attr('src', e.target.result);
        }
        reader.readAsDataURL(file);
    })

    $(document).on('change', '.rQty', function() {
        let qty = $(this).val();
        let id = $(this).attr('data-id');

        if (qty >= 0) {
            $(this).parent().parent().parent().siblings('.modal-footer').children('#restockBtn').attr('href', 'product/restock/' + id + '/' + qty);
        } else {
            $('#restockBtn').on('click', function() {
                alert('Negative values are not allowed.');
            });
        }
    });
});