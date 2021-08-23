<template>
  <div class="login-page-content">
    <div>
      <Form @submit="handleLogin" :validation-schema="schema">
        <h2 class="welcome">Log In</h2>
        <div class="form-group">
          <label for="username">Username</label>
          <input is="Field" id="username" name="username" type="text" placeholder="Username" class="form-control"/>
          <ErrorMessage name="username" class="error-feedback"/>
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input is="Field" id="password" name="password" type="password" placeholder="Password" class="form-control"/>
          <ErrorMessage name="password" class="error-feedback"/>
        </div>
        <div class="form-group">
          <div class="log-in-button">
            <button class="btn btn-primary btn-block" :disabled="loading">
              <span
                  v-show="loading"
                  class="spinner-border spinner-border-sm"
              ></span>
              <span>Login</span>
            </button>
          </div>
          <div class="sign-up-button">
            <router-link to="/register" class="btn btn-block">Sign Up</router-link>
          </div>
        </div>

        <div class="form-group">
          <div v-if="message" class="alert alert-danger" role="alert">
            {{ message }}
          </div>
        </div>
      </Form>
    </div>
    <div>
      <router-link to="/password_reset">Forgot password</router-link>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: "Login",
  components: {Form, Field, ErrorMessage},
  data() {
    const schema = yup.object().shape({
      username: yup.string().required("Username is required!"),
      password: yup.string().required("Password is required!"),
    });

    return {
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
  created() {
    console.log(this.loggedIn);
    console.log(window.origin);
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleLogin(user) {
      this.loading = true;

      this.$store.dispatch("auth/login", user)
          .then(() => {
            this.$router.push("/profile");
          }, (error) => {
            this.loading = false;
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) || error.message || error.toString();
          }
      );
    },
  },
}
</script>

<style scoped>
.login-page-content {
  margin: auto;
  text-align: center;
}

.log-in-button, .sign-up-button {
  display: inline-block;
  margin: 0 20pt 0;
}

</style>