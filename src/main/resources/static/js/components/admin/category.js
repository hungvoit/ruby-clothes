$(document).ready(function() {
    var toastMsg = /*[[${message}]]*/ '';
    if (toastMsg != null) {
        $('.toast').toast('show');
    }

    var idForm = $('#formCategory');
    $(idForm).validate({
        rules: {
            parentId: {required: true},
            code: {required: true, minlength: 2, checkExistedCode: true},
            name: "required"
        },
        messages: {
            parentId: {
                required: /*[[#{parentId.required}]]*/ 'ParentId is required',
                minlength: /*[[#{min('parentId','2')}]]*/ 'The parentId should have at least 2 characters!'
            },
            code: {
                required: /*[[#{code.required}]]*/ 'Code is required',
                minlength: /*[[#{min('code','2')}]]*/ 'The code should have at least 2 characters!'
            },
            name: /*[[#{name.required}]]*/ 'Name is required'
        },
        errorClass: 'red-text px-2',
        errorElement: 'div',
        submitHandler: function() {
            var  req = {
                parentId: $('#parentId').val(),
                code: $('#code').val(),
                name: $('#name').val(),
                id: $('#id').val()
            };
            var myJSON = JSON.stringify(req);
            $.ajax({
                url: '/api/admin/categories',
                type: 'POST',
                data: myJSON,
                contentType: "application/json; charset=utf-8",
                success: function(response) {
                    if (response && response.data) {
                        location.href = '/admin/categories';
                    } else {
                        toastr.error("Fails");
                    }
                },
                error: function(response) {
                    toastr.error("Fails");
                }
            });
        }
    });
    $.validator.addMethod('checkExistedCode', function(value) {
        var result = true;
        $.ajax({
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            type: 'GET',
            url: '/admin/categories/code-exist?code=' + value,
            async: false,
            success: function (response) {
                result = !response.data;
            }
        });
        return result;
    }, /*[[#{code.exist}]]*/ 'The code you entered existed. Please re-enter!');
    getParentCategories();
});

function showUpdateCategory(btn) {
    var id = $(btn).data('id');
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        type: 'GET',
        url: '/admin/categories/findById/' + id,
        async: false,
        success: function (response) {
            if (response && response.data) {
                var object = JSON.parse(response.data);
                $('#parentId').val(object.parentId);
                $('#code').val(object.code);
                $('#name').val(object.name);
                $('#id').val(id);
                setTimeout(function() {
                    $('#modal-category').modal('show');
                }, 500);
            }
        }, error: function (response) {
            toastr.error("Fails");
        }
    });

}

function getParentCategories() {
    $.ajax({
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        type: 'GET',
        url: '/admin/parentCategories',
        async: false,
        success: function (response) {
            if (response && response.data) {
                let listParentCategories = response.data;
                let id = $('#parentId');
                id.empty();
                for (let parentCategory of listParentCategories) {
                    id.append(new Option(parentCategory, parentCategory));
                }
            }
        }, error: function (response) {
            toastr.error("Fails");
        }
    });

}

function openDeleteModal(id){
    $.ajax({
        type:'GET',
        url: "/modal-del/" + id,
        data:{ tabName: 'categories'},
        success: function(data){
            $("#delModalHolder").html(data);
            $('#delModal_categories').modal({
                backdrop: 'static'
            });
        }
    });
}