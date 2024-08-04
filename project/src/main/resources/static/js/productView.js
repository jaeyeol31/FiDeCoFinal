function applyFilter() {
    var filterValue = document.getElementById("productCategory").value;
    var priceSort = document.getElementById("priceSort").value;
    var dateSort = document.getElementById("dateSort").value;
    var searchInput = document.getElementById("searchInput").value.toLowerCase();
    var productContainer = document.getElementById("productContainer");
    var productCards = productContainer.getElementsByClassName("col-md-3");

    var rowsArray = Array.from(productCards);

    rowsArray.forEach(function(card) {
        var category = card.getAttribute("data-category");
        var name = card.querySelector(".card-title").innerText.toLowerCase();
        if ((filterValue === "" || category === filterValue) && 
            (name.includes(searchInput))) {
            card.style.display = "";
        } else {
            card.style.display = "none";
        }
    });

    if (priceSort) {
        rowsArray.sort(function(a, b) {
            var priceA = parseInt(a.getAttribute("data-price"));
            var priceB = parseInt(b.getAttribute("data-price"));
            return priceSort === "asc" ? priceA - priceB : priceB - priceA;
        });
    }

    if (dateSort) {
        rowsArray.sort(function(a, b) {
            var dateA = new Date(a.getAttribute("data-date"));
            var dateB = new Date(b.getAttribute("data-date"));
            return dateSort === "asc" ? dateA - dateB : dateB - dateA;
        });
    }

    rowsArray.forEach(function(card) {
        productContainer.appendChild(card);
    });
}
