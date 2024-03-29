<template>
  <div class="password-reset-page-content">
    <h1>Password reset</h1>
    <div v-show="currentComponent === 0">
      <Form @submit="sendPasswordResetRequest">
        <div class="form-group">
          <Field id="username" name="username" type="text" placeholder="Username" class="form-control"/>
        </div>
        <div class="form-group center">
          <div class="send-button">
            <button class="button button-primary" :disabled="loading">
              <span v-show="loading" class="spinner-border spinner-border-sm"></span>
              <span>This is me, send request</span>
            </button>
          </div>
        </div>
      </Form>
    </div>
    <div v-show="currentComponent === 1">
      <div class="new-password-text-block">
        <p>Message was sent to your email:</p>
        <b>{{ hiddenEmail }}</b>
      </div>
      <div>
        <Form @submit="changePassword">
          <div class="form-group">
            <Field id="code" name="code" type="text" placeholder="Code" class="form-control"/>
          </div>
          <div class="form-group">
            <Field id="psw" name="newPassword" type="password" placeholder="New password" class="form-control"/>
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
            <div class="change-password-button">
              <button class="button button-primary" :disabled="loading">
                <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                <span>Change password</span>
              </button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  </div>
</template>

<script>
import { Form, Field } from "vee-validate";
import PasswordResetService from "@/api/PasswordResetService";
import * as pswChecker from "@/assets/js/password_check";

export default {
  name: "PasswordReset",
  components: { Form, Field },
  data() {
    return {
      loading: false,

      currentComponent: 0,
      hiddenEmail: '',
    };
  },
  methods: {
    sendPasswordResetRequest: function(user) {
      this.loading = true;
      PasswordResetService.sendPasswordResetRequest(user.username)
        .then(response => {
          this.loading = false;
          this.hiddenEmail = response.data.hiddenEmail;
        })
        .catch(e => {
          console.log(e);
        });
      this.currentComponent++;
    },
    changePassword: function (passwordChangeRequest) {
      this.loading = true;
      PasswordResetService.changePassword(passwordChangeRequest.code, passwordChangeRequest.newPassword)
        .then(response => {
          this.loading = false;
          console.log(response.data);
          this.$router.push('/login');
        });
    }
  },
  created() {
    this.$nextTick(pswChecker.passwordChecking);
  }
}
</script>

<style scoped>
@import "../assets/css/password_checking_style.css";

@media screen and (min-width: 720px) {
  /* comes into effect for screens less than 720 pixels (inclusive) */
  .password-reset-page-content {
    margin: auto;
    width: 300pt;
  }
}

h1 {
  color: var(--text-color);
}

.new-password-text-block {
  color: var(--text-color);
  padding: 10pt;
}

.center {
  text-align: center;
}
</style>