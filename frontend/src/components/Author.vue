<template>
  <div>
    <div class="author-page-content" v-if="author">
      <div class="header row">
        <div>
          <h1>{{ author.username }}</h1>
        </div>
      </div>
      <div class="author-bio">
        <p>{{ author.bio }}</p>
      </div>
      <div class="articles">
        <div v-if="articles.length > 0">
          <article-container
              v-for="article in articles"
              v-bind:key="article.id"
              v-bind:article="article"
          ></article-container>
        </div>
        <div v-else-if="articles.length === 0">
          <p>No articles</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthorsService from "@/services/AuthorsService";
import ArticleContainer from "@/components/ArticleContainer";

export default {
  name: "Author",
  components: {
    ArticleContainer,
  },
  data() {
    return {
      author: undefined,
      articles: [],
    }
  },
  created() {
    AuthorsService.getAuthorData(this.$route.params.authorName)
        .then(response => {
          this.author = response.data;
          this.articles = this.author.articles.sort((article1, article2) => {
            if (article1.publicationDate < article2.publicationDate) {
              return -1;
            }
            if (article1.publicationDate > article2.publicationDate) {
              return 1;
            }
            return 0;
          }).reverse(); // newer first;
        })
        .catch(err => {
          console.log(err);
          this.$router.replace('/error'); // redirecting to '/error'
        });
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