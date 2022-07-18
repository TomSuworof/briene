<template>
  <div class="author-page-content" v-if="author">
    <div class="header">
      <div class="avatar-wrapper" v-if="!avatarString">
        <img src="/avatar-empty.webp" alt="Avatar"/>
      </div>
      <div class="avatar-wrapper" v-if="avatarString">
        <img :src="avatarString" alt="Avatar"/>
      </div>
      <div class="author-data">
        <div>
          <div class="author-username">
            <h1><b>{{ author.username }}</b></h1>
          </div>
          <div class="follow-button">
            <button class="button button-primary" v-if="!isFollowing" @click="subscribe">
              <span v-show="loadingSubscriptionChange" class="spinner-border spinner-border-sm"></span>
              <span>Follow</span>
            </button>
            <button class="button button-outline" v-if="isFollowing" @click="unsubscribe">
              <span v-show="loadingSubscriptionChange" class="spinner-border spinner-border-sm"></span>
              <span>Unfollow</span>
            </button>
          </div>
        </div>
        <div class="author-bio">
          <p>{{ author.bio }}</p>
        </div>
        <div class="author-article-count">
          <p>{{ getArticlesMessage(author.articles.totalCount) }}</p>
        </div>
        <div class="author-follower-count">
          <p>{{ getFollowersMessage(author.followersCounter) }}</p>
        </div>
      </div>
    </div>
    <hr align="left">
    <div v-if="loadingArticles">
      <ShimmerBlock/>
    </div>
    <div v-if="!loadingArticles" class="articles">
      <div v-if="articles.length > 0">
        <article-component
            v-for="article in articles"
            v-bind:key="article.id"
            v-bind:article="article"
        ></article-component>
      </div>
      <div v-else-if="articles.length === 0">
        <p>No articles</p>
      </div>
    </div>
    <div class="load-more-button" v-if="hasAfter">
      <button class="button button-primary" @click="loadMoreArticles" :disabled="!hasAfter" title="Load more articles">
        <span v-show="loadingArticles" class="spinner-border spinner-border-sm"></span>
        <span>Load more</span>
      </button>
    </div>
  </div>
</template>

<script>
import AuthorsService from "@/api/AuthorsService";
import ArticleComponent from "@/components/ArticleComponent";
import ShimmerBlock from "@/components/ShimmerBlock";
import AvatarsService from "@/api/AvatarsService";

export default {
  name: "Author",
  components: {
    ShimmerBlock,
    ArticleComponent,
  },
  data() {
    return {
      avatarString: undefined,

      author: undefined,
      loadingArticles: false,
      articles: [],

      hasAfter: true,

      limit: 5,
      offset: 0,

      totalCount: 0,

      isFollowing: false,
      loadingSubscriptionChange: false,
    }
  },
  methods: {
    getArticlesMessage: function (count) {
      if (count % 10 === 1) {
        return `${count} article`
      } else {
        return `${count} articles`
      }
    },
    getFollowersMessage: function (count) {
      if (count % 10 === 1) {
        return `${count} follower`
      } else {
        return `${count} followers`
      }
    },
    loadMoreArticles: function () {
      this.offset += this.limit;
      this.loadAuthorData(this.author.username, this.limit, this.offset);
    },
    getAvatar: function (authorName) {
      AvatarsService.getUserAvatar(authorName)
          .then(response => {
            this.avatarString = response.data;
          })
          .catch(err => {
            console.log(err);
          });
    },
    loadAuthorData: function (authorName, limit, offset) {
      AuthorsService.getAuthorData(authorName, limit, offset)
          .then(response => {
            this.author = response.data;
            this.loadFollowStatus();
            document.title = this.author.username;
            let articles = this.author.articles.entities.sort((article1, article2) => {
              if (article1.publicationDate < article2.publicationDate) {
                return -1;
              }
              if (article1.publicationDate > article2.publicationDate) {
                return 1;
              }
              return 0;
            }).reverse(); // newer first;
            this.articles = this.articles.concat(articles);
            this.hasAfter = this.author.articles.hasAfter;
          })
          .catch(err => {
            console.log(err);
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
    loadFollowStatus: function () {
      AuthorsService.isFollowing(this.author.username)
          .then(response => {
            this.isFollowing = response.data;
          })
          .catch(err => {
            console.log(err);
            this.isFollowing = false;
          });
    },
    subscribe: function () {
      this.loadingSubscriptionChange = true;
      AuthorsService.subscribe(this.author.username)
          .then(response => {
            this.loadingSubscriptionChange = false;
            this.loadFollowStatus();
          })
          .catch(err => {
            this.loadingSubscriptionChange = false;
            console.log(err);
          });
    },
    unsubscribe: function () {
      this.loadingSubscriptionChange = true;
      AuthorsService.unsubscribe(this.author.username)
          .then(response => {
            this.loadingSubscriptionChange = false;
            this.loadFollowStatus();
          })
          .catch(err => {
            this.loadingSubscriptionChange = false;
            console.log(err);
          });
    }
  },
  created() {
    this.loadingArticles = true;
    this.getAvatar(this.$route.params.authorName);
    this.loadAuthorData(this.$route.params.authorName, this.limit, this.offset);
    this.loadingArticles = false;
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>
.author-page-content {
  max-width: 700pt;
}

.avatar-wrapper > img {
  object-fit: cover;
  border-radius: 50%;
  width: 120pt;
  height: 120pt;
}

.header {
  display: grid;
  grid-template-columns: 1fr 4fr;
}

.avatar-wrapper {
  margin-right: 5pt;
}

.author-username, .follow-button {
  display: inline-block;
}

.follow-button {
  float: right;
}

.author-bio {
  white-space: pre-line;
}

.author-article-count, .author-follower-count {
  display: inline-block;
  font-size: 12px;
  margin-right: 10pt;
}
</style>