<template>
  <div class="article-container" @click.self="$router.push('/articles/' + article.url)">
    <div class="article-author">
      <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
    </div>
    <div class="article-header">
      <div>
        <h3 class="article-title">
          <a :href="'/articles/' + article.url">{{ article.title }}</a>
        </h3>
      </div>
      <div class="article-util-container" v-if="actions !== undefined">
        <div class="article-state" v-if="state !== undefined">
          <p>#{{ state }}</p>
        </div>
        <div class="article-action-item"
             href="#"
             v-for="action in actions"
             v-bind:key="action.id"
             @click="action.function(article.id)">
          <span class="button-icon" v-html="action.icon"/>
        </div>
      </div>
    </div>
    <div class="article-subheader">
      <div class="article-publication-date">
        <time :datetime="article.publicationDate">{{ getFinePublicationDate(article.publicationDate) }}</time>
      </div>
      <div class="tags-container">
        <ul class="tags-list">
          <li :class="getClassForTag(tag)" v-for="tag in article.tags">
            <a :href="'/tags/' + tag">{{ tag }}</a>
          </li>
        </ul>
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
  props: ["article", "actions", "state", "highlightedTag"],
  data() {
    return {
      actionsShowed: false,
    }
  },
  methods: {
    getFinePublicationDate: function (publicationDate) {
      return moment(Date.parse(publicationDate)).format("DD.MM.YYYY HH:mm");
    },
    getClassForTag: function (tag) {
      let tagClass = 'tag-item';
      if (tag === this.highlightedTag) {
        tagClass += ' tag-highlight';
      }
      return tagClass;
    }
  }
}

</script>

<style scoped>
.article-container {
  max-width: 700pt;
  border-radius: 9px;
  margin-bottom: 30pt;
  border: solid 1px transparent;
}

.article-container:active {
  box-shadow: rgba(0, 0, 0, 0.1) 0 0.5pt 1pt;
  background-color: var(--background-color-secondary);
}

a {
  color: var(--text-color);
}

a:hover {
  color: #F7C71B;
}

.article-author {
  font-size: 12px;
}

.article-title {
  font-weight: bold;
}

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-header {
  display: grid;
  grid-template-columns: auto auto;
  justify-content: space-between;
}

.article-state, .article-action-item {
  display: inline-block;
  padding-left: 5pt;
  color: var(--text-color);
}

.article-subheader {
  display: flex;
  gap: 10pt;
}

.article-publication-date {
  font-size: 12px;
  margin-bottom: 0;
  padding-bottom: 0;
}

.tags-list {
  padding-left: 0;
  padding-bottom: 5pt;
  margin: 0;
  display: flex;
  float: left;
  gap: 7pt;
  list-style-type: none;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
}

.tag-highlight {
  background-color: #F7F71B;
}

.tag-item > a, .article-publication-date {
  color: #666;
}

.article-summary > p {
  color: var(--text-color);
}
</style>