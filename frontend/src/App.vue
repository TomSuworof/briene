<template>
  <div class="whole-page">
    <div class="header-buttons row">
      <div class="main-link">
        <router-link to="/">Main</router-link>
      </div>
      <div class="search-bar">
        <div>
          <form @submit="search">
            <div class="input-box">
              <input type="text" name="query" class="query form-control" v-model="query"><i class="fa fa-search"></i>
            </div>
            <input type="number" name="limit" v-model="limit" hidden/>
            <input type="number" name="offset" v-model="offset" hidden/>
            <button type="submit" hidden title="Submit search request"/>
          </form>
        </div>
      </div>
      <div class="header-buttons-right">
        <div class="header-button-editor">
          <router-link to="/article_editor">New article</router-link>
        </div>
        <div class="header-button-profile">
          <div v-if="currentUser">
            <router-link to="/profile">Profile</router-link>
          </div>
          <div v-if="!currentUser">
            <router-link to="/login">Login</router-link>
          </div>
        </div>
      </div>
    </div>
    <div class="content">
      <router-view/>
    </div>

  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      query: '',
      limit: 10,
      offset: 0
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  methods: {
    search: function () {
      this.$router.push({
        path: '/search',
        query: { query: this.query, limit: this.limit, offset: this.offset }
      });
    }
  }
}

</script>

<style>
a {
  color: black;
}

a:hover {
  color: #F7C71B;
}

hr {
  max-width: 700pt;
}

@media screen and (max-width: 720px) {
  /* comes into effect for screens less than 720 pixels (inclusive) */
  .content {
    padding: 3% 5% 80pt 5%;
  }
}

@media screen and (min-width: 721px) {
  /* comes into effect for screens larger than or equal to 721 pixels */
  .content {
    padding: 3% 10% 80pt 10%;
  }

  .input-box {
    width: 400pt;
  }
}

.row {
  width: 100%;
}

.header-buttons {
  padding: 10pt;
  justify-content: space-between;
}

.main-link, .header-button-editor, .header-button-profile, .footer-link {
  display: inline-block;
  margin: 0 10pt 10pt 12pt;
}

.input-box {
  height: 100%;
  position: relative
}

.input-box i {
  position: absolute;
  right: 13px;
  top: 10px;
  color: #ced4da
}

.form-control {
  background: white;
  border-radius: 9px;
  margin: 0 0 10pt;
  padding: 10pt;
  transition: border-color, border-width 0.3s;
}

.form-control:focus {
  background-color: #FEFEFE;
  box-shadow: none;
  border-color: #f7c71b;
  border-width: 2px;
}

button:disabled,
button[disabled]{
  border: 1px solid #999999;
  background-color: #cccccc;
  color: #666666;
}

button {
  border: none;
  background: none;
  border-radius: 22px;
}

.button {
  height: 2.3rem;
  padding: 2pt 15pt;
  margin: 2pt;
  border-radius: 22px;
  border: 1px solid;
  background: #f8f9fa;
}

.button:focus {
  box-shadow: 0 0 0 0.2rem rgb(100 100 100 / 70%);
}

.button-primary {
  background: #F7C71B;
  border-color: #F7C71B;
  color: white;
}

.button-primary:hover {
  background: #E7B70A;
  color: white;
}

.button-outline {
  background-color: white;
  border-color: #7C640C;
}

.button-outline:hover {
  background: #f2f6ea;
}

.button-danger {
  background: #dc3545;
  border-color: #dc3545;
  color: white;
}

.button-danger:hover {
  background: #c82333;
}

.button-success {
  background: #A6A228;
  border-color: #A6A228;
  color: white;
}

.button-success:hover {
  background: #85821F;
}

.button-icon, .button-message {
  display: inline-block;
  margin: 2pt;
}


.navigation-button-prev, .navigation-button-next {
  display: inline-block;
  margin: 0 10pt 0;
}

.navigation-button {
  background: white;
  display: block;
  border: none;
  outline: none;
}

</style>