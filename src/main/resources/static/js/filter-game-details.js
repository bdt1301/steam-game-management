function filterList() {
	// Lấy giá trị tìm kiếm theo tên
	const input = document.getElementById('search-game-name');
	const filter = input.value.toLowerCase();

	// Lấy các giá trị lọc theo các tiêu chí
	const minPrice = parseFloat(document.getElementById('min-price').value) || 0;
	const maxPrice = parseFloat(document.getElementById('max-price').value) || Infinity;
	const minPlayers = parseInt(document.getElementById('min-players').value) || 0;
	const maxPlayers = parseInt(document.getElementById('max-players').value) || Infinity;
	const startYear = parseInt(document.getElementById('start-year').value) || 0;
	const endYear = parseInt(document.getElementById('end-year').value) || Infinity;
	const categoryInput = Array.from(document.getElementById('categories-filter').selectedOptions).map(option => option.value.toLowerCase());

	// Lấy bảng và các hàng trong bảng
	const table = document.querySelector('.table');
	const trs = table.getElementsByTagName('tr');

	for (let i = 1; i < trs.length; i++) {
		const tdName = trs[i].getElementsByTagName('td')[0]; // Lọc theo tên game
		const tdDate = trs[i].getElementsByTagName('td')[1]; // Lọc theo ngày phát hành
		const tdPrice = trs[i].getElementsByTagName('td')[2]; // Lọc theo giá
		const tdPlayers = trs[i].getElementsByTagName('td')[3]; // Lọc theo số người chơi đỉnh điểm
		const tdCategories = trs[i].getElementsByTagName('td')[4]; // Lọc theo Categories

		if (tdName && tdPrice && tdDate && tdPlayers && tdCategories) {
			const nameValue = tdName.textContent || tdName.innerText;
			const priceValue = tdPrice.textContent.trim().toLowerCase() === "free" ? 0 : parseFloat(tdPrice.textContent.replace(/[^0-9.-]+/g, ""));
			const yearValue = parseInt(tdDate.textContent.trim().split('/').pop()); // Chỉ lấy năm từ ngày
			const playersValue = parseInt(tdPlayers.textContent.replace(/[^0-9.-]+/g, ""));
			const categories = tdCategories.textContent.toLowerCase().split(',').map(c => c.trim());

			// Kiểm tra điều kiện lọc theo tên, giá, năm và số người chơi
			const matchesName = fuzzySearch(filter, nameValue.toLowerCase());
			const matchesPrice = (priceValue >= minPrice && priceValue <= maxPrice);
			const matchesYear = (yearValue >= startYear && yearValue <= endYear);
			const matchesPlayers = (playersValue >= minPlayers && playersValue <= maxPlayers);
			const matchesCategories = categoryInput.length === 0 || categoryInput.every(inputCategory => categories.includes(inputCategory));

			// Hiển thị hoặc ẩn hàng dựa trên tất cả các điều kiện lọc
			if (matchesName && matchesPrice && matchesYear && matchesPlayers && matchesCategories) {
				trs[i].style.display = ""; // Hiển thị nếu khớp
			} else {
				trs[i].style.display = "none"; // Ẩn nếu không khớp
			}
		}
	}
	paginateTable(table.id, "filter");
}


function clearFilters() {
	// Đặt giá trị các ô lọc về mặc định
	document.getElementById('search-game-name').value = "";
	document.getElementById('categories-filter').value = "";
	document.getElementById('min-price').value = "";
	document.getElementById('max-price').value = "";
	document.getElementById('start-year').value = "";
	document.getElementById('end-year').value = "";
	document.getElementById('min-players').value = "";
	document.getElementById('max-players').value = "";
}
