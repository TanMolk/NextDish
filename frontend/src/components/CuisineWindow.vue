<!--
 @Name CuisineWindow
 @Description The list/detail window
 @author Wei Tan
 @createDate 2023/03/12
-->
<template>
  <div
      ref="windowWrapper"
      class="li-wrapper"
      :class="guidanceMode ? 'v-tour__target--highlighted' : ''"
      :style="{overflow: listShowState?'auto':'hidden'}"
  >
    <div
        :style="{marginTop:listShowState ? '' : '1em'}"
        class="header"
    >
      <span
          :style="{
                    fontSize: listShowState ? '2em' : '1.5em',
                    margin: listShowState ? '20px 0': '-10px 0 0 0',
          }"
          class="title">{{ title }}</span>
      <button
          v-show="supportShare && placeIdForDetail"
          class="share"
          @click="clickShare"
      >
        <el-image
            style="width: 16px;height: 16px;"
            src="/share.png"
        />
      </button>
      <button
          class="exit"
          @click="clickX"
      >
        <el-image
            style="width: 16px;height: 16px;"
            src="/x.png"
        />
      </button>
    </div>
    <div>
      <ul
          style="padding: 0;margin-top: 0;"
          v-loading="listLoadingState"
          :infinite-scroll-immediate="false"
          :infinite-scroll-delay="1000"
          v-infinite-scroll="loadMoreData"
          :infinite-scroll-disabled="listNoMoreState && listShowState"
          v-show="listShowState"
      >
        <li
            style="list-style: none;margin-top: 1px;"
            v-for="item in items"
        >
          <CuisineListItem
              :info="item"
              @click="changeInfoWindow('detail',item)"
          />
        </li>
        <li
            style="margin-top: 10px"
            v-show="listNoMoreState"
        >
          <span style="font-weight: bolder;">No more</span>
        </li>
      </ul>
      <CuisineDetail
          ref="cuisineDetail"
          :map-instance="mapInstance"
          :place-id="placeIdForDetail"
          :share-model="false"
          class="cuisine-detail"
          v-show="!listShowState"
          @direction-request="this.$emit('direction-request',$event);"
          @review-request="this.$emit('review-request')"
      />
    </div>
  </div>
</template>

<script>
import CuisineListItem from "@/components/CuisineListItem.vue";
import CuisineDetail from "@/components/CuisineDetail.vue";
import Constants from "@/constants/Constants";

export default {
  name: "CuisineWindow",
  components: {CuisineDetail, CuisineListItem},
  props: {
    //title of the window
    title: String,
    //list loading state
    listLoadingState: Boolean,
    listNoMoreState: Boolean,
    //all data of list
    items: Array,
    //data of detail
    placeId: String,
    mapInstance: Object,
    guidanceMode: Boolean
  },
  watch: {
    //if prop placeId change, update to placeIdForDetail
    placeId(newVar) {
      this.placeIdForDetail = newVar;
    },
    placeIdForDetail(newVar) {
      //send update event to outside
      this.$emit("place-id-change", newVar)
    },
  },
  data() {
    return {
      /**
       * true: show list, false: show detail
       */
      listShowState: true,
      /**
       * store placeId for change
       */
      placeIdForDetail: null,
      /**
       * the position of scrollbar
       */
      scrollTop: null,
      supportShare: Constants.SUPPORT_SHARE
    }
  },
  methods: {
    /**
     * Event when the X button get clicked
     */
    clickX() {
      //if it is showing detail, change to show list
      this.$emit("click-X", this.listShowState)

      if (!this.listShowState) {
        this.listShowState = true;

        //set to original position
        this.$nextTick(() => {
          this.$refs.windowWrapper.scrollTop = this.scrollTop;
        })
      }
    },
    /**
     * Change showing window
     * @param type detail or list
     * @param item data of showing detail
     */
    changeInfoWindow(type, item) {
      //record scrollbar position
      this.scrollTop = this.$refs.windowWrapper.scrollTop;

      if (type === "detail") {
        this.$nextTick(() => {
          this.$refs.windowWrapper.scrollTop = 0;
          this.listShowState = false;
          this.placeIdForDetail = item.place_id;
          this.$emit("detail-change", item);
        })
      } else if (type === "list") {
        this.listShowState = true;

        //set to original position
        if (item) {
          this.$nextTick(() => {
            this.$refs.windowWrapper.scrollTop = this.scrollTop;
          })
        }
      }
    },
    loadMoreData() {
      this.$emit("load-more-data");
    },
    clickShare() {
      let detail = this.$refs.cuisineDetail.detail;
      const share = {
        title: detail.name,
        text: 'I found a really nice restaurant, come with me!',
        url: '/share?placeId=' + this.placeIdForDetail,
      };

      navigator.share(share);
    },
    freshReviews(){
      this.$refs.cuisineDetail.getCustomerReviews();
    }
  }
}
</script>

<style scoped>
.cuisine-detail {
  width: var(--restaurants-list-detail-window-width);
}

.li-wrapper {
  position: absolute;
  height: var(--restaurants-list-detail-window-height);
  width: var(--restaurants-list-detail-window-width);
  left: var(--restaurants-list-detail-window-left);
  bottom: 0;

  background-color: #9faab8;
  border-radius: 2em 2em 0 0;

  text-align: center;
}

.header {
  line-height: calc(var(--doc-height) * 0.05);
}

.title {
  display: inline-block;
  max-width: 18ch;
  font-weight: 600;
}

.exit {
  position: absolute;
  right: 5%;
  top: 2.5%;

  border: none;

  background-color: transparent;

  font-size: 1.1em;
}

.share {
  position: absolute;
  left: 5%;
  top: 2.5%;

  border: none;

  background-color: transparent;

  font-size: 1.1em;
}
</style>