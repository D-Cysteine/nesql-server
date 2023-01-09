for (el of document.getElementsByClassName("icon")) {
    el.addEventListener("error", setMissingImage, {once: true});
}

function setMissingImage() {
    this.src = "/image/missing.png";
}