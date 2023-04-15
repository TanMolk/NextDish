<!--
 @Name CuisineDetail
 @Description The detail window for restaurant
 @author Wei Tan
 @createDate 2023/03/13
-->
<template>
  <div>
    <!--      Pane Tabs    -->
    <nut-tabs
        v-model="selectedTab"
        :background="'#9faab8'"
    >
      <template v-slot:titles>
        <div
            :style="{
              backgroundColor:selectedTab === item ? 'rgba(55,66,250,0.2)' : '',
              color: selectedTab=== item ? '#3742FA' : ''
            }"
            class="nut-tabs__titles-item tab-class"
            v-for="item in items"
            :key="item"
            @click="selectedTab = item"
        >
          {{ item }}
        </div>
      </template>

      <!--      Panes    -->
      <nut-tab-pane
          class="detail-pane"
          :pane-key="items[0]"
      >
        <template v-slot>
          <div style="text-align: left;font-size: 20px;">
            Distance: {{ directionDetail?.routes[0].legs[0].distance.value }}m
            <br>
            <br>
            Time: {{ directionDetail?.routes[0].legs[0].duration.text }}
            <br>
            <br>
            <button
                style="font-size: 16px;height: 2em;width: 6em;"
                @click="directionButtonClick"
            >Start Go
            </button>
          </div>
        </template>
      </nut-tab-pane>

      <nut-tab-pane
          class="detail-pane"
          :pane-key="items[1]"
      >
        <template v-slot>
          <p
              class="info-title"
          >Menu and environment</p>
          <div
              v-if="detail?.photos"
              class="highlight-container"
          >
            <el-image
                v-for="(image,index) in images"
                class="highlight-image"
                :src="image"
                :loading="'lazy'"
                :initial-index="index"
                :preview-src-list="images"
                :preview-teleported="true"
            />
          </div>
          <p v-else>No information</p>
        </template>
      </nut-tab-pane>

      <nut-tab-pane
          class="detail-pane"
          :pane-key="items[2]"
      >
        <template v-slot>
          <div
              v-if="detail"
          >
            <div style="text-align: left;margin-bottom: 1em;">
                  <span
                      class="info-title title-column"
                  >Average Cost:</span>
              <span>{{
                  detail.price_level
                      ? "£" + (Number(detail.price_level) * 10) + "~" + (Number(detail.price_level) * 10 + 10)
                      : "No information"
                }}
                  </span>
            </div>
            <div>
              <p class="info-title">Open Time</p>
              <table
                  class="info-table"
                  v-if="detail.opening_hours"
              >
                <tr
                    v-for="item in detail.opening_hours.weekday_text"
                >
                  <td class="title-column">{{ item.split(": ")[0] }}</td>
                  <td>{{ item.split(": ")[1] }}</td>
                </tr>
              </table>
              <p v-else>No information</p>
            </div>
          </div>
        </template>
      </nut-tab-pane>

      <nut-tab-pane
          class="detail-pane"
          :pane-key="items[3]"
      >
        <template v-slot>
          <p class="info-title">Reviews</p>
          <div
              style="text-align: left;"
              v-if="detail"
          >
            <div
                style="border-bottom: 1px solid;margin-bottom: 1.5em"
                v-for="review in detail.reviews"
            >
              <p>{{ review.author_name }}</p>
              <p style="word-break: break-word;">{{ review.text }}</p>
            </div>
          </div>
        </template>
      </nut-tab-pane>
    </nut-tabs>
  </div>
</template>

<script>

import PlaceService from "@/service/PlaceService";
import StorageUtil from "@/utils/StorageUtil";
import Constants from "@/constants/Constants";

export default {
  name: "CuisineDetail",
  props: {
    placeId: String,
    mapInstance: Object,
  },
  data() {
    return {
      items: ['Location', 'Highlight', 'Details', 'Reviews'],
      selectedTab: "Location",
      detail: null,
      directionDetail: null,
      /** service of direction */
      directionService: null,
      images: [],
      imageViewShowState: false
    }
  },
  watch: {
    placeId() {
      this.selectedTab = this.items[0];
      this.getDetail();
    }
  },
  methods: {
    async getDetail() {
      if (this.placeId) {
        this.images = [];
        let resp = await PlaceService.getPlaceDetail(this.placeId);
        this.detail = resp.data;

        let photos = this.detail.photos;
        if (photos) {
          for (const photo of photos) {
            PlaceService.getPlaceImage(photo)
                .then(resp => {
                  if (resp) {
                    const blob = new window.Blob([resp.data], {type: 'image/jpeg'})
                    this.images.push(URL.createObjectURL(blob));
                  }
                })
                .catch(err => {
                  console.log(err)
                });
          }
        }

        await this.getDirection();
      }
    },
    /**
     * Get information for direction, best used in the getDetail
     * @returns {Promise<void>}
     */
    async getDirection() {
      if (this.placeId) {
        //if it doesn't exist, initialize
        if (!this.directionService) {
          this.directionService = new this.mapInstance.api.DirectionsService();
        }

        //request direction
        let directionsService = this.directionService;
        let request = {
          origin: {
            location: {
              lat: Number(StorageUtil.get(Constants.STORAGE_USER_LOCATION_LATITUDE,)),
              lng: Number(StorageUtil.get(Constants.STORAGE_USER_LOCATION_LONGITUDE,))
            }
          },
          destination: {
            location: this.detail.geometry.location
          },
          travelMode: 'WALKING'
        };
        directionsService.route(request, (result, status) => {
          if (status === 'OK') {
            this.directionDetail = result;
          }
        });
      }
    },
    directionButtonClick() {
      this.$emit("direction-request", this.directionDetail);
    }
  }
}
</script>
<style scoped>

.tab-class {
  background-color: white;
  border-radius: 0.5em;

  margin: 5px 5px;
}

.detail-pane {
  overflow: auto;
  height: calc(var(--doc-height) * 0.55);
}

.info-title {
  text-align: left;
  margin: 0 0 1em 0;
  font-weight: 700;
}

.info-table {
  margin: 10px auto;
  padding: 0;
}

.title-column {
  padding-right: 16px;
}

.highlight-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
}

.highlight-image {
  width: 10em;
  height: 10em;
  margin-bottom: 1.5em;

  border-radius: 0.5em;
}

</style>