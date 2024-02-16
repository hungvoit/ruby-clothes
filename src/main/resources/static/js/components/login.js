$('#togglePassword').click(function() {
    var eyeIcon = $(this);
    var pwdInput = $('#password');
    pwdInput.off('blur');
    if (eyeIcon.hasClass('fa-eye')) {
        eyeIcon.removeClass('fa-eye').addClass('fa-eye-slash');
        pwdInput.attr('type', 'text').focus();
        pwdInput.one('blur', function(e) {
            if ($(e.relatedTarget).attr('id') !== 'togglePassword') {
                pwdInput.attr('type', 'password');
                eyeIcon.removeClass("fa-eye-slash").addClass('fa-eye');
            }
        });
    } else {
        eyeIcon.removeClass('fa-eye-slash').addClass('fa-eye');
        pwdInput.attr('type', 'password');
    }
});