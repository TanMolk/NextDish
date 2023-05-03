<!--
 @Name FavoriteCuisine
 @Description page to show favorite cuisine
 @author Wei Tan
 @createDate 2023/04/28
-->
<template>
  <div class="wrapper">
    <button
        style="border: none;background: transparent;padding:0;"
        @click="handlerClick"
    >
      <el-image
          src="/favorite.png"
      />
    </button>
    <el-drawer
        :size="ClientVersionUtil.isMobile()?'85%':'30%'"
        v-model="openState"
        title="Favorite"
        direction="ltr"
    >
      <div>
        <ul
            style="padding: 0;margin-top: 0;"
        >
          <nut-swipe
              v-for="(item,index) in items"
              ref="swipes"
          >
            <li
                style="list-style: none;margin-top: 1px;margin-left:4px;"
            >
              <CuisineListItem
                  :info="item"
              />
            </li>
            <template #right>
              <nut-button shape="square" style="height:100%" type="danger" @click="removeFavorite(item,index)">Delete
              </nut-button>
            </template>
          </nut-swipe>
        </ul>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import CuisineListItem from "@/components/CuisineListItem.vue";
import ClientVersionUtil from "@/utils/ClientVersionUtil";
import FavoritesService from "@/service/FavoritesService";
import UserUtil from "@/utils/UserUtil";
import UserData from "@/constants/UserData";

export default {
  name: "FavoriteCuisine",
  computed: {
    ClientVersionUtil() {
      return ClientVersionUtil
    }
  },
  components: {CuisineListItem},
  methods: {
    async handlerClick() {
      this.openState = await this.getFavorites();
    }
    ,
    async getFavorites() {
      let resp = await FavoritesService.get()
          .catch(err => {
            UserUtil.tokenExpired(err);
          });
      if (resp) {
        this.items = resp.data;
        return true;
      }
      return false;
    },
    removeFavorite(item, index) {
      FavoritesService.remove(item.id).then(resp => {
        if (resp.data) {
          this.$notify({
            type: 'success',
            message: 'Delete successfully'
          });
          this.$refs.swipes[index].close();
          this.items.splice(index, 1);
          UserData.freshUserData();
        } else {
          this.$notify({
            type: 'warning',
            message: 'Delete failed'
          })
        }
      })
    }
  },
  data() {
    return {
      openState: false,
      items: []
    }
  }
}
</script>

<style scoped>
.wrapper:deep(.el-drawer__body) {
  padding: 0;
  overflow-x: hidden;
}
</style>