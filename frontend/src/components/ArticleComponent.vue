<template>
  <div class="article-container" @click.self="$router.push('/articles/' + article.url)">
    <div v-if="actions !== undefined">
      <div class="article-util-container">
        <div class="article-state" v-show="state !== undefined">
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
    <div class="article-author">
      <router-link v-bind:to="'/authors/' + article.author">{{ article.author }}</router-link>
    </div>
    <div class="article-title-container">
      <h3 class="article-title">
        <a :href="'/articles/' + article.url">{{ article.title }}</a>
      </h3>
    </div>
    <div class="article-summary">
      <p>{{ article.summary }}</p>
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
.article-title {
  font-weight: bold;
}

.article-title, .article-summary {
  overflow-wrap: break-word;
}

.article-summary > p {
  max-width: 700pt;
  margin-bottom: 0.6rem;
}

.article-container {
  min-width: 300pt;
  max-width: 700pt;
  position: relative;
  border-radius: 9px;
  margin-bottom: 30pt;
  border: solid 1px transparent;
}

.article-container:active {
  border: solid 1px black;
}

.article-author {
  font-size: 12px;
}

.article-util-container {
  position: absolute;
  right: 5pt;
  top: 5pt;
}

.article-state, .article-action-item {
  display: inline-block;
  padding-left: 5pt;
}

.article-publication-date {
  font-size: 12px;
  padding-right: 7pt;
}

.article-publication-date, .tags-container {
  display: inline-block;
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

.tag-highlight {
  background-color: #F7F71B;
}

.tag-item > a, .article-publication-date {
  color: #666;
}
</style>