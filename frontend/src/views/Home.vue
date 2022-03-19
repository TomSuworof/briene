<template>
  <div class="home-page-content">
    <div class="header">
      <h1><pre>{{ logo }}</pre></h1>
    </div>
    <div>
      <h2>Recent articles</h2>
    </div>
    <hr align="left">
    <div v-if="loadingArticles">
      <ShimmerBlock/>
    </div>
    <div v-if="!loadingArticles" id="articles">
      <article-component
          v-for="article in articles"
          v-bind:key="article.id"
          v-bind:article="article"
      ></article-component>
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
import ArticleComponent from "@/components/ArticleComponent";
import ArticlesService from "@/api/ArticlesService";
import ShimmerBlock from "@/components/ShimmerBlock";

export default {
  name: "Home",
  components: {
    ShimmerBlock,
    ArticleComponent,
    ArticleContainer: ArticleComponent
  },
  data() {
    return {
      logo: '<briene>',
      loadingArticles: false,
      articles: []
    }
  },
  methods: {
    getLastArticles: function (limit, offset) {
      this.loadingArticles = true;
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
            this.loadingArticles = false;
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