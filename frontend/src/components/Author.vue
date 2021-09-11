<template>
  <div>
    <div class="author-page-content" v-if="author">
      <div class="header row">
        <div>
          <h1>{{ author.username }}</h1>
        </div>
      </div>
      <div class="author-bio">
        <p>{{ author.bio }}</p>
      </div>
      <div>
        <div class="article-container" v-for="article in author.articles" v-bind:key="article.id">
          <div class="article-about row">
            <div class="article-title-container">
              <h3 class="article-title">
                <a v-bind:href="'/articles/' + article.id">{{ article.title }}</a>
              </h3>
            </div>
            <div class="article-publication-date">
              <p>{{ getFinePublicationDate(article) }}</p>
            </div>
          </div>
          <div class="article-summary">
            <p>{{ article.summary }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthorsService from "@/services/AuthorsService";
import moment from "moment";

export default {
  name: "Author",
  data() {
    return {
      author: undefined,
    }
  },
  methods: {
    getFinePublicationDate: function(article) {
      return moment(Date.parse(article.publicationDate)).format("DD.MM.YYYY HH:mm")
    },
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

.author-bio {
  white-space: pre-line;
  padding: 0 0 5pt;
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

.article-publication-date {
  color: #999;
}

.article-title, .article-summary, .author-bio {
  overflow-wrap: break-word;
}
</style>