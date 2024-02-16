$(document).ready(function() {
    $('#btnRegister').on('click', function() {
        var msgTran = '<spring:message code="label.processing"/>';
        //double submit
        var idForm = $('#frm-confirm');
        if (idForm.attr('submitted')){
            event.preventDefault();
        } else {
            idForm.attr('submitted',true);
        }

        //Processing loading
        var $this = $(this);
        var loadingText = '<i class="fa fa-spinner fa-spin"></i> ' + msgTran;
        if ($(this).html() !== loadingText) {
            $this.data('original-text', $(this).html());
            $this.html(loadingText);
            $this.addClass("disabled");
        }
        setTimeout(function() {
            $this.html($this.data('original-text'));
            $this.removeClass("disabled");
        }, 10000);
    });
});