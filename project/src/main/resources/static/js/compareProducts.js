function compareSelectedProducts() {
    var selectedProducts = document.querySelectorAll('.compare-checkbox:checked');
    if (selectedProducts.length > 4) {
        alert("최대 4개의 제품만 비교할 수 있습니다.");
        return;
    }
    var ids = Array.from(selectedProducts).map(cb => cb.value);
    if (ids.length === 0) {
        alert("비교할 제품을 선택해주세요.");
        return;
    }
    window.location.href = '/products/compareProducts?ids=' + ids.join(',');
}
