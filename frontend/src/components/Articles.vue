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
        <div class="article-about row">
          <div class="article-author">
            <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
          </div>
          <div class="article-publication-date">
            <p>{{ getFinePublicationDate(article) }}</p>
          </div>
        </div>
        <div class="article-title-container">
          <h3 class="article-title">
            <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
          </h3>
        </div>
        <div class="article-summary">
          <p>{{ article.summary }}</p>
        </div>
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
import moment from "moment";

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
    getFinePublicationDate: function(article) {
      return moment(Date.parse(article.publicationDate)).format("DD.MM.YYYY HH:mm")
    },
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

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-container {
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 10px;
  margin: 0 0 10pt;
  padding: 10pt;
}

.article-about {
  padding: 0 15pt 0;
  justify-content: space-between;
}

.article-author, .article-publication-date {
  display: inline-block;
}

.article-publication-date {
  color: #999;
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