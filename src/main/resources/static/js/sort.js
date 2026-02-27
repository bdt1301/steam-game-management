let order = 'asc';
let previousSortedColumn = -1;

document.querySelectorAll('.table thead th').forEach((th, index) => {
    if (!th.classList.contains('unsort-column')) {
        th.addEventListener('click', () => sortTable(index, th));
    }
});

function sortTable(columnIndex, th) {
    const table = th.closest('table');
    const tableBody = table.querySelector('tbody');
    const rows = Array.from(tableBody.rows);
    const type = th.dataset.type || 'string';

    if (columnIndex !== previousSortedColumn) {
        order = 'asc';
        previousSortedColumn = columnIndex;
    }

    rows.sort((rowA, rowB) => {
        const cellA = rowA.cells[columnIndex].textContent.trim();
        const cellB = rowB.cells[columnIndex].textContent.trim();

        const valueA = parseValue(cellA, type);
        const valueB = parseValue(cellB, type);

        let comparison = 0;

        if (valueA > valueB) comparison = 1;
        else if (valueA < valueB) comparison = -1;

        return order === 'asc' ? comparison : -comparison;
    });

    rows.forEach((row) => tableBody.appendChild(row));

    paginateTable(table, 'sort');
    updateSortIcons(columnIndex);

    order = order === 'asc' ? 'desc' : 'asc';
}

function parseValue(value, type) {
    switch (type) {
        case 'number':
            return parseFloat(value.replace(/,/g, '')) || 0;

        case 'price':
            return parseFloat(value.replace(/[^\d.]/g, '')) || 0;

        case 'date':
            // nếu format dd/MM/yyyy
            const parts = value.split('/');
            return new Date(parts[2], parts[1] - 1, parts[0]);

        default:
            return value.toLowerCase();
    }
}

function updateSortIcons(columnIndex) {
    document.querySelectorAll('.sort-icon').forEach((icon) => (icon.textContent = ''));

    const currentIcon = document.querySelectorAll('.sort-icon')[columnIndex];
    currentIcon.innerHTML =
        order === 'asc'
            ? `<i class="fa-solid fa-arrow-up-short-wide"></i>`
            : `<i class="fa-solid fa-arrow-down-wide-short"></i>`;
}
