<template>
  <div>
    <div class="header-main-actions">
      <div class="header-main-link">
        <router-link to="/">Main</router-link>
      </div>
    </div>
    <div class="articles-page-content">
      <div class="header row">
        <div>
          <h1>Articles</h1>
        </div>
        <div class="header-buttons">
          <div class="header-button-editor">
            <router-link to="/article_editor">New article</router-link>
  <!--          todo-->
          </div>
          <div class="header-button-personal-area">
            <router-link to="/personal_area">Personal area</router-link>
          </div>
  <!--        todo-->
        </div>
      </div>
      <div id="articles">
        <div class="article-container" v-for="article in articles" v-bind:key="article.id">
          <h3 class="article-title">
            <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
          </h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import ArticlesService from "../services/ArticlesService";

export default {
  name: "Articles",
  data() {
    return {
      articles: [],
    }
  },
  created() {
    ArticlesService.getAll()
      .then(response => {
        this.articles = response.data;
      })
      .catch(e => {
        console.log(e);
      });
  }
}
</script>

<style scoped>

</style>