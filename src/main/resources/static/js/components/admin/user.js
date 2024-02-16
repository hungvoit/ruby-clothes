$(document).ready(function() {
    var toastMsg = /*[[${message}]]*/ '';
    if (toastMsg != null) {
        $('.toast').toast('show');
    }
});

function openDeleteModal(id){
    $.ajax({
        type:'GET',
        url: "/modal-del/" + id,
        data:{ tabName: 'user'},
        success: function(data){
            $("#delModalHolder").html(data);
            $('#delModal_user').modal({
                backdrop: 'static'
            });
        }
    });
}

function openResetPasswordModal(id){
    $.ajax({
        type:'GET',
        url: "/modal-reset-password/" + id,
        success: function(data){
            $("#resetPasswordModalHolder").html(data);
            $('#resetPinModal').modal({
                backdrop: 'static'
            });
        }
    });
}

function fileChangeEvent(event) {
    var _this = this;
    var file = event.target.files[0];
    var formData = new FormData();
    formData.append('uploadFile', file);
    alert("Message" + file.name + file.lastModified + file.type + file.size);
    $.ajax({
        type:'POST',
        url: '/users/user-image',
        enctype: 'multipart/form-data',
        contentType: false,
        dataType : 'json',
        cache: false,
        timeout: 600000,
        data: JSON.stringify(formData),
        success: function(data){
            alert("Upload file" + data);
        }
    });
}