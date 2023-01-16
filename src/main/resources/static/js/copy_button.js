for (el of document.getElementsByClassName('copy-button')) {
    el.addEventListener('click', copyText);
}

function copyText() {
    navigator.clipboard.writeText(this.dataset.text).then(() => {
        // Success handler
        this.firstElementChild.className = 'bi-clipboard2-check';
    }, () => {
        // Error handler
        this.firstElementChild.className = 'bi-clipboard2-x';
    });
}