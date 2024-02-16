$(document).ready(function(){
    owlCarouselMain('idOwlCarouselSlide');
    owlCarouselMain('idOwlCarouselServices');
    owlCarouselMain('idOwlCarouselFlashSale');
    countDown();

});

function countDown() {
    $('#countdown').countdown('2024/12/1', function(event) {
        var $this = $(this).html(event.strftime(''
            + '<div><span>%d</span><span>Days</span></div>'
            + '<div><span>%H</span><span>Hr</span></div>'
            + '<div><span>%M</span><span>Min</span></div>'
            + '<div><span>%S</span><span>Sec</span></div>'
        ))
    });
}

function owlCarouselMain(id) {
    var owl = $('#' + id);
    if (id === 'idOwlCarouselServices') {
        owl.owlCarousel({
            items: 6,
            animateOut: 'fadeOut'
        });
    } else if (id === 'idOwlCarouselFlashSale') {
        owl.owlCarousel({
            items: 5,
            lazyLoad: true,
            animateOut: 'fadeOut',
            animateIn: 'fadeIn',
            loop: true,
            responsive: {
                0: {
                    items: 1
                },
                550: {
                    items: 2
                },
                800: {
                    items: 3
                },
                1000: {
                    items: 4
                },
                1200: {
                    items: 5
                }
            }
        });
    } else {
        owl.owlCarousel({
            nav: true,
            items: 2,
            animateOut: 'fadeOut',
            autoplay: true,
            autoplayTimeout: 3000,
            autoplayHoverPause:true,
            loop: true
        });
        $('.play').on('click',function(){
            owl.trigger('play.owl.autoplay',[1000]);
            $(this).removeClass('d-block').addClass('d-none');
            $('.stop').removeClass('d-none').addClass('d-block');
        });
        $('.stop').on('click',function(){
            owl.trigger('stop.owl.autoplay');
            $(this).removeClass('d-block').addClass('d-none');
            $('.play').removeClass('d-none').addClass('d-block');
        });
    }
    enableNavCarousel(owl);
    resetOwlCarousel(owl);
}

function enableNavCarousel(owl) {
    owl.find('.owl-nav').removeClass('disabled');
    owl.on('changed.owl.carousel', function(event) {
        $(this).find('.owl-nav').removeClass('disabled');
    });
}

function resetOwlCarousel(owl) {
    owl.trigger('refresh.owl.carousel');
}