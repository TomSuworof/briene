<template>
  <div class="index-page-content">
    <div class="header">
      <h1>Welcome to Briene</h1>
    </div>
    <div class="articles-button">
<!--      <form action="/articles">-->
<!--        <button>Articles</button>-->
<!--      </form>-->
      <router-link to="/articles">Articles</router-link>
    </div>
    <div class="footer">
      <div>
        <router-link to="/terms_of_use">Terms of use</router-link>
      </div>
      <div>
        <a href="https://github.com/TomSuworof/educa">Source code</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>

</style>