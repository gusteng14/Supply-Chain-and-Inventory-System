$(document).ready(function() {
    $('#itemTypeTable').DataTable({
        columns: [{ width: '5%' }, null, null, null, null],
        order: [2, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });
    $('#softDeleteTable').DataTable({
        columns: [{ width: '5%' }, null, null],
        order: [2, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });
    $('#auditTable').DataTable({
        columns: [{ width: '5%' }, null, null, null, null, null],
        order: [2, 'desc'],
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });

    $('#registerForm').on('change', function() {
        var field1 = $('#name').val().length;

        if(field1 < 1) {
            $('#modalTrigger').prop("disabled", true);
        } else {
            $('#modalTrigger').prop("disabled", false);
        }
    });

    $('#confirmDelete').on('click', function() {
        var id = $(this).attr('data-id');

        $.ajax({
            type: "GET",
            url: "/itemType/softdelete",
            data: {id: id},
            success: function(response) {
                window.location.reload();
            },
            error: function(xhr, status, error) {
                $('#errorModalTrigger').trigger('click');
            }
        });
    });
})