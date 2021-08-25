<template>
  <div>
    <div class="author-page-content" v-if="author">
      <div class="header row">
        <div>
          <h1>{{ author.username }}</h1>
        </div>
      </div>
      <div>
        <div class="article-container" v-for="article in author.articles" v-bind:key="article.id">
          <h3 class="article-title">
            <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
          </h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthorsService from "@/services/AuthorsService";

export default {
  name: "Author",
  data() {
    return {
      author: undefined,
    }
  },
  created() {
    AuthorsService.getAuthorData(this.$route.params.authorName)
        .then(response => {
          this.author = response.data;
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

.article-container {
  margin: 0 0 10pt;
}

.article-title {
  overflow-wrap: break-word;
}
</style>