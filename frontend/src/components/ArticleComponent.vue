<template>
  <div class="article-container" @click.self="$router.push('/articles/' + article.url)">
    <div class="article-author">
      <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
    </div>
    <div class="article-title-container">
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
    <div class="article-publication-date">
      <time :datetime="article.publicationDate">{{ getFinePublicationDate(article.publicationDate) }}</time>
    </div>
    <div class="article-tags tags-container">
      <ul class="tags-list">
        <li :class="getClassForTag(tag)" v-for="tag in article.tags">
          <a :href="'/tags/' + tag">{{ tag }}</a>
        </li>
      </ul>
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
a {
  color: var(--text-color);
}

a:hover {
  color: #F7C71B;
}

.article-title {
  font-weight: bold;
}

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-summary > p {
  max-width: 700pt;
  margin-bottom: 0.6rem;
  color: var(--text-color);
}

.article-container {
  max-width: 700pt;
  position: relative;
  border-radius: 9px;
  margin-bottom: 30pt;
  padding-left: 3pt;
  border: solid 1px transparent;
}

.article-container:active {
  box-shadow: rgba(0, 0, 0, 0.1) 0 0.5pt 1pt;
  background-color: var(--background-color-secondary);
}

.article-author {
  font-size: 12px;
}

.article-title-container {
  display: grid;
  grid-template-columns: auto auto;
  justify-content: space-between;
}

.article-state, .article-action-item {
  display: inline-block;
  padding-left: 5pt;
  color: var(--text-color);
}

.article-publication-date {
  font-size: 12px;
  padding-right: 12pt;
}

.article-publication-date, .tags-container {
  display: inline-block;
}

.tags-list {
  padding-left: 0;
  padding-bottom: 5pt;
  margin: 0;
}

.tag-item {
  font-size: 12px;
  text-decoration: underline;
  display: inline-block;
  position: relative;
  margin: 0 5pt 0 0;
}

.tag-highlight {
  background-color: #F7F71B;
}

.tag-item > a, .article-publication-date {
  color: #666;
}
</style>