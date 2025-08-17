$(document).ready(function() {
    $('#password, #confirm-password').on('keyup', function() {
        if($('#password').val() == $('#confirm-password').val()) {
            $('#password-message').html("Password matched!").css('color','green');
            $("#submit").prop('disabled', false);
        } else {
            $('#password-message').html("Password does not match!").css('color','red');
            $("#submit").prop('disabled', true);
        }
    });

    $('#contact_no').on('keyup', function() {
        if ($('#contact_no').val().length == 0 || $('#contact_no').val().length == null) {
            $('#contactNo-message').html('')
        }
        else if($('#contact_no').val().length != 11) {
            $('#contactNo-message').html('Contact number must be valid!').css('color', 'red');
            $("#submit").prop('disabled', true);
        } else {
            $('#contactNo-message').html('')
            $("#submit").prop('disabled', false);
        }

    });

    $('#userTable').DataTable({
        layout: {
            topStart: {
                buttons: ['csv', 'excel', 'pdf', 'print']
            }
        }
    });
    $('#softDeleteTable').DataTable({
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


    $("#registerForm").submit(function(event) {
        event.preventDefault();
        ajaxPost();
        refreshForm();
    });

    $('#registerForm').on('change', function() {
        $('#usernameError').text(" ");
        $('#emailError').text(" ");
    });

    $('#success-close').on('click', function() {
        $('#usernameError').text(" ");
        $('#emailError').text(" ");
        $('#password-message').text(" ");
    })

    $('#view-users').on('click', function() {
        window.location.reload();
    })


    function refreshForm() {
        $("#first_name").val("");
        $("#last_name").val("");
        $("#middle_name").val("");
        $("#contact_no").val("");
        $("#username").val("");
        $("#email").val("");
        $("#password").val("");
        $("#confirm-password").val("");
//        $("#role").val("");
        $("#image").val("");
    }

    function ajaxPost() {
        var formData = {
            firstName : $("#first_name").val(),
            lastName : $("#last_name").val(),
            middleName : $("#middle_name").val(),
            contactNo : $("#contact_no").val(),
            username : $("#username").val(),
            email : $("#email").val(),
            password : $("#password").val(),
            roleRequest : $("#role").val()
        }

        $.ajax({
            type: "POST",
            contentType : "application/json",
            url: "api/v1/registration/test",
            data: JSON.stringify(formData),
            dataType: 'text',
            success : function(response) {
               if(response == "Username already taken.") {
                   $('#usernameError').html('Username already taken.').css('color', 'red');
                   $('#password-message').text(" ");
               } else if (response == "Email already taken.") {
                   $('#emailError').html('Email already taken.').css('color', 'red');
                   $('#password-message').text(" ");
               } else if (response == "Username and email are already taken.") {
                   $('#usernameError').html('Username already taken.').css('color', 'red');
                   $('#emailError').html('Email already taken.').css('color', 'red');
                   $('#password-message').text(" ");
               } else if (response == "Created"){
                   $('#success-modal-trigger').trigger('click');
               }
            },
            error: function(e) {
                console.log(e)
            }
        })
    }
});