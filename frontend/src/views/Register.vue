<template>
  <div class="registration-page-content">
    <div class="registration-form-box">
      <div class="registration-header">
        <h1 class="welcome">Registration</h1>
      </div>
      <div class="registration-form">
        <Form @submit="handleRegister" :validation-schema="schema">
          <div class="form-group">
            <label class="form-input-title">Username:</label>
            <Field id="username" name="username" type="text" class="form-control"/>
            <ErrorMessage name="username" class="error-feedback"/>
          </div>
          <div class="form-group">
            <label class="form-input-title">Email:</label>
            <Field id="email" name="email" type="email" class="form-control"/>
            <ErrorMessage name="email" class="error-feedback"/>
          </div>
          <div class="setting-password">
            <div class="form-group">
              <label class="form-input-title">Password:</label>
              <Field id="psw" type="password" name="password" class="form-control" placeholder="Password" required
                     pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                     title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"/>
            </div>
            <div class="container-for-message">
              <div id="message" class="message">
                <p class="must-contain">Password must contain the following:</p>
                <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
                <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
                <p id="number" class="invalid">A <b>number</b></p>
                <p id="length" class="invalid">Minimum <b>8 characters</b></p>
              </div>
            </div>
            <div class="form-group">
              <Field id="pswConfirm" type="password" name="passwordConfirm" class="form-control"
                     placeholder="Confirm your password" required/>
              <div id="password-matching" class="password-matching">
                <p id="match" class="invalid">Passwords match</p>
              </div>
            </div>
          </div>
          <div>
            <div class="agreement-checkbox">
              <Field type="checkbox" id="agreement" name="agreement" value="true"/>
            </div>
            <div class="agreement-checkbox-text">
              <label for="agreement" class="form-input-title">
                I agree with <a href="/terms_of_use">terms of use</a>
              </label>
            </div>
            <div class="form-group">
              <div v-if="message" class="alert alert-danger" role="alert" v-html="message"></div>
            </div>
          </div>
          <div class="form-group center">
            <div class="sign-up-button">
              <button class="button button-primary" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                <span>Sign Up</span>
              </button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  </div>
</template>

<script>
import {Form, Field, ErrorMessage} from "vee-validate";
import * as yup from "yup";
import * as pswChecker from '@/assets/js/password_check'

export default {
  name: "Register",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      username: yup
          .string()
          .required("Username is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      email: yup
          .string()
          .required("Email is required!")
          .email("Email is invalid!")
          .max(50, "Must be maximum 50 characters!"),
      password: yup
          .string()
          .required("Password is required!")
          .min(8, "Must be at least 8 characters!")
          .max(40, "Must be maximum 40 characters!"),
      agreement: yup
          .bool()
          .oneOf([true])
          .required("You must agree to terms of use")
    });

    return {
      successful: false,
      loading: false,
      message: '',
      schema,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  methods: {
    handleRegister: function (user) {
      if (pswChecker.checkFields()) {
        this.message = "";
        this.successful = false;
        this.loading = true;

        this.$store.dispatch("auth/register", user)
            .then(response => {
              this.successful = true;
              this.loading = false;
              this.$router.push("/profile");
            }, (error) => {
              this.successful = false;
              this.loading = false;

              let start = error.response.data.error.indexOf(']') + 2;
              let message = this.capitalize(error.response.data.error.slice(start));

              this.showSuggestionToLogin(message);
            });
      }
    },
    capitalize: function (str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    },
    showSuggestionToLogin: function (message) {
      if (message.includes('email')) {
        message += '. <a href="/login">Login</a>'
      } else if (message.includes('username')) {
        message += '. <a href="/login">Login</a>'
      }
      this.message = message;
    }
  },
  created() {
    this.$nextTick(pswChecker.passwordChecking);
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
};
</script>

<style scoped>
@import "../assets/css/password_checking_style.css";

@media screen and (min-width: 720px) {
  /* comes into effect for screens less than 720 pixels (inclusive) */
  .registration-form-box {
    margin: auto;
    width: 300pt;
  }
}

.welcome, .form-input-title {
  color: var(--text-color);
}

.agreement-checkbox, .agreement-checkbox-text {
  padding: 3pt;
  display: inline-block;
}

.agreement-checkbox-text > label > a {
  text-decoration: underline;
}

.center {
  text-align: center;
}
</style>