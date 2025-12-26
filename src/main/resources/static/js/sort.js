let order = 'asc';
let previousSortedColumn = -1;

function sortTable(columnIndex) {
	const tableId = document.querySelector(".table").id;
	const tableBody = document.querySelector(".table tbody");
	const rows = Array.from(tableBody.rows);

	if (columnIndex !== previousSortedColumn) {
		order = 'asc';
		previousSortedColumn = columnIndex;
	}

	rows.sort((rowA, rowB) => {
		const cellA = rowA.cells[columnIndex].textContent.trim().toLowerCase();
		const cellB = rowB.cells[columnIndex].textContent.trim().toLowerCase();
		let comparison = 0;

		if (tableId === "users-table") {
			comparison = columnIndex === 0 ? parseFloat(cellA) - parseFloat(cellB) : cellA.localeCompare(cellB);
		}
		else if (tableId === "categories-table" || tableId === "publishers-table") {
			comparison = columnIndex === 1 ? parseFloat(cellA) - parseFloat(cellB) : cellA.localeCompare(cellB);
		} else if (tableId === "records-table") {
			if (columnIndex === 0) {
				const dateA = new Date(cellA.split(' ')[0].split('/').reverse().join('-') + 'T' + cellA.split(' ')[1]);
				const dateB = new Date(cellB.split(' ')[0].split('/').reverse().join('-') + 'T' + cellB.split(' ')[1]);
				comparison = dateA - dateB;
			} else {
				comparison = cellA.localeCompare(cellB);
			}
		} else if (tableId === "games-table") {
			if (columnIndex === 1) {
				const dateA = new Date(cellA.split('/').reverse().join('-'));
				const dateB = new Date(cellB.split('/').reverse().join('-'));
				comparison = dateA - dateB;
			} else if (columnIndex === 2) {
				const priceA = normalizeNumber(cellA);
				const priceB = normalizeNumber(cellB);
				comparison = priceA - priceB;
			} else if (columnIndex === 3) {
				comparison = normalizeNumber(cellA) - normalizeNumber(cellB);
			} else {
				comparison = cellA.localeCompare(cellB);
			}
		}

		return order === 'asc' ? comparison : -comparison;
	});

	rows.forEach(row => tableBody.appendChild(row));

	paginateTable(tableId, "sort");

	updateSortIcons(columnIndex);

	order = order === 'asc' ? 'desc' : 'asc';
}

function normalizeNumber(input) {
	if (input.trim().toLowerCase() === 'free') {
		return 0;
	} else {
		const numericValue = input.toString().replace(/[^0-9]/g, '');
		return numericValue === '' ? 0 : parseFloat(numericValue);
	}
}

function updateSortIcons(columnIndex) {
	document.querySelectorAll(".sort-icon").forEach(icon => icon.textContent = "");

	const currentIcon = document.querySelectorAll(".sort-icon")[columnIndex];
	currentIcon.innerHTML = order === 'asc' ? `<i class="fa-solid fa-arrow-up-short-wide"></i>` : `<i class="fa-solid fa-arrow-down-wide-short"></i>`;
}