<template>
  <div class="home-page-content">
    <div class="header">
      <h1>Briene</h1>
    </div>
    <div>
      <h2>Recent articles</h2>
    </div>
    <div id="articles">
      <article-container
          v-for="article in articles"
          v-bind:key="article.id"
          v-bind:article="article"
      ></article-container>
    </div>
    <div class="articles-button">
      <router-link to="/articles">Show more</router-link>
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
import ArticleContainer from "@/components/ArticleContainer";
import ArticlesService from "@/api/ArticlesService";

export default {
  name: "Home",
  components: {
    ArticleContainer
  },
  data() {
    return {
      articles: []
    }
  },
  methods: {
    getLastArticles: function (limit, offset) {
      ArticlesService.getPublishedArticlesPaginated(limit, offset)
          .then(response => {
            this.articles = response.data.articles.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first
            this.hasBefore = response.data.hasBefore;
            this.hasAfter = response.data.hasAfter;
          })
          .catch(e => {
            console.log(e);
          });
    },
  },
  created() {
    this.getLastArticles(10, 0);
    console.log(this.articles);
  }
}
</script>

<style scoped>
.header {
  margin: 0 0 25pt;
}

.articles-button {
  alignment: bottom;
  margin: 0 0 50pt;
}
</style>