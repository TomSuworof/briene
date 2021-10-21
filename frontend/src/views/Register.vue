<template>
  <div class="registration-page-content">
    <div class="registration-header">
      <h1>Registration</h1>
    </div>
    <div>
      <Form @submit="handleRegister" :validation-schema="schema">
        <div class="form-group">
          <label>Username</label>
          <Field id="username" name="username" type="text" class="form-control"/>
          <ErrorMessage name="username" class="error-feedback"/>
        </div>
        <div class="form-group">
          <label>Email</label>
          <Field id="email" name="email" type="email" class="form-control"/>
          <ErrorMessage name="email" class="error-feedback"/>
        </div>
        <div class="setting-password">
          <div class="form-group">
            <label>Password</label>
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
        <div class="form-group">
          <label>Secret Question</label>
          <Field id="secret_question" name="secretQuestion" type="text" class="form-control" required/>
          <ErrorMessage name="secret_question" class="error-feedback"/>
        </div>
        <div class="form-group">
          <label>Secret Answer</label>
          <Field id="secret_answer" name="secretAnswer" type="text" class="form-control" required/>
          <ErrorMessage name="secret_answer" class="error-feedback"/>
        </div>
        <div>
          <Field type="checkbox" id="agreement" name="agreement" value="true"/>
          <p>I agree with <router-link to="/terms_of_use">terms of use</router-link></p>
          <ErrorMessage name="agreement" class="error-feedback"/>
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="loading">
            <span v-show="loading" class="spinner-border spinner-border-sm"></span>
            Sign Up
          </button>
        </div>
      </Form>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
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
      message: "",
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
            .then((data) => {
              this.message = data.message;
              this.successful = true;
              this.loading = false;
              this.$router.push("/profile");
            }, (error) => {
              this.message =
                  (error.response &&
                      error.response.data &&
                      error.response.data.message) || error.message || error.toString();
              this.successful = false;
              this.loading = false;
            });
      }
    },
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

</style>