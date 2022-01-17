<template>
  <div class="search-page-content">
    <div class="header">
      <p>Search results for:</p>
      <h1>{{ query }}</h1>
    </div>
<!--    <div>-->
<!--      <form @submit="search" class="row">-->
<!--        <input class="query" type="text" name="query" v-model="query"/>-->
<!--        <input type="number" name="limit" v-model="limit" hidden/>-->
<!--        <input type="number" name="offset" v-model="offset" hidden/>-->
<!--        <button class="search-button" type="submit">🔍</button>-->
<!--      </form>-->
<!--    </div>-->
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
</template>

<script lang="js">
import ArticleComponent from "@/components/ArticleComponent";
import SearchService from "@/api/SearchService";

export default {
  name: "Search",
  components: {
    ArticleComponent
  },
  data() {
    return {
      articles: [],

      hasBefore: false,
      hasAfter: false,

      query: '',
      limit: 10,
      offset: 0,
    }
  },
  methods: {
    search: function () {
      SearchService.search(this.query, this.limit, this.offset)
          .then(response => {
            this.articles = response.data.page.articles;
            this.hasBefore = response.data.page.hasBefore;
            this.hasAfter = response.data.page.hasAfter;

            console.log(response.data.suggest);
          })
          .catch(e => {
            console.log(e);
          })
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
</style>