function paginateTable(table, operation) {
    const rowsPerPage = 10;
    const tbody = table.querySelector('tbody');

    let rows = Array.from(tbody.querySelectorAll('tr'));

    if (operation === 'filter') {
        rows = rows.filter((row) => window.getComputedStyle(row).display !== 'none');
    }

    const totalPages = Math.ceil(rows.length / rowsPerPage) || 1;

    let currentPage = parseInt(table.dataset.currentPage) || 1;

    if (operation === 'sort') {
        currentPage = 1; // sort xong reset về page 1
    }

    table.dataset.currentPage = currentPage;

    const paginationContainer = table.parentElement.querySelector('.pagination');

    function displayRows() {
        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = index >= start && index < end ? '' : 'none';
        });

        updatePagination();
    }

    function updatePagination() {
        if (!paginationContainer) return;

        paginationContainer.innerHTML = '';

        // Previous
        const prevButton = createPageButton('Previous', currentPage === 1, () => {
            if (currentPage > 1) {
                currentPage--;
                table.dataset.currentPage = currentPage;
                displayRows();
            }
        });

        paginationContainer.appendChild(prevButton);

        // Page numbers
        for (let i = 1; i <= totalPages; i++) {
            const pageButton = createPageButton(i, i === currentPage, () => {
                currentPage = i;
                table.dataset.currentPage = currentPage;
                displayRows();
            });

            paginationContainer.appendChild(pageButton);
        }

        // Next
        const nextButton = createPageButton('Next', currentPage === totalPages, () => {
            if (currentPage < totalPages) {
                currentPage++;
                table.dataset.currentPage = currentPage;
                displayRows();
            }
        });

        paginationContainer.appendChild(nextButton);
    }

    displayRows();
}

function createPageButton(label, disabled, onClick) {
    const li = document.createElement('li');
    li.classList.add('page-item');
    if (disabled) li.classList.add('disabled');

    const a = document.createElement('a');
    a.classList.add('page-link');
    a.href = '#';
    a.textContent = label;

    a.addEventListener('click', function (event) {
        event.preventDefault();
        if (!disabled) onClick();
    });

    li.appendChild(a);
    return li;
}

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.table').forEach((table) => {
        paginateTable(table);
    });
});
