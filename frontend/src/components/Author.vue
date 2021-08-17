<template>
  <div>
    <div class="header-main-actions">
      <div class="header-main-link">
        <router-link to="/">Main</router-link>
      </div>
    </div>
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
    AuthorsService.get(this.$route.params.authorName)
        .then(response => {
          this.author = response.data;
        })
        .catch(err => {
          console.log(err);
        });
  }
}
</script>

<style scoped>

</style>