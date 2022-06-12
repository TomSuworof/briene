<template>
  <div class="tag-page-content" v-if="tag">
    <div class="header">
      <h1 id="tag-name">#{{ tag }}</h1>
      <p>{{ totalCount }} {{ getFormOfArticles(totalCount) }} this tag</p>
    </div>
    <hr align="left">
    <div class="articles-container">
      <div v-if="articles.length > 0">
        <div id="articles">
          <article-component
              v-for="article in articles"
              :key="article.id"
              :article="article"
              :highlightedTag="tag"
          ></article-component>
        </div>
        <div class="load-more-button">
          <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
            <span>Load more</span>
          </button>
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

      totalCount: 0,
    }
  },
  methods: {
    getFormOfArticles: function (count) {
      if (count === 1) {
        return 'article has'
      }
      return 'articles have'
    },
    loadMoreArticles: function () {
      this.offset += this.limit;
      this.getArticlesPaginated(this.tag, this.limit, this.offset);
    },
    getArticlesPaginated: function (tag, limit, offset) {
      this.tag = tag;
      TagService.getArticlesByTag(tag, limit, offset)
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
            this.hasBefore = response.data.hasBefore;
            this.hasAfter = response.data.hasAfter;
            this.totalCount = response.data.totalCount;
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
#tag-name {
  font-weight: bold;
}
</style>