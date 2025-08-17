$(document).ready(function() {
    $('#categoryTable').DataTable({
        autoWidth: false,
        columns: [{ width: '5%' }, null, null, null, null, null],
        order: [3, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });
    $('#softDeleteTable').DataTable({
        autoWidth: false,
        columns: [{ width: '5%' }, null, null, { width: '25%' }],
        order: [3, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });
    $('#auditTable').DataTable({
        autoWidth: false,
        columns: [{ width: '5%' }, null, null, null, null, null],
        order: [3, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });

    $('#registerForm').on('change', function() {
        var field1 = $('#name').val().length;
        var field2 = $('#description').val().length;

        if(field1 < 1 || field2 < 1) {
            $('#modalTrigger').prop("disabled", true);
        } else {
            $('#modalTrigger').prop("disabled", false);

        }
    });

    $('#confirmDelete').on('click', function() {
        var id = $(this).attr('data-id');

        $.ajax({
            type: "POST",
            url: "/category/softdelete",
            data: {id: id},
            success: function(response) {
                console.log("Data received:", response);
                window.location.reload();
            },
            error: function(xhr, status, error) {
                $('#errorModalTrigger').trigger('click');
            }
        });
    });
})