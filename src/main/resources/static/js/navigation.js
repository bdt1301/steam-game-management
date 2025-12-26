document.addEventListener("DOMContentLoaded", function() {
	const currentUrl = window.location.pathname;

	const navLinks = [
		{ path: "/users", id: "usersLink" },
		{ path: "/records", id: "recordsLink" },
		{ path: "/games", id: "gamesLink" },
		{ path: "/publishers", id: "publishersLink" },
		{ path: "/categories", id: "categoriesLink" },
		{ path: "/user/details", id: "userDetailsLink" },
		{ path: "/", id: "homeLink" }
	];

	for (const { path, id } of navLinks) {
		if (currentUrl.includes(path)) {
			document.getElementById(id).classList.add("active");
			break;
		}
	}
});