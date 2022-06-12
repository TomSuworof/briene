<template>
  <div class="shimmer-container" :style="blockStyle">
    <b><pre>{{ message }}</pre></b>
  </div>
</template>

<script>
export default {
  name: "ShimmerBlock",
  props: ['height', 'width'],
  data() {
    return {
      message: 'Loading...'
    }
  },
  computed: {
    blockStyle() {
      return {
        height: this.height || '100pt',
        width: this.width || '900pt',
      }
    }
  },
  methods: {
    sleep: function (ms) {
      return new Promise((resolve) => {
        setTimeout(resolve, ms);
      });
    }
  },
  async created() {
    for (; ;) {
      await this.sleep(500);
      this.message = 'Loading.. ';
      await this.sleep(500);
      this.message = 'Loading ..';
      await this.sleep(500);
      this.message = 'Loading  .';
    }
  }
}
</script>

<style scoped>
.shimmer-container {
  display: table-cell;
  vertical-align: middle;
  text-align: center;
  border-radius: 9px;
  background: #FAFAF7;
}
</style>