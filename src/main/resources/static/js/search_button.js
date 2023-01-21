for (el of document.getElementsByClassName('search-button')) {
    el.addEventListener('click', submit);
}

function submit() {
    this.getElementsByTagName('span')[0].classList.remove('visually-hidden');
    this.getElementsByTagName('i')[0].classList.add('visually-hidden');
}