const options = {
    valueNames: [
        {name: 'grid-item-img', attr: 'src'}
    ],
    item: '<div class="grid-item" onclick="chooseImgProduct(this)">' +
             '<div class="img-container">' +
                '<div class="img-div">' +
                    '<img class="grid-item-img" src="">' +
                '</div>' +
             '</div>' +
           '</div>'
};

let values = [];
let imgList = new List('list-img', options);

$(function() {
    $('#datetimepicker').datetimepicker({
            minDate: new Date(),
            icons: {
                time: "fa fa-clock",
                date: "fa fa-calendar"
            }
        }
    );

    $('#description').summernote({
            height: 200,
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']]
            ]
        }
    );

    $('#parentCategoryId').change( function() {
        eventChangeParentCategory();
    });

    $('#resizeImgModal').on('hidden.bs.modal', () => {
        if ($('#modal-product').hasClass('show')) {
            $('body').addClass('modal-open');
        }
    });
});

$('#idUploadImgProduct').click(() => {
    $('#resizeImgModal').modal('show');
});

$("#upload-thumbnail").change((event) => {
    let files = event.target.files;
    let formData = new FormData();
    if (files.length > 0) {
        let listImage = [];
        [...files].forEach(item => {
            if(item.type.indexOf('image')> -1){
                listImage.push(item);
            }
        });
        if (listImage.length > 6) {
            $(this).val('');
            return toastr.error("Fails");
        } else {
            if ((files || []).length > 0) {
                [...files].forEach(item => {
                    let reader = new FileReader();
                    reader.readAsDataURL(item);
                    reader.onload = (event) => {
                        let url = event.target.result;
                        if (item.type.indexOf('image')> -1){
                            url = convertBase64ToBlobSanitizer(url);
                            values.unshift({'grid-item-img': url});
                            imgList.clear();
                            imgList.add(values);
                        }
                    };

                });
            }
        }
    }
});

function initListImg(arr) {
    values = arr;
    imgList.add(values);
}

function convertBase64ToBlobSanitizer(base64Image) {
    const parts = base64Image.split(';base64,');
    const imageType = parts[0].split(':')[1];
    const decodedData = window.atob(parts[1]);
    const uInt8Array = new Uint8Array(decodedData.length);
    for (let i = 0; i < decodedData.length; ++i) {
        uInt8Array[i] = decodedData.charCodeAt(i);
    }
    const blob = new Blob([uInt8Array], { type: imageType });
    return URL.createObjectURL(blob);
}

function chooseImgProduct(img) {
    if ($(img).hasClass('choosen')) {
        $(img).toggleClass('choosen');
        $('.btn-delete-img').prop('disabled', true);
        $('.btn-choose-img').prop('disabled', true);
    } else {
        $('.grid-item.choosen').toggleClass('choosen');
        $(img).toggleClass('choosen');
        $('.btn-delete-img').prop('disabled', false);
        $('.btn-choose-img').prop('disabled', false);
    }
}

function eventChangeParentCategory() {
    let valueParentCategory = $('#parentCategoryId').val();
    let idChildCategory = $('#childId');
    idChildCategory.empty();
    if (valueParentCategory) {
        $.ajax({
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            type: 'GET',
            url: '/admin/childCategories/' + valueParentCategory,
            async: false,
            success: function (response) {
                if (response && response.data) {
                    let listChildCategories = response.data;
                    for (let childCategory of listChildCategories) {
                        idChildCategory.append(new Option(childCategory, childCategory));
                    }
                } else {
                    idChildCategory.append(new Option("Please select", ""));
                }
            }, error: function (response) {
                toastr.error("Fails");
            }
        });
    }
}

