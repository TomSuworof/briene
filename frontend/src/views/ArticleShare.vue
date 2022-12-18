<template>
  <div class="article-root">
    <div class="article-content-wrapper">
      <div class="article-author">
        <router-link :to="'/authors/' + article.author">{{ article.author }}</router-link>
      </div>
      <div class="article-header">
        <div class="article-title">
          <h1 id="article-title">{{ article.title }}</h1>
        </div>
      </div>
      <div class="article-subheader">
        <div class="article-publication-date" v-if="article.publicationDate !== undefined">
          <time :datetime="htmlPublicationDate">{{ getFinePublicationDate(article.publicationDate) }}</time>
        </div>
        <div class="tags-container">
          <ul class="tags-list">
            <li class="tag-item" v-for="tag in article.tags">
              <a :href="'/tags/' + tag">{{ tag }}</a>
            </li>
          </ul>
        </div>
      </div>
      <div v-if="loadingArticle">
        <ShimmerBlock/>
      </div>
      <div v-show="!loadingArticle" id="article-content">
        <v-md-editor :model-value="article.content" mode="preview"></v-md-editor>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
import ArticlesService from "@/api/ArticlesService";
import VMdEditor from '@kangc/v-md-editor';
import ShimmerBlock from "@/components/ShimmerBlock";
import ArticleComponent from "@/components/ArticleComponent";

export default {
  name: "ArticleShare",
  components: {
    ShimmerBlock,
    VMdEditor,
    ArticleComponent
  },
  data() {
    return {
      loadingArticle: false,
      article: '',
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    htmlPublicationDate: function () {
      return this.article.publicationDate;
    }
  },
  methods: {
    getFinePublicationDate: function (publicationDate) {
      return moment(Date.parse(publicationDate)).format("DD.MM.YYYY HH:mm");
    },
    loadArticleContent: function (articleId) {
      this.loadingArticle = true;
      ArticlesService.getSharedArticle(articleId)
          .then(response => {
            this.article = response.data;
            this.loadingArticle = false;
            // this.article.content = xss.process(VMdEditor.vMdParser.themeConfig.markdownParser.render(this.article.content));
          })
          .catch(() => {
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
  },
  created() {
    let articleId = this.$route.params.articleId;
    this.loadArticleContent(articleId);
  },
}
</script>

<style scoped>

hr {
  max-width: unset !important;
}

@media screen and (max-width: 720px) {
  .article-root {
  }
}

@media screen and (min-width: 721px) {
  .article-root {
    max-width: 66rem;
    padding: 0 60pt 10pt 60pt;
  }
}

.article-title {
  color: var(--text-color);
  overflow-wrap: break-word;
}

#article-title {
  font-weight: bold;
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

.tag-item > a, .article-publication-date {
  color: #666;
}

</style>