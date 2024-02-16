$(document).ready(function() {
    var toastMsg = /*[[${message}]]*/ '';
    if (toastMsg != null) {
        $('.toast').toast('show');
    }

    //double submit
    var idForm = $('#formGlobal');
    $(idForm).validate({
        rules: {
            code: "required",
            value: "required"
        },
        messages: {
            code: /*[[#{code.required}]]*/ 'Code is required',
            value: /*[[#{value.required}]]*/ 'Value is required'
        },
        errorClass: 'red-text px-2',
        errorElement: 'div',
        submitHandler: function() {
            var msg = /*[[#{message.successfully}]]*/ '';
            var  req = {
                code: $('#code').val(),
                value: $('#value').val(),
                description: $('#description').val(),
                flagChange: $('#flagChange').val()
            };
            var myJSON = JSON.stringify(req);
            $.ajax({
                url: '/api/admin/global',
                type: 'POST',
                data: myJSON,
                contentType: "application/json; charset=utf-8",
                success: function(response) {
                    if (response && response.data) {
                        setTimeout(function() {
                            location.href = '/admin/global';
                        }, 200);
                        toastr.success(msg);
                    } else {
                        toastr.error("Fails");
                    }
                },
                error: function(data) {
                    toastr.error("Fails");
                }
            });
        }
    });
});

function openDeleteModal(id){
    $.ajax({
        type:'GET',
        url: "/modal-del/" + id,
        data:{ tabName: 'global'},
        success: function(data){
            $("#delModalHolder").html(data);
            $('#delModal_global').modal({
                backdrop: 'static'
            });
        }
    });
}