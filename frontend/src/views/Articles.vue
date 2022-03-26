<template>
  <div class="articles-page-content">
    <div class="header">
        <h1>Articles</h1>
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
    <div class="load-more-button">
      <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
        <span>Load more</span>
      </button>
    </div>
  </div>
</template>

<script>
import ArticleComponent from "@/components/ArticleComponent";
import ArticlesService from "@/api/ArticlesService";
import ShimmerBlock from "@/components/ShimmerBlock";

export default {
  name: "Articles",
  components: {
    ShimmerBlock,
    ArticleComponent
  },
  data() {
    return {
      loadingArticles: false,
      articles: [],

      hasAfter: true,

      limit: 5,
      offset: 0,
    }
  },
  methods: {
    loadMoreArticles: function () {
      this.offset += this.limit;
      this.getNextArticles(this.limit, this.offset);
    },
    getNextArticles: function (limit, offset) {
      ArticlesService.getPublishedArticlesPaginated(limit, offset)
          .then(response => {
            let articles = response.data.entities.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first
            this.articles = this.articles.concat(articles);
            this.hasAfter = response.data.hasAfter;
          })
          .catch(e => {
            console.log(e);
          });
    },
  },
  created() {
    this.loadingArticles = true;
    this.getNextArticles(this.limit, this.offset);
    this.loadingArticles = false;
  }
}
</script>

<style scoped>

</style>