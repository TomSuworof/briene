<template>
  <div class="tag-page-content" v-if="tag">
    <div class="header row">
      <div>
        <h1>#{{ tag }}</h1>
      </div>
    </div>
    <div class="articles-container">
      <div v-if="articles.length > 0">
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
      <div v-else-if="articles.length === 0">
        <p>No articles</p>
      </div>
    </div>
  </div>
</template>

<script>
import ArticleComponent from "@/components/ArticleComponent";
import TagService from "@/api/TagService";

export default {
  name: "Tag",
  components: {
    ArticleComponent,
  },
  data() {
    return {
      tag: undefined,
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
      this.getArticlesPaginated(this.tag, this.limit, this.offset);
    },
    getNextPage: function () {
      this.offset += this.limit;
      this.getArticlesPaginated(this.tag, this.limit, this.offset);
    },
    getArticlesPaginated: function (tag, limit, offset) {
      TagService.getArticlesByTag(tag, limit, offset)
          .then(response => {
            this.tag = tag;
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
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
  },
  created() {
    this.getArticlesPaginated(this.$route.params.tag, this.limit, this.offset);
  }
}
</script>

<style scoped>
.header {
  justify-content: space-between;
  margin: 0 0 10pt;
}

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