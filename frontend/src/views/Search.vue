<template>
  <div class="search-page-content">
    <div class="article-header">
      <h2>Found articles</h2>
    </div>
    <hr align="left">
    <div class="articles-loading" v-if="articlesLoading">
      <ShimmerBlock/>
    </div>
    <div class="articles-loaded" v-if="!articlesLoading">
      <div class="articles-not-empty" v-if="articles.length > 0">
        <div id="articles">
          <article-component
              v-for="article in articles"
              v-bind:key="article.id"
              v-bind:article="article"
          ></article-component>
        </div>
        <div class="load-more-button" v-if="hasAfter">
          <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
            <span>Load more</span>
          </button>
        </div>
      </div>
      <div class="articles-empty" v-if="articles.length === 0">
        <h1>(´。＿。｀)</h1>
        <p>Oops. There is nothing to show</p>
      </div>
    </div>
  </div>
</template>

<script lang="js">
import ArticleComponent from "@/components/ArticleComponent";
import SearchService from "@/api/SearchService";
import ShimmerBlock from "@/components/ShimmerBlock";

export default {
  name: "Search",
  components: {
    ShimmerBlock,
    ArticleComponent
  },
  data() {
    return {
      articles: [],
      articlesLoading: false,

      hasAfter: false,

      query: '',
      limit: 10,
      offset: 0,
    }
  },
  methods: {
    search: function () {
      this.articlesLoading = true;
      SearchService.search(this.query, this.limit, this.offset)
          .then(response => {
            this.articles = response.data.page.entities;
            this.hasAfter = response.data.page.hasAfter;
            this.articlesLoading = false;
          })
          .catch(e => {
            console.log(e);
          });
    },
    loadMoreArticles: function () {
      this.offset += this.limit;
      this.search();
    }
  },
  created() {
    let params = this.$route.query;

    this.query = params.query;
    this.limit = params.limit;
    this.offset = params.offset;

    this.search();
  }
}
</script>

<style scoped>

.articles-empty {
  padding: 120pt 0 0 0;
}

.article-header, .articles-empty {
  color: var(--text-color);
}
</style>