<template>
  <div class="article-container">
    <div class="article-about row">
      <div>
        <div class="article-author">
          <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
        </div>
        <div class="article-state" v-show="state !== undefined">
          <p>#{{ state }}</p>
        </div>
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
    <div class="article-action-container" v-for="action in actions" v-bind:key="action.id">
      <div class="article-action-button">
        <a @click="action.function(article.id)" href="#">{{ action.message }}</a>
      </div>
    </div>
    <div class="article-summary">
      <p>{{ article.summary }}</p>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "ArticleComponent",
  props: ["article", "actions", "state"],
  methods: {
    getFinePublicationDate: function (article) {
      return moment(Date.parse(article.publicationDate)).format("DD.MM.YYYY HH:mm")
    },
  }
}

</script>

<style scoped>
.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-container {
  background: white;
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 10px;
  margin: 0 0 10pt;
  padding: 10pt;
}

.article-about {
  padding: 0 10pt 0;
  justify-content: space-between;
}

.article-author, .article-state, .article-publication-date {
  padding: 2pt;
  display: inline-block;
}

.article-publication-date {
  color: #999;
}

.article-action-container, .article-title-container, .article-action-button {
  display: inline-block;
  margin: 0 20pt 0 0;
}
</style>