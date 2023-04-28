<template>
  <p
      style="color:black;"
      @click="showFeedBack"
      class="right-bottom-button"
  >
    feedback
  </p>

  <el-dialog
      v-model="openState"
      width="90%"
      fullscreen
      align-center
  >
    <template v-slot:header>
      <p style="font-size: 1.5em;text-align: center;">Say something</p>
    </template>
    <div class="input-area">
      <el-input
          v-model="feedback"
          :rows="2"
          type="textarea"
          placeholder="Please input"
      />
    </div>
    <el-button
        class="commit"
        @click="submitFeedBack"
    >
      Commit
    </el-button>
  </el-dialog>
</template>

<script>
import MsgBoxUtil from "@/utils/MsgBoxUtil";

export default {
  name: "Feedback",
  methods: {
    showFeedBack() {
      this.openState = true
    },
    submitFeedBack() {
      MsgBoxUtil.alert(
          "Feedback",
          "Thanks for your feedback!",
          "OK",
          () => {
            this.openState = false;
          }
      );
    }
  },
  data() {
    return {
      openState: false,
      feedback: ""
    }
  }
}
</script>

<style scoped>

.input-area /deep/ .el-textarea__inner {
  height: 60vh;
}


.commit {
  background-color: #0050F8;
  color: white;
  border-radius: 100px;
  width: 10em;
  margin-top: 4em;
  margin-left: calc(50% - 5em);
}
</style>