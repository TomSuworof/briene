<template>
  <div class="article-container">
    <div v-if="actions !== undefined">
      <b-dropdown no-caret="true" variant="link" id="dropdown-right" toggle-class="text-decoration-none" class="article-action-container">
        <b-dropdown-item class="article-action-item"
            href="#"
            v-for="action in actions"
            v-bind:key="action.id"
            @click="action.function(article.id)">
          <span class="button-icon" v-html="action.icon"/>
          <span class="button-message">{{ action.message }}</span>
        </b-dropdown-item>
      </b-dropdown>
    </div>
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
        <time :datetime="article.publicationDate">{{ getFinePublicationDate(article.publicationDate) }}</time>
      </div>
    </div>
    <div class="article-title-container">
      <h3 class="article-title">
        <a :href="'/articles/' + article.id">{{ article.title }}</a>
      </h3>
    </div>
    <div class="article-summary">
      <p>{{ article.summary }}</p>
    </div>
    <div class="article-tags tags-container">
      <ul class="tags-list">
        <li class="tag-item" v-for="tag in article.tags">
          <a :href="'/tags/' + tag">{{ tag }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "ArticleComponent",
  props: ["article", "actions", "state"],
  data() {
    return {
      actionsShowed: false,
    }
  },
  methods: {
    getFinePublicationDate: function (publicationDate) {
      return moment(Date.parse(publicationDate)).format("DD.MM.YYYY HH:mm");
    }
  }
}

</script>

<style scoped>
.article-title {
  font-weight: bold;
}

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-container {
  max-width: 700pt;
  position: relative;
  background: white;
  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 9px;
  margin: 0 0 20pt;
  padding: 15pt;
  transition: background 0.1s ease-in-out;
  border: solid 1px transparent;
}

.article-container:hover {
  background: #F7F7F7;
}

.article-container:active {
  border: solid 1px black;
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

.article-action-container {
  position: absolute;
  right: 5pt;
  top: 5pt;
}

.tags-container {
  padding: 5pt 0 0 0;
}

.tags-list {
  padding-left: 0;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
  display: inline-block;
  position: relative;
  margin: 0 10pt 0 0;
}

</style>