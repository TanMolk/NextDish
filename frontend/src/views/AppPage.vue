<!--
 @Name AppPage
 @Description The app page
 @author Ashton & Wei
 @createDate 2023/03/12
-->
<template>
  <GoogleMap
      @click="OnMapClick"
      ref="mapInstance"
      class="map"
      :map-id="Constants.GOOGLE_MAP_ID"
      :api-key="Constants.GOOGLE_MAP_API_KEY"
      :zoom="13"
      :min-zoom="13"
      :restriction="restriction"
      :gesture-handling="'greedy'"
      :disable-default-ui="true"
  >
    <!----------------------Control-------------------->
    <CustomControl
        position="TOP_CENTER"
    >
      <ElButton
          v-show="directionModel"
          @click="
          directionModel = false;
          setMapCenter(userLocation);
          mapInstance.map.setZoom(14);
          this.listShowState = true;
          "
          style="margin-top: 2em;font-weight: bolder;color: black">
        Exit Direction
      </ElButton>
    </CustomControl>
    <!----------------------Control End---------------->


    <!----------------------Markers-------------------->

    <CustomMarker
        :options="userMarkerOption"
    >
      <img style="opacity: 0.7;width: 2em;height: 2em" src="@/assets/me.png" alt="me"/>
    </CustomMarker>
    <Circle
        @click="OnMapClick"
        :options="userCircleOption"
    />
    <Marker
        v-for="item in restaurantData.showingItems"
        :options="{
          icon: {url:'/marker.png',scaledSize: {width:20,height:27}},
          label: item.name.length > 10 ? item.name.substring(0,10) + '...' : item.name,
          position: item.geometry.location,
          title: item.rating + '',
          visible: item.place_id === this.restaurantData.detailPlaceId || !this.directionModel
        }"
        @click="markerClick(item)"
    />
    <!------------------Markers End-------------------->
  </GoogleMap>

  <div class="top-buttons">
    <MeSetting
        class="me-setting"
    />
    <FavoriteCuisine
        class="favorite-cuisine"
        @favorite-item-click="markerClick($event)"
    />
  </div>

  <p
      style="color:black;"
      @click="onFeedbackRequest"
      class="right-bottom-button"
  >
    feedback
  </p>

  <TextArea
      :place-id="restaurantData.detailPlaceId"
      :model="textAreaModel"
      :show-state="textAreaShowState"
      @close-textArea="this.textAreaShowState=false"
      @review-add-success="this.$refs.cuisineList.freshReviews();"
  />

  <CuisineTypeSelect
      :guidance-mode="guidanceModel"
      ref="cuisineTypeFilter"
      v-show="!directionModel"
      class="cuisine-select-wrapper"
      @click="onSelectClick"
      @option-change="selectOptionChange($event)"
      @option-click="selectOptionChange($event)"
  />
  <CuisineWindow
      ref="cuisineList"
      user-guidance="3"
      :guidance-mode="guidanceModel"
      :key="listDetailKey"
      v-show="listShowState"
      :title="restaurantData.title"
      :map-instance="mapInstance"
      :items="restaurantData.showingItems"
      :place-id="restaurantData.detailPlaceId"
      :list-loading-state="listDetailWindowStates.windowShow"
      :list-no-more-state="listDetailWindowStates.noMoreData"
      @click-X="onClickX($event)"
      @place-id-change="onCuisineListChangePlaceId($event)"
      @direction-request="onDirectionRequest($event)"
      @load-more-data="loadRecentRestaurant(restaurantData.currentSelection)"
      @detail-change="onDetailChange($event)"
      @review-request="onReviewRequest"
  />
  <v-tour name="userGuidance" :steps="steps"></v-tour>
</template>

<script>
import {GoogleMap, Marker, Circle, CustomMarker, CustomControl} from 'vue3-google-map'
import {mileToMeter} from "@/utils/MileUtil";
import CuisineTypeSelect from "@/components/CuisineTypeSelect.vue";
import CuisineWindow from "@/components/CuisineWindow.vue";
import Constants from "@/constants/Constants";
import PlaceService from "@/service/PlaceService";
import {ElLoading, ElMessageBox} from 'element-plus';
import StorageUtil from "@/utils/StorageUtil";
import {h} from 'vue';
import FavoriteCuisine from "@/components/FavoriteCuisine.vue";
import TextArea from "@/components/TextArea.vue";
import UserUtil from "@/utils/UserUtil";
import UserData from "@/constants/UserData";
import MeSetting from "@/components/MeSetting.vue";

