<template>
  <div class="search-page-content">
    <div class="header">
      <p>Search results for:</p>
      <h1><b>{{ query }}</b></h1>
    </div>
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
        <div class="navigation-buttons">
          <div class="navigation-button-prev" title="Previous">
            <button @click="getPreviousPage" :disabled="!hasBefore">◀</button>
          </div>
          <div class="navigation-button-next" title="Next">
            <button @click="getNextPage" :disabled="!hasAfter">▶</button>
          </div>
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

      hasBefore: false,
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
            this.hasBefore = response.data.page.hasBefore;
            this.hasAfter = response.data.page.hasAfter;
            this.articlesLoading = false;
          })
          .catch(e => {
            console.log(e);
          });
    },
    getPreviousPage: function () {
      this.offset -= this.limit;
      this.search();
    },
    getNextPage: function () {
      this.offset += this.limit;
      this.search();
    },
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

.articles-empty {
  padding: 120pt 0 0 0;
}
</style>