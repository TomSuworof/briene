<template>
  <div class="author-page-content" v-if="author">
    <div class="header row">
      <div>
        <h1>{{ author.username }}</h1>
      </div>
    </div>
    <div class="author-bio">
      <p>{{ author.bio }}</p>
    </div>
    <div v-if="loadingArticles">
      <ShimmerBlock/>
    </div>
    <div v-if="!loadingArticles" class="articles">
      <div v-if="articles.length > 0">
        <article-component
            v-for="article in articles"
            v-bind:key="article.id"
            v-bind:article="article"
        ></article-component>
      </div>
      <div v-else-if="articles.length === 0">
        <p>No articles</p>
      </div>
    </div>
    <div class="load-more-button">
      <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
        <span>Load more</span>
      </button>
    </div>
  </div>
</template>

<script>
import AuthorsService from "@/api/AuthorsService";
import ArticleComponent from "@/components/ArticleComponent";
import ShimmerBlock from "@/components/ShimmerBlock";

export default {
  name: "Author",
  components: {
    ShimmerBlock,
    ArticleComponent,
  },
  data() {
    return {
      author: undefined,
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
      this.loadAuthorData(this.author.username, this.limit, this.offset);
    },
    loadAuthorData: function (authorName, limit, offset) {
      AuthorsService.getAuthorData(authorName, limit, offset)
          .then(response => {
            this.author = response.data;
            document.title = this.author.username;
            let articles = this.author.articles.entities.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first;
            this.articles = this.articles.concat(articles);
            this.hasAfter = this.author.articles.hasAfter;
          })
          .catch(err => {
            console.log(err);
            this.$router.replace('/error'); // redirecting to '/error'
          });
    }
  },
  created() {
    this.loadingArticles = true;
    this.loadAuthorData(this.$route.params.authorName, this.limit, this.offset);
    this.loadingArticles = false;
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>
.header {
  justify-content: space-between;
  margin: 0 0 10pt;
}

.author-bio {
  white-space: pre-line;
  padding: 0 0 5pt;
}
</style>