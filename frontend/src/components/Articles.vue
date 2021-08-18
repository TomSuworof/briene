<template>
  <div>

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
  mounted() {
    ArticlesService.getPublishedArticles()
      .then(response => {
        console.log(response)
        this.articles = response.data;
      })
      .catch(e => {
        console.log(e);
      });
  }
}
</script>

<style scoped>
.header {
  justify-content: space-between;
}

.header-button-editor, .header-button-personal-area {
  margin: 0 10pt 0;
  display: inline-block;
}

.article-title {
  overflow-wrap: break-word;
}

.article-container {
  margin: 0 0 10pt;
}
</style>