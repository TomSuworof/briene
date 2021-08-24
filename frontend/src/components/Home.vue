<template>
  <div class="home-page-content">
    <div class="header">
      <h1>Briene</h1>
    </div>
    <div class="articles-button">
      <router-link to="/articles">Articles</router-link>
    </div>
    <div class="footer">
      <div>
        <router-link to="/terms_of_use">Terms of use</router-link>
      </div>
      <div>
        <a href="https://github.com/TomSuworof/briene" target="_blank">Source code</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home"
}
</script>

<style scoped>
.home-page-content {
  margin: auto;
  text-align: center;
}

.header {
  margin: 0 0 50pt;
}

.articles-button {
  alignment: bottom;
  margin: 0 0 50pt;
}
</style>