export default {
  name: "AppPage",
  components: {
    MeSetting,
    TextArea,
    FavoriteCuisine,
    CuisineWindow,
    CuisineTypeSelect,
    GoogleMap,
    Marker,
    CustomMarker,
    Circle,
    CustomControl
  },
  computed: {
    Constants() {
      return Constants
    },
    restriction() {
      return {
        latLngBounds: {
          north: this.userLocation.lat + 0.025,
          south: this.userLocation.lat - 0.025,
          east: this.userLocation.lng + 0.05,
          west: this.userLocation.lng - 0.05,
        }
      }
    }
  },
  watch: {
    directionModel(newVar) {
      //if in direction model
      if (newVar) {

      } else {
        //remove direction
        this.directionRender.setDirections({routes: []});
      }
    },
    /**
     * watch if mapInstance has finished initialized.
     */
    mapInstance(newVar) {
      const option = {
        fullscreen: true
      }

      if (newVar) {
        //close loading page, wait 1s for making sure map get initialized
        //why not this.$loading, because in setTimeout this doesn't point vue instance
        setTimeout(() => {
          window.$mapInstance = this.mapInstance;
          ElLoading.service(option).close();
        }, 1000)
      } else {
        //loading page
        ElLoading.service(option);
      }
    },
  },
  data() {
    return {
      /**  To store the Google map instance*/
      mapInstance: null,
      /** render of direction*/
      directionRender: null,
      /**  user current location*/
      userLocation: {
        lat: 55,
        lng: -1.6,
      },
      /**  restaurant data set*/
      restaurantData: {
        /** items of showing restaurants*/
        showingItems: [],
        /** data of the detail windows*/
        detailPlaceId: null,
        /** title of cuisine window*/
        title: "All",
        /** current selection of cuisine type select*/
        currentSelection: "",
        /**set next page token for scrolling*/
        nextPageToken: null,
      },
      mapOption: {
        zoom: 14,
      },
      /** user marker option*/
      userMarkerOption: {
        position: this.userLocation,
        visible: this.ifInitialized
      },
      /** 1-mile circle option*/
      userCircleOption: {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeWeight: 0.5,
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      },
      /** flag of the page initialized state*/
      ifInitialized: false,
      /** the flag of showing the list/detail window*/
      listShowState: false,
      /** if user use direction*/
      directionModel: false,
      /** loadingState*/
      listDetailWindowStates: {
        windowShow: false,
        noMoreData: false
      },
      /** to force this component update by changing key*/
      listDetailKey: 0,
      textAreaShowState: false,
      textAreaModel: '',
      /** user guidance steps*/
      guidanceModel: false,
      steps: [
        {
          target: '.cuisine-select-wrapper',
          params: {
            highlight: true
          },
          content: 'Here you can select your favourite food. Click it!'
        },
        {
          target: '.el-popper',
          params: {
            highlight: true,
            placement: 'left'
          },
          content: 'Choose one type.'
        },
        {
          target: '[user-guidance="3"]',
          params: {
            highlight: true
          },
          content: 'Here it is.'
        },
      ]
    }
  },
  methods: {
    /**
     * set the center of Map to user location
     * set condition to make sure mapInstance is not null
     * set time out to avoid calling this method before mapInstance is properly installed
     * @param location user location
     */
    setMapCenter(location) {
      setTimeout(() => {
        if (this.mapInstance && this.mapInstance.map) {
          this.mapInstance.map.panTo(location);
        }
      }, 300);
    },
    /**
     * IF UPDATE USER POSITION, MUST USE THIS METHOD!
     * IF CHANGE THE ATTRIBUTE DIRECTLY, NO CHANGE WILL HAPPEN.
     * MUST REPLACE THE OBJECT!!!!
     *
     * update the current user marker
     * contains user centre marker and the 1-mile circle
     *
     */
    freshUserMarkerOptions() {
      this.userMarkerOption = {
        position: this.userLocation,
        visible: this.ifInitialized
      }

      this.userCircleOption = {
        center: this.userLocation,
        radius: mileToMeter(1),
        strokeColor: '#d7dbdf',
        visible: this.ifInitialized
      }
    },
    /**
     * Update user current location
     */
    freshUserLocation() {
      //Check if support this feature
      if (navigator.geolocation) {
        //success callback function
        const successCallback = (position) => {
          //update user location
          this.userLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          }

          //If it doesn't initialize, firstly request restaurant data and set refer for mapInstance
          if (!this.ifInitialized) {
            this.loadRecentRestaurant(this.restaurantData.currentSelection);
            this.ifInitialized = true;
            this.mapInstance = this.$refs.mapInstance;
            this.setMapCenter(this.userLocation);

            //register location watcher
            navigator.geolocation.watchPosition(successCallback, errorCallback);
          }

          //fresh user location
          this.freshUserMarkerOptions();

          //if it is in direction model, set map center to user current location when location updated
          if (this.directionModel) {
            this.setMapCenter(this.userLocation)
          }

          //store location for further usage
          StorageUtil.set(Constants.STORAGE_USER_LOCATION_LATITUDE, this.userLocation.lat)
          StorageUtil.set(Constants.STORAGE_USER_LOCATION_LONGITUDE, this.userLocation.lng)

        }

        //If error
        const errorCallback = (error) => {
          let errorMessage = '';
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMessage = "This app cannot accessed without enabling location tracking"
              break;
            case error.POSITION_UNAVAILABLE:
              errorMessage = "Location information is unavailable"
              break;
            case error.TIMEOUT:
              errorMessage = "The request to get location timed out"
              break;
          }
          ElMessageBox({
            title: '',
            message: h('p', {style: 'color: white'}, [
              h('span', {style: 'font-family: inter'}, [
                h('span', {style: 'font-weight: 400'}, [
                  h('span', {style: 'font-size: 1.5em'}, errorMessage)
                ])
              ])
            ]),
            customClass: 'locationServiceAlert',
            confirmButtonClass: 'confirmButton',
            showClose: false,
            center: true,
            buttonSize: "large",
            closeOnClickModal: false,
            showConfirmButton: true,
            autofocus: false,
          })
              //Rerun freshUserLocation after user tap confirm button
              //without fresh the page after allow geolocation in browser
              .then(() => {
                this.freshUserLocation()
              })
        }

        //Set the callback function
        navigator.geolocation.getCurrentPosition(successCallback, errorCallback);
      }
    },
    /**
     * Control event when selection get change
     * @param type restaurant type
     */
    selectOptionChange(type) {
      console.log(type)
      if (this.guidanceModel && this.$tours['userGuidance'].currentStep === 1) {
        this.$tours['userGuidance'].nextStep()
      }

      this.restaurantData.title = type;
      this.restaurantData.nextPageToken = null;
      this.listDetailWindowStates.noMoreData = false;

      if (this.restaurantData.currentSelection !== type) {
        this.loadRecentRestaurant(type);
        this.$refs.cuisineList.changeInfoWindow("list", true);
      } else {
        this.$refs.cuisineList.changeInfoWindow("list");
      }

      this.listShowState = true;
      //quit direction model
      this.directionModel = false;
      this.setMapCenter(this.userLocation);

      //call sub component method
    },
    /**
     * Refresh nearby restaurant data set.
     * @param type Type of restaurant, like America, British
     */
    async loadRecentRestaurant(type) {
      console.log(type)
      this.listDetailWindowStates.windowShow = true;

      let response
      //if not initialize or type change
      if (
          this.restaurantData.currentSelection !== type
          || !this.ifInitialized) {
        response = await PlaceService.getRestaurantWithKeywordInOneMile(this.userLocation, type);
        this.restaurantData.showingItems = response.data.results;

        //only loaded data, change current type.
        this.restaurantData.currentSelection = type;

        //force update
        this.listDetailKey++
      } else {
        response = await PlaceService.getRestaurantWithKeywordInOneMile(
            this.userLocation,
            null,
            this.restaurantData.nextPageToken);
        this.restaurantData.showingItems = [...this.restaurantData.showingItems, ...response.data.results]
      }

      //set next page token for scrolling
      let nextPageToken = response.data.next_page_token;
      if (nextPageToken) {
        this.restaurantData.nextPageToken = nextPageToken;
      } else {
        this.listDetailWindowStates.noMoreData = true;
      }

      this.listDetailWindowStates.windowShow = false;
    },
    /**
     * What happens, if one restaurant marker get clicked
     * @param item Data of the marker
     */
    markerClick(item) {
      //select blur
      this.$refs.cuisineTypeFilter.makeSelectBlur();

      //set market invoke detail flag
      StorageUtil.set(Constants.STORAGE_IF_DETAIL_SHOW_BY_CLICK_MARKER, "true")
      this.listShowState = true;
      this.setMapCenter(item.geometry.location);
      this.restaurantData.detailPlaceId = item.place_id;
      this.$refs.cuisineList.changeInfoWindow("detail", item);
      //quit direction model
      this.directionModel = false;
    },
    /**
     * when place-id-change event happens, the script runs.
     * @param data place_id
     */
    onCuisineListChangePlaceId(data) {
      if (this.guidanceModel && this.$tours['userGuidance'].currentStep === 2) {
        this.guidanceModel = false;
        this.$tours['userGuidance'].finish();
      }

      this.restaurantData.detailPlaceId = data;
      //quit direction model
      this.directionModel = false;
    },
    /**
     * when direction-request event happens, the script runs.
     */
    onDirectionRequest(direction) {
      if (!this.directionRender) {
        this.directionRender = new this.mapInstance.api.DirectionsRenderer();
        this.directionRender.setMap(this.mapInstance.map)
      }
      this.directionRender.setDirections(direction);
      this.listShowState = false;
      this.directionModel = true;
    },
    onClickX(value) {
      if (this.guidanceModel && this.$tours['userGuidance'].currentStep === 2) {
        this.guidanceModel = false;
        this.$tours['userGuidance'].finish();
      }

      //if it is showing list, close cuisine window
      //else change title to current selection
      if (value) {
        this.listShowState = false
      } else {
        this.restaurantData.title = this.restaurantData.currentSelection;
      }

      //if detail window is invoked by markers clicking, close all table, then remove that flag
      if (StorageUtil.get(Constants.STORAGE_IF_DETAIL_SHOW_BY_CLICK_MARKER)) {
        this.listShowState = false;
        StorageUtil.remove(Constants.STORAGE_IF_DETAIL_SHOW_BY_CLICK_MARKER);
      }
    },
    OnMapClick() {
      this.$refs.cuisineTypeFilter.makeSelectBlur();
      this.listShowState = false;
    },
    onDetailChange(e) {
      let name = e.name;
      if (name.length > 47) {
        name = name.substring(0, 46) + "...";
      }
      this.restaurantData.title = name;
      this.setMapCenter(e.geometry.location)
    },
    onSelectClick() {
      if (this.guidanceModel && this.$tours['userGuidance'].currentStep === 0) {
        this.$tours['userGuidance'].nextStep();
      }
    },
    onReviewRequest() {
      this.textAreaModel = "review"
      this.textAreaShowState = true;
    },
    onFeedbackRequest() {
      this.textAreaModel = "feedback"
      this.textAreaShowState = true;
    }
  },
  /**
   * Event when all dom nodes get created
   */
  beforeMount() {
    //show loading
    this.$loading({
      fullscreen: true
    });
    //If it gets location successfully, the loading will disappear.
    this.freshUserLocation();
  },
  mounted() {
    if (!StorageUtil.get(Constants.STORAGE_IF_EXPERIENCE_USER_GUIDANCE)) {
      this.guidanceModel = true;
      this.$tours['userGuidance'].start()
      StorageUtil.set(Constants.STORAGE_IF_EXPERIENCE_USER_GUIDANCE, "true");
    }

    if (StorageUtil.get(Constants.STORAGE_TOKEN)) {
      UserData.freshUserData();
    }
  }
}
</script>

<style>
.el-message-box {
  box-sizing: border-box;
  background: #034BFC;
  border-width: 0;
  text-align-last: center;
}

.confirmButton {
  background: #A4B0BE 70%;
  outline: none;
  font-weight: bold;
  border-width: 0;
}

.confirmButton:hover {
  background: #A4B0BE 50%;
}

</style>

<style scoped>
.map {
  width: 100vw;
  height: var(--doc-height);
  color: #d7dbdf;

}

.cuisine-select-wrapper {
  position: absolute;
  top: 5%;
  right: 5%;
}

.top-buttons {
  position: absolute;
  top: 5%;

  width: 50%;
}

.favorite-cuisine {
  display: inline-block;
  margin-left: 10%;
}

.me-setting {
  display: inline-block;
  margin-left: 5%;
}
</style>