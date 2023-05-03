<template>
  <div>
    <button
        style="border: none;background: transparent;padding:0;"
        @click="handlerClick"
    >
      <el-image
          src="/me-setting.png"
      />
    </button>
    <el-drawer
        :with-header="false"
        :show-close="false"
        :size="ClientVersionUtil.isMobile()?'45%':'25%'"
        v-model="openState"
        direction="ltr"
    >
      <div class="header">
        <el-avatar
            class="avatar"
            :src="'/me-setting.png'"
        />
        <div>
          <p
              v-if="!editMode"
              @click="editMode = true"
              style="text-align: center;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;"
          >
            {{ nickname ? nickname : 'anonymous' }}
          </p>
          <el-input
              v-else
              v-model="nickname"
              style="margin: 16px auto"
              @blur="changeName"
          />
        </div>
      </div>
      <el-button
          class="logout"
          type="info"
          round
          @click="logout"
      >
        LogOut
      </el-button>
    </el-drawer>
  </div>
</template>

<script>
import CuisineListItem from "@/components/CuisineListItem.vue";
import ClientVersionUtil from "@/utils/ClientVersionUtil";
import UserData from "../constants/UserData";
import UserService from "@/service/UserService";
import UserUtil from "@/utils/UserUtil";
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";

export default {
  name: "MeSetting",
  computed: {
    ClientVersionUtil() {
      return ClientVersionUtil
    }
  },
  components: {CuisineListItem},
  methods: {
    async handlerClick() {
      let apiFail = false;
      await UserData.freshUserData()
          .catch(err => {
            UserUtil.tokenExpired(err);
            this.openState = false;
            apiFail = true;
          });

      if (!apiFail) {
        this.nickname = UserData.nickname;
        this.openState = true;
      }
    },
    changeName() {
      UserService.changeName(this.nickname)
          .then(resp => {
            if (resp?.data) {
              this.$notify({
                type: 'success',
                message: "Change nickname successfully"
              });
              UserData.freshUserData();
              this.editMode = false;
            }
          })
          .catch(resp => {
            this.$notify({
              type: 'error',
              message: "Change nickname fail"
            });
            this.nickname = UserData.nickname;
          });
    },
    logout() {
      StorageUtil.remove(Constants.STORAGE_TOKEN);
      this.openState = false;
      this.$notify({
        type: 'success',
        message: 'Logout successfully'
      });
    }
  },
  data() {
    return {
      openState: false,
      nickname: null,
      editMode: false,
    }
  }
}
</script>

<style scoped>
.header {
  height: 30%;
}

.avatar {
  margin: 10% auto 0 auto;
  display: block;
  width: 4em;
  height: 4em;
}

.logout {
  position: absolute;
  bottom: 5%;
  width: 50%;
  left: 25%;
}
</style>