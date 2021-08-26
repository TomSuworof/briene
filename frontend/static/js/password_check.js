let passwordIsStrong = false;
let passwordsMatch = false;

function passwordChecking() {
    const myInput = document.getElementById("psw");
    const letter = document.getElementById("letter");
    const capital = document.getElementById("capital");
    const number = document.getElementById("number");
    const length = document.getElementById("length");

    const psw = document.getElementById("psw");
    const pswConfirm = document.getElementById("pswConfirm");
    const match = document.getElementById("match");

    // When the user clicks on the password field, show the message box
    myInput.onfocus = function () {
        document.getElementById("message").style.display = "block";
    }

    // When the user clicks outside of the password field, hide the message box
    myInput.onblur = function () {
        document.getElementById("message").style.display = "none";
    }

    // When the user starts to type something inside the password field
    myInput.onkeyup = function () {
        // Validate lowercase letters
        const lowerCaseLetters = /[a-z]/g;
        if (myInput.value.match(lowerCaseLetters)) {
            letter.classList.remove("invalid");
            letter.classList.add("valid");
        } else {
            letter.classList.remove("valid");
            letter.classList.add("invalid");
        }

        // Validate capital letters
        const upperCaseLetters = /[A-Z]/g;
        if (myInput.value.match(upperCaseLetters)) {
            capital.classList.remove("invalid");
            capital.classList.add("valid");
        } else {
            capital.classList.remove("valid");
            capital.classList.add("invalid");
        }

        // Validate numbers
        const numbers = /[0-9]/g;
        if (myInput.value.match(numbers)) {
            number.classList.remove("invalid");
            number.classList.add("valid");
        } else {
            number.classList.remove("valid");
            number.classList.add("invalid");
        }

        // Validate length
        if (myInput.value.length >= 8) {
            length.classList.remove("invalid");
            length.classList.add("valid");
        } else {
            length.classList.remove("valid");
            length.classList.add("invalid");
        }

        passwordIsStrong = (
            myInput.value.match(lowerCaseLetters) &&
            myInput.value.match(upperCaseLetters) &&
            myInput.value.match(numbers) &&
            myInput.value.length >= 8
        );

        if (passwordIsStrong) {
            document.getElementById("message").style.display = "none";
        }
    }


    pswConfirm.onfocus = function () {
        document.getElementById("password-matching").style.display = "block";
    }

    pswConfirm.onkeyup = function () {
        if (psw.value === pswConfirm.value) {
            match.classList.remove("invalid");
            match.classList.add("valid");
            passwordsMatch = true;
        } else {
            match.classList.remove("valid");
            match.classList.add("invalid");
            passwordsMatch = false;
        }
    }

    pswConfirm.onblur = function () {
        document.getElementById("password-matching").style.display = "none";
    }
}

function checkFields() {
    if (passwordsMatch) {
        if (passwordIsStrong) {
            return true;
        } else {
            alert("Password is not strong enough");
            return false;
        }
    } else {
        alert("Passwords do not match");
        return false;
    }
}

export { passwordIsStrong, passwordsMatch, passwordChecking, checkFields };