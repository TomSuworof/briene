<template>
  <div class="login-page-content">
    <div class="login-form-box">
      <div class="login-header">
        <h1 class="welcome">Log In</h1>
      </div>
      <div class="login-form">
        <Form @submit="handleLogin" :validation-schema="schema">
          <div class="form-group">
            <label>Username:</label>
            <Field id="username" name="username" type="text" placeholder="Username" class="form-control"/>
            <ErrorMessage name="username" class="error-feedback"/>
          </div>
          <div class="form-group">
            <label>Password:</label>
            <Field id="password" name="password" type="password" placeholder="Password" class="form-control"/>
            <ErrorMessage name="password" class="error-feedback"/>
          </div>
          <div class="form-group center">
            <div class="log-in-button">
              <button class="btn btn-primary btn-block" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                <span>Login</span>
              </button>
            </div>
            <div class="sign-up-button">
              <router-link to="/register" class="btn btn-block btn-light">Sign Up</router-link>
            </div>
          </div>
          <div class="form-group">
            <div v-if="message" class="alert alert-danger" role="alert">
              {{ message }}
            </div>
          </div>
        </Form>
      </div>
      <div class="center">
        <router-link to="/password_reset">Forgot password</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import {Form, Field} from "vee-validate";
import * as yup from 'yup';

export default {
  name: "Login",
  components: {Form, Field},
  data() {
    const schema = yup.object().shape({
      username: yup.string().required("Username if required!"),
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
    if (this.loggedIn) {
      this.$router.push("/profile");
    }
  },
  methods: {
    handleLogin: function (user) {
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
.login-form-box {
  width: 300pt;
  margin: auto;
}

.log-in-button, .sign-up-button {
  padding: 3pt 0;
}

.center {
  text-align: center;
}
</style>