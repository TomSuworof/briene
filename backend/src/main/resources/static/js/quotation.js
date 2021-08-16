function makeQuote(citedText) {
    let currentArticleUrl = window.location.href;
    let citedArticleTitle = document.title;

    let quote = `
<figure>
  <blockquote>
    <q>${citedText}</q>
  </blockquote>
  <p>- <a href="${currentArticleUrl}/#:~:text=${citedText}"><i>${citedArticleTitle}</i></a></p>
</figure>`;

    console.log(quote);

    navigator.clipboard.writeText(quote).then(() => console.log("Quote copied to clipboard"));
} // todo эта функция выполняется дважды - починить

$(document).ready(function() {
    let control = document.getElementById("control");

    $('#article-content').on('mouseup', function() {
       const selectedText = document.getSelection();

       if (!selectedText.toString()) {
           control.style.display = 'none';
           return;
       }

       let rect = selectedText.getRangeAt(0).getBoundingClientRect();

        control.addEventListener('mousedown', function () {
            this.style.display = 'none';
            console.log(selectedText.toString());
            makeQuote(selectedText.toString());
        })

        control.style.top = `calc(${rect.top}px - 48px)`;
        control.style.left = `calc(${rect.left}px + ${rect.width}px / 2 - 20px)`;
        control.style.display = 'block';
    });
})