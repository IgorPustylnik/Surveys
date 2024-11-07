document.querySelector('form[action="surveys"]').addEventListener('submit', function (event) {
    const categorySelect = document.getElementById('categorySelect');
    const fromDate = document.getElementById('fromDate');
    const toDate = document.getElementById('toDate');
    if (categorySelect.value === '') {
        categorySelect.removeAttribute('name');
    }
    if (fromDate.value === '') {
        fromDate.removeAttribute('name');
    }
    if (toDate.value === '') {
        toDate.removeAttribute('name');
    }
});

document.addEventListener("DOMContentLoaded", function () {
    function updateFilterCount() {
        const urlParams = new URLSearchParams(window.location.search);
        let filterCount = 0;

        urlParams.forEach((value, key) => {
            if (key !== "page" && value.trim() !== "") {
                filterCount++;
            }
        });

        const filterCountBadge = document.getElementById("filterCountBadge");
        filterCountBadge.textContent = filterCount;

        filterCountBadge.style.display = filterCount > 0 ? "inline" : "none";
    }

    updateFilterCount();
});