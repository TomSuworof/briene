<template>
  <div class="whole-page">
    <div class="header-buttons row">
      <div class="header-buttons-left">
        <div class="main-link">
          <router-link to="/"><pre>{{ logo }}</pre></router-link>
        </div>
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
        <div class="header-button-editor-img">
          <router-link to="/article_editor" class="fa fa-pencil"></router-link>
        </div>
        <div class="header-button-profile-img">
          <router-link to="/profile" class="fa fa-user"></router-link>
        </div>
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
      logo: '<briene>',

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
  },
  mounted() {
    this.query = new URLSearchParams(window.location.search).get('query');
  }
}

</script>

<style>
pre {
  font-family: JetBrains Mono, monospace;
}

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
  .header-button-editor-img, .header-button-profile-img {
    padding-top: 7pt;
    display: inline-block;
    padding-left: 30pt;
  }

  .header-buttons, .header-buttons-left, .search-bar {
    padding: 3pt !important;
  }

  .main-link {
    margin: 0 0 10pt 10pt !important;
  }

  .search-bar {
    width: 140pt;
  }

  .header-button-editor, .header-button-profile {
    display: none !important;
  }

  .content {
    padding: 3% 5% 80pt 5%;
  }
}

@media screen and (min-width: 721px) {
  .header-button-editor-img, .header-button-profile-img {
    display: none;
  }

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

.main-link {
  font-weight: bold;
  font-style: italic;
  font-size: 1.2rem;
}

.main-link, .header-button-editor, .header-button-profile, .footer-link {
  display: inline-block;
  margin: 0 10pt 10pt 20pt;
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

button > span {
  margin-left: 5pt;
  margin-right: 5pt;
}

.button {
  height: 2.3rem;
  padding: 2pt 10pt;
  margin: 2pt;
  border-radius: 22px;
  border: 1px solid;
  background: #f8f9fa;
  transition: background-color 0.17s;
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
  background: #AFAF28;
  border-color: #AFAF28;
  color: white;
}

.button-success:hover {
  background: #99991F;
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