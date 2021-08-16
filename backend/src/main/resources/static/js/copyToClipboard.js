function copyToClipboard(str) {
    const link = document.location.host.toString() + str;
    navigator.clipboard.writeText(link).then(() => alert("Link copied to clipboard: " + link));
}