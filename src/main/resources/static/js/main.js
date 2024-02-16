$().ready(function() {
    /*Sidebar*/
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

    /*Scroll Top*/
    $(window).scroll(function () {
        var elm = $('.scroll-to-top');
        if ($(this).scrollTop() > 400) {
            elm.addClass('d-block').removeClass('d-none');
        } else {
            elm.addClass('d-none').removeClass('d-block');
        }
    });
    $('.scroll-to-top').click(function () {
        $('html, body').animate({ scrollTop: 0, behavior: 'smooth' }, 600);
        return false;
    });
});


