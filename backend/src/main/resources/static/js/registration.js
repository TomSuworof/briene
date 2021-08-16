function showAgreementError(element) {
    if (element.value !== 'true') {
        element.setCustomValidity('In order to use service you should agree with terms of use')
    }
}