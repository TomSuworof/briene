<template>
  <div class="whole-page">
    <div class="header-buttons row">
      <div class="main-link">
        <router-link to="/">Main</router-link>
      </div>
      <div class="search-bar">
        <div>
          <form @submit="search">
            <input class="query" type="text" name="query" v-model="query"/>
            <button class="search-button" type="submit">🔍</button>
            <input type="number" name="limit" v-model="limit" hidden/>
            <input type="number" name="offset" v-model="offset" hidden/>
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
  /* comes into effect for screens larger than or equal to 481 pixels */
  .content {
    padding: 3% 10% 80pt 10%;
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

.search-button {
  background: white;
  /*display: block;*/
  /*border: none;*/
  /*outline: none;*/
}

</style>