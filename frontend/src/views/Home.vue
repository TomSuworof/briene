<template>
  <div class="home-page-content">
    <div class="home-page-title">
      <h2>Fresh articles</h2>
    </div>
    <hr align="left">
    <div class="articles">
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
      <div class="load-more-button" v-if="hasAfter">
        <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
          <span>More</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ShimmerBlock from "@/components/ShimmerBlock";
import ArticleComponent from "@/components/ArticleComponent";
import ArticlesService from "@/api/ArticlesService";

export default {
  name: "Home",
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
            let articles = response.data.entities;
            this.articles = this.articles.concat(articles);
            this.hasAfter = response.data.hasAfter;
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
.home-page-title {
  color: var(--text-color);
}
</style>