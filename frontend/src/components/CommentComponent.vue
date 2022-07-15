<template>
  <div class="comment-container">
    <div class="comment-author">
      <router-link v-bind:to="'/authors/' + comment.authorUsername">{{ comment.authorUsername }}</router-link>
    </div>
    <div class="comment-message">
      <p>{{comment.message}}</p>
    </div>
    <div class="comment-publication-date">
      <time :datetime="comment.publicationDate">{{ getFinePublicationDate(comment.publicationDate) }}</time>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "CommentComponent",
  props: ["comment"],
  methods: {
    getFinePublicationDate: function (publicationDate) {
      return moment(Date.parse(publicationDate)).format("DD.MM.YYYY HH:mm");
    },
  }
}
</script>

<style scoped>

.comment-container {
  margin-bottom: 20pt;
  max-width: 100%;
  overflow-wrap: break-word;
}

.comment-author {
  font-size: 12px;
  margin-bottom: 0.25rem;
}

.comment-message > p {
  margin-bottom: 0.5rem;
}

.comment-publication-date {
  font-size: 12px;
}

</style>