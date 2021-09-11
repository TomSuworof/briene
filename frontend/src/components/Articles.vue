<template>
  <div class="articles-page-content">
    <div class="header row">
      <div>
        <h1>Articles</h1>
      </div>
      <div class="header-buttons">
        <div class="header-button-editor">
          <router-link to="/article_editor">New article</router-link>
        </div>
        <div class="header-button-personal-area">
          <router-link to="/profile">Profile</router-link>
        </div>
      </div>
    </div>
    <div id="articles">
      <div class="article-container" v-for="article in articles" v-bind:key="article.id">
        <h3 class="article-title">
          <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
        </h3>
      </div>
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

import ArticlesService from "@/services/ArticlesService";

export default {
  name: "Articles",
  data() {
    return {
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
      this.getArticlesPaginated(this.limit, this.offset);
    },
    getNextPage: function () {
      this.offset += this.limit;
      this.getArticlesPaginated(this.limit, this.offset);
    },
    getArticlesPaginated: function (limit, offset) {
      ArticlesService.getPublishedArticlesPaginated(limit, offset)
          .then(response => {
            this.articles = response.data.articles;
            this.hasBefore = response.data.hasBefore;
            this.hasAfter = response.data.hasAfter;
          })
          .catch(e => {
            console.log(e);
          });
    },
  },
  created() {
    this.getArticlesPaginated(this.limit, this.offset);
  }
}
</script>

<style scoped>
.header {
  padding: 0 10pt 0;
  justify-content: space-between;
}

.header-button-editor, .header-button-personal-area {
  margin: 0 10pt 0;
  display: inline-block;
}

.article-title {
  overflow-wrap: break-word;
}

.article-container {
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