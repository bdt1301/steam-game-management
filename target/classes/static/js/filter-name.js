function filterName() {
	const input = document.getElementById('search-name');
	const filter = input.value.toLowerCase();
	const table = document.querySelector('.table');
	const trs = table.getElementsByTagName('tr');

	for (let i = 1; i < trs.length; i++) {
		const td = trs[i].getElementsByTagName('td')[0];
		if (td) {
			const txtValue = td.textContent || td.innerText;
			if (fuzzySearch(filter, txtValue.toLowerCase())) {
				trs[i].style.display = ""; // Hiển thị hàng nếu khớp
			} else {
				trs[i].style.display = "none"; // Ẩn hàng nếu không khớp
			}
		}
	}
	paginateTable(table.id, "filter");
}

function fuzzySearch(pattern, text) {
	let patternIdx = 0;
	let textIdx = 0;
	while (textIdx < text.length) {
		if (pattern[patternIdx] === text[textIdx]) {
			patternIdx++;
		}
		textIdx++;
		if (patternIdx === pattern.length) break;
	}
	return patternIdx === pattern.length;
}