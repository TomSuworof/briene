<template>
  <div class="articles-page-content">
    <div class="header">
        <h1>Articles</h1>
    </div>
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
    <div class="navigation-buttons">
      <div class="navigation-button-prev" title="Previous">
        <button @click="getPreviousPage" :disabled="!hasBefore">◀</button>
      </div>
      <div class="navigation-button-next" title="Next">
        <button @click="getNextPage" :disabled="!hasAfter">▶</button>
      </div>
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

      hasBefore: false,
      hasAfter: true,

      limit: 10,
      offset: 0,
    }
  },
  methods: {
    getPreviousPage: function () {
      this.offset -= this.limit;
      this.getArticlesPaginated(this.limit, this.offset);
    },
    getNextPage: function () {
      this.offset += this.limit;
      this.getArticlesPaginated(this.limit, this.offset);
    },
    getArticlesPaginated: function (limit, offset) {
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
    this.getArticlesPaginated(this.limit, this.offset);
  }
}
</script>

<style scoped>
.navigation-button-prev, .navigation-button-next {
  display: inline-block;
  margin: 0 10pt 0;
}

button {
  background: white;
  display: block;
  border: none;
  outline: none;
}
</style>