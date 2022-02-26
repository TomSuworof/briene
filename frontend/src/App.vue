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
            <button type="submit" hidden/>
          </form>
        </div>
      </div>
      <div class="header-buttons-right">
        <div class="header-button-editor">
          <router-link to="/article_editor">New article</router-link>
        </div>
        <div class="header-button-profile">
          <router-link to="/profile">Profile</router-link>
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

.main-link, .header-button-editor, .header-button-profile {
  display: inline-block;
  margin: 0 10pt 0 12pt;
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
}

.form-control:focus {
  background-color: #FEFEFE;
  box-shadow: none;
  border-color: #EEEEEE;
}

button, .btn {
  border-radius: 9px !important;
}

</style>