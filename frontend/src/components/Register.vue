<template>
  <div class="registration-page-content">
    <div class="registration-header">
      <h1>Registration</h1>
    </div>
    <div>
      <Form @submit="handleRegister" :validation-schema="schema">
        <div class="form-group">
          <label for="username">Username</label>
          <input is="Field" id="username" name="username" type="text" class="form-control"/>
          <ErrorMessage name="username" class="error-feedback"/>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input is="Field" id="email" name="email" type="email" class="form-control" />
          <ErrorMessage name="email" class="error-feedback" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input is="Field" id="password" name="password" type="password" class="form-control" />
          <ErrorMessage name="password" class="error-feedback" />
        </div>
<!--        <div class="setting-password">-->
<!--          <p>Password:</p>-->
<!--          <div>-->
<!--            <label>-->
<!--              <input id="psw" type="password" name="password" placeholder="New password" required pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters">-->
<!--            </label>-->
<!--          </div>-->
<!--          <div class="container-for-message">-->
<!--            <div id="message" class="message">-->
<!--              <p class="must-contain">Password must contain the following:</p>-->
<!--              <p id="letter" class="invalid">A <b>lowercase</b> letter</p>-->
<!--              <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>-->
<!--              <p id="number" class="invalid">A <b>number</b></p>-->
<!--              <p id="length" class="invalid">Minimum <b>8 characters</b></p>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <label>-->
<!--              <input id="pswConfirm" type="password" name="passwordConfirm" placeholder="Confirm your new password" required>-->
<!--            </label>-->
<!--            <div id="password-matching" class="password-matching">-->
<!--              <p id="match" class="invalid">Passwords match</p>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div>-->
<!--            <p th:text="${passwordError}"></p>-->
<!--          </div>-->
<!--        </div>-->
        <div class="form-group">
          <label for="secret_question">Secret Question</label>
          <input is="Field" id="secret_question" name="secret_question" type="text" class="form-control"/>
          <ErrorMessage name="secret_question" class="error-feedback"/>
        </div>
        <div class="form-group">
          <label for="secret_answer">Secret Answer</label>
          <input is="Field" id="secret_answer" name="secret_answer" type="text" class="form-control"/>
          <ErrorMessage name="secret_answer" class="error-feedback"/>
        </div>
        <div>
          <input type="checkbox" id="agreement" name="agreement" value="false">
          <label for="agreement">
            I agree with <router-link to="/terms_of_use">terms of use</router-link>
          </label>
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
          .min(6, "Must be at least 6 characters!")
          .max(40, "Must be maximum 40 characters!"),
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
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleRegister(user) {
      this.message = "";
      this.successful = false;
      this.loading = true;

      this.$store.dispatch("auth/register", user)
          .then((data) => {
            this.message = data.message;
            this.successful = true;
            this.loading = false;
          }, (error) => {
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) || error.message || error.toString();
            this.successful = false;
            this.loading = false;
          }
      );
    },
  },
};
</script>

<style scoped>

</style>