$(document).ready(function() {
	$('.select2').select2({
		allowClear: true,
		placeholder: 'Select options'

	});

	const appImageInput = $("#appImage");
	const imagePreviewList = $("#imagePreviewList");
	const imageUrlInput = $("#imageUrl");

	// Hàm hiển thị ảnh từ appImage
	function renderImagesFromAppImage() {
		const imageUrls = appImageInput.val().split(",").filter(url => url.trim() !== "");
		imagePreviewList.empty(); // Xóa danh sách hiện tại
		imageUrls.forEach(url => {
			const imagePreview = $(`
		                <div class="image-preview" data-url="${url}">
		                    <img src="${url}" alt="Image" />
		                    <button type="button" class="btn btn-danger btn-sm remove-image-btn" data-url="${url}">Remove</button>
		                </div>
		            `);
			imagePreviewList.append(imagePreview);
		});
	}

	// Sự kiện nhấn nút Add
	$("#addImageBtn").click(function() {
		const imageUrl = imageUrlInput.val().trim();
		if (imageUrl) {
			let currentAppImage = appImageInput.val();
			currentAppImage = currentAppImage ? currentAppImage + "," + imageUrl : imageUrl;
			appImageInput.val(currentAppImage);
			renderImagesFromAppImage();
			imageUrlInput.val("");
		}
	});

	// Sự kiện nhấn nút Remove
	imagePreviewList.on("click", ".remove-image-btn", function() {
		const urlToRemove = $(this).data("url");
		let currentAppImage = appImageInput.val().split(",");
		currentAppImage = currentAppImage.filter(url => url.trim() !== urlToRemove);
		appImageInput.val(currentAppImage.join(","));
		renderImagesFromAppImage();
	});

	// Hiển thị ảnh khi tải trang
	renderImagesFromAppImage();
});