<template>
  <div class="article-summary-modal">
    <div>
      <h3>Summary</h3>
      <p>Enter summary for article (max {{ maxLength }} characters):</p>
    </div>
    <div class="article-summary-wrapper">
      <textarea v-model="summary"></textarea>
    </div>
    <div class="tags-container">
      <h3>Tags</h3>
      <ul class="tags-list">
        <li class="tag-item" v-for="tag in tags">
          <div class="tag-item-name">{{ tag }}</div>
          <div class="tag-item-close-button">
            <button @click="removeTag(tag)" class="fa fa-close"></button>
          </div>
        </li>
        <li class="tag-item" v-if="showTagInput">
          <form @submit.prevent="addTag" autocomplete="off">
            <input name="tagValue" class="add-tag-input" list="tagsList"/>
            <datalist id="tagsList">
              <option v-for="suggestedTag in suggestedTags">{{ suggestedTag }}</option>
            </datalist>
          </form>
        </li>
        <li class="tag-item" v-if="!showTagInput">
          <button @click="openAddTagInput()" class="add-tag-button">
            <img loading="eager" src="https://img.icons8.com/material/24/000000/plus-math--v2.png" alt="Add tag"/>
          </button>
        </li>
      </ul>
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
import TagService from "@/api/TagService";

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
      maxLength: 255,
      showTagInput: false,
      loading: false,

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
      alert('Such article already exists. Try to make different or remove old one');
    },
    handleButton: function (buttonAction) {
      if (buttonAction === 'Cancel') {
        this.closeThisModal();
      } else if (buttonAction === 'Done') {
        if (this.formIsValid()) {
          let maxLength = 255
          if (this.summary !== null && this.summary.length > maxLength) {
            alert(`Summary should be less than ${maxLength} characters`);
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
      ArticlesService.uploadArticle(this.title, this.content, this.summary, this.action, this.tags)
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
    },
    openAddTagInput: function () {
      this.showTagInput = true;
    },
    addTag: function (submitEvent) {
      let newTagName = submitEvent.target.elements.tagValue.value;
      this.suggestedTags.splice(this.suggestedTags.indexOf(newTagName), 1);
      this.tags.push(newTagName);
      this.showTagInput = false;
    },
    removeTag: function (tagName) {
      this.tags.splice(this.tags.indexOf(tagName), 1);
    },
  },
  mounted() {
    TagService.getTagsWithExclusion(this.tags)
        .then(response => this.suggestedTags = response.data)
        .catch(err => console.log(err));
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
  height: 100pt;
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

.tags-container {
  padding: 10pt 0 0 0;
}

.tags-list {
  padding-left: 0;
}

.tag-item {
  display: inline-block;
  margin: 0 5pt 5pt 0;
  padding: 5pt;
  border-radius: 22px;
  border: 1px solid;
  position: relative;
}

.add-tag-button {
  background: none;
  border: none;
}

input {
  border: none;
}

.tag-item-name, .tag-item-close-button {
  display: inline-block;
  margin: 0 3pt 0 5pt;
}

.fa-close {
  text-align: center;
  height: 1.7em;
  width: 1.7em;
  border-radius: 2em !important;
  background: none;
  border: 1px solid #AAA;
  color: #AAA;
}
</style>