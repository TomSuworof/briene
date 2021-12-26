function setupAccordion() {
    let acc = document.getElementsByClassName("accordion__intro");
    let i;

    for (i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function () {
            this.classList.toggle("active");
            let panel = this.nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }
            // animated - does not work
            // if (panel.style.maxHeight) {
            //     panel.style.maxHeight = null;
            // } else {
            //     panel.style.maxHeight = panel.scrollHeight + "px";
            // }
        });
    }
}

export { setupAccordion };