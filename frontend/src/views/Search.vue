<template>
  <div class="search-page-content">
    <div class="header">
      <h1>Search</h1>
    </div>
    <div>
      <form @submit="search">
        <input type="text" name="query" v-model="query"/>
        <button type="submit">🔍</button>
      </form>
    </div>
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

<script>
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
      hasAfter: true,

      query: '',
      limit: 10,
      offset: 0,
    }
  },
  methods: {
    search: function () {
      console.log(this.query)
      console.log(this.limit)
      console.log(this.offset)

      SearchService.search(this.query, this.limit, this.offset)
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
    let uri = window.location.search.substring(1);
    let params = new URLSearchParams(uri);

    this.query = params.get("query");
    this.limit = params.get("limit");
    this.offset = params.get("offset");

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