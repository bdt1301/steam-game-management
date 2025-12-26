let currentPage = 1;
const rowsPerPage = 10;

function paginateTable(tableId, operation) {
	const table = document.getElementById(tableId);
	let rows = Array.from(table.querySelectorAll("tbody tr"));
	if (operation === "filter") {
		rows = Array.from(table.querySelectorAll("tbody tr")).filter(row => {
			return window.getComputedStyle(row).display !== "none";
		});
	}
	const totalPages = Math.ceil(rows.length / rowsPerPage);
	const paginationContainer = document.getElementById("paginationContainer");

	function displayRows() {
		const start = (currentPage - 1) * rowsPerPage;
		const end = start + rowsPerPage;
		rows.forEach((row, index) => {
			row.style.display = (index >= start && index < end) ? "" : "none";
		});
		updatePagination();
	}

	function updatePagination() {
		paginationContainer.innerHTML = "";

		// Tạo nút Previous
		const prevButton = document.createElement("li");
		prevButton.classList.add("page-item");
		prevButton.innerHTML = `<a class="page-link" href="#">Previous</a>`;
		if (currentPage === 1) prevButton.classList.add("disabled");
		prevButton.addEventListener("click", function() {
			event.preventDefault();
			if (currentPage > 1) {
				currentPage--;
				displayRows();
			}
		});
		paginationContainer.appendChild(prevButton);

		// Tạo nút số trang
		for (let i = 1; i <= totalPages; i++) {
			const pageButton = document.createElement("li");
			pageButton.classList.add("page-item");
			if (i === currentPage) pageButton.classList.add("active");
			pageButton.innerHTML = `<a class="page-link" href="#">${i}</a>`;
			pageButton.addEventListener("click", function() {
				event.preventDefault();
				currentPage = i;
				displayRows();
			});
			paginationContainer.appendChild(pageButton);
		}

		// Tạo nút Next
		const nextButton = document.createElement("li");
		nextButton.classList.add("page-item");
		nextButton.innerHTML = `<a class="page-link" href="#">Next</a>`;
		if (currentPage === totalPages) nextButton.classList.add("disabled");
		nextButton.addEventListener("click", function() {
			event.preventDefault();
			if (currentPage < totalPages) {
				currentPage++;
				displayRows();
			}
		});
		paginationContainer.appendChild(nextButton);
	}

	displayRows();
}

document.addEventListener("DOMContentLoaded", function() {
	const currentPath = window.location.pathname;
	if (currentPath.includes("/games") || currentPath.includes("/details")) {
		tableId = "games-table";
	} else if (currentPath.includes("/publishers")) {
		tableId = "publishers-table";
	} else if (currentPath.includes("/categories")) {
		tableId = "categories-table";
	} else if (currentPath.includes("/records")) {
		tableId = "records-table";
	} else if (currentPath.includes("/users")) {
		tableId = "users-table";
	}
	paginateTable(tableId);
});