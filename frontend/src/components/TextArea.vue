<template>
  <div>
    <el-dialog
        v-model="openState"
        width="90%"
        fullscreen
        align-center
        @closed="this.$emit('close-textArea');"
    >
      <template v-slot:header>
        <p style="font-size: 1.5em;text-align: center;">Say something</p>
      </template>
      <div class="input-area">
        <el-input
            v-model="text"
            :rows="2"
            type="textarea"
            :placeholder="'Please input your ' + model"
        />
      </div>
      <el-button
          :loading="buttonLoading"
          class="commit"
          @click="submitFeedBack"
      >
        Commit
      </el-button>
    </el-dialog>
  </div>
</template>

<script>
import ReviewService from "@/service/ReviewService";
import FeedbackService from "@/service/FeedbackService";
import UserData from "@/constants/UserData";
import UserUtil from "@/utils/UserUtil";

export default {
  name: "TextArea",
  props: {
    showState: Boolean,
    model: String,
    placeId: String,
  },
  watch: {
    showState(val) {
      this.openState = val
    },
    model() {
      this.text = "";
    }
  },
  methods: {
    submitFeedBack() {
      if (!this.text.trim()) {
        this.$notify({
          type: "warning",
          message: "Content can't be blank"
        });
        return;
      }

      this.buttonLoading = true;
      switch (this.model) {
        case "review":
          ReviewService.add(this.placeId, this.text)
              .then(async resp => {
                if (resp.data) {
                  await UserData.freshUserData().catch(err => {
                    UserUtil.tokenExpired(err);
                  });

                  this.$notify({
                    type: "success",
                    message: "Add review successfully"
                  });

                  this.$emit("review-add-success");
                }
                this.text = "";
                this.buttonLoading = false;
                this.openState = false;
              })
              .catch(err => {
                console.log(err);
                this.$notify({
                  type: "error",
                  message: "Network error"
                });
                this.buttonLoading = false;
              });
          return;
        case "feedback":
          FeedbackService.add(this.text)
              .then(resp => {
                if (resp.data) {
                  this.$notify({
                    type: "success",
                    message: "Add feedback successfully"
                  });
                }
                this.text = "";
                this.buttonLoading = false;
                this.openState = false;
              })
              .catch(err => {
                console.log(err)
                this.$notify({
                  type: "error",
                  message: "Network error"
                });
                this.buttonLoading = false;
              });
          return;
      }
    }
  },
  data() {
    return {
      openState: false,
      text: "",
      buttonLoading: false,
    }
  }
}
</script>

<style scoped>

.input-area:deep(.el-textarea__inner) {
  height: 60vh;
  resize: none;
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