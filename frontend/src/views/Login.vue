<template>
  <div class="login-page-content">
    <div class="login-form-box">
      <div class="login-header">
        <h1 class="welcome">Log In</h1>
      </div>
      <div class="login-form">
        <Form @submit="handleLogin" :validation-schema="schema">
          <div class="form-group">
            <label class="form-input-title">Username:</label>
            <Field id="username" name="username" type="text" autocomplete="username" placeholder="Username" class="form-control"/>
            <ErrorMessage name="username" class="error-feedback alert alert-danger"/>
          </div>
          <div class="form-group">
            <label class="form-input-title">Password:</label>
            <Field id="password" name="password" type="password" autocomplete="current-password" placeholder="Password" class="form-control"/>
            <ErrorMessage name="password" class="error-feedback alert alert-danger"/>
          </div>
          <div class="form-group button-row-center">
            <div class="log-in-button">
              <button class="button button-primary" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                <span>Log In</span>
              </button>
            </div>
            <div class="sign-up-button">
              <button class="button button-outline" @click="$router.push('/register')">
                <span>Sign Up</span>
              </button>
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
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from 'yup';

export default {
  name: "Login",
  components: {
    Form,
    Field,
    ErrorMessage
  },
  props: {
    username: {required:false},
    email: {required:false},
  },
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
                this.message = 'Invalid credentials';
                console.log((error.response &&
                    error.response.data &&
                    error.response.data.message) || error.message || error.toString());
              }
          );
    }
  },
}
</script>

<style scoped>

@media screen and (min-width: 720px) {
  /* comes into effect for screens less than 720 pixels (inclusive) */
  .login-form-box {
    margin: auto;
    width: 300pt;
  }
}

.welcome, .form-input-title {
  color: var(--text-color);
}

.button-row-center {
  display: flex;
  gap: 10pt;
  justify-content: center;
}

.center {
  text-align: center;
}
</style>