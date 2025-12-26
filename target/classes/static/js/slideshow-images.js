$(document).ready(function() {
	const appImages = $("#appImages");
	const carouselItems = $("#carouselItems");
	const gameImage = $("#gameImage");

	// Hàm hiển thị ảnh từ appImage dưới dạng carousel
	function renderImagesFromAppImage() {
		const imageUrls = appImages.text().split(",").filter(url => url.trim() !== "").slice(1);
		carouselItems.empty(); // Xóa danh sách carousel cũ

		imageUrls.forEach((url, index) => {
			const isActive = index === 0 ? 'active' : ''; // Đặt ảnh đầu tiên là ảnh active
			const imagePreview = $(`<div class="carousel-item ${isActive}"><img class="slideshow-img" src="${url}" alt="Image"></div>`);
			carouselItems.append(imagePreview);
		});
	}

	// Hiển thị ảnh khi tải trang
	renderImagesFromAppImage();

	gameImage.on("click", function() {
		$('#imageModal').modal('show'); // Mở modal khi click vào ảnh
	});
});