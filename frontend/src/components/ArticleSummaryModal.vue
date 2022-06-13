<template>
  <div class="article-summary-modal">
    <div>
      <h3>Summary</h3>
      <p>Max {{ maxLength }} characters. This summary will be seen under title in article card</p>
    </div>
    <div class="article-summary-wrapper">
      <textarea v-model="summaryGot" :maxlength="maxLength"></textarea>
    </div>
    <div class="action-buttons">
      <div class="button-row">
        <div class="btn-container">
          <button class="button button-outline" id="cancel" @click="handleButton('Cancel')" type="button"
                  name="action"
                  value="Save">
            <span>Cancel</span>
          </button>
        </div>
        <div class="btn-container">
          <button class="button button-primary" id="done" @click="handleButton('Done')" type="button"
                  name="action"
                  value="Done">
            <span v-show="loading" class="spinner-border spinner-border-sm"></span>
            <span>Done</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {closeModal} from "jenesius-vue-modal";
import ArticlesService from "@/api/ArticlesService";

export default {
  setup: () => ({
    cancel: () => {
      closeModal()
          .catch(() => {
          })
    }
  }),
  name: "ArticleSummaryModal",
  props: {
    title: {required: true},
    content: {required: true},
    summary: {required: true},
    tags: {required: true},
    action: {required: true},
  },
  data() {
    return {
      maxLength: 500,
      showTagInput: false,
      loading: false,

      summaryGot: '',

      suggestedTags: []
    };
  },
  methods: {
    contentNotEmpty: function () {
      return this.content !== '';
    },
    titleNotEmpty: function () {
      return this.title !== '';
    },
    formIsValid: function () {
      return this.contentNotEmpty() && this.titleNotEmpty();
    },
    showWarningEmpty: function () {
      alert('Fields can not be empty');
    },
    showWarningArticleExists: function () {
      alert('Such article already exists. Try to make different title or remove old one');
    },
    handleButton: function (buttonAction) {
      if (buttonAction === 'Cancel') {
        this.closeThisModal();
      } else if (buttonAction === 'Done') {
        if (this.formIsValid()) {
          if (this.summary !== null && this.summary.length > this.maxLength) {
            alert(`Summary should be less than ${this.maxLength} characters`);
          } else {
            this.uploadArticle();
          }
        } else {
          this.showWarningEmpty();
        }
      }
    },
    uploadArticle: function () {
      this.loading = true;
      ArticlesService.uploadArticle(this.title, this.content, this.summaryGot, this.action, this.tags)
          .then(() => {
            this.loading = false;
            this.closeThisModal(); // remember to close. otherwise, scrollbar will disappear
            if (this.action === 'publish') {
              this.$router.push('/');
            } else if (this.action === 'save') {
              alert('Article was saved');
            }
          })
          .catch(err => {
            console.log(err);
            this.showWarningArticleExists();
          });
    },
    closeThisModal: function () {
      this.cancel();
    }
  },
  created() {
    this.summaryGot = this.summary;
    if (this.summaryGot === undefined || this.summaryGot === '') {
      this.summaryGot = this.content.substring(0, this.maxLength);
    }
  }
}
</script>

<style scoped>
.article-summary-modal {
  position: relative;
  background: white;
  width: 300pt;

  box-shadow: rgba(0, 0, 0, 0.05) 0 1px 10px 0, rgba(0, 0, 0, 0.05) 0 0 0 1px;
  border-radius: 9px;
  margin: 0 0 10pt;
  padding: 10pt;
}

textarea {
  padding: 5pt;
  border-radius: 9px;
  width: 100%;
  height: 300pt;
  resize: none;
}

.action-buttons {
  padding: 10pt 0 0 0;
  display: flex;
  justify-content: flex-end;
  flex-direction: column;
}

.button-row {
  display: flex;
  justify-content: flex-end;
  flex-direction: row;
}

.btn-container {
  margin: 0 5pt 0 0;
}

input {
  border: none;
}
</style>