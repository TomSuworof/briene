<template>
  <div class="editor-page-content">
    <form method="post">
      <div class="editor-wrapper">
        <div>
          <label for="title"></label>
          <input id="title" class="title" type="text" placeholder="Title" name="title" required/>
        </div>
        <div>
          <label for="editor"></label>
          <textarea id="editor" placeholder="Content here ..." name="content"></textarea>
        </div>
      </div>
      <div class="action-buttons">
        <input type="submit" name="action" value="Publish"/>
        <input type="submit" name="action" value="Save"/>
      </div>
    </form>
  </div>
</template>

<script>
//import Editor from '/static/js/lepture_editor';

export default {
  name: "ArticleEditor",
  created() {
  }
}
</script>

<style scoped>

</style>