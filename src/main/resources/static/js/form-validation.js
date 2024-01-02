$(function() {
    $('#formuser').validate({
        rules: {
            firstName: { required: true},
            lastName: { required: true},
            email: { required: true },
            phone: { required: true }
        },
        messages: {
            firstName: "<spring:message code=\"firstName.required\"/>",
            lastName: "<spring:message code=\"lastName.required\"/>",
            email: "<spring:message code=\"email.required\"/>",
            phone: "<spring:message code=\"phone.required\"/>"
        },
        submitHandler: function(form) {
            $.ajax({
                url: '/global/saveGlobal',
                type: 'POST',
                data: $(form).serialize(),
                success: function (response) {
                    alert("Send mail")
                }
            });
        }
    });
});
