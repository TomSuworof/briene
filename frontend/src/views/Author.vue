<template>
  <div class="author-root" v-if="author">
    <div class="author-container">
      <div class="avatar-wrapper" v-if="!avatarString">
        <img src="@/assets/images/avatar-empty-transparent.png" alt="Avatar"/>
      </div>
      <div class="avatar-wrapper" v-if="avatarString">
        <img :src="avatarString" alt="Avatar"/>
      </div>
      <div class="author-data">
        <div class="author-header">
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
        <div class="author-subheader">
          <div class="author-article-count">
            <p>{{ getArticlesMessage(author.articles.totalCount) }}</p>
          </div>
          <div class="author-follower-count">
            <p>{{ getFollowersMessage(author.followersCounter) }}</p>
          </div>
        </div>
        <div class="author-bio">
          <p>{{ author.bio }}</p>
        </div>
      </div>
    </div>
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
      if (count === 1) {
        return `${count} article`
      } else {
        return `${count} articles`
      }
    },
    getFollowersMessage: function (count) {
      if (count === 1) {
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
          });
    },
    loadAuthorData: function (authorName, limit, offset) {
      AuthorsService.getAuthorData(authorName, limit, offset)
          .then(response => {
            this.author = response.data;
            document.title = this.author.username;
            let articles = this.author.articles.entities;
            this.articles = this.articles.concat(articles);
            this.hasAfter = this.author.articles.hasAfter;
          })
          .catch(() => {
            this.$router.replace('/error'); // redirecting to '/error'
          });
    },
    loadFollowStatus: function (authorName) {
      AuthorsService.getFollowingStatus(authorName)
          .then(response => {
            this.isFollowing = response.data;
          })
          .catch(() => {
            this.isFollowing = false;
          });
    },
    subscribe: function () {
      this.loadingSubscriptionChange = true;
      AuthorsService.subscribe(this.author.username)
          .then(response => {
            this.loadingSubscriptionChange = false;
            this.loadFollowStatus(this.author.username);
          })
          .catch(() => {
            this.loadingSubscriptionChange = false;
          });
    },
    unsubscribe: function () {
      this.loadingSubscriptionChange = true;
      AuthorsService.unsubscribe(this.author.username)
          .then(response => {
            this.loadingSubscriptionChange = false;
            this.loadFollowStatus(this.author.username);
          })
          .catch(() => {
            this.loadingSubscriptionChange = false;
          });
    }
  },
  created() {
    let authorName = this.$route.params.authorName;
    this.loadingArticles = true;
    this.loadFollowStatus(authorName);
    this.getAvatar(authorName);
    this.loadAuthorData(authorName, this.limit, this.offset);
    this.loadingArticles = false;
  },
  beforeUnmount() {
    document.title = 'Briene';
  }
}
</script>

<style scoped>
.author-root {
  max-width: 700pt;
}

@media screen and (max-width: 720px) {
  .avatar-wrapper > img {
    object-fit: cover;
    border-radius: 50%;
    width: 60pt;
    height: 60pt;
  }
}


@media screen and (min-width: 720px) {
  .avatar-wrapper > img {
    object-fit: cover;
    border-radius: 50%;
    width: 120pt;
    height: 120pt;
  }
}

.author-container {
  display: grid;
  grid-template-columns: 1fr 4fr;
  margin-bottom: 25pt;
}

.avatar-wrapper {
  margin-right: 5pt;
}

.author-header {
  display: grid;
  grid-template-columns: auto auto;
  justify-content: space-between;
}

.author-username {
  color: var(--text-color);
}

.follow-button {
  float: right;
}

.author-subheader {
  display: flex;
  gap: 10pt;
}

.author-article-count, .author-follower-count {
  font-size: 12px;
  color: #666;
}

.author-bio {
  color: var(--text-color);
  white-space: pre-line;
}
</style>